package br.com.vr.beneficios.controller;

import br.com.vr.beneficios.dto.CartaoDTO;
import br.com.vr.beneficios.service.CartaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cartoes")
@RequiredArgsConstructor
public class CartaoController {

    private final CartaoService cartaoService;
    private final ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<?> createCard(@RequestBody CartaoDTO cartao) {
        ResponseEntity<?> responseEntity = cartaoService.createCard(cartao.getNumeroCartao(), cartao.getSenhaCartao());
        CartaoDTO cartaoResponseDTO = objectMapper.convertValue(responseEntity.getBody(), CartaoDTO.class);
        return ResponseEntity.status(responseEntity.getStatusCode())
                .body(cartaoResponseDTO);
    }

    @GetMapping("/{numeroCartao}")
    public ResponseEntity<?> checkBalance(@PathVariable String numeroCartao) {
        Double balance = cartaoService.checkBalance(numeroCartao);
        return balance == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(balance);

    }
}
