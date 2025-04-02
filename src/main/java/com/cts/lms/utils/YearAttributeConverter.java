package com.cts.lms.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.Year;

@Converter(autoApply = true)
public class YearAttributeConverter implements AttributeConverter<Year, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Year attribute) {
        if(attribute!=null){
            System.out.println("Converting year to DB column: "+attribute.getValue());
            return attribute.getValue();
        }
        return null;
    }

    @Override
    public Year convertToEntityAttribute(Integer dbData) {
        if(dbData!=null){
            System.out.println("Converting DB column to year: "+dbData);
            return Year.of(dbData);
        }
        return null;
    }
}