package mike.samples.test.process;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ProcessBuilderTest {

    private static final Logger log = LoggerFactory.getLogger(ProcessBuilderTest.class);
    
    @Test
    void return_java_version() throws InterruptedException, IOException {
        
        var pb = new ProcessBuilder();
        
        var environment = pb.environment();
        environment.put("JAVA_HOME", "C:\\Users\\cathare\\Homeware\\java\\jdk-17.0.4+8");
        
        //environment.forEach((key, value) -> log.debug("env: {} = {}", key, value));
        
        
        pb.command("C:\\Users\\cathare\\Homeware\\java\\jdk-17.0.4+8\\bin\\java.exe", "-version");
        
        var process = pb.start();
        var results = this.readOutput(process.getInputStream());
        
        assertThat(results).isNotEmpty().contains("java version");
        
        int rc = process.waitFor();
        
        assertThat(rc).isZero();
    }
    
    
    private List<String> readOutput(InputStream is)  {
        try (var br = new BufferedReader(new InputStreamReader(is))) {
            return br.lines().toList();
        } catch (IOException ioe) {
            throw new UncheckedIOException("read process output: " + ioe.getMessage(), ioe);
        }
    }
}
