package com.example.tfgv20.POJO;

public class ObjetoMiniatura {
    private String imagen;
    private int id;

    public ObjetoMiniatura(String imagen, int id) {
        this.imagen = imagen;
        this.id = id;
    }

    public ObjetoMiniatura() {
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
}
