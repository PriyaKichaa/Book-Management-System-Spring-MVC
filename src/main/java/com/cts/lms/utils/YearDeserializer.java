package com.cts.lms.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.Year;

public class YearDeserializer extends JsonDeserializer<Year> {
    @Override
    public Year deserialize(JsonParser p, DeserializationContext context) throws IOException {
        int year = p.getIntValue();
        System.out.println("Received year: "+ year);
        return Year.of(p.getIntValue());
    }
}