package com.ldnhat.springbootblog.controller;

import com.ldnhat.springbootblog.dto.LoginDto;
import com.ldnhat.springbootblog.dto.SignupDto;
import com.ldnhat.springbootblog.entity.RoleEntity;
import com.ldnhat.springbootblog.entity.UserEntity;
import com.ldnhat.springbootblog.payload.JwtAuthResponse;
import com.ldnhat.springbootblog.repository.RoleRepository;
import com.ldnhat.springbootblog.repository.UserRepository;
import com.ldnhat.springbootblog.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/sign-in")
    public ResponseEntity<JwtAuthResponse> authenticateUser(@RequestBody LoginDto loginDto){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginDto.getUsernameOrEmail(), loginDto.getPassword()));

        ///The SecurityContext is used to store the details of
        // the currently authenticated user, also known as a principle.
        // So, if you have to get the username or any other user details,
        // you need to get this SecurityContext first. The SecurityContextHolder
        // is a helper class, which provide access to the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JwtAuthResponse(token));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@RequestBody SignupDto signupDto){
        if (userRepository.existsByUsername(signupDto.getUsername())){
            return new ResponseEntity<>("username is already taken!", HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(signupDto.getEmail())){
            return new ResponseEntity<>("email is already taken!", HttpStatus.BAD_REQUEST);
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setName(signupDto.getName());
        userEntity.setEmail(signupDto.getEmail());
        userEntity.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        userEntity.setUsername(signupDto.getUsername());

        RoleEntity roleEntity = roleRepository.findByName("ROLE_ADMIN").get();
        userEntity.setRoleEntities(Collections.singleton(roleEntity));

        userRepository.save(userEntity);
        return new ResponseEntity<>("User register successfully!", HttpStatus.OK);
    }
}
