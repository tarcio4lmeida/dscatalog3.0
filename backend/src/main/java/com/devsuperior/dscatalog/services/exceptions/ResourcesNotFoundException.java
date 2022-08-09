package com.devsuperior.dscatalog.services.exceptions;

public class ResourcesNotFoundException extends RuntimeException{

    public ResourcesNotFoundException(String message) {
        super(message);
    }
}
