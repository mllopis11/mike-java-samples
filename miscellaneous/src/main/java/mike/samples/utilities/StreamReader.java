package mike.samples.utilities;

import java.io.IOException;
import java.util.stream.Stream;

public interface StreamReader extends AutoCloseable {

    Stream<String> lines() throws IOException;
}
