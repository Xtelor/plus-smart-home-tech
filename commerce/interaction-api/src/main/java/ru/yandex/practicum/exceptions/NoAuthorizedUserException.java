package ru.yandex.practicum.exceptions;

public class NoAuthorizedUserException extends RuntimeException {
    public NoAuthorizedUserException(String message) {
        super(message);
    }
}
