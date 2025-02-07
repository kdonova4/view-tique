package learn.review_tique.security;

import learn.review_tique.data.AppUserRepository;
import learn.review_tique.models.AppUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.List;


@Service
public class AppUserService implements UserDetailsService {
    private final AppUserRepository repository;
    private final PasswordEncoder encoder;

    public AppUserService(AppUserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public AppUser getUserById(int userId) {
        return repository.findById(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = repository.findByUsername(username);

        if (appUser == null || !appUser.isEnabled()) {
            throw new UsernameNotFoundException(username + " NOT FOUND");
        }

        return appUser;
    }

    public AppUser create(String username, String password) {
        validate(username);
        validatePassword(password);

        password = encoder.encode(password);

        AppUser appUser = new AppUser(0, username, password, false, List.of("User"));

        return repository.create(appUser);
    }

    private void validate(String username) {
        if(username == null || username.isBlank()) {
            throw new ValidationException("Username IS REQUIRED");
        }

        if(username.length() > 50) {
            throw new ValidationException("Username MUST BE LESS THAN 50 CHARACTERS");
        }
    }

    private void validatePassword(String password) {
        if(password == null || password.isBlank()) {
            throw new ValidationException("Password MUST BE AT LEAST 8 CHARACTERS");
        }

        int digits = 0;
        int letters = 0;
        int others = 0;

        for(char c : password.toCharArray()) {
            if(Character.isDigit(c)) {
                digits++;
            } else if(Character.isLetter(c)) {
                letters++;
            } else {
                others++;
            }
        }

        if(digits == 0 || letters == 0 || others == 0) {
            throw new ValidationException("Password MUST CONTAIN A DIGIT, A LETTER, AND A NON-DIGIT/NON-LETTER");
        }
    }
}
