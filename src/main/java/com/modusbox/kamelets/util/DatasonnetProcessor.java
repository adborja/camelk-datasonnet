package com.modusbox.kamelets.util;

import com.datasonnet.Mapper;
import com.datasonnet.document.DefaultDocument;
import com.datasonnet.document.Document;
import com.datasonnet.document.MediaTypes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangeProperty;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.apache.camel.support.DefaultMessage;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public class DatasonnetProcessor {
    private static final Logger log = LoggerFactory.getLogger(DatasonnetProcessor.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private String template;

    public void setTemplate(String template) {
        this.template = template;
    }

    public void process(final Exchange ex) throws Exception {
        var script = new String(Base64.decodeBase64(template));
        var jsonNodeBody = MAPPER.valueToTree(ex.getMessage().getBody());
        var mapper = new Mapper(script);
        var transformed = mapper
                .transform(new DefaultDocument<>(jsonNodeBody.toString(), MediaTypes.APPLICATION_JSON));
        ex.setProperty(Exchange.CONTENT_TYPE, transformed.getMediaType());
        var content = transformed.getContent();
        var contentMap = MAPPER.readValue(content, JsonNode.class);
        ex.getMessage().setBody(contentMap);
    }

    public static void main(String[] args) throws Exception {
        var context = new DefaultCamelContext();
        var ex = new DefaultExchange(context);

        var json = "{\n" +
                "\t\"employee\": {\n" +
                "\t\t\"name\": \"David\",\n" +
                "\t    \"lastName\": \"Borja\",\n" +
                "\t    \"designation\": \"Solutions Architect\",\n" +
                "\t    \"address\": {\n" +
                "\t        \"country\": \"US\"\n" +
                "\t    },\n" +
                "\t    \"salaryPerYear\": {\n" +
                "\t        \"value\": 30000\n" +
                "\t    }\n" +
                "\t}\n" +
                "}";

        var map = MAPPER.readValue(json, Map.class);
        var message = new DefaultMessage(ex);
        message.setBody(map);
        ex.setMessage(message);

        var processor = new DatasonnetProcessor();
        processor.setTemplate("ewogICAgIkNvdW50cnlDdXJyZW5jeSI6IHsKICAgICAgICAic0NvdW50cnlJU09Db2RlIjogcGF5bG9hZC5lbXBsb3llZS5hZGRyZXNzLmNvdW50cnkKICAgIH0KfQ==");
        processor.process(ex);
        /*
        var resultMap = (LinkedHashMap<?, ?>) ex.getMessage().getBody();
        var first = resultMap.keySet().stream().findFirst();
        if (first.isPresent()) {
            var t = resultMap.get(first.get());
            System.out.println(t);
         */
        var resultMap = ex.getMessage().getBody();
        System.out.println(resultMap);
        //System.out.println(resultMap.keySet().stream().findFirst().get());
    }
}
