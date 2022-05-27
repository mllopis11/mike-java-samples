package mike.samples.test.threads;

import static org.assertj.core.api.Assertions.*;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mike.samples.utilities.Resource;
import mike.samples.utilities.ResourceReader;
import mike.samples.utilities.StreamReader;

/**
 * ExecutorService <b>WorkStealingPool</b> is a ForkJoinPool with preconfigured
 * default parameters:<br>
 * - availableProcessors: <i>number of processors availables</i><br>
 * - defaultForkJoinWorkerThreadFactory: <i>default thread factory to return new
 * threads</i><br>
 * - uncaughtExceptionHandler: <i>no UncaughtExceptionHandler thread configured
 * (null)</i><br>
 * - aysncMode: <i>true (makes it work in aysnc mode and sets the FIFO order for
 * forked tasks)</i><br>
 * 
 * @author Mike
 */
class WorkStealingPoolTest {

    private static final Logger log = LoggerFactory.getLogger(WorkStealingPoolTest.class);

    @Test
    void should_return_text_information() throws Exception {
	var sections = this.readLoremIpsum();

	assertThat(sections).isNotNull().hasSize(5);
	assertThat(sections.get(1)).hasSize(2);
	assertThat(sections.get(2)).hasSize(3);
	assertThat(sections.get(3)).hasSize(3);
	assertThat(sections.get(4)).hasSize(2);
	assertThat(sections.get(5)).hasSize(3);

	var executor = Executors.newWorkStealingPool(2);

	sections.entrySet().stream()
		.map(TextMetricWorker::new)
		.map(worker -> CompletableFuture.supplyAsync(() -> {
		    	try {
		    	    return worker.call();
		    	} catch (Exception ex) {
		    	    log.error("Worker {}: {}", worker.getSectionNo(), ex.getMessage());
		    	    return null;
		    	}
		}, executor))
		.filter(s -> s != null)
		.map(cf -> cf.join())
		.forEach(s -> log.debug(s.toString()));

	executor.shutdown();
	executor.awaitTermination(10, TimeUnit.SECONDS);
    }

    private Map<Integer, List<String>> readLoremIpsum() throws Exception {
	var resource = Resource.of("data/lorem-ipsum.txt");

	var pNum = new AtomicInteger(1);

	try (StreamReader reader = ResourceReader.newResourceStreamReader(resource)) {
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
    }
}

class TextMetricWorker implements Callable<Section> {

    private static final Logger log = LoggerFactory.getLogger(TextMetricWorker.class);

    private final Entry<Integer, List<String>> section;

    TextMetricWorker(Entry<Integer, List<String>> section) {
	this.section = section;
    }

    public int getSectionNo() {
	return this.section.getKey();
    }

    @Override
    public Section call() throws Exception {
	log.debug("ComputeMetrics: num={}", section.getKey());
	return new Section(this.section.getKey(), this.section.getValue());
    }
}

class Section {

    private static final Pattern spacePattern = Pattern.compile("\\s+");

    private final int num;
    private final int lines;
    private final AtomicInteger length = new AtomicInteger();
    private final AtomicLong words = new AtomicLong();

    Section(int num, List<String> lines) {
	this.num = num;
	this.lines = lines.size();
	lines.stream().forEach(line -> {
	    this.length.addAndGet(line.length());
	    this.words.addAndGet(spacePattern.splitAsStream(line).count());
	});
    }

    public int getNum() {
	return num;
    }

    public int getLines() {
	return lines;
    }

    public long getLength() {
	return length.get();
    }

    public long getWords() {
	return words.get();
    }

    @Override
    public String toString() {
	return String.format("Section [num=%s, lines=%s length=%s, words=%s]", this.getNum(), this.getLines(),
		this.getLength(), this.getWords());
    }
}