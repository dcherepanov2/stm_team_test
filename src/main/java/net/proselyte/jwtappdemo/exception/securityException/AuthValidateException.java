package net.proselyte.jwtappdemo.exception.securityException;

public class AuthValidateException extends Exception{

    private String message;

    public AuthValidateException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
