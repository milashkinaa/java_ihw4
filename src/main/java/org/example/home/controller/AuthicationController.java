package org.example.home.controller;
import org.example.home.dto.DtoLogin;
import org.example.home.dto.DtoUserInfo;
import org.example.home.security.Generator;
import org.example.home.domain.entity.Role;
import org.example.home.domain.entity.User;
import org.example.home.dto.DtoRegister;
import org.example.home.service.ServiceSession;
import org.example.home.service.ServiceUser;
import org.example.home.help.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.validator.routines.EmailValidator;


@RestController
@RequestMapping(value = "/api/auth")
public class AuthicationController {

    private final ServiceUser serviceUser;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ServiceSession serviceSession;

    @Autowired
    public AuthicationController(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder,
                                 ServiceUser serviceUser, ServiceSession serviceSession) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.serviceUser = serviceUser;
        this.serviceSession = serviceSession;
    }

    @PostMapping("register")
    @Transactional
    public ResponseEntity<String> register(@RequestBody DtoRegister dtoRegister) {
        if (dtoRegister.getUserRole() == null || dtoRegister.getUsername() == null ||
                dtoRegister.getEmail() == null || dtoRegister.getPassword() == null) {
            return new ResponseEntity<>("Error! Not all fields are filled in", HttpStatus.BAD_REQUEST);
        }
        if (serviceUser.findByEmail(dtoRegister.getEmail()) != null) {
            return new ResponseEntity<>("An account with such a mail is already registered", HttpStatus.BAD_REQUEST);
        } else if (serviceUser.findByUsername(dtoRegister.getUsername()) != null) {
            return new ResponseEntity<>("An account with this username is already registered", HttpStatus.BAD_REQUEST);
        }

        Role role;
        try {
            role = Valid.validate(dtoRegister.getUserRole());
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>("The wrong role", HttpStatus.BAD_REQUEST);
        }

        if (!EmailValidator.getInstance().isValid(dtoRegister.getEmail())) {
            return new ResponseEntity<>("The mailing address is incorrect", HttpStatus.BAD_REQUEST);
        }

        serviceUser.saveUser(dtoRegister, passwordEncoder, role);
        serviceSession.saveSession(serviceUser.findByEmail(dtoRegister.getEmail()), new Generator());

        return new ResponseEntity<>("User is registered successfully", HttpStatus.OK);
    }

    @PostMapping("get_info")
    public ResponseEntity<String> getInfo(@RequestBody DtoUserInfo dtoUserInfo) {
        if (dtoUserInfo.getUsername() == null) {
            return new ResponseEntity<>("Error! Empty username", HttpStatus.BAD_REQUEST);
        }
        User user = serviceUser.findByUsername(dtoUserInfo.getUsername());
        if (user == null) {
            return new ResponseEntity<>("The user with the specified nickname is not registered", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    "ID = " + user.getId() +
                    "; USERNAME = " + user.getUsername() +
                    "; EMAIL = " + user.getEmail() +
                    "; ROLE = " + user.getRole().toString() +
                    "; CREATED_AT = " + user.getCreatedAt() +
                    ";", HttpStatus.OK);
        }
    }
    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody DtoLogin dtoLogin) {
        if (dtoLogin.getEmail() == null || dtoLogin.getPassword() == null) {
            return new ResponseEntity<>("Error! Not all fields are filled in", HttpStatus.BAD_REQUEST);
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dtoLogin.getEmail(), dtoLogin.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return new ResponseEntity<>("You entered", HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Incorrect credentials", HttpStatus.BAD_REQUEST);
        }
    }
}
