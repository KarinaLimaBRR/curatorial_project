package com.curadoria.dao;

import com.curadoria.db.ConnectionFactory;
import com.curadoria.model.Usuario;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public Usuario buscarPorLogin(String login, String senhaDigitada) {
        String sql = "SELECT idUsuario, nome, idade, login, tipo, status, senha, interesses FROM usuario WHERE login = ? AND status = TRUE";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, login);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    String hash = rs.getString("senha");
                    if (hash != null && BCrypt.checkpw(senhaDigitada, hash)) {
                        Usuario u = new Usuario();
                        u.setIdUsuario(rs.getInt("idUsuario"));
                        u.setNome(rs.getString("nome"));
                        u.setIdade(rs.getInt("idade"));
                        u.setLogin(rs.getString("login"));
                        u.setTipo(rs.getString("tipo"));
                        u.setStatus(rs.getBoolean("status"));
                        u.setSenha(hash);
                        u.setInteresses(rs.getString("interesses"));
                        return u;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Usuario buscarPorId(int idUsuario) {
        String sql = "SELECT idUsuario, nome, idade, login, tipo, status, senha, interesses FROM usuario WHERE idUsuario = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, idUsuario);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setIdUsuario(rs.getInt("idUsuario"));
                    u.setNome(rs.getString("nome"));
                    u.setIdade(rs.getInt("idade"));
                    u.setLogin(rs.getString("login"));
                    u.setTipo(rs.getString("tipo"));
                    u.setStatus(rs.getBoolean("status"));
                    u.setSenha(rs.getString("senha")); // Mantém o hash da senha
                    u.setInteresses(rs.getString("interesses"));
                    return u;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void cadastrar(Usuario usuario) {
        String sql = "INSERT INTO usuario (nome, idade, login, senha, tipo, status, interesses) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            String senhaHash = BCrypt.hashpw(usuario.getSenha(), BCrypt.gensalt());
            pstm.setString(1, usuario.getNome());
            pstm.setInt(2, usuario.getIdade());
            pstm.setString(3, usuario.getLogin());
            pstm.setString(4, senhaHash);
            pstm.setString(5, usuario.getTipo());
            pstm.setBoolean(6, usuario.getStatus());
            pstm.setString(7, usuario.getInteresses());
            pstm.executeUpdate();

            try (ResultSet keys = pstm.getGeneratedKeys()) {
                if (keys.next()) {
                    usuario.setIdUsuario(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void atualizar(Usuario usuario) {
        String sql = "UPDATE usuario SET nome = ?, idade = ?, login = ?, senha = ?, tipo = ?, status = ?, interesses = ? WHERE idUsuario = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            String senha = usuario.getSenha();
            String senhaHash = senha;
            if (senha == null) senhaHash = null;
            else if (!senha.startsWith("$2a$") && !senha.startsWith("$2y$") && !senha.startsWith("$2b$")) {
                senhaHash = BCrypt.hashpw(senha, BCrypt.gensalt());
            }

            pstm.setString(1, usuario.getNome());
            pstm.setInt(2, usuario.getIdade());
            pstm.setString(3, usuario.getLogin());
            pstm.setString(4, senhaHash);
            pstm.setString(5, usuario.getTipo());
            pstm.setBoolean(6, usuario.getStatus());
            pstm.setString(7, usuario.getInteresses());
            pstm.setInt(8, usuario.getIdUsuario());

            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void inativar(int idUsuario) {
        String sql = "UPDATE usuario SET status = FALSE WHERE idUsuario = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, idUsuario);
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Usuario> listarTodos() {
        String sql = "SELECT idUsuario, nome, idade, login, tipo, status, interesses FROM usuario ORDER BY nome";
        List<Usuario> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("idUsuario"));
                u.setNome(rs.getString("nome"));
                u.setIdade(rs.getInt("idade"));
                u.setLogin(rs.getString("login"));
                u.setTipo(rs.getString("tipo"));
                u.setStatus(rs.getBoolean("status"));
                u.setInteresses(rs.getString("interesses"));
                lista.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
