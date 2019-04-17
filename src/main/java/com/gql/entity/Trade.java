package com.gql.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Getter
@Setter
public class Trade {

    @Id
    private Integer id;
    private String counterparty;
    private Double quantity;
    private Date tradeDateTime;
    private String riskClass;
    private Double notional;

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    //@Column(table = "Instrument")
    @JoinColumn(name="instrumentID")
    @ManyToOne
    private Instrument instrument;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(String counterparty) {
        this.counterparty = counterparty;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Date getTradeDateTime() {
        return tradeDateTime;
    }

    public void setTradeDateTime(Date tradeDateTime) {
        this.tradeDateTime = tradeDateTime;
    }

    public String getRiskClass() {
        return riskClass;
    }

    public void setRiskClass(String riskClass) {
        this.riskClass = riskClass;
    }

    public Double getNotional() {
        return notional;
    }

    public void setNotional(Double notional) {
        this.notional = notional;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "id=" + id +
                ", counterparty='" + counterparty + '\'' +
                ", quantity=" + quantity +
                ", tradeDateTime=" + tradeDateTime +
                ", riskClass='" + riskClass + '\'' +
                ", notional=" + notional +
                ", instrument=" + instrument +
                '}';
    }
}
