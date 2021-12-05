package mike.samples.test.spel.model;

public class MapperException extends RuntimeException {

    private static final long serialVersionUID = -6181158816164270362L;

    public MapperException(String message, Object... args) {
	super(String.format(message, args));
    }
    
    public MapperException(Throwable ex, String message, Object... args) {
	super(String.format(message, args), ex);
    }

}
