package br.com.vr.beneficios.service;

import br.com.vr.beneficios.entities.Cartao;
import br.com.vr.beneficios.entities.Transacao;
import br.com.vr.beneficios.exception.CardNotFoundException;
import br.com.vr.beneficios.exception.InsufficientBalanceException;
import br.com.vr.beneficios.exception.InvalidPasswordException;
import br.com.vr.beneficios.repository.CartaoRepository;
import br.com.vr.beneficios.repository.TransacaoRepository;
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

class TransacaoServiceTest {

    @Mock
    private TransacaoRepository transacaoRepository;

    @Mock
    private CartaoRepository cartaoRepository;

    @InjectMocks
    private TransacaoService transacaoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthorizeTransaction() {
        String cardNumber = "1234567890123456";
        String senhaCartao = "1234";
        double amount = 100.0;

        Cartao cartao = Cartao.builder()
                .id(1L)
                .numeroCartao(cardNumber)
                .senhaCartao(senhaCartao)
                .saldo(500.0)
                .build();
        when(cartaoRepository.findByNumeroCartao(eq(cardNumber))).thenReturn(Optional.of(cartao));

        Transacao mockedTransacao = Transacao.builder()
                .id(1L)
                .numeroCartao(cardNumber)
                .senhaCartao(senhaCartao)
                .valor(amount)
                .build();
        when(transacaoRepository.save(any(Transacao.class))).thenReturn(mockedTransacao);

        ResponseEntity<?> responseEntity = transacaoService.authorizeTransaction(cardNumber, senhaCartao, amount);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        verify(cartaoRepository, times(1)).findByNumeroCartao(eq(cardNumber));
        verify(cartaoRepository, times(1)).save(eq(cartao));
        verify(transacaoRepository, times(1)).save(any(Transacao.class));
    }

    @Test
    void testAuthorizeTransactionSenhaInvalida() {
        String cardNumber = "1234567890123456";
        String senhaCartao = "invalid_password";
        double amount = 100.0;

        Cartao cartao = Cartao.builder()
                .numeroCartao(cardNumber)
                .senhaCartao("correct_password")
                .saldo(500.0)
                .build();
        when(cartaoRepository.findByNumeroCartao(eq(cardNumber))).thenReturn(Optional.of(cartao));

        assertThrows(InvalidPasswordException.class, () -> {
            transacaoService.authorizeTransaction(cardNumber, senhaCartao, amount);
        });

        verify(cartaoRepository, times(1)).findByNumeroCartao(eq(cardNumber));
        verifyNoInteractions(transacaoRepository);
    }

    @Test
    void testAuthorizeTransactionSaldoInsuficiente() {
        String cardNumber = "1234567890123456";
        String senhaCartao = "1234";
        double amount = 600.0;

        Cartao cartao = Cartao.builder()
                .numeroCartao(cardNumber)
                .senhaCartao(senhaCartao)
                .saldo(500.0)
                .build();
        when(cartaoRepository.findByNumeroCartao(eq(cardNumber))).thenReturn(Optional.of(cartao));

        assertThrows(InsufficientBalanceException.class, () -> {
            transacaoService.authorizeTransaction(cardNumber, senhaCartao, amount);
        });

        verify(cartaoRepository, times(1)).findByNumeroCartao(eq(cardNumber));
        verifyNoInteractions(transacaoRepository);
    }

    @Test
    void testAuthorizeTransactionCartaoInexistente() {
        String cardNumber = "non_existing_card_number";
        String senhaCartao = "1234";
        double amount = 100.0;

        when(cartaoRepository.findByNumeroCartao(eq(cardNumber))).thenReturn(Optional.empty());

        assertThrows(CardNotFoundException.class, () -> {
            transacaoService.authorizeTransaction(cardNumber, senhaCartao, amount);
        });

        verify(cartaoRepository, times(1)).findByNumeroCartao(eq(cardNumber));
        verifyNoInteractions(transacaoRepository);

    }
}

