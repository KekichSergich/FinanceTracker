package presentation.controller;

import application.service.DataService;

public class DataController {

    private final DataService dataService;

    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    public void exportToJson(String filePath) {
        // TODO
    }

    public void importFromJson(String filePath) {
        // TODO
    }

    public void exportToXml(String filePath) {
        // TODO
    }

    public void importFromXml(String filePath) {
        // TODO
    }
}