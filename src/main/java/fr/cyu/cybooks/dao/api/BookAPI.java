package fr.cyu.cybooks.dao.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;

import java.nio.charset.StandardCharsets;
import java.util.*;

import fr.cyu.cybooks.models.Book;

public class BookAPI {
    private final HttpClient client;
    Map<String, String> map = new HashMap<>();
    Integer index = 1;
    Integer max = null;
    Integer jump = 10;

    public BookAPI() {
        this.client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
    }

    public List<Book> searchBooksByMap() {
        try {
            if (jump > 0 && index > 0) {
                URI uri = buildSearchUri(this.mapToString(), index.toString(), jump.toString());
                HttpRequest request = createHttpRequest(uri);
                HttpResponse<String> response = sendHttpRequest(request);
                if (max == null){
                    max = BookParser.getNumberResults(response.body());
                }else if (max < index){
                    return null;
                }
                if (response.statusCode() == 200) {
                    return BookParser.parseBooksFromResponse(response.body());
                }
            } else {
                return null;
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

    private URI buildSearchUri(String keyword, String index, String jump) throws Exception {
        String base = "https://catalogue.bnf.fr/api/SRU";
        String query = String.format("bib.frenchNationalBibliography all \"Books\" and bib.language any \"fre\" and %s", keyword);
        return buildUri(base, query, index, jump);
    }


    private URI buildFetchUri(String bookId) throws Exception {
        String base = "https://catalogue.bnf.fr/api/SRU";
        String query = String.format("bib.persistentid=\"%s\"", bookId);
        return buildUri(base, query, "1","1");
    }

    private URI buildUri(String base, String query, String index, String jump) throws Exception {
        String version = "version=1.2";
        String operation = "operation=searchRetrieve";
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String fullUrl = String.format("%s?%s&%s&query=%s&recordSchema=dublincore&maximumRecords=%s&startRecord=%s", base, version, operation, encodedQuery, jump, index);
        return URI.create(fullUrl);
    }

    public void addFilter(String condition, String message){
        if(!Objects.equals(message, "")) {
            if (Objects.equals(condition, "author")) {
                this.map.put("bib.author all ", "\"" + message + "\"");
            } else if (Objects.equals(condition, "title")) {
                this.map.put("bib.title all ", "\"" + message + "\"");
            } else if (Objects.equals(condition, "date")) {
                this.map.put("bib.date all ", "\"" + message + "\"");
            } else if (Objects.equals(condition, "genre")) {
                this.map.put("bib.otherid all ", "\"" + message + "\"");
            }
        }else{
            if (Objects.equals(condition, "author")) {
                this.map.remove("bib.author all ");
            } else if (Objects.equals(condition, "title")) {
                this.map.remove("bib.title all ");
            } else if (Objects.equals(condition, "date")) {
                this.map.remove("bib.date all ");
            } else if (Objects.equals(condition, "genre")) {
                this.map.remove("bib.otherid all ");
            }
        }
        index = 1;
        max = null;
    }

    public void clearFilter(){
        this.map.clear();
        index = 1;
        max=null;
    }

    public boolean isFilterEmpty(){
        return this.map.isEmpty();
    }

    private String mapToString(){
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey()).append(entry.getValue()).append(" and ");
        }
        if (!map.isEmpty()) {
            sb.delete(sb.length() - 5, sb.length());
        }
        return sb.toString();
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

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getMax() {
        return max;
    }

    public void setJump(Integer jump) {
        this.jump = jump;
    }

    public Integer getJump() {
        return jump;
    }
}
