package com.example.budgetapp.service;

import com.example.budgetapp.dto.TransactionDto;
import com.example.budgetapp.entity.Transaction;
import com.example.budgetapp.entity.User;
import com.example.budgetapp.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
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

    // 정렬 조회
    public List<TransactionDto> getSortedTransactions(User user, String sort){
        List<Transaction> transactions = transactionRepository.findByUser(user);

        switch (sort){
            case "amountAsc" :
                transactions.sort(Comparator.comparing(Transaction::getAmount));
                break;
            case "amountDesc" :
                transactions.sort(Comparator.comparing(Transaction::getAmount).reversed());
                break;
            case "categoryAsc" :
                transactions.sort(Comparator.comparing(Transaction::getCategory));
                break;
            case "categoryDesc" :
                transactions.sort(Comparator.comparing(Transaction::getCategory).reversed());
                break;
            case "dateAsc" :
                transactions.sort(Comparator.comparing(Transaction::getDate));
                break;
            case "dateDesc" :
                transactions.sort(Comparator.comparing(Transaction::getDate).reversed());
            case "idAsc" :
                transactions.sort(Comparator.comparing(Transaction::getId));
                break;
            case "idDesc" :
            default :
                transactions.sort(Comparator.comparing(Transaction::getId).reversed());
                break;
        }
        return transactions.stream().map(this::toDto).collect(Collectors.toList());
    }

    // 삭제
    public void deleteTransaction(Long id){
        transactionRepository.deleteById(id);
    }

    // DTO 변환
    private TransactionDto toDto(Transaction t) {
        return TransactionDto.builder()
                .id(t.getId())
                .title(t.getTitle())
                .amount(t.getAmount())
                .type(t.getType())
                .date(t.getDate())
                .category(t.getCategory())
                .build();
    }
}