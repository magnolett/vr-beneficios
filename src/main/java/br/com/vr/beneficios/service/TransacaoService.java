package br.com.vr.beneficios.service;

import br.com.vr.beneficios.entities.Cartao;
import br.com.vr.beneficios.entities.Transacao;
import br.com.vr.beneficios.exception.CardNotFoundException;
import br.com.vr.beneficios.exception.InsufficientBalanceException;
import br.com.vr.beneficios.exception.InvalidPasswordException;
import br.com.vr.beneficios.repository.CartaoRepository;
import br.com.vr.beneficios.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final CartaoRepository cartaoRepository;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ResponseEntity<?> authorizeTransaction(String cardNumber, String senhaCartao, double valor) {
        Cartao cartao = cartaoRepository.findByNumeroCartao(cardNumber)
                .orElseThrow(() -> new CardNotFoundException("CARTAO_INEXISTENTE"));

        validatePassword(cartao, senhaCartao);
        validateBalance(cartao, valor);

        cartao.setSaldo(cartao.getSaldo() - valor);
        cartaoRepository.save(cartao);

        Transacao transacao = Transacao.builder()
                .numeroCartao(cardNumber)
                .senhaCartao(senhaCartao)
                .valor(valor)
                .build();
        transacaoRepository.save(transacao);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    private void validatePassword(Cartao cartao, String senhaCartao) {
        Optional.of(cartao)
                .filter(c -> c.getSenhaCartao().equals(senhaCartao))
                .orElseThrow(() -> new InvalidPasswordException("SENHA_INVALIDA"));
    }

    private void validateBalance(Cartao cartao, double valor) {
        Optional.of(cartao)
                .filter(c -> c.getSaldo() >= valor)
                .orElseThrow(() -> new InsufficientBalanceException("SALDO_INSUFICIENTE"));
    }
}

