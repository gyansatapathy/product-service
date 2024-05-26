//package com.gs.product.controller;
//
//import com.gs.product.domain.ApplicationUser;
//import com.gs.product.dto.JwtRequest;
//import com.gs.product.dto.JwtResponse;
//import com.gs.product.exception.ProductAppAuthenticationException;
//import com.gs.product.repository.ApplicationUserRepository;
//import com.gs.product.service.ApplicationUserDetailService;
//import com.gs.product.util.JwtTokenUtil;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.DisabledException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@CrossOrigin
//public class LoginController {
//    private final ApplicationUserRepository applicationUserRepository;
//    private final ApplicationUserDetailService userDetailService;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtTokenUtil jwtTokenUtil;
//    private final AuthenticationManager authenticationManager;
//
//
//    public LoginController(ApplicationUserRepository applicationUserRepository,
//                           ApplicationUserDetailService userDetailService,
//                           PasswordEncoder passwordEncoder,
//                           JwtTokenUtil jwtTokenUtil,
//                           AuthenticationManager authenticationManager) {
//        this.applicationUserRepository = applicationUserRepository;
//        this.userDetailService = userDetailService;
//        this.passwordEncoder = passwordEncoder;
//        this.jwtTokenUtil = jwtTokenUtil;
//        this.authenticationManager = authenticationManager;
//    }
//
//    @PostMapping("/signup")
//    public void signUp(@RequestBody JwtRequest jwtRequest) {
//        ApplicationUser applicationUser = new ApplicationUser();
//        applicationUser.setUsername(jwtRequest.getUsername());
//        applicationUser.setPassword(passwordEncoder.encode(jwtRequest.getPassword()));
//        applicationUserRepository.save(applicationUser);
//    }
//
//    @PostMapping(value = "/authenticate")
//    public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {
//
//        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
//
//        final UserDetails userDetails = userDetailService
//                .loadUserByUsername(authenticationRequest.getUsername());
//
//        final String token = jwtTokenUtil.generateToken(userDetails);
//
//        return ResponseEntity.ok(new JwtResponse(token));
//    }
//
//    private void authenticate(String username, String password) throws ProductAppAuthenticationException {
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//        } catch (DisabledException e) {
//            throw new ProductAppAuthenticationException("USER_DISABLED", e);
//        } catch (BadCredentialsException e) {
//            throw new ProductAppAuthenticationException("INVALID_CREDENTIALS", e);
//        }
//    }
//}
