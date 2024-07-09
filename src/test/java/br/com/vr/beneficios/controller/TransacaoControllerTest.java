package br.com.vr.beneficios.controller;

import br.com.vr.beneficios.dto.CartaoDTO;
import br.com.vr.beneficios.dto.TransacaoDTO;
import br.com.vr.beneficios.service.TransacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TransacaoControllerTest {

    @Mock
    private TransacaoService transacaoService;

    @InjectMocks
    private TransacaoController transacaoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthorizeTransaction_Success() {

        String cardNumber = "1234567890123456";
        String senhaCartao = "1234";
        CartaoDTO cartao = new CartaoDTO(cardNumber, senhaCartao);

        TransacaoDTO transacao = new TransacaoDTO(cardNumber, senhaCartao, 500.0);

        when(transacaoService.authorizeTransaction(anyString(), anyString(), anyDouble()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

        ResponseEntity<?> responseEntity = transacaoController.authorizeTransaction(transacao);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        verify(transacaoService, times(1)).authorizeTransaction(anyString(), anyString(), anyDouble());
    }
}
