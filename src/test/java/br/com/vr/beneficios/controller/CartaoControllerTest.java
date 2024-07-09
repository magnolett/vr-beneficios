package br.com.vr.beneficios.controller;

import br.com.vr.beneficios.entities.Card;
import br.com.vr.beneficios.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CardControllerTest {

    @Mock
    private CardService cardService;

    @InjectMocks
    private CardController cardController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCard_Success() {
        String cardNumber = "1234567890123456";
        String password = "1234";

        when(cardService.createCard(anyString(), anyString())).thenReturn(new Card());

        ResponseEntity<?> responseEntity = cardController.createCard(new Card(123L, cardNumber, password, 500.0));

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        verify(cardService, times(1)).createCard(cardNumber, password);
    }

    @Test
    void testCheckBalance_CardExists() {
        String cardNumber = "1234567890123456";
        double expectedBalance = 500.0;

        when(cardService.checkBalance(anyString())).thenReturn(expectedBalance);

        ResponseEntity<?> responseEntity = cardController.checkBalance(cardNumber);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedBalance, responseEntity.getBody());

        verify(cardService, times(1)).checkBalance(cardNumber);
    }
}

