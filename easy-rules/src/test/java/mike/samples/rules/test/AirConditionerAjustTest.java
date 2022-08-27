package mike.samples.rules.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import mike.samples.rules.domain.AirConditioner;

class AirConditionerAjustTest {

    public static final String TEMPERATURE_NAME = "temperature";
    
    @ParameterizedTest
    @ValueSource(ints = { 17, 20, 25 })
    void ajustTemperatureTo20WhenTemperatureEquals(int current) {

        var airConditioner = new AirConditioner(20);
        airConditioner.start(current);

        assertThat(airConditioner.getFinalTemperature())
            .isEqualTo(airConditioner.getExpectedTemperature());
    }
}
