package fr.cyu.cybooks.dao.api;

import fr.cyu.cybooks.models.Book;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.io.StringReader;
import java.util.List;

/**
 * This class provides methods to parse XML responses from book API requests.
 */
public class BookParser {

    /**
     * Parses the XML response containing book information and returns a list of Book objects.
     *
     * @param xmlResponse the XML response string
     * @return a list of Book objects parsed from the XML response
     * @throws Exception if there is an error while parsing the XML
     */
    public static List<Book> parseBooksFromResponse(String xmlResponse) throws Exception {
        Document doc = parseXmlResponse(xmlResponse);

        NodeList recordList = doc.getElementsByTagNameNS("http://www.loc.gov/zing/srw/", "record");
        List<Book> books = new ArrayList<>();

        for (int i=0; i<recordList.getLength(); i++) {
            Element recordElement = (Element) recordList.item(i);
            Book book = parseBookFromRecordElement(recordElement);
            books.add(book);
        }

        return books;
    }

    /**
     * Parses a single book from a record element in the XML response.
     *
     * @param recordElement the XML element representing a single book record
     * @return the Book object parsed from the record element
     * @throws Exception if there is an error while parsing the book record
     */
    public static Book parseBookFromRecordElement(Element recordElement) throws Exception {
        Book book = new Book();

        String recordIdentifier = parseRecordIdentifier(recordElement);
        book.setId(recordIdentifier);

        NodeList dataFieldList = recordElement.getElementsByTagName("oai_dc:dc");
        for (int j=0; j < dataFieldList.getLength(); j++) {
            Element dataField = (Element) dataFieldList.item(j);

            // Extract and update book details here
            String fullTitle= extractTextContent(dataField, "dc:title");
            String date=extractTextContent(dataField, "dc:date");
            String author=extractTextContent(dataField, "dc:creator");
            String contributors=extractTextContent(dataField, "dc:contributor");
            assert fullTitle != null;
            String title=extractTitle(fullTitle);

            // Set book details
            book.setTitle(title);
            book.setAuthor(author);
            book.setDate(date);
            book.setContributors(contributors);
        }

        return book;
    }

    /**
     * Extracts the title from the full book information string.
     *
     * @param bookInfo the full book information string
     * @return the extracted title of the book
     */
    private static String extractTitle(String bookInfo) {
        int slashIndex = bookInfo.indexOf('/');
        int parenthesisIndex = bookInfo.indexOf(')');

        // If '/' comes before ')' or ')' is not found, extract title before '/'
        if (slashIndex != -1 && (parenthesisIndex == -1 || slashIndex < parenthesisIndex)) {
            return bookInfo.substring(0, slashIndex).trim();
        } else if (parenthesisIndex != -1) {
            // Extract title before ')' including ')'
            return bookInfo.substring(0, parenthesisIndex + 1).trim();
        }

        // If neither '/' nor ')' is found, return the original string
        return bookInfo.trim();
    }

    /**
     * Parses the record identifier from the record element.
     *
     * @param recordElement the XML element representing a single book record
     * @return the record identifier of the book
     */
    public static String parseRecordIdentifier(Element recordElement) {
        String recordIdentifier = null;
        NodeList recordIdentifierList = recordElement.getElementsByTagNameNS("http://www.loc.gov/zing/srw/", "recordIdentifier");

        if (recordIdentifierList.getLength() > 0) {
            Element recordIdentifierElement = (Element) recordIdentifierList.item(0);
            recordIdentifier = recordIdentifierElement.getTextContent();
        }
        return recordIdentifier;
    }

    /**
     * Extracts text content from the specified XML element based on the given tag name.
     *
     * @param parentElement the parent XML element
     * @param tagName       the tag name of the element to extract text content from
     * @return the text content of the specified element, or null if not found
     */
    private static String extractTextContent(Element parentElement, String tagName) {
        NodeList nodeList = parentElement.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return null;
    }

    /**
     * Parses the XML response string into a Document object.
     *
     * @param xml the XML response string
     * @return the Document object representing the parsed XML
     * @throws Exception if there is an error while parsing the XML
     */
    private static Document parseXmlResponse(String xml) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setNamespaceAware(true);
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        return dBuilder.parse(new java.io.ByteArrayInputStream(xml.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
    }

    /**
     * Retrieves the total number of results from the XML response.
     *
     * @param xmlResponse the XML response string
     * @return the total number of results parsed from the XML response
     * @throws Exception if there is an error while parsing the XML
     */
    public static Integer getNumberResults(String xmlResponse) throws Exception {
        try {
            // Load XML document from string
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xmlResponse)));

            // Get the root element
            Element root = document.getDocumentElement();

            // Get the number of records
            NodeList numberOfRecordsList = root.getElementsByTagName("srw:numberOfRecords");
            if (numberOfRecordsList.getLength() > 0) {
                Element numberOfRecordsElement = (Element) numberOfRecordsList.item(0);
                String numberOfRecordsStr = numberOfRecordsElement.getTextContent();
                return Integer.parseInt(numberOfRecordsStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0; // Return 0 if no records found or error occurred
    }
}
