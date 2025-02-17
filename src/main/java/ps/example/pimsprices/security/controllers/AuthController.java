package ps.example.pimsprices.security.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

import ps.example.pimsprices.security.exceptions.TokenRefreshException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ps.example.pimsprices.security.entities.ERole;
import ps.example.pimsprices.security.entities.RefreshToken;
import ps.example.pimsprices.security.entities.Role;
import ps.example.pimsprices.security.entities.User;

import ps.example.pimsprices.security.payload.request.LoginRequest;
import ps.example.pimsprices.security.payload.request.SignupRequest;
import ps.example.pimsprices.security.payload.request.TokenRefreshRequest;
import ps.example.pimsprices.security.payload.response.JwtResponse;
import ps.example.pimsprices.security.payload.response.MessageResponse;
import ps.example.pimsprices.security.payload.response.TokenRefreshResponse;
import ps.example.pimsprices.security.repositories.RoleRepository;
import ps.example.pimsprices.security.repositories.UserRepository;
import ps.example.pimsprices.security.jwts.JwtUtils;
import ps.example.pimsprices.security.services.RefreshTokenService;
import ps.example.pimsprices.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getFirstname(), signUpRequest.getSurname());

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) { // adnotacjami w dtos
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> { //w stream
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
                userDetails.getUsername(), userDetails.getEmail(), userDetails.getFirstname(), userDetails.getSurname(), roles));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser) // użytkownika nie z bazy tylko z access tokena sprawdzić czy to byłoby wystarczające
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());

                    RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user.getId());
                    refreshTokenService.deleteByToken(requestRefreshToken);

                    return ResponseEntity.ok(new TokenRefreshResponse(token, newRefreshToken.getToken()));
                })
//                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
//              })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

//        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Long userId = userDetails.getId();

        refreshTokenService.deleteByToken(requestRefreshToken); // jak zrobią bug przy logout przy logout urządzenie podaje swoje id i nie zabezpieczamy endpointa
//        refreshTokenService.deleteByUserId(userId);
        return ResponseEntity.ok(new MessageResponse("Log out successful!"));
    }

}



































//
//import ps.example.pimsprices.controller.auth.dto.JwtTokenRequest;
//import ps.example.pimsprices.controller.auth.dto.JwtTokenResponse;
//import ps.example.pimsprices.controller.auth.dto.UserRegistrationDto;
//import ps.example.pimsprices.security.entities.RefreshToken;
//import ps.example.pimsprices.security.entities.Role;
//import ps.example.pimsprices.security.entities.User;
//import ps.example.pimsprices.security.repositories.RoleRepository;
//import ps.example.pimsprices.security.repositories.UserRepository;
//import ps.example.pimsprices.service.auth.JwtTokenService;
//
//import ps.example.pimsprices.service.auth.RefreshTokenService;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//import static ps.example.pimsprices.config.auth.SpringSecurityConfig.APP_ADMIN;
//import static ps.example.pimsprices.config.auth.SpringSecurityConfig.APP_GUEST;
//
//
//@RestController
//@RequestMapping("/api/v1")
//@CrossOrigin(origins = "http://localhost:3000")
//public class AuthController {
//
//    private AuthenticationManager authenticationManager;
//    private JwtTokenService jwtTokenService;
//        private RefreshTokenService refreshTokenService;
//
//    public AuthController(AuthenticationManager authenticationManager, JwtTokenService jwtTokenService, RefreshTokenService refreshTokenService){
//        this.authenticationManager = authenticationManager;
//        this.jwtTokenService = jwtTokenService;
//        this.refreshTokenService = refreshTokenService;
//    }
//
//    @PostMapping("/login")
//    public JwtTokenResponse login(@Valid @RequestBody JwtTokenRequest jwtTokenRequest) {
//        var authentication = new UsernamePasswordAuthenticationToken(
//                jwtTokenRequest.username(), jwtTokenRequest.password()
//        );
//        authenticationManager.authenticate(authentication);
//
//        // Assuming you have a User object representing the authenticated user
//        User user = userRepository.findByEmail(jwtTokenRequest.username()).orElseThrow();
//
//        // Generate both access and refresh tokens
//        String accessToken = jwtTokenService.generateAccessToken(jwtTokenRequest.username());
////        String refreshToken = jwtTokenService.generateRefreshToken(user);
////        String refreshToken = refreshTokenService.generateRefreshToken(user);
//        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
////        String refreshToken = jwtTokenService.generateRefreshToken(jwtTokenRequest.username());
//        String userEmail = user.getEmail();
//        String userName = user.getName();
//        String userSurname = user.getSurname();
//
//        return new JwtTokenResponse(accessToken, refreshToken.getToken(), userEmail, userName, userSurname);
//    }
//
//    //TODO ugly code
////    1. register method should return some dto with new user data, including uuid
////    2. Use some UserService/AuthService for registration, use PasswordEncoder in it
////    3. Above service should use repositories
////    4. Above service should use mapper to map DTOs and Entities
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private RoleRepository roleRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @PostMapping("/register")
//    public void register(@Valid @RequestBody UserRegistrationDto newUserDto) {
//        User newUser = new User();
//        newUser.setEmail(newUserDto.email());
//        newUser.setPassword(passwordEncoder.encode(newUserDto.password()));
//
//        Role userRole = roleRepository.findByName(APP_GUEST).get();
//        newUser.addRole(userRole);
//        userRole.addUser(newUser);
//
//        userRepository.save(newUser);
//    }
//}