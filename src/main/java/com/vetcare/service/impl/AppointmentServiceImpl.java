package com.vetcare.service.impl;

import com.vetcare.exception.EntityNotFoundException;
import com.vetcare.exception.HorarioNoDisponibleException;
import com.vetcare.exception.ValidationException;
import com.vetcare.model.Appointment;
import com.vetcare.model.Pet;
import com.vetcare.model.Veterinarian;
import com.vetcare.model.enums.EstadoCita;
import com.vetcare.repository.AppointmentRepository;
import com.vetcare.repository.PetRepository;
import com.vetcare.repository.VeterinarianRepository;
import com.vetcare.repository.impl.AppointmentRepositoryImpl;
import com.vetcare.repository.impl.PetRepositoryImpl;
import com.vetcare.repository.impl.VeterinarianRepositoryImpl;
import com.vetcare.service.AppointmentService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PetRepository petRepository;
    private final VeterinarianRepository veterinarianRepository;

    public AppointmentServiceImpl() {
        this.appointmentRepository = new AppointmentRepositoryImpl();
        this.petRepository = new PetRepositoryImpl();
        this.veterinarianRepository = new VeterinarianRepositoryImpl();
    }

    @Override
    public Appointment agendar(Appointment appointment) throws SQLException {
        validarDatosBasicos(appointment);

        Optional<Pet> pet = petRepository.findById(appointment.getPetId());
        if (pet.isEmpty()) {
            throw new EntityNotFoundException("Pet", appointment.getPetId());
        }

        Optional<Veterinarian> veterinarian = veterinarianRepository.findById(appointment.getVeterinarianId());
        if (veterinarian.isEmpty()) {
            throw new EntityNotFoundException("Veterinarian", appointment.getVeterinarianId());
        }

        boolean vetOcupado = appointmentRepository.existsByVeterinarianAndFechaHora(
                appointment.getVeterinarianId(), appointment.getFecha(), appointment.getHora());
        if (vetOcupado) {
            throw new HorarioNoDisponibleException(
                    "El veterinario ya tiene una cita agendada en esa fecha y hora");
        }

        boolean petOcupada = appointmentRepository.existsByPetAndFechaHora(
                appointment.getPetId(), appointment.getFecha(), appointment.getHora());
        if (petOcupada) {
            throw new HorarioNoDisponibleException(
                    "La mascota ya tiene una cita agendada en esa fecha y hora");
        }

        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment buscarPorId(Long id) throws SQLException {
        Optional<Appointment> resultado = appointmentRepository.findById(id);

        if (resultado.isEmpty()) {
            throw new EntityNotFoundException("Appointment", id);
        }

        return resultado.get();
    }

    @Override
    public List<Appointment> listarTodas() throws SQLException {
        return appointmentRepository.findAll();
    }

    @Override
    public List<Appointment> listarPorMascota(Long petId) throws SQLException {
        return appointmentRepository.findByPetId(petId);
    }

    @Override
    public List<Appointment> listarPorVeterinario(Long veterinarianId) throws SQLException {
        return appointmentRepository.findByVeterinarianId(veterinarianId);
    }

    @Override
    public void confirmar(Long id) throws SQLException {
        buscarPorId(id);

        appointmentRepository.updateEstado(id, EstadoCita.CONFIRMADA.name());
    }

    @Override
    public void cancelar(Long id) throws SQLException {
        buscarPorId(id);

        appointmentRepository.updateEstado(id, EstadoCita.CANCELADA.name());
    }

    private void validarDatosBasicos(Appointment appointment) {
        if (appointment.getPetId() == null) {
            throw new ValidationException("Debe indicar la mascota para la cita");
        }

        if (appointment.getVeterinarianId() == null) {
            throw new ValidationException("Debe indicar el veterinario para la cita");
        }

        if (appointment.getFecha() == null) {
            throw new ValidationException("La fecha de la cita es obligatoria");
        }

        if (appointment.getFecha().isBefore(LocalDate.now())) {
            throw new ValidationException("La fecha de la cita no puede ser anterior a hoy");
        }

        if (appointment.getHora() == null) {
            throw new ValidationException("La hora de la cita es obligatoria");
        }
    }
}