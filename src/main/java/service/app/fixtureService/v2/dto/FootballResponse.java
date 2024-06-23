package service.app.fixtureService.v2.dto;

import com.google.gson.JsonArray;

public record FootballResponse(
        JsonArray response,
        JsonArray errors,
        int results
) {
}
