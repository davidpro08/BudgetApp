package com.example.budgetapp.controller;

import com.example.budgetapp.dto.TransactionDto;
import com.example.budgetapp.entity.User;
import com.example.budgetapp.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    // 리스트로 보여주기
    @GetMapping("/list")
    public String showList(Model model){
        // TODO : 나중에 실제 유저 받는거로 바꾸기
        User user = getDummyUser(); // 임시 유저
        // Transaction을 Dto형식으로 받아오기
        List<TransactionDto> list = transactionService.getTransactions(user);
        model.addAttribute("transaction", list);
        return "transaction/list"; // 타임리프 템플릿 이름
    }


    // 등록 폼 이동
    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("transactionDto", new TransactionDto());
        return "transaction/form";
    }

    // transaction 등록하기
    @PostMapping("/")
    public String postTransaction(@ModelAttribute TransactionDto dto){
        // TODO : 나중에 실제 유저 받는거로 바꾸기
        User user = getDummyUser();
        transactionService.saveTransaction(dto, user);
        return "redirect:/transactions";
    }

    // 리스트 보기
    @GetMapping
    public String list(Model model) {
        User user = getDummyUser();
        List<TransactionDto> list = transactionService.getTransactions(user);
        model.addAttribute("transactions", list);
        return "transaction/list";
    }

    // 임시 유저 (나중에 로그인 기능 넣을 때 대체)
    private User getDummyUser() {
        return User.builder()
                .id(1L)
                .username("testuser")
                .password("secret")
                .build();
    }
}