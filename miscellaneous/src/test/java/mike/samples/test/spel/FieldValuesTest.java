package mike.samples.test.spel;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import mike.samples.test.spel.domain.ExpressionException;
import mike.samples.test.spel.domain.FieldType;
import mike.samples.test.spel.domain.FieldValueFactory;
import mike.samples.test.spel.domain.FieldValues;

@DisplayName("FieldValues")
class FieldValuesTest {

    @Test
    void should_throw_mapper_exception_when_unsupported_value_object_type() {
	
	assertThatExceptionOfType(ExpressionException.class)
		.isThrownBy( () -> FieldValueFactory.of("FOO", true));
	
	assertThatExceptionOfType(ExpressionException.class)
		.isThrownBy( () -> FieldValueFactory.of("FOO", null));
    }
    
    @Test
    void should_return_foovalue_when_raw_value_of_type() {
	FieldValues fooValues = FieldValues.of(
		List.of(FieldValueFactory.of(FieldType.CHAR, "FD_CHAR", "My_Value"), 
			FieldValueFactory.of(FieldType.DATE, "FD_DATE", "20210112"),
			FieldValueFactory.of(FieldType.DATE, "FD_DATE_EMPTY", ""),
			FieldValueFactory.of(FieldType.NUMBER, "FD_NUMBER", "+54321"),
			FieldValueFactory.of(FieldType.NUMBER, "FD_NUMBER_EMPTY", ""),
			FieldValueFactory.of(FieldType.NUMBER, "FD_NUMBER_LONG", "1234567890000"),
			FieldValueFactory.of(FieldType.NUMBER, "FD_INTEGER", "-1234"),
			FieldValueFactory.of(FieldType.FLOAT, "FD_DOUBLE", "-987.25"),
			FieldValueFactory.of(FieldType.FLOAT, "FD_DOUBLE_EMPTY", ""))
		);
	
	assertThat(fooValues.getValues()).hasSize(9);
	assertThat(fooValues.getVariables()).isEmpty();
    }
    
    @Test
    void should_return_foovalue_when_supported_value_object_type() {
	
	FieldValues fooValues = FieldValues.of(
		List.of(FieldValueFactory.of("FD_CHAR", "My_Value"), 
			FieldValueFactory.of("FD_DATE", LocalDate.of(2021, 1, 12)),
			FieldValueFactory.of("FD_LONG", -54321L),
			FieldValueFactory.of("FD_INTEGER", 1234),
			FieldValueFactory.of("FD_DOUBLE", 987.25),
			FieldValueFactory.of("FD_DECIMAL", BigDecimal.valueOf(1200.659)))
		);
	
	fooValues.setVariable(FieldValueFactory.of("LC_TEST", "TEST"));
	
	assertThat(fooValues.getValues()).hasSize(6);
	assertThat(fooValues.getVariables()).hasSize(1);
	
    }
}
