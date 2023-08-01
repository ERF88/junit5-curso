package com.github.erf88.domain;

import com.github.erf88.domain.exception.ValidationException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class Conta {

    private Long id;
    private String nome;
    private Usuario usuario;

    public Conta(Long id, String nome, Usuario usuario) {
        if (nome == null) throw new ValidationException("Nome é obrigatório");
        if (usuario == null) throw new ValidationException("Usuário é obrigatório");

        this.id = id;
        this.nome = nome;
        this.usuario = usuario;
    }
}
