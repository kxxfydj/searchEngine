package com.kxxfydj.exception;

/**
 * Created by kxxfydj on 2018/3/5.
 */
public class ResponseStatusException extends RuntimeException {
    public ResponseStatusException() {
        super();
    }

    public ResponseStatusException(String message) {
        super(message);
    }

    public ResponseStatusException(Throwable cause) {
        super(cause);
    }

    public ResponseStatusException(String message, Throwable cause) {
        super(message, cause);
    }
}
