package mike.samples.test.spel;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import mike.samples.test.spel.domain.FieldType;
import mike.samples.test.spel.domain.FieldValue;
import mike.samples.test.spel.domain.FieldValueFactory;
import mike.samples.test.spel.domain.FieldValues;

@DisplayName("FooValueNumber")
class FieldValueNumberTest {

    private static final StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
    private static final SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
    
    @ParameterizedTest
    @CsvSource({ "0, false", "99, false", "3, false", "300, true", "100, true" })
    void should_return_expected_boolean_when_number_ge_100(String number, boolean expected) {
	
	FieldValue fooValue = FieldValueFactory.of(FieldType.NUMBER, "FD_NUMBER", number);
	FieldValues fooValues = FieldValues.of(fooValue);
	Expression expression = this.compileCondition("ge");

	boolean eval = expression.getValue(evaluationContext, fooValues, Boolean.class);
	
	assertThat(eval).isEqualTo(expected);
    }
    
    private Expression compileCondition(String relationalOperator) {
	String condition = String.format("values['%s'].getObjValue() %s 100", "FD_NUMBER", relationalOperator);
	return spelExpressionParser.parseExpression(condition);
    }
}
