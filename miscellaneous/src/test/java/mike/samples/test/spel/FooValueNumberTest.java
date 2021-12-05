package mike.samples.test.spel;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import mike.samples.test.spel.model.FooValue;
import mike.samples.test.spel.model.FooValueFactory;
import mike.samples.test.spel.model.FooValues;
import mike.samples.test.spel.model.FooValueType;

@DisplayName("FooValueNumber")
class FooValueNumberTest {

    private static final StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
    private static final SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
    
    @ParameterizedTest
    @CsvSource({ "0, false", "99, false", "3, false", "300, true", "100, true" })
    void should_return_expected_boolean_when_number_ge_100(String number, boolean expected) {
	
	FooValue fooValue = FooValueFactory.of(FooValueType.NUMBER, "FD_NUMBER", number);
	FooValues fooValues = FooValues.of(fooValue);
	Expression expression = this.compileCondition("ge");

	boolean eval = expression.getValue(evaluationContext, fooValues, Boolean.class);
	
	assertThat(eval).isEqualTo(expected);
    }
    
    private Expression compileCondition(String relationalOperator) {
	String condition = String.format("values['%s'].getObjValue() %s 100", "FD_NUMBER", relationalOperator);
	return spelExpressionParser.parseExpression(condition);
    }
}
