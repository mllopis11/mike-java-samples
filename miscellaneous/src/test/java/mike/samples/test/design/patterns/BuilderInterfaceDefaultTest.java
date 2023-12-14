package mike.samples.test.design.patterns;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

class BuilderInterfaceDefaultTest {

    @Test
    void should_build_FileReader_with_default_charset_and_predicate() {
        var file = Path.of("foo/bar.txt");
        var reader = FileReader.create().file(file).iso88591().noFilter();
        
        assertThat(reader.file()).isEqualTo(file);
        assertThat(reader.charset()).isEqualTo(StandardCharsets.ISO_8859_1);
        assertThat(reader.recordFilter()).isNotNull();
    }
}

class FileReader {
    
    private final Path file;
    private final Charset charset;
    private final Predicate<String> recordFilter;
    
    private FileReader(Path file, Charset charset, Predicate<String> recordFilter) {
        this.file = file;
        this.charset = charset;
        this.recordFilter = recordFilter;
    }
    
    public Path file() {
        return file;
    }
    
    public Charset charset() {
        return charset;
    }
    
    public Predicate<String> recordFilter() {
        return recordFilter;
    }
    
    public static Builder create() {
        return file -> charset -> filter -> new FileReader(file, charset, filter);
    }
    
    interface Builder {
        
        ReaderCharset file(Path file);
        
        interface ReaderCharset {
            
            RecordFilter charset(Charset charset);
            
            default RecordFilter iso88591() {
                return this.charset(StandardCharsets.ISO_8859_1);
            }
            
            default RecordFilter utf8() {
                return this.charset(StandardCharsets.UTF_8);
            }
        }
        
        interface RecordFilter {
            FileReader filter(Predicate<String> recordFilter);
            
            default FileReader noFilter() {
                return this.filter(r -> true);
            }
        }
    }
}
