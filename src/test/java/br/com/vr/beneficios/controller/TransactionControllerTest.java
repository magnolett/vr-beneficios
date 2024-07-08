package br.com.vr.beneficios.controller;

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
        Transaction transaction = new Transaction();
        transaction.setCardNumber("1234567890123456");
        transaction.setAmount(100.0);
        transaction.setAuthorized(true);

        when(transactionService.authorizeTransaction(anyString(), anyDouble(), anyBoolean()))
                .thenReturn(new Transaction()); // Mocking the service method to return true for successful authorization

        ResponseEntity<?> responseEntity = transactionController.authorizeTransaction(transaction);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Transaction authorized", responseEntity.getBody());

        verify(transactionService, times(1)).authorizeTransaction(anyString(), anyDouble(), anyBoolean());
    }
}
