package infrastructure.persistence.repository;

import application.service.StatisticsService;
import application.service.TransactionService;
import domain.model.Category;
import domain.model.Transaction;
import domain.model.TransactionType;
import domain.repository.TransactionRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.spi.ToolProvider.findFirst;

public class FileTransactionRepository implements TransactionRepository {

    private final Path filePath;

    public FileTransactionRepository(Path filePath) {
        this.filePath = filePath;
    }

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

    private Transaction fromJson(String json) {
        json = json.trim().replaceAll("[{}]", "");
        String[] parts = json.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

        Long id = null; double amount = 0;
        TransactionType type = null; Category category = null;
        LocalDate date = null; String note = "";

        for (String part : parts) {
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

    private List<Transaction> readAll() {
        if (!Files.exists(filePath)) return new ArrayList<>();
        try {
            String content = Files.readString(filePath).trim();
            if (content.isEmpty() || content.equals("[]")) return new ArrayList<>();

            content = content.substring(1, content.length() - 1).trim();
            List<Transaction> result = new ArrayList<>();
            int depth = 0;
            StringBuilder obj = new StringBuilder();
            for (char c : content.toCharArray()) {
                if (c == '{') depth++;
                if (c == '}') depth--;
                obj.append(c);
                if (depth == 0 && !obj.toString().isBlank()) {
                    result.add(fromJson(obj.toString()));
                    obj.setLength(0);
                }
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException("Cannot read: " + filePath, e);
        }
    }

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
        } catch (IOException e) {
            throw new RuntimeException("Cannot write: " + filePath, e);
        }
    }

    @Override
    public void save(Transaction transaction) {
        List<Transaction> transactions = readAll();
        transactions.add(transaction);
        writeAll(transactions);
    }

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

    @Override
    public void deleteById(Long id) {
        List<Transaction> transactions = findAll();
        transactions.removeIf(t -> t.getId().equals(id));
        writeAll(transactions);
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        List<Transaction> transactions = findAll();
        return transactions.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Transaction> findAll() {
        List<Transaction> result = readAll();
        return result;
    }

    @Override
    public List<Transaction> findByCategory(Category category) {
        return findAll().stream()
                .filter(t -> t.getCategory().equals(category))
                .toList();
    }

    @Override
    public List<Transaction> findByDateRange(LocalDate from, LocalDate to) {
        return findAll().stream()
                .filter(t -> !t.getDate().isBefore(from) && !t.getDate().isAfter(to))
                .toList();
    }
}