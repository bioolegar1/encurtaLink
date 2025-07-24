package olegari.bio.encurtalink.auth;

import olegari.bio.encurtalink.config.TokenService;
import olegari.bio.encurtalink.dto.auth.AuthenticationResponse;
import olegari.bio.encurtalink.dto.auth.AuthenticationRequest;
import olegari.bio.encurtalink.dto.auth.RegisterRequest;
import olegari.bio.encurtalink.user.User;
import olegari.bio.encurtalink.user.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new IllegalArgumentException("Username já está em uso.");
        }
        User user = new User();
        request.username().contains(passwordEncoder.encode(request.password())
                );
        userRepository.save(user);
        String jwtToken = tokenService.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        String jwtToken = tokenService.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }
}
