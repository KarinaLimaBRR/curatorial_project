package com.curadoria.dao;

import com.curadoria.db.ConnectionFactory;
import com.curadoria.model.Recurso;
import com.curadoria.model.Categoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecursoDAO {

    public void cadastrar(Recurso r) {
        String sql = "INSERT INTO recurso (titulo, autor, idCategoria, idUsuario) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setString(1, r.getTitulo());
            pstm.setString(2, r.getAutor());
            pstm.setInt(3, r.getCategoria().getIdCategoria());
            pstm.setInt(4, r.getIdUsuario());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Recurso> listarTodos() {
        String sql = "SELECT r.idRecurso, r.titulo, r.autor, r.idCategoria, c.nome AS categoria_nome, r.idUsuario " +
                     "FROM recurso r LEFT JOIN categoria c ON r.idCategoria = c.idCategoria " +
                     "ORDER BY r.titulo ASC";
        List<Recurso> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                Recurso r = new Recurso();
                r.setIdRecurso(rs.getInt("idRecurso"));
                r.setTitulo(rs.getString("titulo"));
                r.setAutor(rs.getString("autor"));
                Categoria c = new Categoria();
                c.setIdCategoria(rs.getInt("idCategoria"));
                c.setNome(rs.getString("categoria_nome"));
                r.setCategoria(c);
                r.setIdUsuario(rs.getInt("idUsuario"));
                lista.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
