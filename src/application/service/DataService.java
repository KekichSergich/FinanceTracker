package application.service;

import domain.model.ImportMode;
import domain.model.Transaction;
import domain.model.TransactionType;
import domain.model.Category;
import domain.repository.TransactionRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DataService {

    private final TransactionRepository transactionRepository;

    public DataService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void exportToJson(String filePath) {
        List<Transaction> all = transactionRepository.findAll();
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
        } catch (IOException e) {
            throw new RuntimeException("Export failed: " + filePath, e);
        }
    }

    // возвращает список строк с описанием пропущенных записей
    public List<String> importFromJson(String filePath, ImportMode mode) {
        String content;
        try {
            content = Files.readString(Path.of(filePath)).trim();
        } catch (IOException e) {
            throw new RuntimeException("Import failed: " + filePath, e);
        }

        List<Transaction> parsed = new ArrayList<>();
        List<String> skipped = new ArrayList<>();

        if (!content.isEmpty() && !content.equals("[]")) {
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
                        skipped.add(raw + " → " + e.getMessage());
                    }
                }
            }
        }

        if (mode == ImportMode.REPLACE) {
            transactionRepository.deleteAll();
        }
        transactionRepository.saveAll(parsed);

        return skipped;
    }

    private Transaction parseTransaction(String json) {
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
}