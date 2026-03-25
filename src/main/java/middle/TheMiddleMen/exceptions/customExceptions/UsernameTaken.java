package middle.TheMiddleMen.exceptions.customExceptions;

public class UsernameTaken extends RuntimeException {
    public UsernameTaken(String message) {
        super(message);
    }
}
