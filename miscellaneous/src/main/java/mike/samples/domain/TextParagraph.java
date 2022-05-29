package mike.samples.domain;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

public class TextParagraph {

    private static final Pattern spacePattern = Pattern.compile("\\s+");

    private final int num;
    private final int lines;
    private final AtomicInteger length = new AtomicInteger();
    private final AtomicLong words = new AtomicLong();

    public TextParagraph(int num, List<String> lines) {
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
	return String.format("TextSection [num=%s, lines=%s length=%s, words=%s]", this.getNum(), this.getLines(),
		this.getLength(), this.getWords());
    }
}
