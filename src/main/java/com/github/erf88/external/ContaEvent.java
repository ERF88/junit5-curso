package com.github.erf88.external;

import com.github.erf88.domain.Conta;
import com.github.erf88.domain.EventType;

public interface ContaEvent {

    void dispatch(Conta conta, EventType type) throws Exception;

}
