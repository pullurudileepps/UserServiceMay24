package org.example.userservicemay24.exceptions;

public class InvalidLoginRequestException extends Exception {
    public InvalidLoginRequestException(String message) {
        super(message);
    }
}
