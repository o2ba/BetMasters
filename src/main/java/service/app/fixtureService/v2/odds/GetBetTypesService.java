package service.app.fixtureService.v2.odds;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GetBetTypesService {
    public String getBetTypesAsJsonArray() throws JSONException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JSONArray jsonArray = new JSONArray();
        List<Object> list = new ArrayList<>();

        for (BetTypes betType : BetTypes.values()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", betType.getId());
            jsonObject.put("shortName", betType.getShortName());
            jsonObject.put("longName", betType.getLongName());
            jsonObject.put("possibleValues", betType.getPossibleValues());
            jsonArray.put(jsonObject);

            Map<String, Object> map = new HashMap<>();
            map.put("id", jsonObject.get("id"));
            map.put("shortName", jsonObject.get("shortName"));
            map.put("longName", jsonObject.get("longName"));
            map.put("possibleValues", jsonObject.get("possibleValues"));
            list.add(map);
        }

        return gson.toJson(list);
    }
}