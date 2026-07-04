package com.taskmanager.controller;
import com.taskmanager.dto.Dtos.*;
import com.taskmanager.model.User;
import com.taskmanager.repository.UserRepository;
import com.taskmanager.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
@RestController @RequestMapping("/api/auth")
public class AuthController {
  private final UserRepository users; private final PasswordEncoder passwords; private final JwtService jwt;
  public AuthController(UserRepository u,PasswordEncoder p,JwtService j){users=u;passwords=p;jwt=j;}
  @PostMapping("/register") @ResponseStatus(HttpStatus.CREATED) public Auth register(@Valid @RequestBody Register r){
    if(users.existsByEmail(r.email().toLowerCase())) throw new ResponseStatusException(HttpStatus.CONFLICT,"Email already registered");
    User u=new User();u.setName(r.name());u.setEmail(r.email().toLowerCase());u.setPassword(passwords.encode(r.password()));users.save(u);return auth(u);
  }
  @PostMapping("/login") public Auth login(@Valid @RequestBody Login r){User u=users.findByEmail(r.email().toLowerCase())
    .filter(x->passwords.matches(r.password(),x.getPassword())).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid credentials"));return auth(u);}
  @PostMapping("/refresh") public Auth refresh(@Valid @RequestBody Refresh r){String email=jwt.email(r.refreshToken(),"refresh");
    return auth(users.findByEmail(email).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED)));}
  private Auth auth(User u){return new Auth(jwt.access(u.getEmail()),jwt.refresh(u.getEmail()),new UserView(u.getId(),u.getName(),u.getEmail()));}
}
