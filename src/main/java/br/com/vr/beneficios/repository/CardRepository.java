package br.com.vr.beneficios.repository;

import br.com.vr.beneficios.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    Optional<Card> findByCardNumber(String cardNumber);

    Optional<Card> findByCardNumberAndPasswordAndBalanceGreaterThanEqual(
            String cardNumber, String password, double balance);
}
