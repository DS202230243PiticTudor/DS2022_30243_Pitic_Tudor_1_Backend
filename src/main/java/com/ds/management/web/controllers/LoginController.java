package com.ds.management.web.controllers;

import com.ds.management.domain.dtos.PersonDeviceDTO;
import com.ds.management.domain.dtos.PersonLoginDTO;
import com.ds.management.domain.entities.Person;
import com.ds.management.domain.entities.UserPrincipal;
import com.ds.management.security.utility.JWTTokenProvider;
import com.ds.management.services.PersonService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import static com.ds.management.security.constant.SecurityConstant.*;

@RestController
@RequestMapping(value = "/login")
public class LoginController {

    private PersonService personService;
    private ModelMapper modelMapper;
    private AuthenticationManager authenticationManager;
    private JWTTokenProvider jwtTokenProvider;

    @Autowired
    public LoginController(
            PersonService personService,
            ModelMapper modelMapper,
            AuthenticationManager authenticationManager,
            JWTTokenProvider jwtTokenProvider
    ){
        this.personService = personService;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping()
    public ResponseEntity<PersonDeviceDTO> login(@RequestBody PersonLoginDTO dto) {
        authenticate(dto.getUsername(), dto.getPassword());
        Person loginPerson = personService.findEntityByUsername(dto.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(loginPerson);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        return ResponseEntity.ok()
                .headers(jwtHeader)
                .body(this.modelMapper.map(loginPerson, PersonDeviceDTO.class));
    }

    private HttpHeaders getJwtHeader(UserPrincipal userPrincipal) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, this.jwtTokenProvider.generateJwtToken(userPrincipal));
        return headers;
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}
