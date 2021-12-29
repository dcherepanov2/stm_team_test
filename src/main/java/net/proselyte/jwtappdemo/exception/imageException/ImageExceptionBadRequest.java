package net.proselyte.jwtappdemo.exception.imageException;

public class ImageExceptionBadRequest extends Exception{

    private final String message;

    public ImageExceptionBadRequest(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
