package edu.upc.essi.sushitos.imsglc.basiclti.extensions.serviceresponses;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ReadResultServiceResponse extends ServiceResponse {

    public ReadResultServiceResponse(String message)
            throws ParserConfigurationException, SAXException, IOException {

        super(message);
        // TODO Auto-generated constructor stub
    }

    public String getScore() throws XPathExpressionException,
            AttributeNotFoundException {
        XPathExpression expr = xpath
                .compile("//message_response/result/resultscore/textstring/text()");
        Object result = expr.evaluate(doc, XPathConstants.NODESET);
        NodeList nodes = (NodeList) result;

        if (nodes.getLength() != 1)
            throw new AttributeNotFoundException();

        return nodes.item(0).getNodeValue();
    }

}
