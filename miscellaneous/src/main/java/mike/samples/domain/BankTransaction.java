package mike.samples.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BankTransaction {

    private final long id;
    private final long userId;
    private final BigDecimal amount;
    private final LocalDateTime dateTime;
    
    public BankTransaction(long id, long userId, BigDecimal amount, LocalDateTime dateTime) {
	this.id = id;
	this.userId = userId;
	this.amount = amount;
	this.dateTime = dateTime;
    }

    public long getId() {
        return id;
    }
    
    public long getUserId() {
        return userId;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public LocalDateTime getDateTime() {
        return dateTime;
    }
    
    @Override
    public String toString() {
	return String.format("Transaction [id=%s, userId=%s, amount=%s, dateTime=%s]", id, userId, amount.longValue(), dateTime);
    }
}
