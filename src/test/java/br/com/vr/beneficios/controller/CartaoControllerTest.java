package br.com.vr.beneficios.controller;

import br.com.vr.beneficios.dto.CartaoDTO;
import br.com.vr.beneficios.service.CartaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CartaoControllerTest {

    @Mock
    private CartaoService cartaoService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private CartaoController cartaoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cartaoController = new CartaoController(cartaoService, objectMapper);
    }

    @Test
    void testCreateCard_Success() {
        String cardNumber = "1234567890123456";
        String password = "1234";

        when(cartaoService.createCard(anyString(), anyString())).thenReturn(new ResponseEntity(HttpStatus.CREATED));

        ResponseEntity<?> responseEntity = cartaoController.createCard(new CartaoDTO(cardNumber, password));

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        verify(cartaoService, times(1)).createCard(cardNumber, password);
    }

    @Test
    void testCheckBalance_CardExists() {
        String cardNumber = "1234567890123456";
        double expectedBalance = 500.0;

        when(cartaoService.checkBalance(anyString())).thenReturn(expectedBalance);

        ResponseEntity<?> responseEntity = cartaoController.checkBalance(cardNumber);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedBalance, responseEntity.getBody());

        verify(cartaoService, times(1)).checkBalance(cardNumber);
    }
}

