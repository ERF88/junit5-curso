package com.github.erf88.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode(exclude = { "id" })
public class Transacao {

    private Long id;
    private String descricao;
    private BigDecimal valor;
    private Conta conta;
    private LocalDate data;
    private Boolean status;

}
