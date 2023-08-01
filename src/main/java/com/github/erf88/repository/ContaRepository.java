package com.github.erf88.repository;

import com.github.erf88.domain.Conta;

import java.util.List;

public interface ContaRepository {

    Conta salvar(Conta conta);

    List<Conta> obterContaPorUsuario(Long usuarioId);

    void delete(Conta conta);

}
