package com.gql.dao;

import com.gql.entity.Instrument;
import org.springframework.data.repository.CrudRepository;

public interface InstrumentRepository extends CrudRepository<Instrument, Integer>{
}
