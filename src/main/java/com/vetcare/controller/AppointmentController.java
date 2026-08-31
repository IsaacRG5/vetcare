package com.vetcare.controller;

import com.vetcare.model.Appointment;
import com.vetcare.service.AppointmentService;
import com.vetcare.service.impl.AppointmentServiceImpl;

import java.sql.SQLException;
import java.util.List;

public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController() {
        this.appointmentService = new AppointmentServiceImpl();
    }

    public Appointment agendarAppointment(Appointment appointment) throws SQLException {
        return appointmentService.agendar(appointment);
    }

    public Appointment buscarAppointmentPorId(Long id) throws SQLException {
        return appointmentService.buscarPorId(id);
    }

    public List<Appointment> listarAppointments() throws SQLException {
        return appointmentService.listarTodas();
    }

    public List<Appointment> listarAppointmentsPorMascota(Long petId) throws SQLException {
        return appointmentService.listarPorMascota(petId);
    }

    public List<Appointment> listarAppointmentsPorVeterinario(Long veterinarianId) throws SQLException {
        return appointmentService.listarPorVeterinario(veterinarianId);
    }

    public void confirmarAppointment(Long id) throws SQLException {
        appointmentService.confirmar(id);
    }

    public void cancelarAppointment(Long id) throws SQLException {
        appointmentService.cancelar(id);
    }
}
