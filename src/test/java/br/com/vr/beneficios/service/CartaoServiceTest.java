package br.com.vr.beneficios.service;

import br.com.vr.beneficios.entities.Cartao;
import br.com.vr.beneficios.repository.CartaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CartaoServiceTest {

    @Mock
    private CartaoRepository cartaoRepository;

    @InjectMocks
    private CartaoService cartaoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCard_Success() {
        String cardNumber = "1234567890123456";
        String password = "1234";

        when(cartaoRepository.findByNumeroCartao(anyString())).thenReturn(Optional.empty());

        Cartao savedCartao = Cartao.builder()
                .numeroCartao(cardNumber)
                .senhaCartao(password)
                .build();
        when(cartaoRepository.save(any(Cartao.class))).thenReturn(savedCartao);

        Cartao createdCartao = (Cartao) cartaoService.createCard(cardNumber, password).getBody();

        assertNotNull(createdCartao);
        assertEquals(cardNumber, createdCartao.getNumeroCartao());
        assertEquals(password, createdCartao.getSenhaCartao());

        verify(cartaoRepository, times(1)).findByNumeroCartao(cardNumber);

        verify(cartaoRepository, times(1)).save(any(Cartao.class));
    }


    @Test
    void testCreateCard_CardAlreadyExists() {
        String cardNumber = "1234567890123456";
        String password = "1234";

        Cartao existingCartao = Cartao.builder()
                .numeroCartao(cardNumber)
                .senhaCartao(password)
                .saldo(500.0)
                .build();

        when(cartaoRepository.findByNumeroCartao(cardNumber)).thenReturn(Optional.of(existingCartao));

        ResponseEntity<?> responseEntity = cartaoService.createCard(cardNumber, password);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        verify(cartaoRepository, times(1)).findByNumeroCartao(cardNumber);
        verify(cartaoRepository, never()).save(any(Cartao.class));
    }

    @Test
    void testCheckBalance_CardExists() {
        String cardNumber = "1234567890123456";
        double expectedBalance = 500.00;

        when(cartaoRepository.findByNumeroCartao(anyString())).thenReturn(Optional.of(new Cartao()));

        double balance = cartaoService.checkBalance(cardNumber);

        assertEquals(expectedBalance, balance);

        verify(cartaoRepository, times(1)).findByNumeroCartao(cardNumber);
    }

    @Test
    void testCheckBalance_CardNotExists() {
        String cardNumber = "1234567890123456";

        when(cartaoRepository.findByNumeroCartao(anyString())).thenReturn(Optional.empty());

        Double balance = cartaoService.checkBalance(cardNumber);

        assertNull(balance);

        verify(cartaoRepository, times(1)).findByNumeroCartao(cardNumber);
    }
}

