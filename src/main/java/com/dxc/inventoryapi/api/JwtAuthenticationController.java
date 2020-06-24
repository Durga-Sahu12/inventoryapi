package com.dxc.inventoryapi.api;

 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

 

import com.dxc.inventoryapi.config.JwtTokenUtil;
import com.dxc.inventoryapi.entity.InventoryUser;
import com.dxc.inventoryapi.entity.JwtToken;
import com.dxc.inventoryapi.service.UserDetailsServiceImpl;

 

@RestController
@CrossOrigin
public class JwtAuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;  //hv to generatr token
    
    @Autowired
    private UserDetailsServiceImpl userDetailsService;  //store user

 

    @PostMapping("/signin")
    public ResponseEntity<JwtToken> createAuthenticationToken(@RequestBody InventoryUser user) throws Exception {
        authenticate(user.getUserName(), user.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtToken(token));
    }

 

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

 

    @PostMapping("/signup")
    public ResponseEntity<InventoryUser> saveUser(@RequestBody  InventoryUser user) throws Exception {
        return ResponseEntity.ok(userDetailsService.save(user));
    }
}