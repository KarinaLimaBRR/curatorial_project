package com.curadoria.dao;

import com.curadoria.db.ConnectionFactory;
import com.curadoria.model.Categoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    public List<Categoria> listarTodas() {
        String sql = "SELECT idCategoria, nome, descricao FROM categoria ORDER BY nome";
        List<Categoria> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                Categoria c = new Categoria();
                c.setIdCategoria(rs.getInt("idCategoria"));
                c.setNome(rs.getString("nome"));
                c.setDescricao(rs.getString("descricao"));
                lista.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public Categoria buscarPorId(int id) {
        String sql = "SELECT idCategoria, nome, descricao FROM categoria WHERE idCategoria = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, id);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    Categoria c = new Categoria();
                    c.setIdCategoria(rs.getInt("idCategoria"));
                    c.setNome(rs.getString("nome"));
                    c.setDescricao(rs.getString("descricao"));
                    return c;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void inserirPadroes() {
        String sql = "INSERT IGNORE INTO categoria (nome, descricao) VALUES (?, ?)";
        String[][] pad = {
            {"IA Responsável","Categoria sobre IA Responsável"},
            {"Cibersegurança","Categoria sobre Cibersegurança"},
            {"Privacidade & Ética Digital","Categoria sobre Privacidade e Ética Digital"}
        };
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {
            for (String[] c : pad) {
                pstm.setString(1, c[0]);
                pstm.setString(2, c[1]);
                pstm.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void salvar(Categoria categoria) {
        String sql = "INSERT INTO categoria (nome, descricao) VALUES (?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, categoria.getNome());
            pstm.setString(2, categoria.getDescricao());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
