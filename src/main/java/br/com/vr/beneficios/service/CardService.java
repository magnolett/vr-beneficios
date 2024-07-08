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
        return cardRepository.findByCardNumber(cardNumber)
                .orElseGet(() -> {
                    Card newCard = new Card();
                    newCard.setNumeroCartao(cardNumber);
                    newCard.setSenha(password);
                    return cardRepository.save(newCard);
                });
    }

    @Transactional(readOnly = true)
    public Double checkBalance(String cardNumber) {
        return cardRepository.findByCardNumber(cardNumber)
                .map(Card::getSaldo)
                .orElse(null);
    }

    @Transactional
    public boolean authorizeTransaction(String cardNumber, String password, double amount) {
        return cardRepository.findByCardNumberAndPasswordAndBalanceGreaterThanEqual(
                        cardNumber, password, amount)
                .map(card -> {
                    card.setSaldo(card.getSaldo() - amount);
                    cardRepository.save(card);
                    return true;
                })
                .orElse(false);
    }
}

