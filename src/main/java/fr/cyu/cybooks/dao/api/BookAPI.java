package fr.cyu.cybooks.dao.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

import fr.cyu.cybooks.models.Book;

public class BookAPI {
    private final HttpClient client;

    public BookAPI() {
        this.client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
    }

    public List<Book> searchBooksByKeyword(String keyword) {
        try {
            URI uri = buildSearchUri(keyword);
            HttpRequest request = createHttpRequest(uri);
            HttpResponse<String> response = sendHttpRequest(request);
            if (response.statusCode() == 200) {
                return BookParser.parseBooksFromResponse(response.body());
            }
        } catch (Exception e) {
            System.err.println("Error during HTTP request: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public Book fetchBookById(String bookId) {
        try {
            URI uri = buildFetchUri(bookId);
            HttpRequest request = createHttpRequest(uri);
            HttpResponse<String> response = sendHttpRequest(request);
            if (response.statusCode() == 200) {
                List<Book> books = BookParser.parseBooksFromResponse(response.body());
                if (!books.isEmpty()) {
                    return books.get(0);
                } else {
                    System.err.println("No book details found for book ID: " + bookId);
                    return null;
                }
            } else {
                System.err.println("Failed to fetch book details: HTTP status " + response.statusCode());
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error during HTTP request: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private URI buildSearchUri(String keyword) throws Exception {
        String base = "https://catalogue.bnf.fr/api/SRU";
        String query = String.format("bib.frenchNationalBibliography all \"Books\" and bib.language any \"fre\" and bib.title all \"%s\"", keyword);
        return buildUri(base, query);
    }

    private URI buildFetchUri(String bookId) throws Exception {
        String base = "https://catalogue.bnf.fr/api/SRU";
        String query = String.format("bib.persistentid=\"%s\"", bookId);
        return buildUri(base, query);
    }

    private URI buildUri(String base, String query) throws Exception {
        String version = "version=1.2";
        String operation = "operation=searchRetrieve";
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String fullUrl = String.format("%s?%s&%s&query=%s", base, version, operation, encodedQuery);
        return URI.create(fullUrl);
    }

    private HttpRequest createHttpRequest(URI uri) {
        return HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
    }

    private HttpResponse<String> sendHttpRequest(HttpRequest request) throws Exception {
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
