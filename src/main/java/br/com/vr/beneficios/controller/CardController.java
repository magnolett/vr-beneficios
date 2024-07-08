package br.com.vr.beneficios.controller;

import br.com.vr.beneficios.entities.Card;
import br.com.vr.beneficios.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cartoes")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @PostMapping
    public ResponseEntity<?> createCard(@RequestBody Card card) {
        Card createdCard = cardService.createCard(card.getNumeroCartao(), card.getSenha());
        return ResponseEntity.status(createdCard != null ? HttpStatus.CREATED : HttpStatus.UNPROCESSABLE_ENTITY)
                .body(createdCard);
    }

    @GetMapping("/{numeroCartao}")
    public ResponseEntity<?> checkBalance(@PathVariable String numeroCartao) {
        Double balance = cardService.checkBalance(numeroCartao);
        return balance == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(balance);

    }

    @PostMapping("/transacoes")
    public ResponseEntity<?> authorizeTransaction(
            @RequestParam String cardNumber,
            @RequestParam String password,
            @RequestParam double amount) {

        boolean authorized = cardService.authorizeTransaction(cardNumber, password, amount);
        if (!authorized) {
            return ResponseEntity.unprocessableEntity().build();
        }
        return ResponseEntity.ok("Transaction authorized");
    }
}
