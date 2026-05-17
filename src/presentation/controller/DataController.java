package presentation.controller;

import application.service.DataService;
import domain.model.ImportMode;

import java.util.List;

public class DataController {

    private final DataService dataService;

    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    /**
     * Exports all transactions to a JSON file at the given path.
     */
    public void exportToJson(String filePath) {
        dataService.exportToJson(filePath);
    }

    /**
     * Imports transactions from a JSON file.
     * Mode defines whether to replace or merge with existing data.
     * Returns a list of skipped records with error descriptions.
     */
    public List<String> importFromJson(String filePath, ImportMode mode) {
        return dataService.importFromJson(filePath, mode);
    }
}