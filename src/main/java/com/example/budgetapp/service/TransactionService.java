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
                break;
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

    // 정렬 + 검색 조회
    public List<TransactionDto> getFilteredAndSortedTransactions(User user, String sort, String title) {
        List<Transaction> transactions;

        if (title != null && !title.isBlank()) {
            transactions = transactionRepository.findByUserAndTitleContaining(user, title);
        } else {
            transactions = transactionRepository.findByUser(user);
        }

        switch (sort) {
            case "amountAsc":
                transactions.sort(Comparator.comparing(Transaction::getAmount));
                break;
            case "amountDesc":
                transactions.sort(Comparator.comparing(Transaction::getAmount).reversed());
                break;
            case "categoryAsc":
                transactions.sort(Comparator.comparing(Transaction::getCategory));
                break;
            case "categoryDesc":
                transactions.sort(Comparator.comparing(Transaction::getCategory).reversed());
                break;
            case "dateAsc":
                transactions.sort(Comparator.comparing(Transaction::getDate));
                break;
            case "dateDesc":
                transactions.sort(Comparator.comparing(Transaction::getDate).reversed());
                break;
            case "idAsc":
                transactions.sort(Comparator.comparing(Transaction::getId));
                break;
            case "idDesc":
            default:
                transactions.sort(Comparator.comparing(Transaction::getId).reversed());
                break;
        }

        return transactions.stream().map(this::toDto).collect(Collectors.toList());
    }

    // id 이용 단일 Dto 받기
    public TransactionDto getTransactionDtoById(Long id){
        Transaction transaction = transactionRepository.findTransactionById(id);
        return toDto(transaction);
    }

    // 삭제
    public void deleteTransaction(Long id){
        transactionRepository.deleteById(id);
    }

    // 수정
    public void updateTransaction(Long id, TransactionDto dto, User user) {

        Transaction transaction = transactionRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 내역이 존재하지 않습니다: " + id));

        // 유저 일치 여부도 체크 가능
        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new SecurityException("본인만 수정 가능합니다.");
        }

        transaction.setTitle(dto.getTitle());
        transaction.setAmount(dto.getAmount());
        transaction.setDate(dto.getDate());
        transaction.setType(dto.getType());
        transaction.setCategory(dto.getCategory());

        transactionRepository.save(transaction);
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