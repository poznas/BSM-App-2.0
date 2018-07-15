package com.bsm.mobile.common;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class NonNullObjectMapper{

    public static Map<String, Object> map(Object object){

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        Map<String, Object> result = objectMapper.convertValue(
                object, new TypeReference<Map<String, Object>>() {});

        Log.d(NonNullObjectMapper.class.getSimpleName(), "result : " + result );
        return result;
    }
}
