package com.github.smartbuf.springcloud;

import com.github.smartbuf.SmartPacket;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;

/**
 * feign调用中protostuff编解码器
 **/
public class SmartbufMessageConverter extends AbstractHttpMessageConverter<Object> {

    public static final MediaType SMARTBUF = new MediaType("application", "x-smartbuf");

    public SmartbufMessageConverter() {
        super(SMARTBUF);
    }

    @Override
    protected boolean supports(Class<?> aClass) {
        return true;
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
    protected void writeInternal(Object obj, HttpOutputMessage msg) throws IOException, HttpMessageNotWritableException {
        MediaType contentType = msg.getHeaders().getContentType();
        if (contentType == null) {
            contentType = SMARTBUF;
        }
        if (!SMARTBUF.isCompatibleWith(contentType)) {
            throw new UnsupportedOperationException("unsupported context-type: " + contentType);
        }
        byte[] bytes = SmartPacket.serialize(obj);
        IOUtils.write(bytes, msg.getBody());
    }

}
