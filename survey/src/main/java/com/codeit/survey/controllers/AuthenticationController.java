package com.codeit.survey.controllers;

import com.codeit.survey.DTOs.AuthenticationRequest;
import com.codeit.survey.DTOs.AuthenticationResponse;
import com.codeit.survey.entities.SurveyUser;
import com.codeit.survey.security.CustomUserDetails;
import com.codeit.survey.services.AuthenticationService;
import com.codeit.survey.services.CustomUserDetailService;
import com.codeit.survey.services.UserService;
import com.codeit.survey.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private AuthenticationManager authenticationManager;
    private CustomUserDetailService customUserDetailService;
    private UserService userService;
    private JwtUtil jwtUtil;
    private AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    CustomUserDetailService customUserDetailService,
                                    UserService userService,
                                    AuthenticationService authenticationService,
                                    JwtUtil jwtUtil){
        this.authenticationManager = authenticationManager;
        this.customUserDetailService = customUserDetailService;
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()
                    )
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong Username or Password");
        }
        // authentication done successfully
        UserDetails userDetails =  customUserDetailService.loadUserByUsername(authenticationRequest.getUsername());

        // get the SurveyUser object
        SurveyUser surveyUser = userService.getSurveyUserByUserName(userDetails.getUsername());

        // get a JWT using this userDetail
        String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(authenticationService.createAuthenticationResponseFromUser(jwt, surveyUser));
    }

}
