package presentation.controller;

import application.service.DataService;
import domain.model.ImportMode;

import java.util.List;

public class DataController {

    private final DataService dataService;

    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    public void exportToJson(String filePath) {
        dataService.exportToJson(filePath);
    }

    public List<String> importFromJson(String filePath, ImportMode mode) {
        return dataService.importFromJson(filePath, mode);
    }
}