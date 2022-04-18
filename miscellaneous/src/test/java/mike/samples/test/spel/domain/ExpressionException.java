package mike.samples.test.spel.domain;

public class ExpressionException extends RuntimeException {

    private static final long serialVersionUID = -6181158816164270362L;

    public ExpressionException(String message, Object... args) {
	super(String.format(message, args));
    }
    
    public ExpressionException(Throwable ex, String message, Object... args) {
	super(String.format(message, args), ex);
    }

}
