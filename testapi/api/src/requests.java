import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

class Call {
    public static void main(String[] args) {
        String searchTerm = "Académie française"; // Modify this with the title of the book you want to search for

        // Encode the search term
        String encodedSearchTerm = URLEncoder.encode(searchTerm, StandardCharsets.UTF_8);

        // Build the request with the search term
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://catalogue.bnf.fr/api/SRU?version=1.2&operation=searchRetrieve&query=bib.persistentid%20any%20%22ark:/12148/cb38499612v%20ark:/12148/cb402237466%22"))
                .header("User-Agent", "Java HttpClient")
                .header("Accept", "application/xml")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        // Send the request and handle the response
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<byte[]> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
            assert response != null;
            byte[] responseBody = response.body();

            // Print raw XML response for debugging
            System.out.println("Raw XML response:");
            System.out.println(new String(responseBody, StandardCharsets.UTF_8));

            // Parse XML response
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            ByteArrayInputStream input = new ByteArrayInputStream(responseBody);
            Document document = builder.parse(input);

            // Extract book information
            NodeList recordList = document.getElementsByTagName("srw:record");
            if (recordList.getLength() > 0) {
                for (int i = 0; i < recordList.getLength(); i++) {
                    Node recordNode = recordList.item(i);
                    NodeList dataNodes = recordNode.getChildNodes();
                    List<String> bookInfo = new ArrayList<>();
                    for (int j = 0; j < dataNodes.getLength(); j++) {
                        Node dataNode = dataNodes.item(j);
                        if (dataNode.getNodeType() == Node.ELEMENT_NODE) {
                            String nodeName = dataNode.getNodeName();
                            String nodeValue = dataNode.getTextContent();
                            bookInfo.add(nodeName + ": " + nodeValue);
                        }
                    }
                    // Print book information
                    System.out.println("Book information:");
                    for (String info : bookInfo) {
                        System.out.println(info);
                    }
                    System.out.println();
                }
            } else {
                System.out.println("No records found.");
            }
        } catch (IOException | InterruptedException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }
}
