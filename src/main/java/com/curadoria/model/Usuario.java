package com.curadoria.model;

public class Usuario {
    
    private int IdUsuario;
    private String nome;
    private int idade;
    private String login;
    private String senha;
    private String tipo;
    private Boolean status;
    private String interesses;

    public Usuario() {}


    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public int getIdade() {
        return idade;       
}   
    public void setIdade(int idade) {
        this.idade = idade;
    }
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public Boolean getStatus() {
        return status;
    }
    public void setStatus(Boolean status) {
        this.status = status;
    }
    public int getIdUsuario() {
        return IdUsuario;
    }
    public void setIdUsuario(int idUsuario) {
        IdUsuario = idUsuario;
    }

    public String getInteresses() { return interesses; }
    public void setInteresses(String interesses) { this.interesses = interesses; }

}
