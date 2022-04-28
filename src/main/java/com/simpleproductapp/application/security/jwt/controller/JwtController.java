package com.simpleproductapp.application.security.jwt.controller;

import com.simpleproductapp.application.security.jwt.models.AuthenticationRequest;
import com.simpleproductapp.application.security.jwt.models.AuthenticationResponse;
import com.simpleproductapp.application.security.jwt.util.JwtUtil;
import com.simpleproductapp.application.security.userDetailsService.MyUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestController
@CrossOrigin
@Slf4j
public class JwtController {

    private final AuthenticationManager authenticationManager;
    private final MyUserDetailsService userDetailsService;
    private final JwtUtil jwtTokenUtil;

    public JwtController(AuthenticationManager authenticationManager, MyUserDetailsService userDetailsService, JwtUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/token")
    public ResponseEntity<?> createAutenthicationToken(@RequestBody AuthenticationRequest authenticationRequest,
                                                       HttpServletRequest request) throws Exception{
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
            log.debug("Dodjeljivanje novog tokena useru: "+ authenticationRequest.getUsername()+", time: "+ LocalDateTime.now()+", IP adress: "+request.getRemoteAddr()+", Browser info: "+ request.getHeader("User-Agent"));
        }
        catch (BadCredentialsException e){
            throw new Exception("Netocan email ili password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt="Bearer "+ jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));

    }

    @GetMapping("/log_out")
    @PreAuthorize("hasAnyRole('ROLE_SUPERUSER', 'ROLE_SUPERADMIN', 'ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_DRIVER')")
    public void logout(HttpServletRequest request){
        log.debug("Logout user:: "+ request.getRemoteUser()+", time: "+ LocalDateTime.now()+", IP adress: "+request.getRemoteAddr()+", Browser info: "+ request.getHeader("User-Agent"));
    }
}
