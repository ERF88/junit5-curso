package com.github.erf88.service;

import com.github.erf88.domain.Transacao;
import com.github.erf88.domain.exception.ValidationException;
import com.github.erf88.external.ClockService;
import com.github.erf88.repository.TransacaoDao;

import java.time.LocalDateTime;
import java.util.Date;

public class TransacaoService {

    private TransacaoDao transacaoDao;
    private ClockService clock;

    public Transacao salvar(Transacao transacao) {

//        if(clock.getCurrentTime().getHour() > 8) throw new RuntimeException("Tente novamente amanhã");
//        if(new Date().getHours() > 8) throw new RuntimeException("Tente novamente amanhã");
        if(getTime().getHour() > 10) throw new RuntimeException("Tente novamente amanhã");

        validarCamposObrigatorios(transacao);

        return transacaoDao.salvar(transacao);
    }

    private void validarCamposObrigatorios(Transacao transacao) {
        if(transacao.getDescricao() == null) throw new ValidationException("Descrição inexistente");
        if(transacao.getValor() == null) throw new ValidationException("Valor inexistente");
        if(transacao.getData() == null) throw new ValidationException("Data inexistente");
        if(transacao.getConta() == null) throw new ValidationException("Conta inexistente");
        if(transacao.getStatus() == null) transacao.setStatus(false);
    }

    protected LocalDateTime getTime() {
        return LocalDateTime.now();
    }

}
