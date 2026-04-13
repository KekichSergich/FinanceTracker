package presentation.controller;

import application.service.TransactionService;
import presentation.dto.TransactionDto;

import java.util.List;

public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public void handleAddTransaction(TransactionDto dto) {
        // TODO
    }

    public void handleDeleteTransaction(Long id) {
        // TODO
    }

    public List<TransactionDto> getAllTransactions() {
        // TODO
        return List.of();
    }
}