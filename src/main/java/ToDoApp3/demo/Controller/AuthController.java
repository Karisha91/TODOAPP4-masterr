package ToDoApp3.demo.Controller;

import ToDoApp3.demo.Dto.LoginDto;
import ToDoApp3.demo.Dto.RegistrationDto;
import ToDoApp3.demo.Model.User;
import ToDoApp3.demo.Service.JWTService;
import ToDoApp3.demo.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private JWTService jwtService;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, JWTService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;

    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegistrationDto registrationDto) {
        User newUser = userService.registerNewUser(registrationDto);
        return ResponseEntity.ok(newUser);
    }
    // New login method
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(),
                            loginDto.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtService.generateToken(loginDto.getUsername());

            // Return proper JSON response
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("message", "Login successful");

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Authentication failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }
}
