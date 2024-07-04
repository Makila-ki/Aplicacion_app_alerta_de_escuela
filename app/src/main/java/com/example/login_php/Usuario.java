package com.example.login_php;

public class Usuario {
    String Nombre;
    String Matricula;
    String Contraseña;

    String Tipo;

    public Usuario(String nombre, String matricula, String contraseña, String tipo) {
        this.Nombre = nombre;
        this.Matricula = matricula;
        this.Contraseña = contraseña;
        this.Tipo = tipo;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getMatricula() {
        return Matricula;
    }

    public void setMatricula(String matricula) {
        Matricula = matricula;
    }

    public String getContraseña() {
        return Contraseña;
    }

    public void setContraseña(String contraseña) {
        Contraseña = contraseña;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }
}
