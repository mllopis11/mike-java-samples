package mike.samples.utilities;

import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Path;

import org.apache.tika.Tika;

/**
 * Media content resolver.
 * 
 * @author Mike (2022-06)
 */
public enum MimeType {

    ZIP("application/zip"), 
    GZIP("application/gzip"), 
    TEXT("text/plain"), 
    JSON("application/gzip"),
    UNKOWN("application/unknown");

    private final String media;

    private MimeType(String media) {
	this.media = media;
    }

    /**
     * @return the standard media type representation of the mime type (ex.:
     *         text/plain, application/zip ...)
     */
    public String media() {
	return this.media;
    }

    /**
     * @param mediaType a standard media type
     * @param type      the mime type to compare to.
     * @return true if the media type is equal to the the given MimeType media,
     *         otherwise false.
     */
    public static boolean compare(String mediaType, MimeType type) {
	return mediaType.equals(type.media);
    }

    /**
     * @param type the mime type to convert
     * @return the corresponding mime type value or <i>UNKNOWN</i> if the given type
     *         does not match any declared MimeType.
     */
    public static MimeType of(String type) {
	try {
	    return MimeType.valueOf(Utils.strip(type.toUpperCase()));
	} catch (IllegalArgumentException iae) {
	    return UNKOWN;
	}
    }

    /**
     * Tries to detect the media type of the file.
     * 
     * @param path the file path
     * @return he detected media type or <i>application/unknown</i> if the media
     *         cannot by detected.
     * @see MimeType#resolve(Resource)
     */
    public static String resolve(Path path) {
	return resolve(Resource.of(path));
    }

    /**
     * Tries to detect the media type of the resource at the given URL without
     * accessing to the document.
     * <p>
     * The method first tries to detect the media using
     * {@link Tika#detect(java.net.URL)}, then fallback to the method
     * {@link MimeType#resolve(String)} if the the first attempt fails.
     * 
     * @param resource the resource to check
     * @return the detected media type or <i>application/unknown</i> if the media
     *         cannot by detected.
     * @see Tika#detect(java.net.URL)
     * @see MimeType#resolve(String)
     */
    public static String resolve(Resource resource) {
	Tika tika = new Tika();
	String mime = null;

	try {
	    mime = tika.detect(resource.getURL());
	} catch (IOException ioe) {
	    // Ignore
	}

	if (mime == null) {
	    String filename = resource.getURI().toString();
	    mime = resolve(filename);
	}

	return mime != null ? mime : UNKOWN.media();
    }

    /**
     * Tries to determine the content type of the file from its name.
     * 
     * @param filename a file name (pathname, URI or URL)
     * @return a guess as to what the content type of the object is,based upon its
     *         file name.
     * @see URLConnection#guessContentTypeFromName(String)
     */
    public static String resolve(String filename) {
	return URLConnection.guessContentTypeFromName(filename);
    }
}
