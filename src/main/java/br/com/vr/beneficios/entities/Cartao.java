package br.com.vr.beneficios.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numeroCartao")
    private String numeroCartao;

    @Column(name = "senha")
    private String senha;

    @Column(name = "saldo")
    private double saldo = 500.00;
}

