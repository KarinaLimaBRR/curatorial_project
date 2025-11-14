-- ============================================
-- Script de Criação do Banco de Dados
-- Sistema de Curadoria e Compartilhamento de Recursos sobre IA
-- ============================================

-- Criar o banco de dados
CREATE DATABASE IF NOT EXISTS DB_CuradoriaIA;
USE DB_CuradoriaIA;

-- ============================================
-- Tabela: categoria
-- Armazena as categorias fixas do sistema
-- ============================================
CREATE TABLE IF NOT EXISTS categoria (
    idCategoria INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    descricao VARCHAR(255),
    CONSTRAINT uk_categoria_nome UNIQUE (nome)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Tabela: usuario
-- Armazena os usuários do sistema (Admin e Comum)
-- ============================================
CREATE TABLE IF NOT EXISTS usuario (
    idUsuario INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    idade INT NOT NULL,
    login VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    status BOOLEAN DEFAULT TRUE,
    interesses VARCHAR(255),
    CONSTRAINT chk_idade_positiva CHECK (idade > 0),
    CONSTRAINT chk_tipo_valido CHECK (tipo IN ('Administrador', 'Usuário comum', 'Admin', 'Comum')),
    CONSTRAINT uk_usuario_login UNIQUE (login)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Tabela: recurso
-- Armazena os recursos cadastrados pelos usuários
-- ============================================
CREATE TABLE IF NOT EXISTS recurso (
    idRecurso INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    autor VARCHAR(100) NOT NULL,
    idCategoria INT NOT NULL,
    idUsuario INT NOT NULL,
    dataCadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idCategoria) REFERENCES categoria(idCategoria) ON DELETE RESTRICT,
    FOREIGN KEY (idUsuario) REFERENCES usuario(idUsuario) ON DELETE CASCADE,
    INDEX idx_titulo (titulo),
    INDEX idx_categoria (idCategoria),
    INDEX idx_usuario (idUsuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Inserir Categorias Padrão
-- ============================================
INSERT IGNORE INTO categoria (nome, descricao) VALUES
('IA Responsável', 'Categoria sobre IA Responsável'),
('Cibersegurança', 'Categoria sobre Cibersegurança'),
('Privacidade & Ética Digital', 'Categoria sobre Privacidade e Ética Digital');

-- ============================================
-- Inserir Administrador Padrão
-- Login: admin
-- Senha: admin123 (hash BCrypt)
-- IMPORTANTE: Altere a senha após o primeiro acesso!
-- ============================================
-- Hash BCrypt para senha "admin123": $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
INSERT IGNORE INTO usuario (nome, idade, login, senha, tipo, status, interesses) VALUES
('Administrador do Sistema', 30, 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Administrador', TRUE, 'IA Responsável, Cibersegurança');

-- ============================================
-- Verificações
-- ============================================
SELECT 'Banco de dados criado com sucesso!' AS Status;
SELECT COUNT(*) AS TotalCategorias FROM categoria;
SELECT COUNT(*) AS TotalUsuarios FROM usuario;
SELECT COUNT(*) AS TotalRecursos FROM recurso;

