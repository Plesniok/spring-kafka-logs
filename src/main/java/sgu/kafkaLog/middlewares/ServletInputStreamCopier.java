package sgu.kafkaLog.middlewares;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ServletInputStreamCopier extends ServletInputStream {

    private ServletInputStream servletInputStream;
    private ByteArrayOutputStream copy;

    public ServletInputStreamCopier(ServletInputStream inputStream) {
        this.servletInputStream = inputStream;
        this.copy = new ByteArrayOutputStream(1024);
    }

    @Override
    public int read() throws IOException {
        int data = servletInputStream.read();
        if (data != -1) {
            copy.write(data);
        }
        return data;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int bytesRead = servletInputStream.read(b, off, len);
        if (bytesRead > 0) {
            copy.write(b, off, bytesRead);
        }
        return bytesRead;
    }

    @Override
    public int read(byte[] b) throws IOException {
        int bytesRead = servletInputStream.read(b);
        if (bytesRead > 0) {
            copy.write(b, 0, bytesRead);
        }
        return bytesRead;
    }

    public byte[] getCopy() {
        return copy.toByteArray();
    }

    @Override
    public void close() throws IOException {
        servletInputStream.close();
        copy.close();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setReadListener(ReadListener readListener) {

    }
}
