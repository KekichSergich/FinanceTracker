package application.service;

import domain.model.ImportMode;
import domain.model.Transaction;
import domain.model.TransactionType;
import domain.model.Category;
import domain.repository.TransactionRepository;
import util.AppLogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DataService {

    private static final Logger logger = AppLogger.get(DataService.class);

    private final TransactionRepository transactionRepository;

    public DataService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Exports all transactions to a JSON file at the given path.
     * Builds JSON manually without external libraries.
     */
    public void exportToJson(String filePath) {
        List<Transaction> all = transactionRepository.findAll();
        logger.info("Exporting " + all.size() + " transactions to " + filePath);
        StringBuilder sb = new StringBuilder("[\n");
        for (int i = 0; i < all.size(); i++) {
            Transaction t = all.get(i);
            sb.append("  {")
                    .append("\"id\":").append(t.getId()).append(",")
                    .append("\"amount\":").append(t.getAmount()).append(",")
                    .append("\"type\":\"").append(t.getType().name()).append("\",")
                    .append("\"category\":\"").append(t.getCategory().name()).append("\",")
                    .append("\"date\":\"").append(t.getDate()).append("\",")
                    .append("\"note\":\"").append(t.getNote() != null ? t.getNote() : "").append("\"")
                    .append("}");
            if (i < all.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("]");
        try {
            Files.writeString(Path.of(filePath), sb.toString());
            logger.info("Export successful: " + filePath);
        } catch (IOException e) {
            logger.severe("Export failed: " + filePath + " | " + e.getMessage());
            throw new RuntimeException("Export failed: " + filePath, e);
        }
    }

    /**
     * Imports transactions from a JSON file.
     * Depending on the mode: REPLACE clears existing data, MERGE appends to it.
     * Invalid records are skipped and returned as a list of error descriptions.
     */
    public List<String> importFromJson(String filePath, ImportMode mode) {
        logger.info("Importing from " + filePath + " mode=" + mode);
        String content;
        try {
            content = Files.readString(Path.of(filePath)).trim();
        } catch (IOException e) {
            logger.severe("Import failed to read file: " + filePath + " | " + e.getMessage());
            throw new RuntimeException("Import failed: " + filePath, e);
        }

        List<Transaction> parsed = new ArrayList<>();
        List<String> skipped = new ArrayList<>();

        if (!content.isEmpty() && !content.equals("[]")) {
            // strip outer brackets and iterate character by character to split JSON objects
            content = content.substring(1, content.length() - 1).trim();
            int depth = 0;
            StringBuilder obj = new StringBuilder();
            for (char c : content.toCharArray()) {
                if (c == '{') depth++;
                if (c == '}') depth--;
                obj.append(c);
                if (depth == 0 && obj.toString().contains("{")) {
                    String raw = obj.toString().trim();
                    obj.setLength(0);
                    try {
                        parsed.add(parseTransaction(raw));
                    } catch (Exception e) {
                        // collect invalid records instead of failing the whole import
                        logger.warning("Skipped invalid record: " + raw + " | reason: " + e.getMessage());
                        skipped.add(raw + " → " + e.getMessage());
                    }
                }
            }
        }

        if (mode == ImportMode.REPLACE) {
            transactionRepository.deleteAll();
            logger.info("Existing transactions cleared (REPLACE mode)");
        }
        transactionRepository.saveAll(parsed);
        logger.info("Import complete: loaded=" + parsed.size() + " skipped=" + skipped.size());

        return skipped;
    }

    /**
     * Parses a single JSON object string into a Transaction.
     * Uses a simple key-value split without external JSON libraries.
     * Throws an exception if any required field is missing or malformed.
     */
    private Transaction parseTransaction(String json) {
        json = json.trim().replaceAll("[{}]", "");
        // split by commas that are not inside quotes
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
}