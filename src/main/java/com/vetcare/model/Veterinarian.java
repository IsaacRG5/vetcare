package com.vetcare.model;

import com.vetcare.model.enums.EstadoGeneral;
import com.vetcare.model.enums.Especialidad;

public class Veterinarian {

    private Long id;
    private String numeroDocumento;
    private String nombre;
    private String tarjetaProfesional;
    private Especialidad especialidad;
    private String telefono;
    private String correo;
    private EstadoGeneral estado;

    public Veterinarian() {
    }

    public Veterinarian(String numeroDocumento, String nombre, String tarjetaProfesional,
                        Especialidad especialidad, String telefono, String correo,
                        EstadoGeneral estado) {
        this.numeroDocumento = numeroDocumento;
        this.nombre = nombre;
        this.tarjetaProfesional = tarjetaProfesional;
        this.especialidad = especialidad;
        this.telefono = telefono;
        this.correo = correo;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTarjetaProfesional() {
        return tarjetaProfesional;
    }

    public void setTarjetaProfesional(String tarjetaProfesional) {
        this.tarjetaProfesional = tarjetaProfesional;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public EstadoGeneral getEstado() {
        return estado;
    }

    public void setEstado(EstadoGeneral estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Veterinarian{" +
                "id=" + id +
                ", numeroDocumento='" + numeroDocumento + '\'' +
                ", nombre='" + nombre + '\'' +
                ", tarjetaProfesional='" + tarjetaProfesional + '\'' +
                ", especialidad=" + especialidad +
                ", telefono='" + telefono + '\'' +
                ", correo='" + correo + '\'' +
                ", estado=" + estado +
                '}';
    }
}