package mike.samples.test.spel.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FooValueFactory {

    private FooValueFactory() {}
    
    public static FooValue of(String name, Object objValue) {
	FooValueType type = FooValueType.CHAR;
	
	if ( objValue instanceof String ) {
	    type = FooValueType.CHAR;
	} else if ( objValue instanceof LocalDate ) {
	    type = FooValueType.DATE;
	} else if ( objValue instanceof Long || objValue instanceof Integer) {
	    type = FooValueType.NUMBER;
	} else if ( objValue instanceof Double || objValue instanceof BigDecimal) {
	    type = FooValueType.FLOAT;
	} else {
	    String objType = objValue == null ? null : objValue.getClass().getSimpleName();
	    throw new MapperException("Unsupported value type '%s' for field '%s'", objType, name);
	}
	
	return new FooValue(type, name, String.valueOf(objValue), objValue);
    }
    
    public static FooValue of(FooValueType type, String name, String rawValue) {
	Object objValue = switch(type) {
		case CHAR -> rawValue;
		case DATE -> rawValue.isBlank() ? null : LocalDate.parse(rawValue, DateTimeFormatter.BASIC_ISO_DATE);
		case NUMBER -> rawValue.isBlank() ? 0 : Long.parseLong(rawValue);
		case FLOAT -> rawValue.isBlank() ? Double.valueOf(0) : Double.valueOf(rawValue);
		};
	
	return new FooValue(type, name, rawValue, objValue);
    }
}
