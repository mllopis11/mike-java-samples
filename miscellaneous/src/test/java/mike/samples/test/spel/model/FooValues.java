package mike.samples.test.spel.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FooValues {

    private final Map<String, FooValue> values; 
    private final Map<String, FooValue> variables = new HashMap<>();
    
    public static FooValues of(FooValue value) {
	return new FooValues(List.of(value));
    }
    
    public static FooValues of(List<FooValue> values) {
	return new FooValues(values);
    }
    
    private FooValues(List<FooValue> values) {
	this.values = values.stream()
		.collect(Collectors.toUnmodifiableMap(FooValue::getName, Function.identity()));
    }

    public Map<String, FooValue> getValues() {
        return values;
    }

    public Optional<FooValue> getValue(String key) {
	return Optional.ofNullable(values.get(key));
    }
    
    public Map<String, FooValue> getVariables() {
        return variables;
    }

    public void setVariable(FooValue value) {
	this.variables.put(value.getName(), value);
    }
    
    public Optional<FooValue> getVariable(String key) {
	return Optional.ofNullable(variables.get(key));
    }

    @Override
    public String toString() {
	return String.format("FooValues [values=%s, variables=%s]", values, variables);
    }
}
