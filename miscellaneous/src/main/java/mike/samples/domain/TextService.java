package mike.samples.domain;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextService {

    private static final Logger log = LoggerFactory.getLogger(TextService.class);

    private static final Random random = new Random();

    private TextService() {}

    public static TextMetrics computeMetrics(Map<Integer, List<String>> paragraphs, int parallelism) {

	var workerPool = new ExecutorWorkerPool(parallelism);

	// @formatter:off
	var metrics = paragraphs.entrySet().stream()
		.map(p -> CompletableFuture.supplyAsync(
			() -> TextService.analyzeSection(p), workerPool.getExecutor())
			.thenApply(s -> { 
			    log.debug(s.toString()); 
			    return s;
			}))
		.toList()
		.stream()
		.map(CompletableFuture::join)
		.collect(TextMetrics.compute());
	// @formatter:on

	log.debug("{}", metrics);

	workerPool.shutdown();
	
	return metrics;
    }

    private static TextParagraph analyzeSection(Entry<Integer, List<String>> text) {

	int seconds = random.nextInt((5 - 1) + 1) + 1;

	log.debug("ComputeMetrics: num={} (delay: {} seconds)", text.getKey(), seconds);

	delay(seconds);

	return new TextParagraph(text.getKey(), text.getValue());
    }

    private static void delay(int seconds) {
	try {
	    TimeUnit.SECONDS.sleep(seconds);
	} catch (InterruptedException ie) {
	    log.warn("Function::delay: interrupted, reason: {}", ie.getMessage());
	    Thread.currentThread().interrupt();
	}
    }

    private static class ExecutorWorkerPool {

	private ExecutorService executor;

	public ExecutorWorkerPool(int parallelism) {
	    this.executor = Executors.newWorkStealingPool(parallelism);
	}

	public ExecutorService getExecutor() {
	    return this.executor;
	}

	public void shutdown() {
	    this.executor.shutdown();

	    try {
		this.executor.awaitTermination(10, TimeUnit.SECONDS);
	    } catch (InterruptedException ie) {
		log.warn("Executor::termination: not terminated within the expected time: {}", ie.getMessage());
		Thread.currentThread().interrupt();
	    }
	}
    }
}
