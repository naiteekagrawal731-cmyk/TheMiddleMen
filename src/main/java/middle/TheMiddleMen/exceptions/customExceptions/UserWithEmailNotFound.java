package middle.TheMiddleMen.exceptions.customExceptions;

public class UserWithEmailNotFound extends RuntimeException {
    public UserWithEmailNotFound(String message) {
        super(message);
    }
}
