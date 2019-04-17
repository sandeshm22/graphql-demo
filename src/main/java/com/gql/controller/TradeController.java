package com.gql.controller;

import com.gql.TradeRepository;
import com.gql.dao.InstrumentRepository;
import com.gql.entity.Instrument;
import com.gql.entity.Trade;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.execution.Execution;
import graphql.scalars.ExtendedScalars;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

@RestController
public class TradeController {

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Value("classpath:trade.graphqls")
    private Resource schemaResource;
    private GraphQL graphQL;

    private static int TRADE_ID = 1;
    private static int INSTRUMENT_ID = 1;


    @PostConstruct
    public void init() throws IOException {
        File schemaFile = schemaResource.getFile();
        TypeDefinitionRegistry registry = new SchemaParser().parse(schemaFile);
        RuntimeWiring runtimeWiring = buildRuntimeWiring();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(registry, runtimeWiring);

        graphQL = GraphQL.newGraphQL(graphQLSchema).build();
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        Runnable r = ()->{buildTrade();};
        executorService.schedule(r,1, SECONDS);
        executorService.schedule(r,1, SECONDS);
        executorService.schedule(r,1, SECONDS);
        executorService.schedule(r,1, SECONDS);
        executorService.schedule(r,1, SECONDS);
        executorService.schedule(r,1, SECONDS);
        executorService.schedule(r,1, SECONDS);
        executorService.schedule(r,1, SECONDS);
    }

    private void buildTrade() {
        Instrument instrument = new Instrument();
        instrument.setId(INSTRUMENT_ID);
        instrument.setIsin("ISIN-" + INSTRUMENT_ID);
        instrument.setSedol("SEDOL-" + INSTRUMENT_ID);
        INSTRUMENT_ID++;

        instrumentRepository.save(instrument);

        Trade trade = new Trade();
        trade.setId(TRADE_ID);
        trade.setInstrument(instrument);
        trade.setQuantity((double) (100 + TRADE_ID));
        trade.setNotional((double) (1000 + TRADE_ID));
        trade.setCounterparty("MORGON_STANLEY");
        trade.setRiskClass("OIL");
        TRADE_ID++;
        tradeRepository.save(trade);
        System.out.println(trade);
    }

    private RuntimeWiring buildRuntimeWiring() {
        DataFetcher<List<Trade>> allTradeDataFetcher = data -> {
            return (List<Trade>) tradeRepository.findAll();
        };

        DataFetcher<List<Trade>> tradeDataFetcher = data -> {
            return (List<Trade>) tradeRepository.findByCounterpartyAndRiskClass(data.getArgument("counterparty"), data.getArgument("riskClass"));
        };

        //RuntimeWiring.Builder builder = () -> builder.dataFetcher("allTrade", allTradeDataFetcher).dataFetcher("findTradesByCounterparty", tradeDataFetcher).build();
        RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring().type("Query", (TypeRuntimeWiring.Builder typeWriting) ->
                typeWriting.dataFetcher("allTrade", allTradeDataFetcher).dataFetcher("findTradesByCounterparty", tradeDataFetcher)).scalar(ExtendedScalars.DateTime).build();
        return runtimeWiring;
    }

    @PostMapping("/allTrade/")
    public ResponseEntity<Object> addTrades(@RequestBody  String query) {
        ExecutionResult execution = graphQL.execute(query);
        return new ResponseEntity<Object>(execution, HttpStatus.OK);
    }

    @PostMapping("/findTradesByCounterparty/")
    public ResponseEntity<Object> findTradesByCpty(@RequestBody  String query) {
        ExecutionResult execution = graphQL.execute(query);
        return new ResponseEntity<Object>(execution, HttpStatus.OK);
    }


}
