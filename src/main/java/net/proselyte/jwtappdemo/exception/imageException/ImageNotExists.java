package net.proselyte.jwtappdemo.exception.imageException;

public class ImageNotExists extends Exception {
    private final String message;

    public ImageNotExists(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
