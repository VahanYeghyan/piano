package exception;

public class WrongNumberException extends Exception{
    public WrongNumberException() {
        super();
    }

    public WrongNumberException(String message) {
        super(message);
    }
}
