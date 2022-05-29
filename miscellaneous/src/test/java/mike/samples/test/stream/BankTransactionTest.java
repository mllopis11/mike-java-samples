package mike.samples.test.stream;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mike.samples.domain.BankTransaction;
import mike.samples.domain.BankTransactionMetrics;

class BankTransactionTest {

    private static final Logger log = LoggerFactory.getLogger(BankTransactionTest.class);
    
    @Test
    void should_return_transaction_statistics() {
	
	var baseTransactionDateTime = LocalDateTime.of(2022, 5, 19, 0, 0, 0);
	
	var statistics = Stream.of(
		new BankTransaction(1L, 1L, new BigDecimal(100), baseTransactionDateTime),
		new BankTransaction(2L, 1L, new BigDecimal(50), baseTransactionDateTime.minusMinutes(10)),
		new BankTransaction(2L, 1L, new BigDecimal(120), baseTransactionDateTime.plusMinutes(5)),
		new BankTransaction(4L, 1L, new BigDecimal(70), baseTransactionDateTime.plusHours(1)))
	.collect(BankTransactionMetrics.compute());
	
	log.debug("{}", statistics);
	
	assertThat(statistics.getCount()).isEqualTo(4);
    }
}
