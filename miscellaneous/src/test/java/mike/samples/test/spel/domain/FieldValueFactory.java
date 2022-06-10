package mike.samples.test.spel.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FieldValueFactory {

    private FieldValueFactory() {}
    
    public static FieldValue of(String name, Object objValue) {
	FieldType type = FieldType.CHAR;
	
	if ( objValue instanceof String ) {
	    type = FieldType.CHAR;
	} else if ( objValue instanceof LocalDate ) {
	    type = FieldType.DATE;
	} else if ( objValue instanceof Long || objValue instanceof Integer) {
	    type = FieldType.NUMBER;
	} else if ( objValue instanceof Double || objValue instanceof BigDecimal) {
	    type = FieldType.REAL;
	} else {
	    String objType = objValue == null ? null : objValue.getClass().getSimpleName();
	    throw new ExpressionException("Unsupported value type '%s' for field '%s'", objType, name);
	}
	
	String rawValue = objValue != null ? String.valueOf(objValue) : "";
	return new FieldValue(type, name, rawValue, objValue);
    }
    
    public static FieldValue of(FieldType type, String name, String rawValue) {
	Object objValue = switch(type) {
		case CHAR -> rawValue;
		case DATE -> rawValue.isBlank() ? null : LocalDate.parse(rawValue, DateTimeFormatter.BASIC_ISO_DATE);
		case NUMBER -> rawValue.isBlank() ? 0 : Long.parseLong(rawValue);
		case REAL -> rawValue.isBlank() ? Double.valueOf(0) : Double.valueOf(rawValue);
		};
	
	return new FieldValue(type, name, rawValue, objValue);
    }
}
