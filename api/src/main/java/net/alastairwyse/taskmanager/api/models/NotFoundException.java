package net.alastairwyse.taskmanager.api.models;

/**
 * An exception which is thrown when a resource doesn't exist or could not be found.
 */
public class NotFoundException extends RuntimeException{

    /**
     * Constructs a NotFoundException.
     * @param msg Details of the exception.
     */
    public NotFoundException(String msg) {
        super(msg);
    }
}
