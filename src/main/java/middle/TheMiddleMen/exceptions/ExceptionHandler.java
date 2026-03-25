package middle.TheMiddleMen.exceptions;

import middle.TheMiddleMen.exceptions.customExceptions.AccessTokenExpired;
import middle.TheMiddleMen.exceptions.customExceptions.InvalidToken;
import middle.TheMiddleMen.exceptions.customExceptions.UsernameNotFound;
import middle.TheMiddleMen.exceptions.customExceptions.UsernameTaken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(AccessTokenExpired.class)
    public ResponseEntity<String> accessTokenExpired(AccessTokenExpired ex){
        return ResponseEntity.status(401).body(ex.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidToken.class)
    public ResponseEntity<String> invalidToken(InvalidToken ex){
        return ResponseEntity.status(401).body(ex.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UsernameNotFound.class)
    public ResponseEntity<String> usernameNotFound(UsernameNotFound ex){
        return ResponseEntity.status(402).body(ex.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UsernameTaken.class)
    public ResponseEntity<String>  usernameTaken(UsernameTaken ex){
        return ResponseEntity.status(409).body(ex.getMessage());
    }


}
