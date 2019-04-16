package com.gql.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@Getter
@Setter
public class Instrument {

    @Id
    private Integer id;
    private String isin;
    private String sedol;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIsin() {
        return isin;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    public String getSedol() {
        return sedol;
    }

    public void setSedol(String sedol) {
        this.sedol = sedol;
    }

}
