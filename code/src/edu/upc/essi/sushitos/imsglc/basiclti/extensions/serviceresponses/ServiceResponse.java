package edu.upc.essi.sushitos.imsglc.basiclti.extensions.serviceresponses;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public abstract class ServiceResponse {

    protected Document doc;
    protected XPath xpath;

    public ServiceResponse(String message) throws ParserConfigurationException,
            SAXException, IOException {
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(message));
        doc = builder.parse(is);
        XPathFactory xfactory = XPathFactory.newInstance();
        xpath = xfactory.newXPath();
    }

    public String getCodemajor() throws XPathExpressionException, AttributeNotFoundException {
        XPathExpression expr = xpath
                .compile("//message_response/statusinfo/codemajor/text()");
        Object result = expr.evaluate(doc, XPathConstants.NODESET);
        NodeList nodes = (NodeList) result;
        
        if (nodes.getLength() != 1 )
            throw new AttributeNotFoundException();
        
        return nodes.item(0).getNodeValue();
    }

    public String getCodeminor() throws XPathExpressionException, AttributeNotFoundException {
        XPathExpression expr = xpath
                .compile("//message_response/statusinfo/codeminor/text()");
        Object result = expr.evaluate(doc, XPathConstants.NODESET);
        NodeList nodes = (NodeList) result;
        
        if (nodes.getLength() != 1 )
            throw new AttributeNotFoundException();
        
        return nodes.item(0).getNodeValue();
    }

    public String getSeverity() throws XPathExpressionException, AttributeNotFoundException {
        XPathExpression expr = xpath
                .compile("//message_response/statusinfo/severity/text()");
        Object result = expr.evaluate(doc, XPathConstants.NODESET);
        NodeList nodes = (NodeList) result;
        
        if (nodes.getLength() != 1 )
            throw new AttributeNotFoundException();
        
        return nodes.item(0).getNodeValue();
    }

    public String getDescription() throws XPathExpressionException, AttributeNotFoundException {
        XPathExpression expr = xpath
                .compile("//message_response/statusinfo/description/text()");
        Object result = expr.evaluate(doc, XPathConstants.NODESET);
        NodeList nodes = (NodeList) result;
        
        if (nodes.getLength() != 1 )
            throw new AttributeNotFoundException();
        
        return nodes.item(0).getNodeValue();
    }
    
    public String getMessageType() throws XPathExpressionException, AttributeNotFoundException {
        XPathExpression expr = xpath
                .compile("//message_response/lti_message_type/text()");
        Object result = expr.evaluate(doc, XPathConstants.NODESET);
        NodeList nodes = (NodeList) result;
        
        if (nodes.getLength() != 1 )
            throw new AttributeNotFoundException();
        
        return nodes.item(0).getNodeValue();
    }
    
}
