package com.vetcare.model;

import com.vetcare.model.enums.EstadoGeneral;
import java.time.LocalDate;

public class Medicine {
    private Long id;
    private String codigo;
    private String nombre;
    private String presentacion;
    private String laboratorio;
    private Integer cantidadDisponible;
    private Integer cantidadMinima;
    private Double precioUnitario;
    private EstadoGeneral estado;
    private LocalDate fechaRegistro;

    public Medicine() {
    }

    public Medicine(String codigo, String nombre, String presentacion, String laboratorio,
                    Integer cantidadDisponible, Integer cantidadMinima, Double precioUnitario) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.presentacion = presentacion;
        this.laboratorio = laboratorio;
        this.cantidadDisponible = cantidadDisponible;
        this.cantidadMinima = cantidadMinima;
        this.precioUnitario = precioUnitario;
        this.estado = EstadoGeneral.ACTIVO;
        this.fechaRegistro = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getLaboratorio() {
        return laboratorio;
    }

    public void setLaboratorio(String laboratorio) {
        this.laboratorio = laboratorio;
    }

    public Integer getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(Integer cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public Integer getCantidadMinima() {
        return cantidadMinima;
    }

    public void setCantidadMinima(Integer cantidadMinima) {
        this.cantidadMinima = cantidadMinima;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public EstadoGeneral getEstado() {
        return estado;
    }

    public void setEstado(EstadoGeneral estado) {
        this.estado = estado;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @Override
    public String toString() {
        return "Medicine{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", presentacion='" + presentacion + '\'' +
                ", laboratorio='" + laboratorio + '\'' +
                ", cantidadDisponible=" + cantidadDisponible +
                ", cantidadMinima=" + cantidadMinima +
                ", precioUnitario=" + precioUnitario +
                ", estado=" + estado +
                ", fechaRegistro=" + fechaRegistro +
                '}';
    }

}
