package com.github.erf88.domain.builders;

import com.github.erf88.domain.Conta;
import com.github.erf88.domain.Transacao;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.github.erf88.domain.builders.ContaBuilder.umaConta;


public class TransacaoBuilder {
    private Transacao elemento;
    private TransacaoBuilder(){}

    public static TransacaoBuilder umaTransacao() {
        TransacaoBuilder builder = new TransacaoBuilder();
        inicializarDadosPadroes(builder);
        return builder;
    }

    public static void inicializarDadosPadroes(TransacaoBuilder builder) {
        builder.elemento = new Transacao();
        Transacao elemento = builder.elemento;

        elemento.setId(1L);
        elemento.setDescricao("Transação Válida");
        elemento.setValor(BigDecimal.valueOf(10));
        elemento.setConta(umaConta().agora());
        elemento.setData(LocalDate.now());
        elemento.setStatus(false);
    }

    public TransacaoBuilder comId(Long param) {
        elemento.setId(param);
        return this;
    }

    public TransacaoBuilder comDescricao(String param) {
        elemento.setDescricao(param);
        return this;
    }

    public TransacaoBuilder comValor(BigDecimal param) {
        elemento.setValor(param);
        return this;
    }

    public TransacaoBuilder comConta(Conta param) {
        elemento.setConta(param);
        return this;
    }

    public TransacaoBuilder comData(LocalDate param) {
        elemento.setData(param);
        return this;
    }

    public TransacaoBuilder comStatus(Boolean param) {
        elemento.setStatus(param);
        return this;
    }

    public Transacao agora() {
        return elemento;
    }

}