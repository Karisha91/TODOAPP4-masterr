package ToDoApp3.demo.Controller;

import ToDoApp3.demo.Service.JWTService;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class TestController {

    private JWTService jwtService;

    public TestController(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping("/")
    public String welcome(Principal principal){
        return "Welcome to TODO APP " +principal.getName();
    }
    @GetMapping("/test")
    public ResponseEntity<String> testAuth() {
        return ResponseEntity.ok("Authenticated successfully!");
    }

    @GetMapping("/test-token")
    public String testToken(@RequestHeader("Authorization") String authHeader) {
        System.out.println("Raw Authorization header: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return "Invalid Authorization header format. Should be: Bearer <token>";
        }

        String token = authHeader.substring(7);
        System.out.println("Extracted token: '" + token + "'");
        System.out.println("Token length: " + token.length());
        System.out.println("Number of periods: " + token.chars().filter(c -> c == '.').count());

        try {
            String username = jwtService.extractUsername(token);
            return "Token valid for user: " + username;
        } catch (Exception e) {
            return "Token invalid: " + e.getMessage();
        }
    }
    @GetMapping("/simple-test")
    public String simpleTest(@RequestHeader("Authorization") String authHeader) {
        return "Received: " + authHeader;
    }
    @GetMapping("/test-token-manually")
    public String testTokenManually(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7); // Remove "Bearer "
            System.out.println("Testing token: " + token);

            // Test extraction
            String username = jwtService.extractUsername(token);
            System.out.println("Username extracted: " + username);



            return "Token is valid for user: " + username;
        } catch (Exception e) {
            System.out.println("Manual test failed: " + e.getMessage());
            e.printStackTrace();
            return "Token test failed: " + e.getMessage();
        }
    }

}



