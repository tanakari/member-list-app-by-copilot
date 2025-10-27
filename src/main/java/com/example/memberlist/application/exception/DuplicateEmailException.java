package com.example.memberlist.application.exception;

/**
 * Exception thrown when attempting to register a member with a duplicate email address.
 */
public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String message) {
        super(message);
    }

    public DuplicateEmailException(String email, Throwable cause) {
        super("メールアドレスが既に登録されています: " + email, cause);
    }
}
