package com.terreneitors.backendclintec.auth.domain;

import com.terreneitors.backendclintec.users.domain.Rol;

public class AuthUser {
    private String email;
    private String password;
    private Rol rol;
    private Boolean activo;

    public AuthUser() {
    }

    public AuthUser(String email, String password, Rol rol, Boolean activo) {
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.activo = activo;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Rol getRol() {
        return rol;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
