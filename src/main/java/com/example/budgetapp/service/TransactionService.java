package com.example.budgetapp.service;

import com.example.budgetapp.dto.TransactionDto;
import com.example.budgetapp.entity.Transaction;
import com.example.budgetapp.entity.User;
import com.example.budgetapp.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    // 등록
    public void saveTransaction(TransactionDto dto, User user) {
        Transaction transaction = Transaction.builder()
                .title(dto.getTitle())
                .amount(dto.getAmount())
                .type(dto.getType())
                .date(dto.getDate())
                .category(dto.getCategory())
                .user(user)
                .build();

        transactionRepository.save(transaction);
    }

    // 전체 조회
    public List<TransactionDto> getTransactions(User user) {
        List<Transaction> transactions = transactionRepository.findByUser(user);
        return transactions.stream().map(this::toDto).collect(Collectors.toList());
    }

    // DTO 변환
    private TransactionDto toDto(Transaction t) {
        return TransactionDto.builder()
                .title(t.getTitle())
                .amount(t.getAmount())
                .type(t.getType())
                .date(t.getDate())
                .category(t.getCategory())
                .build();
    }
}