package com.vetcare.service;

import com.vetcare.model.Appointment;
import java.sql.SQLException;
import java.util.List;


public interface AppointmentService {

    Appointment agendar(Appointment appointment) throws SQLException;

    Appointment buscarPorId(Long id) throws SQLException;

    List<Appointment> listarTodas() throws SQLException;

    List<Appointment> listarPorMascota(Long petId) throws SQLException;

    List<Appointment> listarPorVeterinario(Long veterinarianId) throws SQLException;

    void confirmar(Long id) throws SQLException;

    void cancelar(Long id) throws SQLException;
}
