package br.com.vr.beneficios.service;

import br.com.vr.beneficios.entities.Card;
import br.com.vr.beneficios.entities.Transaction;
import br.com.vr.beneficios.exception.CardNotFoundException;
import br.com.vr.beneficios.exception.InsufficientBalanceException;
import br.com.vr.beneficios.exception.InvalidPasswordException;
import br.com.vr.beneficios.repository.CardRepository;
import br.com.vr.beneficios.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CardRepository cardRepository;

    @Transactional
    public ResponseEntity<?> authorizeTransaction(String cardNumber, String senhaCartao, double valor) {
        Card card = cardRepository.findByNumeroCartao(cardNumber)
                .orElseThrow(() -> new CardNotFoundException("CARTAO_INEXISTENTE"));

        validatePassword(card, senhaCartao);
        validateBalance(card, valor);

        card.setSaldo(card.getSaldo() - valor);
        cardRepository.save(card);

        Transaction transaction = Transaction.builder()
                .card(card)
                .amount(valor)
                .build();
        transactionRepository.save(transaction);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    private void validatePassword(Card card, String senhaCartao) {
        Optional.of(card)
                .filter(c -> c.getSenha().equals(senhaCartao))
                .orElseThrow(() -> new InvalidPasswordException("SENHA_INVALIDA"));
    }

    private void validateBalance(Card card, double valor) {
        Optional.of(card)
                .filter(c -> c.getSaldo() >= valor)
                .orElseThrow(() -> new InsufficientBalanceException("SALDO_INSUFICIENTE"));
    }
}

