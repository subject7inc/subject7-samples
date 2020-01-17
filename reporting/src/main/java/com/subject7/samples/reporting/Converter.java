package com.subject7.samples.reporting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class Converter {
    public static <T> T convertToObjectT(Class<T> clazz, String jsonString) throws Exception {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper.readValue(jsonString, clazz);
        } catch (Exception e) {
            throw new Exception("Cannot convert to object of class " + clazz.getName() + " from: " + jsonString, e);
        }
    }
}
