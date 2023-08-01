package com.github.erf88.service;

import com.github.erf88.domain.Conta;
import com.github.erf88.domain.EventType;
import com.github.erf88.domain.exception.ValidationException;
import com.github.erf88.external.ContaEvent;
import com.github.erf88.repository.ContaRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class ContaService {

    private final ContaEvent event;
    private final ContaRepository repository;

    public Conta salvar(Conta conta) {
        boolean existe = repository.obterContaPorUsuario(conta.getUsuario().getId()).stream()
                .map(Conta::getNome)
                .anyMatch(nome -> conta.getNome().equals(nome));

        if(existe) {
            throw new ValidationException("Usuário já possui uma conta com este nome");
        }
        Conta contaPersistida = repository.salvar(new Conta(conta.getId(), conta.getNome() + LocalDateTime.now(), conta.getUsuario()));
        try {
            event.dispatch(contaPersistida, EventType.CREATED);
        } catch (Exception e) {
            repository.delete(contaPersistida);
            throw new RuntimeException("Falha na criação da conta, tente novamente");
        }
        return contaPersistida;
    }

}
