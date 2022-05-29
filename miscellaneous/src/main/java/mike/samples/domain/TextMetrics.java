package mike.samples.domain;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Collector.Characteristics;

public class TextMetrics implements Consumer<TextParagraph> {

    private final AtomicInteger paragraphs = new AtomicInteger();
    private final AtomicLong length = new AtomicLong();
    private final AtomicInteger lines = new AtomicInteger();
    private final AtomicLong words = new AtomicLong();
    
    public static Collector<TextParagraph, ?, TextMetrics> compute() {
        return Collector.of(
        	TextMetrics::new, TextMetrics::accept,
        	TextMetrics::combine, TextMetrics::finisher, 
        	Characteristics.CONCURRENT, Characteristics.IDENTITY_FINISH, Characteristics.UNORDERED);
    }
    
    private TextMetrics() {}
    
    @Override
    public void accept(TextParagraph section) {
	this.paragraphs.incrementAndGet();
	this.length.addAndGet(section.getLength());
	this.lines.addAndGet(section.getLines());
	this.words.addAndGet(section.getWords());
    }

    public TextMetrics combine(TextMetrics other) {
        return this;
    }

    public TextMetrics finisher() {
        return this;
    }
    
    public int getParagraphs() {
        return paragraphs.get();
    }

    public long getLength() {
        return length.get();
    }

    public int getLines() {
        return lines.get();
    }

    public long getWords() {
        return words.get();
    }

    @Override
    public String toString() {
	return String.format(
		"TextMetrics [paragraphs=%s, length=%s, lines=%s, words=%s]", getParagraphs(), getLength(), getLines(), getWords());
    }
}
