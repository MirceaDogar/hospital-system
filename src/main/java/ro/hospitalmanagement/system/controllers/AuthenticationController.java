package ro.hospitalmanagement.system.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.hospitalmanagement.system.entities.RoleEntity;
import ro.hospitalmanagement.system.entities.UserEntity;
import ro.hospitalmanagement.system.exceptions.InexistentResourceException;
import ro.hospitalmanagement.system.model.Role;
import ro.hospitalmanagement.system.repositories.RoleRepository;
import ro.hospitalmanagement.system.repositories.UserRepository;
import ro.hospitalmanagement.system.requests.LoginRequest;
import ro.hospitalmanagement.system.requests.SignupRequest;
import ro.hospitalmanagement.system.responses.JwtResponse;
import ro.hospitalmanagement.system.security.JwtUtils;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/auth")
@Validated
public class AuthenticationController {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;

    @Operation(summary = "Sign-in")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = JwtResponse.class)))
    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateToken(authentication);
        User user = (User) authentication.getPrincipal();
        JwtResponse jwtResponse = new JwtResponse(user.getUsername(), jwt);
        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }

    @Operation(summary = "Sign-up")
    @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(schema = @Schema(implementation = JwtResponse.class)))
    @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(schema = @Schema(implementation = Void.class)))
    @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(schema = @Schema(implementation = Void.class)))
    @PostMapping("/signup")
    public ResponseEntity<SignupRequest> registerUser(@Valid @RequestBody SignupRequest signupRequest) throws InexistentResourceException {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        UserEntity user = new UserEntity();
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());
        user.setUsername(signupRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setAge(signupRequest.getAge());
        user.setActive(signupRequest.isActive());
        Set<String> signupRequestRole = signupRequest.getRole();
        Set<RoleEntity> roles = new HashSet<>();
        if (signupRequestRole == null) {
            RoleEntity userRole = roleRepository.findByName(Role.ROLE_DOCTOR).orElseThrow(() -> new InexistentResourceException("Add role!!!"));
            roles.add(userRole);
        } else {
            signupRequestRole.forEach(role -> {
                switch (role) {
                    case "administrator" -> {
                        RoleEntity adminRole = roleRepository.findByName(Role.ROLE_ADMIN).orElseThrow(RuntimeException::new);
                        roles.add(adminRole);
                    }
                    case "doctor" -> {
                        RoleEntity doctorRole = roleRepository.findByName(Role.ROLE_DOCTOR).orElseThrow(RuntimeException::new);
                        roles.add(doctorRole);
                    }
                    case "nurse" -> {
                        RoleEntity nurseRole = roleRepository.findByName(Role.ROLE_NURSE).orElseThrow(RuntimeException::new);
                        roles.add(nurseRole);
                    }
                    case "patient" -> {
                        RoleEntity patientRole = roleRepository.findByName(Role.ROLE_PATIENT).orElseThrow(RuntimeException::new);
                        roles.add(patientRole);
                    }
                    default -> {
                        try {
                            throw new InexistentResourceException("Incorrect role");
                        } catch (InexistentResourceException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}