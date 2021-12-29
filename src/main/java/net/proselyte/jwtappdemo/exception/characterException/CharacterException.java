package net.proselyte.jwtappdemo.exception.characterException;


public class CharacterException extends Exception{

    private final String message;

    public CharacterException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
