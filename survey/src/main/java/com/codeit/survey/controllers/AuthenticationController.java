package com.codeit.survey.controllers;

import com.codeit.survey.DTOs.AuthenticationRequest;
import com.codeit.survey.DTOs.AuthenticationResponse;
import com.codeit.survey.security.CustomUserDetails;
import com.codeit.survey.services.CustomUserDetailService;
import com.codeit.survey.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private AuthenticationManager authenticationManager;
    private CustomUserDetailService customUserDetailService;
    private JwtUtil jwtUtil;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    CustomUserDetailService customUserDetailService,
                                    JwtUtil jwtUtil){
        this.authenticationManager = authenticationManager;
        this.customUserDetailService = customUserDetailService;
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
        }catch (BadCredentialsException e){
            throw new Exception("incorrect username or password", e);
        }

        // authentication done successfully
        // get the userDetails
        UserDetails userDetails =  customUserDetailService.loadUserByUsername(authenticationRequest.getUsername());

        // get a JWT using this userDetail
        String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

}
