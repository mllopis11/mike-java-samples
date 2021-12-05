package mike.samples.test.spel;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import mike.samples.test.spel.model.FooValueFactory;
import mike.samples.test.spel.model.FooValueType;
import mike.samples.test.spel.model.FooValues;
import mike.samples.test.spel.model.MapperException;

@DisplayName("FooValues")
class FooValuesTest {

    @Test
    void should_throw_mapper_exception_when_unsupported_value_object_type() {
	
	assertThatExceptionOfType(MapperException.class)
		.isThrownBy( () -> FooValueFactory.of("FOO", true));
	
	assertThatExceptionOfType(MapperException.class)
		.isThrownBy( () -> FooValueFactory.of("FOO", null));
    }
    
    @Test
    void should_return_foovalue_when_raw_value_of_type() {
	FooValues fooValues = FooValues.of(
		List.of(FooValueFactory.of(FooValueType.CHAR, "FD_CHAR", "My_Value"), 
			FooValueFactory.of(FooValueType.DATE, "FD_DATE", "20210112"),
			FooValueFactory.of(FooValueType.DATE, "FD_DATE_EMPTY", ""),
			FooValueFactory.of(FooValueType.NUMBER, "FD_NUMBER", "+54321"),
			FooValueFactory.of(FooValueType.NUMBER, "FD_NUMBER_EMPTY", ""),
			FooValueFactory.of(FooValueType.NUMBER, "FD_NUMBER_LONG", "1234567890000"),
			FooValueFactory.of(FooValueType.NUMBER, "FD_INTEGER", "-1234"),
			FooValueFactory.of(FooValueType.FLOAT, "FD_DOUBLE", "-987.25"),
			FooValueFactory.of(FooValueType.FLOAT, "FD_DOUBLE_EMPTY", ""))
		);
	
	assertThat(fooValues.getValues()).hasSize(9);
	assertThat(fooValues.getVariables()).isEmpty();
    }
    
    @Test
    void should_return_foovalue_when_supported_value_object_type() {
	
	FooValues fooValues = FooValues.of(
		List.of(FooValueFactory.of("FD_CHAR", "My_Value"), 
			FooValueFactory.of("FD_DATE", LocalDate.of(2021, 1, 12)),
			FooValueFactory.of("FD_LONG", -54321L),
			FooValueFactory.of("FD_INTEGER", 1234),
			FooValueFactory.of("FD_DOUBLE", 987.25),
			FooValueFactory.of("FD_DECIMAL", BigDecimal.valueOf(1200.659)))
		);
	
	fooValues.setVariable(FooValueFactory.of("LC_TEST", "TEST"));
	
	assertThat(fooValues.getValues()).hasSize(6);
	assertThat(fooValues.getVariables()).hasSize(1);
	
    }
}
