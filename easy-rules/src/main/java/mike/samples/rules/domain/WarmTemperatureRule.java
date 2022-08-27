package mike.samples.rules.domain;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Rule(name = "WarmTemperatureRule", description = "Increase temperature", priority = 2)
public class WarmTemperatureRule implements AirConditionerSetup {

    private static final Logger log = LoggerFactory.getLogger(WarmTemperatureRule.class);

    private final int expectedTemperature;
    
    public static WarmTemperatureRule max(int expectedTemperature) {
        return new WarmTemperatureRule(expectedTemperature);
    }
    
    private WarmTemperatureRule(int expectedTemperature) {
        this.expectedTemperature = expectedTemperature;
    }
    
    @Condition
    public boolean isCold(@Fact(TEMPERATURE_NAME) Integer temperature) {
        return temperature < expectedTemperature;
    }
    
    @Action
    public void ajust(Facts facts) {
        int temperature = facts.get(TEMPERATURE_NAME);
        log.debug("Rule::increase: {}={} (Warming !)", TEMPERATURE_NAME, temperature);
        facts.put(TEMPERATURE_NAME, temperature + 1);
    }
}
