package com.tkachuk.pet.exception;

/**
 * Exception that we get when we try saving some object but such object already exist,
 * then we get {@link NotSavedException}.
 *
 * @author Nazarii Tkachuk
 * @version 1.0
 */
public class NotSavedException extends RuntimeException {
    /**
     * Constructor for NotSavedException.
     *
     * @param message - giving message.
     */
    public NotSavedException(String message) {
        super(message);
    }
}