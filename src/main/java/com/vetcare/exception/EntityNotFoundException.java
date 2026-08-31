package com.vetcare.exception;

public class EntityNotFoundException extends VetCareException{

    public EntityNotFoundException(String entityName, Long id) {
        super(entityName + " no encontrado con id: " + id);
    }
}
