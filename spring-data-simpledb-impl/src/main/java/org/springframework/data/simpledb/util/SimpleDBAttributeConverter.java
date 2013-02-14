package org.springframework.data.simpledb.util;

import org.springframework.data.mapping.model.MappingException;
import org.springframework.util.Assert;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

public final class SimpleDBAttributeConverter {


    private SimpleDBAttributeConverter() {
    	/* utility class */
    }

    public static String encode(Object ob) {
        String integerNumberEncoding = AmazonSimpleDBUtil.encodeAsIntegerNumber(ob);

        if(integerNumberEncoding!=null){
            return integerNumberEncoding;
        }

        String realNumberEncoding = AmazonSimpleDBUtil.encodeAsRealNumber(ob);
        if(realNumberEncoding != null){
            return  realNumberEncoding;
        }

        if (ob instanceof Date) {
            Date d = (Date) ob;
            return AmazonSimpleDBUtil.encodeDate(d);
        } else if (ob instanceof byte[]) {
            return AmazonSimpleDBUtil.encodeByteArray((byte[]) ob);
        }

        return ob.toString();
    }

    public static List<String> encodePrimitiveArray(final Object primitiveCollectionFieldValues) {
        Assert.notNull(primitiveCollectionFieldValues);

        final List<String> attributeValues = new ArrayList<>();
        final int primitiveCollLength = Array.getLength(primitiveCollectionFieldValues);

        for (int idx = 0; idx < primitiveCollLength; idx++) {
            Object itemValue = Array.get(primitiveCollectionFieldValues, idx);
            attributeValues.add(encode(itemValue));
        }

        return attributeValues;
    }


    public static Object decodeToFieldOfType(String value, Class<?> retType) throws ParseException {
        Object val = null;


        if (Integer.class.isAssignableFrom(retType) || retType == int.class) {
            val = AmazonSimpleDBUtil.decodeIntegerNumber(value).intValue();
        } else if (Long.class.isAssignableFrom(retType) || retType == long.class) {
            val = AmazonSimpleDBUtil.decodeIntegerNumber(value).longValue();
        }
        if (Short.class.isAssignableFrom(retType) || retType == short.class) {
            val = AmazonSimpleDBUtil.decodeIntegerNumber(value).shortValue();
        } else if (Byte.class.isAssignableFrom(retType) || retType == byte.class) {
            val = AmazonSimpleDBUtil.decodeIntegerNumber(value).byteValue();
        } else if (Float.class.isAssignableFrom(retType) || retType == float.class) {
            val = AmazonSimpleDBUtil.decodeRealNumber(value).floatValue();
        } else if (Double.class.isAssignableFrom(retType) || retType == double.class) {
            val = AmazonSimpleDBUtil.decodeRealNumber(value).doubleValue();
        } else if (BigDecimal.class.isAssignableFrom(retType)) {
            val = AmazonSimpleDBUtil.decodeRealNumber(value);
        } else if (byte[].class.isAssignableFrom(retType)) {
            val = AmazonSimpleDBUtil.decodeByteArray(value);
        } else if (Date.class.isAssignableFrom(retType)) {
            val = AmazonSimpleDBUtil.decodeDate(value);
        } else if (Boolean.class.isAssignableFrom(retType) || retType == boolean.class) {
            val = Boolean.parseBoolean(value);
        } else if (String.class.isAssignableFrom(retType)) {
            val = value;
        }

        return val;
    }

    public static Object decodeToPrimitiveArray(List<String> fromSimpleDbAttValues, Class<?> retType) throws ParseException {
        Object primitiveCollection = Array.newInstance(retType, fromSimpleDbAttValues.size());
        int idx = 0;

        for (Iterator<String> iterator = fromSimpleDbAttValues.iterator(); iterator.hasNext(); idx++) {
            Array.set(primitiveCollection, idx, decodeToFieldOfType(iterator.next(), retType));
        }

        return primitiveCollection;
    }






}