package service.app.fixtureService.v2.common.model;

import com.google.gson.JsonArray;

public record FootballResponse(
        JsonArray response,
        JsonArray errors,
        int results
) {
}
