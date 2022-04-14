package com.modusbox.kamelets.datasonnet.util;

import com.datasonnet.Mapper;
import com.datasonnet.document.DefaultDocument;
import com.datasonnet.document.MediaTypes;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.modusbox.kamelets.datasonnet.model.ExchangeWrapper;
import org.apache.camel.Exchange;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatasonnetProcessor {
    private static final Logger log = LoggerFactory.getLogger(DatasonnetProcessor.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private String template;
    private String resultType;

    public void setTemplate(String template) {
        this.template = template;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public void process(final Exchange ex) throws Exception {
        log.info("Exchange Message Headers: {}", ex.getMessage().getHeaders());
        var script = new String(Base64.decodeBase64(template));
        var exchangeWrapper = ExchangeWrapper.fromExchange(ex);
        var jsonNodeBody = MAPPER.valueToTree(exchangeWrapper);
        var mapper = new Mapper(script);
        var transformed = mapper
                .transform(new DefaultDocument<>(jsonNodeBody.toString(), MediaTypes.APPLICATION_JSON));
        var content = MAPPER.readValue(transformed.getContent(), Class.forName(resultType));
        ex.setProperty(Exchange.CONTENT_TYPE, transformed.getMediaType());
        ex.getMessage().setBody(content);
    }
}
