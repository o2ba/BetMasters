package dto.response.model;

import com.google.gson.JsonElement;
import okhttp3.Response;

public record FootballResponsePayload (Response rawResponse, JsonElement jsonResponse) {}