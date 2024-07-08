package br.com.vr.beneficios.service;

import br.com.vr.beneficios.entities.Card;
import br.com.vr.beneficios.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardService cardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCard_Success() {
        String cardNumber = "1234567890123456";
        String password = "1234";

        when(cardRepository.findByCardNumber(anyString())).thenReturn(Optional.empty());
        when(cardRepository.save(any(Card.class))).thenReturn(new Card());

        Card createdCard = cardService.createCard(cardNumber, password);

        assertEquals(cardNumber, createdCard.getNumeroCartao());
        assertEquals(password, createdCard.getSenha());

        verify(cardRepository, times(1)).findByCardNumber(cardNumber);
        verify(cardRepository, times(1)).save(any(Card.class));
    }

    @Test
    void testCreateCard_CardAlreadyExists() {
        String cardNumber = "1234567890123456";
        String password = "1234";

        when(cardRepository.findByCardNumber(anyString())).thenReturn(Optional.of(new Card()));

        Card createdCard = cardService.createCard(cardNumber, password);

        assertEquals(null, createdCard);

        verify(cardRepository, times(1)).findByCardNumber(cardNumber);
        verify(cardRepository, never()).save(any(Card.class));
    }

    @Test
    void testCheckBalance_CardExists() {
        String cardNumber = "1234567890123456";
        double expectedBalance = 500.00;

        when(cardRepository.findByCardNumber(anyString())).thenReturn(Optional.of(new Card()));

        double balance = cardService.checkBalance(cardNumber);

        assertEquals(expectedBalance, balance);

        verify(cardRepository, times(1)).findByCardNumber(cardNumber);
    }

    @Test
    void testCheckBalance_CardNotExists() {
        String cardNumber = "1234567890123456";

        when(cardRepository.findByCardNumber(anyString())).thenReturn(Optional.empty());

        double balance = cardService.checkBalance(cardNumber);

        assertEquals(-1.0, balance);

        verify(cardRepository, times(1)).findByCardNumber(cardNumber);
    }
}

