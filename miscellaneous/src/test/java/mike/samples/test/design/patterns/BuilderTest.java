package mike.samples.test.design.patterns;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class BuilderTest {

    @ParameterizedTest
    @CsvSource({ "1, foo" })
    void should_build_a_simple_bean_when(int index, String name) {

	var ex = SimpleBean.builder().index(index).name(name);

	assertThat(ex.index()).isEqualTo(index);
	assertThat(ex.name()).isEqualTo(name);
	assertThat(ex.toString()).isNotBlank().startsWith("SimpleBean");
    }
}

class SimpleBean {

    private final int index;
    private final String name;

    private SimpleBean(final int index, final String name) {
	this.index = index;
	this.name = name;
    }

    public int index() {
	return index;
    }

    public String name() {
	return name;
    }

    @Override
    public String toString() {
	return String.format("SimpleBean [index=%s, name=%s]", index, name);
    }

    /**
     * Builder
     */
    public static Builder builder() {
	return index -> name -> new SimpleBean(index, name);
    }

    public interface Builder {
	
	BeanName index(int index);

	interface BeanName {
	    SimpleBean name(final String name);
	}
    }
}
