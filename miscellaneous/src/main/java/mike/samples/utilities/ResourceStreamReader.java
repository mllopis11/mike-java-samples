package mike.samples.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ResourceStreamReader implements StreamReader {

    private static final Logger log = LoggerFactory.getLogger(ResourceStreamReader.class);
    
    private final Resource resource;
    private final Predicate<String> filter;
    private final Charset charset;
    
    private InputStream is;
    private InputStreamReader isr;
    private BufferedReader reader;
    
    ResourceStreamReader(Resource resource, Predicate<String> filter, Charset... charset) {
	this.resource = resource;
	this.filter = filter;
	this.charset = charset.length > 0 ? charset[0] : StandardCharsets.ISO_8859_1;
    }
    
    @Override
    public Stream<String> lines() throws IOException {
	try {
	    this.is = resource.getInputStream();
	    this.isr = new InputStreamReader(is, charset);
	    this.reader = new BufferedReader(isr);
	    
	    return reader.lines().filter(filter);
	} catch (IOException ioe) {
	    this.close();
	    throw ioe;
	}
    }
    
    @Override
    public void close() {
	log.debug("Resource::close: closing resources ...");
	
	if (this.reader != null ) {
	    try {
		this.reader.close();
	    } catch (IOException ioe) {
		log.warn("Resource::close: bufferedReader (reason: {})", ioe.getMessage());
	    }
	}
	
	if (this.isr != null ) {
	    try {
		this.isr.close();
	    } catch (IOException ioe) {
		log.warn("Resource::close: inputStreamReader (reason: {})", ioe.getMessage());
	    }
	}
	
	if (this.is != null ) {
	    try {
		this.is.close();
	    } catch (IOException ioe) {
		log.warn("Resource::close: inputStream (reason: {})", ioe.getMessage());
	    }
	}
    }
}
