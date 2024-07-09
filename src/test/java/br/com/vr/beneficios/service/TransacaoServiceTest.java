package br.com.vr.beneficios.service;

import br.com.vr.beneficios.entities.Card;
import br.com.vr.beneficios.entities.Transaction;
import br.com.vr.beneficios.exception.CardNotFoundException;
import br.com.vr.beneficios.exception.InsufficientBalanceException;
import br.com.vr.beneficios.exception.InvalidPasswordException;
import br.com.vr.beneficios.repository.CardRepository;
import br.com.vr.beneficios.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthorizeTransaction() {
        String cardNumber = "1234567890123456";
        String senhaCartao = "1234";
        double amount = 100.0;

        Card card = Card.builder()
                .id(1L)
                .numeroCartao(cardNumber)
                .senha(senhaCartao)
                .saldo(500.0)
                .build();
        when(cardRepository.findByNumeroCartao(eq(cardNumber))).thenReturn(Optional.of(card));

        Transaction mockedTransaction = Transaction.builder()
                .id(1L)
                .card(card)
                .amount(amount)
                .build();
        when(transactionRepository.save(any(Transaction.class))).thenReturn(mockedTransaction);

        ResponseEntity<?> responseEntity = transactionService.authorizeTransaction(cardNumber, senhaCartao, amount);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        verify(cardRepository, times(1)).findByNumeroCartao(eq(cardNumber));
        verify(cardRepository, times(1)).save(eq(card));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testAuthorizeTransactionSenhaInvalida() {
        String cardNumber = "1234567890123456";
        String senhaCartao = "invalid_password";
        double amount = 100.0;

        Card card = Card.builder()
                .numeroCartao(cardNumber)
                .senha("correct_password")
                .saldo(500.0)
                .build();
        when(cardRepository.findByNumeroCartao(eq(cardNumber))).thenReturn(Optional.of(card));

        assertThrows(InvalidPasswordException.class, () -> {
            transactionService.authorizeTransaction(cardNumber, senhaCartao, amount);
        });

        verify(cardRepository, times(1)).findByNumeroCartao(eq(cardNumber));
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void testAuthorizeTransactionSaldoInsuficiente() {
        String cardNumber = "1234567890123456";
        String senhaCartao = "1234";
        double amount = 600.0;

        Card card = Card.builder()
                .numeroCartao(cardNumber)
                .senha(senhaCartao)
                .saldo(500.0)
                .build();
        when(cardRepository.findByNumeroCartao(eq(cardNumber))).thenReturn(Optional.of(card));

        assertThrows(InsufficientBalanceException.class, () -> {
            transactionService.authorizeTransaction(cardNumber, senhaCartao, amount);
        });

        verify(cardRepository, times(1)).findByNumeroCartao(eq(cardNumber));
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void testAuthorizeTransactionCartaoInexistente() {
        String cardNumber = "non_existing_card_number";
        String senhaCartao = "1234";
        double amount = 100.0;

        when(cardRepository.findByNumeroCartao(eq(cardNumber))).thenReturn(Optional.empty());

        assertThrows(CardNotFoundException.class, () -> {
            transactionService.authorizeTransaction(cardNumber, senhaCartao, amount);
        });

        verify(cardRepository, times(1)).findByNumeroCartao(eq(cardNumber));
        verifyNoInteractions(transactionRepository);

    }
}

