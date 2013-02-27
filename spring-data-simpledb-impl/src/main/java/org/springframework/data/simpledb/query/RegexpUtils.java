package org.springframework.data.simpledb.query;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.springframework.data.simpledb.query.parser.PatternConstants;
import org.springframework.util.StringUtils;

/**
 * Main Responsibility of RegexpUtils class is to work on regular expressions to build the Strings
 */
public class RegexpUtils {
    
    public static Map<String, String> createFieldNameRawParameterExpression(String pattern, String[] rawParameterExpressions){
        final Pattern regex = Pattern.compile(pattern);
        final Map<String, String> fieldNameWithParamHash = new LinkedHashMap<>();
        
        for (String eachExpression : rawParameterExpressions) {
        	final Matcher rawParameterMatcher = regex.matcher(eachExpression);
	        	 if (rawParameterMatcher.find()) {
	                 String fieldName = rawParameterMatcher.group(1);
	                 fieldNameWithParamHash.put(fieldName, eachExpression);
	        	 } else {
	        		 throw new IllegalArgumentException( "Parameter not found by Matcher: " + eachExpression + ", Matcher: " + rawParameterMatcher.toString());
	        	 }
        }
        
        return Collections.synchronizedMap(fieldNameWithParamHash);
    }
    
    public static List<String> createFieldNameList(String pattern, String[] rawParameterExpressions){
        final Pattern regex = Pattern.compile(pattern);
        final List<String> list = new ArrayList<>();
        
        for (String eachExpression : rawParameterExpressions) {
        	final Matcher rawParameterMatcher = regex.matcher(eachExpression);
	        	 if (rawParameterMatcher.find()) {
	                 String fieldName = rawParameterMatcher.group(1);
	                 list.add(fieldName);
	        	 } else {
	        		 throw new IllegalArgumentException( "Parameter not found by Matcher: " + eachExpression + ", Matcher: " + rawParameterMatcher.toString());
	        	 }
        }
        
        return Collections.synchronizedList(list);
    }
    
	public static String convertToSimpleDbExpression(PatternConstants queryPattern, String rawExpression, Field idField) {
		final Pattern regex = Pattern.compile(queryPattern.getPattternString());
		final Matcher matcher = regex.matcher(rawExpression);

		if (matcher.find()) {
			String fieldName = matcher.group(1);
			if (idField != null && fieldName.equals(idField.getName())) {
				 return StringUtils.replace(rawExpression.trim(), fieldName, "itemName()");
			} else {
				 return StringUtils.replace(rawExpression.trim(), fieldName,  "`" + fieldName + "`");
			}
		} else {
			throw new IllegalArgumentException("Usage: Wrong Parameter In Query Clause : " + rawExpression + ", select = {\"id\", \"name\"} for SELECT stmt, and where = {\"name\" = 3}" );
		}
	}
}