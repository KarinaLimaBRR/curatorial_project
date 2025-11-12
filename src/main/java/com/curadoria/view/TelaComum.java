package com.curadoria.view;

import com.curadoria.dao.CategoriaDAO;
import com.curadoria.dao.RecursoDAO;
import com.curadoria.model.Categoria;
import com.curadoria.model.Recurso;
import com.curadoria.model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaComum extends JFrame {
    private Usuario usuarioLogado;
    private RecursoDAO recursoDAO;
    private CategoriaDAO categoriaDAO;

    private JTextField txtTitulo, txtAutor;
    private JComboBox<Categoria> cmbCategoria;
    private JButton btnCadastrar;
    private JTable tabelaRecursos;
    private DefaultTableModel modeloTabela;

    public TelaComum(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
        setTitle("Recursos sobre IA e Cibersegurança - Usuário: " + usuarioLogado.getNome());
        setSize(800, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        recursoDAO = new RecursoDAO();
        categoriaDAO = new CategoriaDAO();

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Cadastrar Recurso", criarPainelCadastro());
        tabs.addTab("Listagem de Recursos", criarPainelListagem());

        add(tabs, BorderLayout.CENTER);

        JButton btnSair = new JButton("Sair");
        btnSair.addActionListener(e -> {
            dispose();
            new TelaLogin().setVisible(true);
        });
        JPanel bottom = new JPanel();
        bottom.add(btnSair);
        add(bottom, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel criarPainelCadastro() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8,8,8,8);
        c.gridx = 0; c.gridy = 0; c.anchor = GridBagConstraints.WEST;
        p.add(new JLabel("Título:"), c);
        txtTitulo = new JTextField(30);
        c.gridx = 1; p.add(txtTitulo, c);

        c.gridx = 0; c.gridy++;
        p.add(new JLabel("Autor:"), c);
        txtAutor = new JTextField(30);
        c.gridx = 1; p.add(txtAutor, c);

        c.gridx = 0; c.gridy++;
        p.add(new JLabel("Categoria:"), c);
        cmbCategoria = new JComboBox<>();
        carregarCategorias();
        c.gridx = 1; p.add(cmbCategoria, c);

        c.gridx = 1; c.gridy++;
        btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.addActionListener(e -> cadastrarRecurso());
        p.add(btnCadastrar, c);

        return p;
    }

    private JPanel criarPainelListagem() {
        JPanel p = new JPanel(new BorderLayout());
        modeloTabela = new DefaultTableModel(new Object[]{"Título","Autor","Categoria"}, 0);
        tabelaRecursos = new JTable(modeloTabela);
        p.add(new JScrollPane(tabelaRecursos), BorderLayout.CENTER);
        carregarTabelaRecursos();
        return p;
    }

    private void carregarCategorias() {
        cmbCategoria.removeAllItems();
        List<Categoria> lista = categoriaDAO.listarTodas();
        if (lista == null || lista.isEmpty()) {
            categoriaDAO.inserirPadroes();
            lista = categoriaDAO.listarTodas();
        }
        for (Categoria c : lista) cmbCategoria.addItem(c);
    }

    private void carregarTabelaRecursos() {
        modeloTabela.setRowCount(0);
        List<Recurso> lista = recursoDAO.listarTodos();
        for (Recurso r : lista) {
            modeloTabela.addRow(new Object[]{r.getTitulo(), r.getAutor(), r.getCategoria() != null ? r.getCategoria().getNome() : ""});
        }
    }

    private void cadastrarRecurso() {
        String titulo = txtTitulo.getText().trim();
        String autor = txtAutor.getText().trim();
        Categoria cat = (Categoria) cmbCategoria.getSelectedItem();
        if (titulo.isEmpty() || autor.isEmpty() || cat == null) {
            JOptionPane.showMessageDialog(this, "Título, Autor e Categoria são obrigatórios!", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Recurso r = new Recurso();
        r.setTitulo(titulo);
        r.setAutor(autor);
        r.setCategoria(cat);
        r.setIdUsuario(usuarioLogado.getIdUsuario());
        recursoDAO.cadastrar(r);
        JOptionPane.showMessageDialog(this, "Recurso cadastrado com sucesso!");
        txtTitulo.setText("");
        txtAutor.setText("");
        carregarTabelaRecursos();
    }
}
