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
        System.out.println(xmlResponse);
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

        NodeList dataFieldList = recordElement.getElementsByTagName("oai_dc:dc");
        for (int j=0; j < dataFieldList.getLength(); j++) {
            Element dataField = (Element) dataFieldList.item(j);
            // Extract and update book details here
            String bookinfo = extractTextContent(dataField, "dc:title");
            assert bookinfo != null;
            String title=extractTitle(bookinfo);
            String author=extractAuthor(bookinfo);
            System.out.println("title: " + title);
            System.out.println("author: " + author);
            // Set book details
            book.setTitle(title);
            book.setAuthor(author);
        }

        return book;
    }

    private static String extractTitle(String bookInfo) {
        int slashIndex = bookInfo.indexOf('/');
        int parenthesisIndex = bookInfo.indexOf('(');
        int endIndex = Math.min(slashIndex != -1 ? slashIndex : Integer.MAX_VALUE,
                parenthesisIndex != -1 ? parenthesisIndex : Integer.MAX_VALUE);
        return bookInfo.substring(0, endIndex).trim();
    }

    private static String extractAuthor(String bookInfo) {
        int slashIndex = bookInfo.indexOf('/');
        int parenthesisIndex = bookInfo.indexOf(')');
        int semicolonIndex = bookInfo.indexOf(';');
        int startIndex = Math.max(slashIndex != -1 ? slashIndex + 1 : 0,
                parenthesisIndex != -1 ? parenthesisIndex + 1 : 0);
        int endIndex = semicolonIndex != -1 ? semicolonIndex : bookInfo.length();
        return bookInfo.substring(startIndex, endIndex).trim();
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

    private static String extractTextContent(Element parentElement, String tagName) {
        NodeList nodeList = parentElement.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return null;
    }

    private static Document parseXmlResponse(String xml) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setNamespaceAware(true);
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        return dBuilder.parse(new java.io.ByteArrayInputStream(xml.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
    }
}
