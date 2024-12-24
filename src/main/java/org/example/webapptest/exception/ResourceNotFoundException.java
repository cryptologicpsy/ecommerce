package org.example.webapptest.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    // Μπορείτε επίσης να προσθέσετε περισσότερους constructors αν χρειάζεται
}

