package com.example.budgetapp.controller;

import com.example.budgetapp.dto.TransactionDto;
import com.example.budgetapp.entity.User;
import com.example.budgetapp.service.TransactionService;
import jakarta.persistence.Id;
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

    /*
    자신의 리스트 보기.
    정렬 기능 존재.
    * */
    @GetMapping
    public String list(@RequestParam(defaultValue = "idDesc") String sort, Model model) {
        // TODO : 나중에 실제 유저 받는거로 바꾸기
        User user = getDummyUser();

        List<TransactionDto> list = transactionService.getSortedTransactions(user, sort);
        model.addAttribute("transactions", list);
        return "transaction/list";
    }

    // 등록 폼 이동
    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("transactionDto", new TransactionDto());
        return "transaction/form";
    }

    // transaction 등록하기
    @PostMapping("/add")
    public String postTransaction(@ModelAttribute TransactionDto dto){
        // TODO : 나중에 실제 유저 받는 거로 바꾸기
        User user = getDummyUser();

        transactionService.saveTransaction(dto, user);
        return "redirect:/transactions";
    }

    // 삭제 만들기
    // 삭제는 원래 DELETE이지만, HTML에서 form의 요청을 잘 못받아들이는 경우 많음
    // 그러므로 POST 사용
    @PostMapping("/delete/{id}")
    public String deleteTransaction(@PathVariable Long id){
        // TODO : 나중에 파라미터의 id 사용

        User user = getDummyUser();

        //transactionService.deleteTransaction(user.getId());
        transactionService.deleteTransaction(id);

        return "redirect:/transactions";
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