package com.curadoria.view;

import com.curadoria.dao.UsuarioDAO;
import com.curadoria.model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaAdmin extends JFrame {

    private final Usuario admin; 
    private final UsuarioDAO usuarioDAO;
    private JTable tabela;
    private DefaultTableModel modelo;

    public TelaAdmin(Usuario admin) {
        this.admin = admin;
        this.usuarioDAO = new UsuarioDAO();

        setTitle("Painel Administrativo - Logado como: " + admin.getNome());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        inicializarComponentes();
        carregarTabela();
        setVisible(true);
    }

    private void inicializarComponentes() {
        modelo = new DefaultTableModel(new Object[]{"ID", "Nome", "Login", "Tipo", "Status"}, 0);
        tabela = new JTable(modelo);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton btnNovo = new JButton("Novo");
        JButton btnEditar = new JButton("Editar");
        JButton btnInativar = new JButton("Inativar");
        JButton btnSair = new JButton("Sair");

        painelBotoes.add(btnNovo);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnInativar);
        painelBotoes.add(btnSair);

        add(painelBotoes, BorderLayout.SOUTH);

        // === AÇÕES ===
        btnNovo.addActionListener(e -> {
            new TelaCadastroEdicaoUsuario(this, null);
            carregarTabela();
        });

        btnEditar.addActionListener(e -> editarUsuario());

        btnInativar.addActionListener(e -> inativarUsuario());

        btnSair.addActionListener(e -> {
            dispose();
            new TelaLogin().setVisible(true);
        });
    }

    private void carregarTabela() {
        modelo.setRowCount(0);
        List<Usuario> usuarios = usuarioDAO.listarTodos();

        for (Usuario u : usuarios) {
            modelo.addRow(new Object[]{
                    u.getIdUsuario(),
                    u.getNome(),
                    u.getLogin(),
                    u.getTipo(),
                    u.getStatus() ? "Ativo" : "Inativo"
            });
        }
    }

    private void editarUsuario() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) modelo.getValueAt(linha, 0);
        Usuario usuario = usuarioDAO.buscarPorId(id);

        if (usuario != null) {
            new TelaCadastroEdicaoUsuario(this, usuario);
            carregarTabela();
        } else {
            JOptionPane.showMessageDialog(this, "Usuário não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void inativarUsuario() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para inativar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) modelo.getValueAt(linha, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja inativar este usuário?", "Confirmação", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            usuarioDAO.inativar(id);
            carregarTabela();
        }
    }
}
