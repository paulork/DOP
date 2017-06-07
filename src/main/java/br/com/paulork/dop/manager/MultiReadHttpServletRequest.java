package br.com.paulork.dop.manager;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @author Paulo R. Kraemer <paulork10@gmail.com>
 */
public class MultiReadHttpServletRequest extends HttpServletRequestWrapper {

    private String _body;

    public MultiReadHttpServletRequest(HttpServletRequest request) throws IOException {
        super(request);
        _body = "";
        request.setCharacterEncoding("UTF-8");
        BufferedReader bufferedReader = request.getReader();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            _body += line;
        }
    }

    @Override
    public void setCharacterEncoding(String enc) throws UnsupportedEncodingException {
    }

    @Override
    public String getCharacterEncoding() {
        return super.getCharacterEncoding();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(_body.getBytes(getCharacterEncoding()));

        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }

            @Override
            public boolean isFinished() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean isReady() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream(), getCharacterEncoding()));
    }
}
