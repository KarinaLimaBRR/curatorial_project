package com.curadoria.model;

public class Recurso {
    
    private int idRecurso;
    private String titulo;
    private String autor;
    private int idUsuario;
    private Categoria categoria; // Associação com Categoria


    public Recurso(){}

    public Recurso(int idRecurso, String titulo, String autor, int idUsuario, Categoria categoria) {
        this.idRecurso = idRecurso;
        this.titulo = titulo;
        this.autor = autor;
        this.idUsuario = idUsuario;
        this.categoria = categoria;
    }
    public int getIdRecurso() {
        return idRecurso;
    }

    public void setIdRecurso(int idRecurso) {
        this.idRecurso = idRecurso;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
