package com.curadoria.view;

import javax.swing.*;
import java.awt.event.*;
import com.curadoria.dao.UsuarioDAO;
import com.curadoria.model.Usuario;

public class TelaLogin extends JFrame implements ActionListener {

    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JButton btnEntrar, btnSair;

    public TelaLogin() {
        setTitle("Sistema de Curadoria - Login");
        setSize(420, 240);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblLogin = new JLabel("Login:");
        lblLogin.setBounds(40, 30, 80, 25);
        add(lblLogin);

        txtLogin = new JTextField();
        txtLogin.setBounds(140, 30, 220, 25);
        add(txtLogin);

        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setBounds(40, 70, 80, 25);
        add(lblSenha);

        txtSenha = new JPasswordField();
        txtSenha.setBounds(140, 70, 220, 25);
        add(txtSenha);

        btnEntrar = new JButton("Entrar");
        btnEntrar.setBounds(140, 120, 100, 30);
        btnEntrar.addActionListener(this);
        add(btnEntrar);

        btnSair = new JButton("Sair");
        btnSair.setBounds(260, 120, 100, 30);
        btnSair.addActionListener(this);
        add(btnSair);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnEntrar) {
            realizarLogin();
        } else if (e.getSource() == btnSair) {
            System.exit(0);
        }
    }

    private void realizarLogin() {
        String login = txtLogin.getText().trim();
        String senha = new String(txtSenha.getPassword()).trim();

        UsuarioDAO dao = new UsuarioDAO();
        Usuario usuario = dao.buscarPorLogin(login, senha);

        if (usuario != null) {
            String tipo = usuario.getTipo() != null ? usuario.getTipo().toLowerCase() : "";
            if (tipo.contains("admin")) {
                new TelaAdmin(usuario).setVisible(true);
            } else {
                new TelaComum(usuario).setVisible(true);
            }
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Login ou senha incorretos / conta inativa.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaLogin());
    }
}
