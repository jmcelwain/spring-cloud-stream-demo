package com.example.demo;

import com.sun.org.apache.xml.internal.security.Init;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.AbstractMessageConverter;
import org.springframework.util.MimeType;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.print.Doc;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DocumentMessageConverter extends AbstractMessageConverter {
    private final DocumentBuilderFactory documentBuilderFactory;

    static {
        Init.init();
    }

    public DocumentMessageConverter() {
        super(new MimeType("application", "xml"));
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        this.documentBuilderFactory = documentBuilderFactory;
    }

    @Override
    protected boolean supports(Class<?> aClass) {
        return Document.class.isAssignableFrom(aClass);
    }

    @Override
    protected Object convertToInternal(Object payload, MessageHeaders headers, Object conversionHint) {
        Document document = (Document) payload;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLUtils.outputDOM(document.getDocumentElement(), baos);
        return baos.toByteArray();
    }

    @Override
    protected Object convertFromInternal(Message<?> message, Class<?> targetClass, Object conversionHint) {
        DocumentBuilder builder = null;
        try {
            byte[] payload = (byte[]) message.getPayload();
            ByteArrayInputStream bais = new ByteArrayInputStream(payload);
            builder = documentBuilderFactory.newDocumentBuilder();
            Document document = builder.parse(bais);
            document.setXmlStandalone(true);
            return document;
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }

        return null;
    }
}
