package mike.samples.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Collector.Characteristics;

public class BankTransactionMetrics implements Consumer<BankTransaction> {

    private final AtomicInteger count = new AtomicInteger();
    private final AtomicReference<BigDecimal> averageAmount = new AtomicReference<>(BigDecimal.ZERO);
    private final AtomicReference<BigDecimal> totalAmount = new AtomicReference<>(BigDecimal.ZERO);
    private final AtomicReference<BigDecimal> maxAmount = new AtomicReference<>(BigDecimal.ZERO);
    private final AtomicReference<LocalDateTime> lastTransactionDateTime = new AtomicReference<>(LocalDateTime.MIN);

    public static Collector<BankTransaction, BankTransactionMetrics, BankTransactionMetrics> compute() {
	return Collector.of(BankTransactionMetrics::new, BankTransactionMetrics::accept,
		BankTransactionMetrics::combine, BankTransactionMetrics::finisher,
		Characteristics.CONCURRENT, Characteristics.IDENTITY_FINISH, Characteristics.UNORDERED);
    }

    private BankTransactionMetrics() {}

    @Override
    public void accept(BankTransaction t) {
	this.count.incrementAndGet();

	// Set the Max Amount
	if (this.getMaxAmount().compareTo(t.getAmount()) < 0) {
	    this.maxAmount.set(t.getAmount());
	}

	// Find the latest Transaction Date
	if (this.getLastTransactionDateTime().compareTo(t.getDateTime()) < 0) {
	    this.lastTransactionDateTime.set(t.getDateTime());
	}

	// Calculate Total amount
	this.totalAmount.set(this.getTotalAmount().add(t.getAmount()));

	// Calculate Average
	this.averageAmount.set(this.getTotalAmount().divide(new BigDecimal(this.getCount())));
    }

    public BankTransactionMetrics combine(BankTransactionMetrics other) {
	if (this.getMaxAmount().compareTo(other.getMaxAmount()) < 0) {
	    this.maxAmount.set(other.getMaxAmount());
	}

	this.averageAmount.set(this.getAverageAmount().add(other.getAverageAmount()).divide(new BigDecimal(2)));

	this.totalAmount.set(this.getTotalAmount().add(other.getTotalAmount()));

	if (this.getLastTransactionDateTime().compareTo(other.getLastTransactionDateTime()) < 0) {
	    this.lastTransactionDateTime.set(other.getLastTransactionDateTime());
	}

	return this;
    }

    public BankTransactionMetrics finisher() {
	return this;
    }

    public int getCount() {
	return this.count.get();
    }

    public BigDecimal getTotalAmount() {
	return this.totalAmount.get();
    }

    public BigDecimal getMaxAmount() {
	return this.maxAmount.get();
    }

    public BigDecimal getAverageAmount() {
	return this.averageAmount.get();
    }

    public LocalDateTime getLastTransactionDateTime() {
	return lastTransactionDateTime.get();
    }

    @Override
    public String toString() {
	return String.format(
		"TransactionStatistics [transactions=%s, average=%s, total=%s, max=%s, lastTransactionDateTime=%s]",
		count, this.getAverageAmount().longValue(), this.getTotalAmount(), this.getMaxAmount().longValue(),
		this.getLastTransactionDateTime());
    }
}
