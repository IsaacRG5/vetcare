package com.vetcare.view;

import com.vetcare.controller.OwnerController;
import com.vetcare.exception.VetCareException;
import com.vetcare.model.Owner;
import com.vetcare.model.enums.EstadoGeneral;

import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.util.List;

public class OwnerView {

    private final OwnerController ownerController;

    public OwnerView() {
        this.ownerController = new OwnerController();
    }

    public void mostrarMenu() {
        boolean volver = false;

        while (!volver) {
            String[] opciones = {
                    "Registrar propietario", "Listar propietarios",
                    "Buscar por id", "Actualizar", "Eliminar", "Volver"
            };

            int seleccion = JOptionPane.showOptionDialog(
                    null, "Gestión de Propietarios", "VetCare - Propietarios",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, opciones, opciones[0]
            );

            try {
                switch (seleccion) {
                    case 0 -> registrarOwner();
                    case 1 -> listarOwners();
                    case 2 -> buscarOwner();
                    case 3 -> actualizarOwner();
                    case 4 -> eliminarOwner();
                    default -> volver = true;
                }
            } catch (VetCareException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(),
                        "Validación", JOptionPane.WARNING_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error inesperado: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void registrarOwner() {
        String tipoIdentificacion = JOptionPane.showInputDialog(
                null, "Tipo de identificación (CC, CE, etc.):");
        if (tipoIdentificacion == null) return;

        String numeroIdentificacion = JOptionPane.showInputDialog(
                null, "Número de identificación:");
        if (numeroIdentificacion == null) return;

        String nombreCompleto = JOptionPane.showInputDialog(
                null, "Nombre completo:");
        if (nombreCompleto == null) return;

        String telefono = JOptionPane.showInputDialog(
                null, "Teléfono:");
        if (telefono == null) return;

        String correo = JOptionPane.showInputDialog(
                null, "Correo (opcional):");

        String direccion = JOptionPane.showInputDialog(
                null, "Dirección (opcional):");

        Owner owner = new Owner(tipoIdentificacion, numeroIdentificacion, nombreCompleto,
                telefono, correo, direccion, EstadoGeneral.ACTIVO, LocalDate.now());

        try {
            Owner guardado = ownerController.registrarOwner(owner);
            JOptionPane.showMessageDialog(null,
                    "Propietario registrado con éxito. ID: " + guardado.getId());
        } catch (java.sql.SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void listarOwners() throws java.sql.SQLException {
        List<Owner> owners = ownerController.listarOwners();

        if (owners.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay propietarios registrados");
            return;
        }

        StringBuilder texto = new StringBuilder();
        for (Owner owner : owners) {
            texto.append("ID: ").append(owner.getId())
                    .append(" | ").append(owner.getNombreCompleto())
                    .append(" | ").append(owner.getNumeroIdentificacion())
                    .append(" | ").append(owner.getTelefono())
                    .append(" | ").append(owner.getEstado())
                    .append("\n");
        }

        JOptionPane.showMessageDialog(null, texto.toString(), "Lista de Propietarios",
                JOptionPane.PLAIN_MESSAGE);
    }

    private void buscarOwner() throws java.sql.SQLException {
        String idTexto = JOptionPane.showInputDialog(null, "Ingrese el ID del propietario:");
        if (idTexto == null) return;

        Long id = Long.parseLong(idTexto);

        Owner owner = ownerController.buscarOwnerPorId(id);

        JOptionPane.showMessageDialog(null, owner.toString(), "Propietario encontrado",
                JOptionPane.PLAIN_MESSAGE);
    }

    private void actualizarOwner() throws java.sql.SQLException {
        String idTexto = JOptionPane.showInputDialog(null, "Ingrese el ID del propietario a actualizar:");
        if (idTexto == null) return;

        Long id = Long.parseLong(idTexto);
        Owner owner = ownerController.buscarOwnerPorId(id);

        String nuevoTelefono = JOptionPane.showInputDialog(null,
                "Nuevo teléfono (actual: " + owner.getTelefono() + "):", owner.getTelefono());
        if (nuevoTelefono == null) return;

        String nuevaDireccion = JOptionPane.showInputDialog(null,
                "Nueva dirección (actual: " + owner.getDireccion() + "):", owner.getDireccion());

        owner.setTelefono(nuevoTelefono);
        owner.setDireccion(nuevaDireccion);

        ownerController.actualizarOwner(owner);

        JOptionPane.showMessageDialog(null, "Propietario actualizado correctamente");
    }

    private void eliminarOwner() throws java.sql.SQLException {
        String idTexto = JOptionPane.showInputDialog(null, "Ingrese el ID del propietario a eliminar:");
        if (idTexto == null) return;

        Long id = Long.parseLong(idTexto);

        int confirmacion = JOptionPane.showConfirmDialog(null,
                "¿Está seguro de eliminar este propietario?", "Confirmar",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            ownerController.eliminarOwner(id);
            JOptionPane.showMessageDialog(null, "Propietario eliminado correctamente");
        }
    }
}