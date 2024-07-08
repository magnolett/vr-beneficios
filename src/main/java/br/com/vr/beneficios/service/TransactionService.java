package br.com.vr.beneficios.service;

import br.com.vr.beneficios.entities.Transaction;
import br.com.vr.beneficios.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Transactional
    public Transaction authorizeTransaction(String cardNumber, double amount, boolean authorized) {
        Transaction transaction = new Transaction();
        transaction.setCardNumber(cardNumber);
        transaction.setAmount(amount);
        transaction.setAuthorized(authorized);
        return transactionRepository.save(transaction);
    }
}

