package mike.samples.rules.domain;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.InferenceRulesEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AirConditioner implements AirConditionerSetup {

    private static final Logger log = LoggerFactory.getLogger(AirConditioner.class);
            
    private final int expectedTemperature;
    private final Rules rules = new Rules();
    private final RulesEngine rulesEngine = new InferenceRulesEngine();
    
    private int finalTemperature; 
    
    public AirConditioner(int expectedTemperature) {
        this.expectedTemperature = expectedTemperature;
        this.rules.register(CoolTemperatureRule.min(expectedTemperature));
        this.rules.register(WarmTemperatureRule.max(expectedTemperature));
    }
    
    public void start(int currentTemperature) {
        Facts facts = new Facts();
        facts.put(TEMPERATURE_NAME, currentTemperature);

        var label = this.temperatureLabel(currentTemperature);

        log.debug("AirCooler::start: {}={} ({})", TEMPERATURE_NAME, currentTemperature, label);
        
        this.rulesEngine.fire(rules, facts);
        
        finalTemperature = facts.get(TEMPERATURE_NAME);
        log.debug("AirCooler::stop: {}={}", TEMPERATURE_NAME, finalTemperature);
    }

    public int getExpectedTemperature() {
        return expectedTemperature;
    }

    
    public int getFinalTemperature() {
        return finalTemperature;
    }
    
    private String temperatureLabel(int currentTemperature) {
        if (currentTemperature > expectedTemperature) {
            return "Hot";
        } else if (currentTemperature < expectedTemperature) {
            return "Cold";
        } else {
            return "Even";
        }
    }
}
