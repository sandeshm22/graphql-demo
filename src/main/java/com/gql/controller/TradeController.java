package com.gql.controller;

import com.gql.TradeRepository;
import com.gql.dao.InstrumentRepository;
import com.gql.entity.Instrument;
import com.gql.entity.Trade;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
public class TradeController {

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Value("classpath:trade.graphqls")
    private Resource schemaResource;
    private GraphQL graphQL;

    @PostConstruct
    public void init() throws IOException {
        File schemaFile = schemaResource.getFile();
        TypeDefinitionRegistry registry = new SchemaParser().parse(schemaFile);
        RuntimeWiring runtimeWiring = buildRuntimeWiring();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(registry, runtimeWiring);

        graphQL = GraphQL.newGraphQL(graphQLSchema).build();
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
                typeWriting.dataFetcher("allTrade", allTradeDataFetcher).dataFetcher("findTradesByCounterparty", tradeDataFetcher)).build();
        return runtimeWiring;
    }

    @PostMapping("/allTrade/")
    public String addTrades(@RequestBody List<Trade> trades) {
        tradeRepository.saveAll(trades);
        return "Committed successfully " + trades.size();
    }


    @PostMapping("/instrument/")
    public String addInstruments(@RequestBody List<Instrument> instruments) {
        instrumentRepository.saveAll(instruments);
        return "Committed successfully " + instruments.size();
    }

    @GetMapping("/findTradesByCounterparty/")
    public List<Trade> getTrades(String counterparty, String riskClass) {
        return (List<Trade>) tradeRepository.findByCounterpartyAndRiskClass(counterparty, riskClass);
    }


}
