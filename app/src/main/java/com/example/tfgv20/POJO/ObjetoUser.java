package com.example.tfgv20.POJO;

public class ObjetoUser {
    private String imagen;
    private int id;
    private String nombre;

    public ObjetoUser(String imagen, int id, String nombre) {
        this.imagen = imagen;
        this.id = id;
        this.nombre = nombre;
    }

    public ObjetoUser() {
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
