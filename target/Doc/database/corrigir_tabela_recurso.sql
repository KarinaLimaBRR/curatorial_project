-- ============================================
-- Script de Correção da Tabela RECURSO
-- Adiciona colunas faltantes: autor, idUsuario, dataCadastro
-- ============================================

USE DB_CuradoriaIA;

-- Verificar e adicionar coluna 'autor' se não existir
SET @col_exists = (SELECT COUNT(*) FROM information_schema.COLUMNS 
    WHERE TABLE_SCHEMA = 'DB_CuradoriaIA' 
    AND TABLE_NAME = 'recurso' 
    AND COLUMN_NAME = 'autor');

SET @sql = IF(@col_exists = 0,
    'ALTER TABLE recurso ADD COLUMN autor VARCHAR(100) NOT NULL DEFAULT "" AFTER titulo',
    'SELECT "Coluna autor já existe" AS mensagem');

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Verificar e adicionar coluna 'idUsuario' se não existir
SET @col_exists = (SELECT COUNT(*) FROM information_schema.COLUMNS 
    WHERE TABLE_SCHEMA = 'DB_CuradoriaIA' 
    AND TABLE_NAME = 'recurso' 
    AND COLUMN_NAME = 'idUsuario');

SET @sql = IF(@col_exists = 0,
    'ALTER TABLE recurso ADD COLUMN idUsuario INT NOT NULL DEFAULT 1 AFTER idCategoria',
    'SELECT "Coluna idUsuario já existe" AS mensagem');

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Verificar e adicionar coluna 'dataCadastro' se não existir
SET @col_exists = (SELECT COUNT(*) FROM information_schema.COLUMNS 
    WHERE TABLE_SCHEMA = 'DB_CuradoriaIA' 
    AND TABLE_NAME = 'recurso' 
    AND COLUMN_NAME = 'dataCadastro');

SET @sql = IF(@col_exists = 0,
    'ALTER TABLE recurso ADD COLUMN dataCadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP AFTER idUsuario',
    'SELECT "Coluna dataCadastro já existe" AS mensagem');

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Adicionar foreign key para idUsuario se não existir
SET @fk_exists = (SELECT COUNT(*) FROM information_schema.KEY_COLUMN_USAGE 
    WHERE TABLE_SCHEMA = 'DB_CuradoriaIA' 
    AND TABLE_NAME = 'recurso' 
    AND COLUMN_NAME = 'idUsuario' 
    AND REFERENCED_TABLE_NAME IS NOT NULL);

SET @sql = IF(@fk_exists = 0,
    'ALTER TABLE recurso ADD CONSTRAINT fk_recurso_usuario FOREIGN KEY (idUsuario) REFERENCES usuario(idUsuario) ON DELETE CASCADE',
    'SELECT "Foreign key idUsuario já existe" AS mensagem');

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Verificar estrutura final
DESCRIBE recurso;

SELECT 'Tabela recurso corrigida com sucesso!' AS Status;

