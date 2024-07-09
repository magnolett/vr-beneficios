package br.com.vr.beneficios.service;

import br.com.vr.beneficios.entities.Cartao;
import br.com.vr.beneficios.repository.CartaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartaoService {

    private final CartaoRepository cartaoRepository;

    @Transactional
    public ResponseEntity<?> createCard(String cardNumber, String password) {
        return cartaoRepository.findByNumeroCartao(cardNumber)
                .map(existingCartao -> ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(existingCartao))
                .orElseGet(() -> {
                    Cartao newCartao = Cartao.builder()
                            .numeroCartao(cardNumber)
                            .senhaCartao(password)
                            .build();
                    cartaoRepository.save(newCartao);
                    return ResponseEntity.status(HttpStatus.CREATED).body(newCartao);
                });
    }

    @Transactional(readOnly = true)
    public Double checkBalance(String cardNumber) {
        return cartaoRepository.findByNumeroCartao(cardNumber)
                .map(Cartao::getSaldo)
                .orElse(null);
    }
}

