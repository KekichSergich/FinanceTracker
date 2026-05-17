package infrastructure.persistence.repository;

import domain.model.Category;
import domain.model.Transaction;
import domain.model.TransactionType;
import domain.repository.TransactionRepository;
import util.AppLogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

public class FileTransactionRepository implements TransactionRepository {

    private static final Logger logger = AppLogger.get(FileTransactionRepository.class);

    private final Path filePath;

    public FileTransactionRepository(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Serializes a Transaction into a JSON object string.
     */
    private String toJson(Transaction t) {
        return "{" +
                "\"id\":" + t.getId() + "," +
                "\"amount\":" + t.getAmount() + "," +
                "\"type\":\"" + t.getType().name() + "\"," +
                "\"category\":\"" + t.getCategory().name() + "\"," +
                "\"date\":\"" + t.getDate() + "\"," +
                "\"note\":\"" + (t.getNote() != null ? t.getNote() : "") + "\"" +
                "}";
    }

    /**
     * Deserializes a JSON object string into a Transaction.
     * Splits by commas not inside quotes to handle all fields correctly.
     */
    private Transaction fromJson(String json) {
        json = json.trim().replaceAll("[{}]", "");
        String[] parts = json.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

        Long id = null; double amount = 0;
        TransactionType type = null; Category category = null;
        LocalDate date = null; String note = "";

        for (String part : parts) {
            if (part.trim().isEmpty()) continue;
            String[] kv = part.split(":", 2);
            String key   = kv[0].trim().replaceAll("\"", "");
            String value = kv[1].trim().replaceAll("\"", "");
            switch (key) {
                case "id"       -> id = Long.parseLong(value);
                case "amount"   -> amount = Double.parseDouble(value);
                case "type"     -> type = TransactionType.valueOf(value);
                case "category" -> category = Category.valueOf(value);
                case "date"     -> date = LocalDate.parse(value);
                case "note"     -> note = value;
            }
        }
        return new Transaction(id, amount, type, category, date, note);
    }

    /**
     * Reads all transactions from the JSON file.
     * Returns an empty list if the file does not exist or is empty.
     * Parses JSON by tracking brace depth to split individual objects.
     */
    private List<Transaction> readAll() {
        if (!Files.exists(filePath)) {
            logger.fine("File not found, returning empty list: " + filePath);
            return new ArrayList<>();
        }
        try {
            String content = Files.readString(filePath).trim();
            if (content.isEmpty() || content.equals("[]")) return new ArrayList<>();

            // strip outer array brackets
            content = content.substring(1, content.length() - 1).trim();
            List<Transaction> result = new ArrayList<>();
            int depth = 0;
            StringBuilder obj = new StringBuilder();
            for (char c : content.toCharArray()) {
                if (c == '{') depth++;
                if (c == '}') depth--;
                obj.append(c);
                if (depth == 0 && obj.toString().contains("{")) {
                    result.add(fromJson(obj.toString()));
                    obj.setLength(0);
                }
            }
            logger.fine("Read " + result.size() + " transactions from " + filePath);
            return result;
        } catch (IOException e) {
            logger.severe("Cannot read file: " + filePath + " | " + e.getMessage());
            throw new RuntimeException("Cannot read: " + filePath, e);
        }
    }

    /**
     * Writes the given list of transactions to the JSON file.
     * Creates parent directories if they do not exist.
     */
    private void writeAll(List<Transaction> list) {
        try {
            if (filePath.getParent() != null) Files.createDirectories(filePath.getParent());
            StringBuilder sb = new StringBuilder("[\n");
            for (int i = 0; i < list.size(); i++) {
                sb.append("  ").append(toJson(list.get(i)));
                if (i < list.size() - 1) sb.append(",");
                sb.append("\n");
            }
            sb.append("]");
            Files.writeString(filePath, sb.toString());
            logger.fine("Wrote " + list.size() + " transactions to " + filePath);
        } catch (IOException e) {
            logger.severe("Cannot write file: " + filePath + " | " + e.getMessage());
            throw new RuntimeException("Cannot write: " + filePath, e);
        }
    }

    /**
     * Saves a new transaction with an auto-generated ID.
     * ID is set to max existing ID + 1.
     */
    @Override
    public void save(Transaction transaction) {
        List<Transaction> transactions = readAll();
        long newId = transactions.stream()
                .mapToLong(Transaction::getId)
                .max()
                .orElse(0L) + 1;
        transaction.setId(newId);
        transactions.add(transaction);
        writeAll(transactions);
    }

    /**
     * Replaces an existing transaction with the same ID.
     */
    @Override
    public void update(Transaction transaction) {
        List<Transaction> transactions = findAll();
        for (int i = 0; i < transactions.size(); i++) {
            if (Objects.equals(transactions.get(i).getId(), transaction.getId())) {
                transactions.set(i, transaction);
                break;
            }
        }
        writeAll(transactions);
    }

    /**
     * Removes the transaction with the given ID.
     */
    @Override
    public void deleteById(Long id) {
        List<Transaction> transactions = findAll();
        transactions.removeIf(t -> t.getId().equals(id));
        writeAll(transactions);
    }

    /**
     * Finds a transaction by ID. Returns empty Optional if not found.
     */
    @Override
    public Optional<Transaction> findById(Long id) {
        return findAll().stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();
    }

    /**
     * Returns all transactions from the file.
     */
    @Override
    public List<Transaction> findAll() {
        return readAll();
    }

    /**
     * Returns transactions filtered by category.
     */
    @Override
    public List<Transaction> findByCategory(Category category) {
        return findAll().stream()
                .filter(t -> t.getCategory().equals(category))
                .toList();
    }

    /**
     * Returns transactions within the given date range (inclusive).
     */
    @Override
    public List<Transaction> findByDateRange(LocalDate from, LocalDate to) {
        return findAll().stream()
                .filter(t -> !t.getDate().isBefore(from) && !t.getDate().isAfter(to))
                .toList();
    }

    /**
     * Deletes all transactions by overwriting the file with an empty array.
     */
    @Override
    public void deleteAll() {
        writeAll(new ArrayList<>());
    }

    /**
     * Saves a list of transactions, assigning new IDs to avoid conflicts with existing ones.
     * New IDs are generated sequentially starting from max existing ID + 1.
     */
    @Override
    public void saveAll(List<Transaction> transactions) {
        List<Transaction> existing = readAll();
        long maxId = existing.stream()
                .mapToLong(Transaction::getId)
                .max()
                .orElse(0L);

        for (Transaction t : transactions) {
            t.setId(++maxId);
            existing.add(t);
        }
        writeAll(existing);
    }
}