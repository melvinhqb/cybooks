package fr.cyu.cybooks.dao.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;

import java.nio.charset.StandardCharsets;
import java.util.*;

import fr.cyu.cybooks.models.Book;

/**
 * This class interacts with the external Book API to search for books and fetch book details.
 */
public class BookAPI {
    private final HttpClient client;
    Map<String, String> map = new HashMap<>();
    Integer index = 1;
    Integer max = null;
    Integer jump = 10;

    /**
     * Constructs a BookAPI object with a default HTTP client.
     */
    public BookAPI() {
        this.client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
    }

    /**
     * Searches for books based on the specified filters and pagination parameters.
     *
     * @return a list of books matching the search criteria
     */
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

    /**
     * Fetches details for a specific book using its ID.
     *
     * @param bookId the ID of the book to fetch
     * @return the Book object containing details of the fetched book
     */
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

    /**
     * Builds the URI for searching books based on the provided keyword, index, and pagination parameters.
     *
     * @param keyword the keyword to search for in the book titles and authors
     * @param index   the index of the first result to retrieve
     * @param jump    the maximum number of results to retrieve
     * @return the constructed URI for searching books
     * @throws Exception if there is an error while constructing the URI
     */
    private URI buildSearchUri(String keyword, String index, String jump) throws Exception {
        String base = "https://catalogue.bnf.fr/api/SRU";
        String query = String.format("bib.frenchNationalBibliography all \"Books\" and bib.language any \"fre\" and %s", keyword);
        return buildUri(base, query, index, jump);
    }

    /**
     * Builds the URI for fetching details of a book using its ID.
     *
     * @param bookId the ID of the book to fetch
     * @return the constructed URI for fetching book details
     * @throws Exception if there is an error while constructing the URI
     */
    private URI buildFetchUri(String bookId) throws Exception {
        String base = "https://catalogue.bnf.fr/api/SRU";
        String query = String.format("bib.persistentid=\"%s\"", bookId);
        return buildUri(base, query, "1","1");
    }

    /**
     * Builds a URI with the specified base, query, index, and pagination parameters.
     *
     * @param base  the base URL of the API
     * @param query the search query or filter conditions
     * @param index the index of the first result to retrieve
     * @param jump  the maximum number of results to retrieve
     * @return the constructed URI
     * @throws Exception if there is an error while constructing the URI
     */
    private URI buildUri(String base, String query, String index, String jump) throws Exception {
        String version = "version=1.2";
        String operation = "operation=searchRetrieve";
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String fullUrl = String.format("%s?%s&%s&query=%s&recordSchema=dublincore&maximumRecords=%s&startRecord=%s", base, version, operation, encodedQuery, jump, index);
        return URI.create(fullUrl);
    }

    /**
     * Adds or updates a filter condition for searching books.
     *
     * @param condition the filter condition (author, title, date, genre)
     * @param message   the value of the filter condition
     */
    public void addFilter(String condition, String message, int method){
        String finalMethod;
        if (method == 1){
            finalMethod = "and";
        } else if (method == 2) {
            finalMethod = "not";
        }else{
            System.out.println("method not correct");
            return;
        }

        if(!Objects.equals(message, "")) {
            if (Objects.equals(condition, "author")) {
                this.map.put("bib.author all ", "\"" + message + "\" "+finalMethod);
            } else if (Objects.equals(condition, "title")) {
                this.map.put("bib.title all ", "\"" + message + "\" "+finalMethod);
            } else if (Objects.equals(condition, "date")) {
                this.map.put("bib.date all ", "\"" + message + "\" "+finalMethod);
            } else if (Objects.equals(condition, "genre")) {
                this.map.put("bib.otherid all ", "\"" + message + "\" "+finalMethod);
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

    /**
     * Clears all filter conditions for searching books.
     */
    public void clearFilter(){
        this.map.clear();
        index = 1;
        max=null;
    }

    /**
     * Checks if there are any active filter conditions.
     *
     * @return true if there are no active filter conditions, false otherwise
     */
    public boolean isFilterEmpty(){
        return this.map.isEmpty();
    }

    /**
     * Converts the map of filter conditions to a string representation for use in the search query.
     *
     * @return the string representation of the filter conditions
     */
    private String mapToString(){
        List<String> reformattedList = new ArrayList<>();

        // Reformat the entries and add to the list
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            // Split value into words
            String[] words = value.split(" ");
            // Get the last word (finalMethod)
            String finalMethod = words[words.length - 1];
            // Get the rest of the message
            String message = String.join(" ", Arrays.copyOf(words, words.length - 1));

            // Ensure a space between " and finalMethod
            if (message.endsWith("\"")) {
                message += " ";
            }

            // Reformat the string
            String reformatted = finalMethod + " " + key + message;
            reformattedList.add(reformatted);
        }

        // Sort the list alphabetically
        Collections.sort(reformattedList);

        // Merge the list into a single string
        String finalString = String.join("", reformattedList);

        // Remove the first four characters if the finalString is longer than 4 characters
        if (finalString.length() > 4) {
            finalString = finalString.substring(4);
        }

        return finalString;
    }

    /**
     * Creates an HTTP request with the specified URI.
     *
     * @param uri the URI of the request
     * @return the HTTP request object
     */
    private HttpRequest createHttpRequest(URI uri) {
        return HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
    }

    /**
     * Sends an HTTP request and retrieves the response.
     *
     * @param request the HTTP request to send
     * @return the HTTP response
     * @throws Exception if there is an error while sending the request or processing the response
     */
    private HttpResponse<String> sendHttpRequest(HttpRequest request) throws Exception {
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Gets the current index used for pagination.
     *
     * @return the current index
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * Sets the current index used for pagination.
     *
     * @param index the new index value
     */
    public void setIndex(Integer index) {
        this.index = index;
    }

    /**
     * Gets the maximum number of results to retrieve in each request.
     *
     * @return the maximum number of results
     */
    public Integer getMax() {
        return max;
    }

    /**
     * Sets the maximum number of results to retrieve in each request.
     *
     * @param jump the new maximum number of results
     */
    public void setJump(Integer jump) {
        this.jump = jump;
    }

    /**
     * Gets the current maximum number of results to retrieve in each request.
     *
     * @return the current maximum number of results
     */
    public Integer getJump() {
        return jump;
    }
}
