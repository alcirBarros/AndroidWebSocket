package android.web.socket;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 *
 * @author alci
 */
public class NoCopyByteArrayOutputStream extends ByteArrayOutputStream {

    /**
     * Create new OutputStream backed by byte array.
     */
    public NoCopyByteArrayOutputStream() {
        super();
    }

    /**
     * Create new OutputStream backed by byte array.
     *
     * @param size Initial size of underlying byte array.
     */
    public NoCopyByteArrayOutputStream(int size) {
        super(size);
    }

    /**
     * Wraps the underyling byte array into an InputStream.
     *
     * @return New InputStream wrapping byte buffer underlying this stream.
     */
    public InputStream getInputStream() {
        return new ByteArrayInputStream(buf, 0, count);
    }

    /**
     * Get byte array underlying this OutputStream. This does not copy any data, but return reference to the underlying byte array.
     *
     * @return Underlying byte array by reference.
     */
    public byte[] getByteArray() {
        return buf;
    }
}
