package controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Api(value = "Fixtures Management")
@Controller
public class MiscController {

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("Will later redirect to documentation.");
    }

    @ApiOperation(value = "Supported Leagues", notes = "Gets a list of the leagues supported by the API.")
    @GetMapping("/leagues/supported")
    @ResponseBody
    public ResponseEntity<String> leagues() {
        try {
            ClassPathResource cpr = new ClassPathResource("supportedLeagues.json");
            byte[] bdata = FileCopyUtils.copyToByteArray(cpr.getInputStream());
            String data = new String(bdata, StandardCharsets.UTF_8);
            return ResponseEntity.ok(data);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reading leagues file");
        }
    }

    /**
     * Returns a 418 I'm a teapot response. Serves to test the error handling.
     * @return a 418 I'm a teapot response
     */
    @GetMapping("/teapot")
    @ResponseBody
    public ResponseEntity<String> teapot() {
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("I'm a teapot");
    }

}
