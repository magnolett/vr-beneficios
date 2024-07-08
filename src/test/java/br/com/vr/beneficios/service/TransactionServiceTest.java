package br.com.vr.beneficios.service;

import br.com.vr.beneficios.entities.Transaction;
import br.com.vr.beneficios.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthorizeTransaction() {
        String cardNumber = "1234567890123456";
        double amount = 100.0;
        boolean authorized = true;

        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());

        Transaction authorizedTransaction = transactionService.authorizeTransaction(cardNumber, amount, authorized);

        assertEquals(cardNumber, authorizedTransaction.getCardNumber());
        assertEquals(amount, authorizedTransaction.getAmount());
        assertEquals(authorized, authorizedTransaction.isAuthorized());

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }
}

