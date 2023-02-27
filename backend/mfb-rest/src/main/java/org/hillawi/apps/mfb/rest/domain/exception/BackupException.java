package org.hillawi.apps.mfb.rest.domain.exception;

/**
 * @author Ahmed Hillawi
 * @since 14/11/22
 */
public class BackupException extends RuntimeException {

    public BackupException() {
    }

    public BackupException(String message) {
        super(message);
    }

    public BackupException(String message, Throwable cause) {
        super(message, cause);
    }
}
