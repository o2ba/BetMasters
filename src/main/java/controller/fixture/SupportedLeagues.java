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
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error reading file");
        }
    }
}
