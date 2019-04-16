package com.gql;

import com.gql.entity.Trade;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TradeRepository extends CrudRepository<Trade, Integer> {
    List<Trade> findByCounterpartyAndRiskClass(String counterparty, String riskClass);
}
