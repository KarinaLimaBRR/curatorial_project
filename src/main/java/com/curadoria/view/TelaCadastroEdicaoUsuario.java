package com.curadoria.view;

import com.curadoria.dao.UsuarioDAO;
import com.curadoria.model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

public class TelaCadastroEdicaoUsuario extends JDialog {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private Usuario usuarioEdicao;

    private JTextField txtNome, txtIdade, txtLogin;
    private JPasswordField txtSenha, txtConfirmaSenha;
    private JComboBox<String> cmbTipo;
    private JCheckBox chkIA, chkCyber, chkEtica;
    private JButton btnSalvar, btnCancelar;

    public TelaCadastroEdicaoUsuario(JFrame parent, Usuario usuario) {
        super(parent, usuario == null ? "Cadastrar Usuário" : "Editar Usuário", true);
        this.usuarioEdicao = usuario;
        initComponents();
        pack();
        setMinimumSize(new Dimension(480, 420));
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void initComponents() {
        setLayout(new BorderLayout(8,8));
        JPanel form = new JPanel(new GridLayout(7,2,8,8));
        form.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        txtNome = new JTextField();
        txtIdade = new JTextField();
        txtLogin = new JTextField();
        txtSenha = new JPasswordField();
        txtConfirmaSenha = new JPasswordField();
        cmbTipo = new JComboBox<>(new String[]{"Admin","Comum"});
        chkIA = new JCheckBox("IA Responsável");
        chkCyber = new JCheckBox("Cibersegurança");
        chkEtica = new JCheckBox("Privacidade & Ética Digital");

        form.add(new JLabel("Nome:")); form.add(txtNome);
        form.add(new JLabel("Idade:")); form.add(txtIdade);
        form.add(new JLabel("Login:")); form.add(txtLogin);
        form.add(new JLabel("Senha:")); form.add(txtSenha);
        form.add(new JLabel("Confirme Senha:")); form.add(txtConfirmaSenha);
        form.add(new JLabel("Tipo:")); form.add(cmbTipo);

        add(form, BorderLayout.NORTH);

        JPanel interesses = new JPanel(new GridLayout(0,1));
        interesses.setBorder(BorderFactory.createTitledBorder("Interesses (até 2)"));
        interesses.add(chkIA); interesses.add(chkCyber); interesses.add(chkEtica);
        add(interesses, BorderLayout.CENTER);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSalvar = new JButton(usuarioEdicao == null ? "Cadastrar" : "Salvar");
        btnCancelar = new JButton("Cancelar");
        botoes.add(btnCancelar); botoes.add(btnSalvar);
        add(botoes, BorderLayout.SOUTH);

        // listeners
        btnCancelar.addActionListener(e -> dispose());
        btnSalvar.addActionListener(e -> onSalvar());
        ItemListener limitador = ev -> {
            int cnt = 0;
            if (chkIA.isSelected()) cnt++;
            if (chkCyber.isSelected()) cnt++;
            if (chkEtica.isSelected()) cnt++;
            if (cnt > 2) {
                JCheckBox src = (JCheckBox) ev.getItemSelectable();
                src.setSelected(false);
                JOptionPane.showMessageDialog(this, "Máximo de 2 interesses permitidos.");
            }
        };
        chkIA.addItemListener(limitador);
        chkCyber.addItemListener(limitador);
        chkEtica.addItemListener(limitador);

        if (usuarioEdicao != null) preencherParaEdicao();
    }

    private void preencherParaEdicao() {
        txtNome.setText(usuarioEdicao.getNome());
        txtIdade.setText(String.valueOf(usuarioEdicao.getIdade()));
        txtLogin.setText(usuarioEdicao.getLogin());
        txtLogin.setEditable(false);
        cmbTipo.setSelectedItem(usuarioEdicao.getTipo());
        String ints = usuarioEdicao.getInteresses();
        if (ints != null) {
            String s = ints.toLowerCase();
            chkIA.setSelected(s.contains("ia"));
            chkCyber.setSelected(s.contains("cyber"));
            chkEtica.setSelected(s.contains("ética") || s.contains("etica"));
        }
    }

    private boolean validar() {
        String nome = txtNome.getText().trim();
        String idade = txtIdade.getText().trim();
        String login = txtLogin.getText().trim();
        String s1 = new String(txtSenha.getPassword());
        String s2 = new String(txtConfirmaSenha.getPassword());
        if (nome.isEmpty() || idade.isEmpty() || login.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome, idade e login são obrigatórios.");
            return false;
        }
        try { Integer.parseInt(idade); } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Idade inválida.");
            return false;
        }
        if (usuarioEdicao == null && (s1.isEmpty() || s2.isEmpty())) {
            JOptionPane.showMessageDialog(this, "Senha e confirmação são obrigatórias.");
            return false;
        }
        if (!s1.equals(s2)) {
            JOptionPane.showMessageDialog(this, "Senhas não coincidem.");
            return false;
        }
        return true;
    }

    private void onSalvar() {
        if (!validar()) return;
        Usuario u = usuarioEdicao == null ? new Usuario() : usuarioEdicao;
        u.setNome(txtNome.getText().trim());
        u.setIdade(Integer.parseInt(txtIdade.getText().trim()));
        if (usuarioEdicao == null) u.setLogin(txtLogin.getText().trim());
        String senha = new String(txtSenha.getPassword()).trim();
        if (!senha.isEmpty()) u.setSenha(senha);
        u.setTipo((String) cmbTipo.getSelectedItem());
        u.setStatus(true);
        List<String> ints = new ArrayList<>();
        if (chkIA.isSelected()) ints.add("IA");
        if (chkCyber.isSelected()) ints.add("Cibersegurança");
        if (chkEtica.isSelected()) ints.add("Privacidade & Ética Digital");
        u.setInteresses(String.join(", ", ints));

        try {
            if (usuarioEdicao == null) usuarioDAO.cadastrar(u);
            else usuarioDAO.atualizar(u);
            JOptionPane.showMessageDialog(this, "Salvo com sucesso!");
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage());
        }
    }
}
