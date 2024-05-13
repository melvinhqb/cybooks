package fr.cyu.cybooks.dao.api;

import fr.cyu.cybooks.models.Book;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;

public class BookParser {

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

    public static Book parseBookFromRecordElement(Element recordElement) throws Exception {
        Book book = new Book();

        String recordIdentifier = parseRecordIdentifier(recordElement);
        book.setId(recordIdentifier);

        NodeList dataFieldList = recordElement.getElementsByTagNameNS("info:lc/xmlns/marcxchange-v2", "datafield");
        for (int j=0; j < dataFieldList.getLength(); j++) {
            Element datafieldElement = (Element) dataFieldList.item(j);
            updateBookDetailsFromDataField(book, datafieldElement);
        }

        return book;
    }

    public static String parseRecordIdentifier(Element recordElement) {
        String recordIdentifier = null;
        NodeList recordIdentifierList = recordElement.getElementsByTagNameNS("http://www.loc.gov/zing/srw/", "recordIdentifier");

        if (recordIdentifierList.getLength() > 0) {
            Element recordIdentifierElement = (Element) recordIdentifierList.item(0);
            recordIdentifier = recordIdentifierElement.getTextContent();
        }
        return recordIdentifier;
    }

    private static void updateBookDetailsFromDataField(Book bookInfo, Element dataField) {
        String tag = dataField.getAttribute("tag");
        NodeList subfields = dataField.getElementsByTagNameNS("info:lc/xmlns/marcxchange-v2", "subfield");
        for (int i = 0; i < subfields.getLength(); i++) {
            Element subfield = (Element) subfields.item(i);
            String code = subfield.getAttribute("code");
            String value = subfield.getTextContent();
            if (tag.equals("200")) {
                if (code.equals("a")) bookInfo.setTitle(value);
            }
        }
    }

    private static Document parseXmlResponse(String xml) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setNamespaceAware(true);
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        return dBuilder.parse(new java.io.ByteArrayInputStream(xml.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
    }
}
