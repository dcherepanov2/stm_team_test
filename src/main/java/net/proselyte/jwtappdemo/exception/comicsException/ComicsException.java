package net.proselyte.jwtappdemo.exception.comicsException;

public class ComicsException extends Exception{
    private final String message;

    public ComicsException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
