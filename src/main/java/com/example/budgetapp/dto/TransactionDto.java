package com.example.budgetapp.dto;

import com.example.budgetapp.entity.TransactionType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDto {
    private Long id;
    private String title;
    private Long amount;
    private TransactionType type;
    private LocalDate date;
    private String category;
}