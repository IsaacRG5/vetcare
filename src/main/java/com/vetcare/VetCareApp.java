package com.vetcare;

import com.vetcare.config.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class VetCareApp {

    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            System.out.println("Conexión exitosa a la base de datos VetCare");
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos:");
            e.printStackTrace();
        }
    }
}