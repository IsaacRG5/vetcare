package com.vetcare.view;

import javax.swing.JOptionPane;

public class MainMenu {

    public void mostrar() {
        boolean salir = false;

        while (!salir) {
            String[] opciones = {
                    "Propietarios", "Mascotas", "Veterinarios",
                    "Citas", "Atenciones Médicas", "Medicamentos", "Salir"
            };

            int seleccion = JOptionPane.showOptionDialog(
                    null,
                    "Bienvenido a VetCare\nSeleccione una opción:",
                    "VetCare - Menú Principal",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            switch (seleccion) {
                case 0 -> new OwnerView().mostrarMenu();
                case 1 -> JOptionPane.showMessageDialog(null, "Módulo de Mascotas - próximamente");
                case 2 -> JOptionPane.showMessageDialog(null, "Módulo de Veterinarios - próximamente");
                case 3 -> JOptionPane.showMessageDialog(null, "Módulo de Citas - próximamente");
                case 4 -> JOptionPane.showMessageDialog(null, "Módulo de Atenciones - próximamente");
                case 5 -> JOptionPane.showMessageDialog(null, "Módulo de Medicamentos - próximamente");
                default -> salir = true;
            }
        }

        JOptionPane.showMessageDialog(null, "Gracias por usar VetCare");
    }

}