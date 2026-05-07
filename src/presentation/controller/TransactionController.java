package presentation.controller;

import application.service.TransactionService;
import domain.model.Transaction;
import infrastructure.persistence.mapper.TransactionMapper;
import presentation.dto.TransactionDto;
import presentation.dto.TransactionFormDto;

import java.util.ArrayList;
import java.util.List;

public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public void handleAddTransaction(TransactionFormDto dto) {
        Transaction transaction = new Transaction(null, dto.getAmount(), dto.getType(), dto.getCategory(), dto.getDate(), dto.getNote());
        transactionService.addTransaction(transaction);
    }

    public void handleDeleteTransaction(Long id) {
        transactionService.deleteTransaction(id);
    }

    public List<TransactionDto> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        List<TransactionDto> dtos = new ArrayList<>();
        for (Transaction transaction : transactions) {
            TransactionDto dto = new TransactionDto();
            dto.setId(transaction.getId());
            dto.setAmount(String.valueOf(transaction.getAmount()));
            dto.setType(transaction.getType().name());
            dto.setCategory(transaction.getCategory().name());
            dto.setDate(transaction.getDate().toString());
            dto.setNote(transaction.getNote());
            dtos.add(dto);
        }
        return dtos;
    }
}