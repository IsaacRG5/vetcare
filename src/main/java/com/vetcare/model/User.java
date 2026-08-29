package com.vetcare.model;

import com.vetcare.model.enums.Rol;
import com.vetcare.model.enums.EstadoGeneral;

public class User {


    private Long id;
    private String username;
    private String password;
    private Rol rol;
    private EstadoGeneral estado;

    public User (){

    }

    public User (String username, String password, Rol rol){

        this.username = username;
        this.password = password;
        this.rol = rol;
        this.estado = EstadoGeneral.ACTIVO;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public EstadoGeneral getEstado() {
        return estado;
    }

    public void setEstado(EstadoGeneral estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", rol=" + rol +
                ", estado=" + estado +
                '}';
    }

}
