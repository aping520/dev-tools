package com.lz.framework.tools.serializer;


import java.io.IOException;
import java.util.Date;

import org.joda.time.DateTime;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.lz.framework.tools.utils.StringUtil;

public class DateDeserializer extends JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonParser jp, DeserializationContext ctxt)throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        String value =  node.asText();
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        try {
        	return new DateTime(value).toDate();
        } catch (Exception e) {
            try {
                Long timestamp = Long.parseLong(value);
                return new Date(timestamp);
            } catch (Exception e1) {
                throw new RuntimeException(String.format("parser %s to Date fail", value));
            }
        }
    }
}
