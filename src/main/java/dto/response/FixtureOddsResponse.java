package dto.response;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * A class to represent the response from the API.
 *
 * @param <T> The type of __object that the payload contains.
 */
public class FixtureOddsResponse<T> {
    // -------------- Fields --------------

    /**
     * The total number of results returned by the API.
     */
    private final int amountOfResults;

    /**
     * The parameters sent in the API request.
     */
    private final JsonElement parameters;

    /**
     * Any errors returned by the API.
     */
    private final JsonArray error;

    /**
     * The actual data returned by the API, converted into objects of type T.
     * This, for example would be the list of fixtures or leagues.
     */
    private List<T> payload;

    /**
     * Pagination system. This is the current page.
     */
    private int currentPage;

    /**
     * Pagination system. This is how many results are on each page.
     */
    private int totalPages;

    /** Response code */
    private final int responseCode;

    /**
     * The response headers.
     */
    private final @NotNull Map<String, List<String>> responseHeaders;


    /**
     * The time taken to construct the __object. This involves converting the JsonElement into objects of type T.
     */
    private final @NotNull Long constructorTime;

    /* -------------- Constructors -------------- */

    /**
     * Constructs an APIPayload __object without a limit on the number of objects in the payload.
     *
     * @param jsonData The raw data from the API.
     * @param converter A function that converts a JsonElement into an __object of type T.
     */
    public FixtureOddsResponse(JsonElement jsonData, Function<JsonElement, T> converter, int responseCode,
                               @NotNull Map<String, List<String>> responseHeaders) {


        long startTime = System.currentTimeMillis();

        this.responseHeaders = responseHeaders;

        this.responseCode = responseCode;
        this.amountOfResults = jsonData.getAsJsonObject().get("results").getAsInt();
        this.parameters = jsonData.getAsJsonObject().get("parameters");
        this.error = jsonData.getAsJsonObject().get("errors").getAsJsonArray();

        try {
            this.currentPage = jsonData.getAsJsonObject().get("paging").getAsJsonObject().get("current").getAsInt();
            this.totalPages = jsonData.getAsJsonObject().get("paging").getAsJsonObject().get("total").getAsInt();
        } catch (Exception e) {
            this.currentPage = 1;
            this.totalPages = 1;
        }

        try {
            this.payload = toObjectList(jsonData.getAsJsonObject().get("response"), converter);
        } catch (Exception e) {
            System.out.println("Failed to parse payload. Error: " + e.getMessage());
            this.payload = new ArrayList<>();
        }

        this.constructorTime = System.currentTimeMillis() - startTime;

    }

    /* -------------- Setters -------------- */

    /**
     * Sets the payload.
     *
     * @param payload The payload.
     */
    public void setPayload(List<T> payload) { this.payload = payload; }

    /* -------------- Getters -------------- */

    /**
     * Returns the payload.
     *
     * @return The payload.
     */
    public List<T> getPayload() { return payload; }

    /**
     * Returns the amount of results.
     *
     * @return The amount of results.
     */
    public int getAmountOfResults() { return amountOfResults; }

    /**
     * Returns the parameters.
     *
     * @return The parameters.
     */
    @SuppressWarnings("unused")
    public JsonElement getParameters() { return parameters; }

    /**
     * Returns the error.
     *
     * @return The error.
     */
    public JsonArray getError() { return error; }

    /**
     * Returns the current page.
     *
     * @return The current page.
     */
    @SuppressWarnings("unused")
    public int getCurrentPage() { return currentPage; }

    /**
     * Returns the total pages.
     *
     * @return The total pages.
     */
    public int getTotalPages() { return totalPages; }

    /**
     * Returns the response code.
     *
     * @return The response code.
     */
    public int getResponseCode() { return responseCode; }

    /**
     * Returns the response headers.
     *
     * @return The response headers.
     */
    @NotNull
    public Map<String, List<String>> getResponseHeaders() { return responseHeaders; }

    /**
     * Returns the time taken to construct the __object.
     *
     * @return The time taken to construct the __object.
     */
    public long getConstructorTime() { return constructorTime; }

    /* -------------- Helper Methods -------------- */

    /**
     * Converts a JsonElement into a List of objects of type T.
     *
     * @param data The JsonElement to convert.
     * @param converter The function to use for the conversion.
     * @return A List of objects of type T.
     */
    private List<T> toObjectList(JsonElement data, Function<JsonElement, T> converter) {
        List<T> list = new ArrayList<>();
        JsonArray jsonArray = data.getAsJsonArray();
        for (JsonElement element : jsonArray) {
            list.add(converter.apply(element));
        }
        return list;
    }
}