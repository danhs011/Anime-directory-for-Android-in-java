package com.example.tfgv20.POJO;

public class ObjetoRelation {

    private String imagen;
    private String texto1;
    private String testo2;

    public ObjetoRelation(String imagen, String texto1, String testo2) {
        this.imagen = imagen;
        this.texto1 = texto1;
        this.testo2 = testo2;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getTexto1() {
        return texto1;
    }

    public void setTexto1(String texto1) {
        this.texto1 = texto1;
    }

    public String getTesto2() {
        return testo2;
    }

    public void setTesto2(String testo2) {
        this.testo2 = testo2;
    }
}
