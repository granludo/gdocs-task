package edu.upc.essi.sushitos.imsglc.basiclti.extensions.serviceresponses;

import java.io.IOException;
import java.util.LinkedList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ReadMembershipsServiceResponse extends ServiceResponse {

    public ReadMembershipsServiceResponse(String message)
            throws ParserConfigurationException, SAXException, IOException {

        super(message);
        // TODO Auto-generated constructor stub
    }

    public String getResultSourcedId(String userid)
            throws XPathExpressionException, AttributeNotFoundException {
        XPathExpression expr = xpath
                .compile("//message_response/memberships/member[user_id='"
                        + userid + "']/lis_result_sourcedid/text()");
        Object result = expr.evaluate(doc, XPathConstants.NODESET);
        NodeList nodes = (NodeList) result;

        if (nodes.getLength() != 1)
            throw new AttributeNotFoundException();

        return nodes.item(0).getNodeValue();

    }

    public LinkedList<String> getMembers() throws XPathExpressionException,
            AttributeNotFoundException {
        XPathExpression expr = xpath
                .compile("//message_response/memberships/member/user_id/text()");
        Object result = expr.evaluate(doc, XPathConstants.NODESET);

        NodeList nodes = (NodeList) result;
        LinkedList<String> l = new LinkedList<String>();
        for (int i = 0; i < nodes.getLength(); i++) {
            l.add(nodes.item(i).getNodeValue());
        }
        return l;
    }

}
