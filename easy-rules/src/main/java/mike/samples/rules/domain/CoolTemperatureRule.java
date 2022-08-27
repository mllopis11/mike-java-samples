package mike.samples.rules.domain;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Rule(name = "CoolTemperatureRule", description = "Decrease temperature", priority = 1)
class CoolTemperatureRule implements AirConditionerSetup {

    private static final Logger log = LoggerFactory.getLogger(CoolTemperatureRule.class);

    private final int expectedTemperature;
    
    public static CoolTemperatureRule min(int expectedTemperature) {
        return new CoolTemperatureRule(expectedTemperature);
    }
    
    private CoolTemperatureRule(int expectedTemperature) {
        this.expectedTemperature = expectedTemperature;
    }
    
    @Condition
    public boolean isHot(@Fact(TEMPERATURE_NAME) Integer temperature) {
        return temperature > expectedTemperature;
    }
    
    @Action
    public void ajust(Facts facts) {
        int temperature = facts.get(TEMPERATURE_NAME);
        log.debug("Rule::decrease: {}={} (Cooling !)", TEMPERATURE_NAME, temperature);
        facts.put(TEMPERATURE_NAME, temperature - 1);
    }
}
