package mike.samples.utilities;

import java.util.Collection;
import java.util.Map;

/**
 * Static convenience methods that help a method or constructor check 
 * whether it was invoked correctly (whether its preconditions have been met).<br>
 * The goal of this class is to improve readability of code;
 * 
 * @author Mike
 */
public class PreConditions {

    private PreConditions() {}
    
    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     * 
     * @param condition a boolean expression; will be negate
     * @param message   exception message used if the test fails; will be converted to a string using <i>String.format</i>.
     * @param args      optional exception message arguments 
     * @throws IllegalArgumentException if conditions is false.
     */
    public static void test(boolean condition, String message, Object... args) {
	if ( ! condition ) {
	    throw new IllegalArgumentException(String.format(message, args));
	}
    }
    
    /**
     * The object must not be null.
     * 
     * @param obj     object to assert
     * @param message exception message used if the test fails
     * @param args    optional exception message arguments 
     * @See {@link PreConditions#test(boolean, String, Object...)}
     */
    public static <T> T notNull(T obj, String message, Object... args) {
	PreConditions.test(obj != null, message, args);
	return obj; 
    }
    
    /**
     * The string value must not be null and must contain at least one non-whitespace character.
     * 
     * @param val     value to assert
     * @param message exception message used if the test fails
     * @param args    optional exception message arguments 
     * @See {@link PreConditions#test(boolean, String, Object...)}
     */
    public static String notBlank(String val, String message, Object... args) {
	PreConditions.test(val != null && ! val.isBlank(), message, args);
	return val;
    }
    
    /**
     * The object (String, Map or Collection) must not be null nor empty.
     * 
     * @param obj     object to assert
     * @param message exception message used if the test fails
     * @param args    optional exception message arguments 
     * @return the original object if conditions have been met.
     * @throws UnsupportedOperationException if object type is not supported. 
     */
    public static <T> T notEmpty(T obj, String message, Object... args) {
	
	PreConditions.notNull(obj, message, args);
	
	if (obj instanceof String string) {
	    PreConditions.test(!string.isEmpty(), message, args);
	} else if (obj instanceof Collection<?> collection) {
	    PreConditions.test(!collection.isEmpty(), message, args);
	} else if (obj instanceof Map<?, ?> map) {
	    PreConditions.test(!map.isEmpty(), message, args);
	} else {
	    throw new UnsupportedOperationException(
		    String.format("Function::notEmpty: object type '%s' not supported", obj.getClass().getName()));
	}
	
	return obj;
    }
}
