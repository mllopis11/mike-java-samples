package mike.samples.test.spel;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
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

/**
 * SPeL Date Comparison.
 * <p> 
 * From SPeL documentation on Relational Operators:
 * <pre>
 * Any value is always greater than null (X > null is always true) and is ever less than nothing (X < null is always false).
 * </pre>
 * 
 * @author Mike
 */
@DisplayName("FooValueDate")
class FooValueDateTest {

    private static final StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
    private static final SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
    
    @BeforeAll
    static void init() {
	evaluationContext.setVariable("GV_ASOFDATE", LocalDate.of(2021, 1, 15));
    }
    
    @ParameterizedTest
    @CsvSource({ "'', false", "20210130, true", "20210115, true", "20210114, false" })
    void should_return_expected_boolean_when_asofdate_ge_20210115(String asOfDate, boolean expected) {
	
	FooValue fooValue = FooValueFactory.of(FooValueType.DATE, "AS_OF_DATE", asOfDate);
	FooValues fooValues = FooValues.of(fooValue);
	Expression expression = this.compileCondition("ge");
	boolean eval = expression.getValue(evaluationContext, fooValues, Boolean.class);
	
	assertThat(eval).isEqualTo(expected);
    }
    
    @ParameterizedTest
    @CsvSource({ "'', true", "20210130, false", "20210115, true", "20210114, true" })
    void should_return_expected_boolean_when_asofdate_le_20210115(String asOfDate, boolean expected) {
	
	FooValue fooValue = FooValueFactory.of(FooValueType.DATE, "AS_OF_DATE", asOfDate);
	FooValues fooValues = FooValues.of(fooValue);
	Expression expression = this.compileCondition("le");
	boolean eval = expression.getValue(evaluationContext, fooValues, Boolean.class);
	
	assertThat(eval).isEqualTo(expected);
    }
    
    private Expression compileCondition(String relationalOperator) {
	String condition = String.format("values['%s'].getObjValue() %s #GV_ASOFDATE", "AS_OF_DATE", relationalOperator);
	return spelExpressionParser.parseExpression(condition);
    }
}

