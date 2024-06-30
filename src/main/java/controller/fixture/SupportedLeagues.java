package controller.fixture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
public class SupportedLeagues {

    private ResourceLoader resourceLoader;

    private String supportedLeagues = "[\n" +
            "  {\n" +
            "    \"name\": \"Serie A\",\n" +
            "    \"id\": 71,\n" +
            "    \"logo\": \"https://media.api-sports.io/football/leagues/71.png\",\n" +
            "    \"country\": \"Brazil\",\n" +
            "    \"country_flag\": \"https://media.api-sports.io/flags/br.svg\",\n" +
            "    \"current_season\": \"2024\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Primeria Nacional\",\n" +
            "    \"id\": 129,\n" +
            "    \"logo\": \"https://media.api-sports.io/football/leagues/129.png\",\n" +
            "    \"country\": \"Argentina\",\n" +
            "    \"country_flag\": \"https://media.api-sports.io/flags/ar.svg\",\n" +
            "    \"current_season\": \"2024\"\n" +
            "  }\n" +
            "]";

    @Autowired
    public SupportedLeagues(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @GetMapping("/leagues/supported")
    public ResponseEntity<String> getSupportedLeagues() {
        try {
            Resource resource = resourceLoader.getResource("classpath:supportedLeagues.json");
            String content = Files.readString(Path.of(resource.getURI()));
            return ResponseEntity.ok(content);
        } catch (Exception e) {
            try {
                return ResponseEntity.ok(supportedLeagues);
            } catch (Exception e2) {
                return ResponseEntity.status(500).body(e2.getMessage());
            }
        }
    }
}
