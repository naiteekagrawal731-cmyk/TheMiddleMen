package middle.TheMiddleMen.services;

import lombok.extern.slf4j.Slf4j;
import middle.TheMiddleMen.dtos.requestDtos.ChangePasswordRequest;
import middle.TheMiddleMen.entities.User;
import middle.TheMiddleMen.exceptions.customExceptions.UserWithEmailNotFound;
import middle.TheMiddleMen.exceptions.customExceptions.UsernameNotFound;
import middle.TheMiddleMen.exceptions.customExceptions.UsernameTaken;
import middle.TheMiddleMen.repos.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password4j.BcryptPassword4jPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final RandomPasswordGenerator randomPasswordGenerator;
    //Used becuase at the time of OAuth2 the spring boot has not yet created beens
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository, RandomPasswordGenerator randomPasswordGenerator) {
        this.userRepository = userRepository;
        this.randomPasswordGenerator = randomPasswordGenerator;
    }

    Optional<User> getUserByEmail(String email){
        Optional<User> user = userRepository.getUserByEmail(email);
        return user;
    }
    Optional<User> getUserByUsername(String username){
        return userRepository.getUserByUsername(username);
    }

    void createUserByEmai(String email){
        if(getUserByEmail(email).isPresent())return;
        User user = User.builder()
                .email(email)
                .username(email)
                .password(passwordEncoder.encode(randomPasswordGenerator.generatePassowrd()))
                .build();
        userRepository.save(user);
    }

    void createUserByUsernameAndPassword(String username,String password){
        if(getUserByUsername(username).isPresent()){
            log.error("User with username = "+username+" already exist");
            throw new UsernameTaken("User with username = "+username+" already exist");
        }
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();
        userRepository.save(user);
        log.info("User with username = "+username+" ,Created SuccessFully");
    }

    public ResponseEntity<String> changeUserPassword(ChangePasswordRequest request){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getUserByUsername(username).orElseThrow(() -> new UsernameNotFound("User with username = "+username+" does not exists"));
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        log.info("User = "+username+" password updated successfully");
        return ResponseEntity.accepted().body("User password changed successfully");
    }



}
