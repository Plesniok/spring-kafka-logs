package sgu.kafkaLog.middlewares;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.IOException;
import java.io.PrintWriter;

public class HttpServletRequestCopier extends HttpServletRequestWrapper {

    private ServletInputStream inputStream;
    private PrintWriter writer;
    private ServletInputStreamCopier copier;

    public HttpServletRequestCopier(HttpServletRequest request) throws IOException {
        super(request);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (writer != null) {
            throw new IllegalStateException("getWriter() has already been called on this response.");
        }

        if (inputStream == null) {
            inputStream = getRequest().getInputStream();
            copier = new ServletInputStreamCopier(inputStream);
        }

        return copier;
    }

    public byte[] getCopy() {
        if (copier != null) {
            return copier.getCopy();
        } else {
            return new byte[0];
        }
    }
}
