package net.proselyte.jwtappdemo.exception.characterException;

public class CharacterAddException extends Exception{

    private String message;

    public CharacterAddException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
