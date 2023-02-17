package com.example.soapclientdemo;

import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUrlConnectionTest {

    // https://limdevbasic.tistory.com/14

    private static final String URL = "http://localhost:8080/ws/user";
    private static final String POST = "POST";
    private static final String GET = "GET";
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String DATA = "test data";

    public static void main(String[] args) throws IOException {
        HttpUrlConnectionTest oProc = new HttpUrlConnectionTest();
        oProc.doWork();
        System.exit(0);
    }

    public void doWork() throws IOException {

        doPostCall();

    }

    public String getXMLData() {
        return "<SOAP-ENV:Envelope xmlns:SOAP-ENV = \"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:wsdl = \"http://ws.cxf.soapdemo.example.com/\">\n" +
                "    <SOAP-ENV:Header/>\n" +
                "    <SOAP-ENV:Body>\n" +
                "        <wsdl:getUserList>\n" +
                "            <userId>jim</userId>\n" +
                "        </wsdl:getUserList>\n" +
                "    </SOAP-ENV:Body>\n" +
                "</SOAP-ENV:Envelope>";
    }

    public void doPostCall() throws IOException {
        URL url = new URL(URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod(POST);
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setDoOutput(true);

        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes(getXMLData());
        outputStream.flush();
        outputStream.close();

        int responseCode = connection.getResponseCode();
        System.out.println("responseCode = " + responseCode);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuffer stringBuffer = new StringBuffer();
        String inputLine;

        while ((inputLine = bufferedReader.readLine()) != null)  {
            stringBuffer.append(inputLine);
        }
        bufferedReader.close();

        System.out.println("inputLine = \n" + prettyPrintByTransformer(stringBuffer.toString(), 2, false));
        System.out.println("inputLine = \n" + prettyPrintByDom4j(stringBuffer.toString(), 2, false));
    }

    public void doGetCall() throws IOException {
        URL url = new URL(URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod(GET);
        connection.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = connection.getResponseCode();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuffer stringBuffer = new StringBuffer();
        String inputLine;

        while ((inputLine = bufferedReader.readLine()) != null)  {
            stringBuffer.append(inputLine);
        }
        bufferedReader.close();

        System.out.println("inputLine = \n" + prettyPrintByTransformer(stringBuffer.toString(), 2, true));
        System.out.println("inputLine = \n" + prettyPrintByDom4j(stringBuffer.toString(), 2, false));

    }

    /**
     * raw XML string to a pretty-formatted string
     * (standard Java API)
     * @param xmlString xml 문자열
     * @param indent indent-size
     * @param ignoreDeclaration suppressing XML declaration
     * @return
     */
    public static String prettyPrintByTransformer(String xmlString, int indent, boolean ignoreDeclaration) {

        /*
        First, we parse the raw XML string and get a Document object.
        Next, we obtain a TransformerFactory instance and set the required indent-size attribute.
        Then, we can get a default transformer instance from the configured tranformerFactory object.
        The transformer object supports various output properties. To decide if we want to skip the declaration, we set the OutputKeys.OMIT_XML_DECLARATION attribute.
        Since we would like to have a pretty-formatted String object, finally, we transform() the parsed XML Document to a StringWriter and return the transformed String.
         */

        try {
            InputSource src = new InputSource(new StringReader(xmlString));
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(src);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", indent);

            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, ignoreDeclaration ? "yes" : "no");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            Writer out = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(out));
            return out.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error occurs when pretty-printing xml:\n" + xmlString, e);
        }
    }

    /**
     * raw XML string to a pretty-formatted string
     * (using an external library)
     * @param xmlString xml 문자열
     * @param indent indent-size
     * @param skipDeclaration suppressing XML declaration
     * @return
     */
    public static String prettyPrintByDom4j(String xmlString, int indent, boolean skipDeclaration) {
        try {
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setIndentSize(indent);
            format.setSuppressDeclaration(skipDeclaration);
            format.setEncoding("UTF-8");

            org.dom4j.Document document = DocumentHelper.parseText(xmlString);
            StringWriter sw = new StringWriter();
            XMLWriter writer = new XMLWriter(sw, format);
            writer.write(document);
            return sw.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error occurs when pretty-printing xml:\n" + xmlString, e);
        }
    }
}
