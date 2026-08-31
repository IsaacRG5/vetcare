package com.vetcare.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String medicineName, int disponible, int solicitado) {
        super("Stock insuficiente de " + medicineName + ". Disponible: " + disponible +
                ", solicitado: " + solicitado);
    }

}
