package br.com.vr.beneficios.service;

import br.com.vr.beneficios.entities.Card;
import br.com.vr.beneficios.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;

    private final Card card;

    @Transactional
    public Card createCard(String cardNumber, String password) {
        return cardRepository.findByNumeroCartao(cardNumber)
                .orElseGet(() -> {
                    Card newCard = Card.builder()
                            .numeroCartao(cardNumber)
                            .senha(password)
                            .build();
                    return cardRepository.save(newCard);
                });
    }

    @Transactional(readOnly = true)
    public Double checkBalance(String cardNumber) {
        return cardRepository.findByNumeroCartao(cardNumber)
                .map(Card::getSaldo)
                .orElse(null);
    }
}

