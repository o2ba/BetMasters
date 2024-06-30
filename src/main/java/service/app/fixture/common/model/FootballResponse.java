package service.app.fixture.common.model;

import com.google.gson.JsonArray;

public record FootballResponse(
        JsonArray response,
        JsonArray errors,
        int results
) {
}
