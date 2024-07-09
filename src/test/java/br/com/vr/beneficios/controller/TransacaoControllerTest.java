package br.com.vr.beneficios.controller;

import br.com.vr.beneficios.entities.Card;
import br.com.vr.beneficios.entities.Transaction;
import br.com.vr.beneficios.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthorizeTransaction_Success() {

        String cardNumber = "1234567890123456";
        String senhaCartao = "1234";
        Card card = Card.builder().numeroCartao(cardNumber).senha(senhaCartao).build();

        Transaction transaction = Transaction.builder().card(card).amount(100.0).build();

        when(transactionService.authorizeTransaction(anyString(), anyString(), anyDouble()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

        ResponseEntity<?> responseEntity = transactionController.authorizeTransaction(transaction);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        verify(transactionService, times(1)).authorizeTransaction(anyString(), anyString(), anyDouble());
    }
}
