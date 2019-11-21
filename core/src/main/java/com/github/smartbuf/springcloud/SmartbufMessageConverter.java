package com.github.smartbuf.springcloud;

import com.github.smartbuf.SmartPacket;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Smartbuf's HttpMessageConverter adaptor.
 * <p>
 * It use the packet mode of 'smartbuf' to expose a better option for data encoding and decoding.
 *
 * @author sulin
 * @since 2019-11-21 14:22:50
 **/
public class SmartbufMessageConverter extends AbstractGenericHttpMessageConverter<Object> {

    public static final MediaType SMARTBUF = new MediaType("application", "x-smartbuf");

    public SmartbufMessageConverter() {
        super(SMARTBUF);
    }

    @Override
    protected boolean supports(Class<?> aClass) {
        return true;
    }

    public Object read(Type type, Class<?> cls, HttpInputMessage msg) throws IOException, HttpMessageNotReadableException {
        MediaType contentType = msg.getHeaders().getContentType();
        if (contentType == null) {
            contentType = SMARTBUF;
        }
        if (!SMARTBUF.isCompatibleWith(contentType)) {
            throw new UnsupportedOperationException("unsupported context-type: " + contentType);
        }
        byte[] bytes = IOUtils.toByteArray(msg.getBody());
        return SmartPacket.deserialize(bytes, type);
    }

    @Override
    protected Object readInternal(Class<?> cls, HttpInputMessage msg) throws IOException, HttpMessageNotReadableException {
        MediaType contentType = msg.getHeaders().getContentType();
        if (contentType == null) {
            contentType = SMARTBUF;
        }
        if (!SMARTBUF.isCompatibleWith(contentType)) {
            throw new UnsupportedOperationException("unsupported context-type: " + contentType);
        }
        byte[] bytes = IOUtils.toByteArray(msg.getBody());
        return SmartPacket.deserialize(bytes, cls);
    }

    @Override
    protected void writeInternal(Object o, Type type, HttpOutputMessage msg) throws IOException, HttpMessageNotWritableException {
        MediaType contentType = msg.getHeaders().getContentType();
        if (contentType == null) {
            contentType = SMARTBUF;
        }
        if (!SMARTBUF.isCompatibleWith(contentType)) {
            throw new UnsupportedOperationException("unsupported context-type: " + contentType);
        }
        byte[] bytes = SmartPacket.serialize(o);
        IOUtils.write(bytes, msg.getBody());
    }

}
