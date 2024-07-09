package br.com.vr.beneficios.controller;

import br.com.vr.beneficios.dto.TransacaoDTO;
import br.com.vr.beneficios.service.TransacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transacoes")
@RequiredArgsConstructor
public class TransacaoController {

    private final TransacaoService transacaoService;

    @PostMapping
    public ResponseEntity<String> authorizeTransaction(@RequestBody TransacaoDTO transacao) {
        transacaoService.authorizeTransaction(transacao.getNumeroCartao(), transacao.getSenhaCartao(), transacao.getValor());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("OK");
    }
}

