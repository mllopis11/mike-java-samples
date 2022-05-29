package mike.samples.test.threads;

import static org.assertj.core.api.Assertions.*;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import mike.samples.domain.TextService;
import mike.samples.utilities.Resource;
import mike.samples.utilities.StreamReader;

/**
 * ExecutorService <b>WorkStealingPool</b> is a ForkJoinPool with preconfigured default parameters:<br>
 * - availableProcessors: <i>number of processors availables</i><br>
 * - defaultForkJoinWorkerThreadFactory: <i>default thread factory to return new threads</i><br>
 * - uncaughtExceptionHandler: <i>no UncaughtExceptionHandler thread configured (null)</i><br>
 * - aysncMode: <i>true (makes it work in aysnc mode and sets the FIFO order for forked tasks)</i><br>
 * 
 * @author Mike
 */
class WorkStealingPoolTest {

    @Test
    void should_return_text_information() throws Exception {
	var sections = this.readLoremIpsum();

	assertThat(sections).isNotNull().hasSize(5);
	assertThat(sections.get(1)).hasSize(2);
	assertThat(sections.get(2)).hasSize(3);
	assertThat(sections.get(3)).hasSize(3);
	assertThat(sections.get(4)).hasSize(2);
	assertThat(sections.get(5)).hasSize(3);

	var statictics = TextService.computeMetrics(sections, 2);
	
	assertThat(statictics).isNotNull();
	assertThat(statictics.getParagraphs()).isEqualTo(5);
    }

    private Map<Integer, List<String>> readLoremIpsum() throws Exception {
	var resource = Resource.of("data/lorem-ipsum.txt");

	var pNum = new AtomicInteger(1);

	// @formatter:off
	try (StreamReader reader = resource.streamReader()) {
	    return reader.lines().map( line -> { 
			if ( ! line.isBlank() ) { 
			    return new AbstractMap.SimpleEntry<>(pNum.get(), line); 
			} else { 
			    pNum.incrementAndGet();
			    return null;
			}})
		    .filter(e -> e != null)
		    .collect(Collectors.groupingBy(Entry::getKey, Collectors.mapping(Entry::getValue, Collectors.toList())));
	}
	// @formatter:on
    }
}



