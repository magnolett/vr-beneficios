package br.com.vr.beneficios.controller;

import br.com.vr.beneficios.entities.Transaction;
import br.com.vr.beneficios.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<?> authorizeTransaction(@RequestBody Transaction transaction) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionService.authorizeTransaction(
                        transaction.getCardNumber(),
                        transaction.getAmount(),
                        transaction.isAuthorized()));
    }
}

