package mike.samples.utilities;

import java.nio.charset.StandardCharsets;
import java.util.function.Predicate;

public class ResourceReader {

    private ResourceReader() {}
    
    public static StreamReader newResourceStreamReader(Resource resource) {
	return newResourceStreamReader(resource, s -> true);
    }
    
    public static StreamReader newResourceStreamReader(Resource resource, Predicate<String> filter) {
	return new ResourceStreamReader(resource, filter, StandardCharsets.ISO_8859_1);
    }
}
