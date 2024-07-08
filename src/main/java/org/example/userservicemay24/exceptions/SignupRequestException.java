package org.example.userservicemay24.exceptions;

public class SignupRequestException extends RuntimeException {
    public SignupRequestException(String message) {
        super(message);
    }
}
