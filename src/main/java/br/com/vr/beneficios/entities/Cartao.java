package br.com.vr.beneficios.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Cartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numeroCartao")
    private String numeroCartao;

    @Column(name = "senha")
    private String senhaCartao;

    @Builder.Default
    @Column(name = "saldo")
    private double saldo = 500.00;
}

