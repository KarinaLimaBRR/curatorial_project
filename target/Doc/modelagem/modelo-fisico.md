# Modelo Físico do Banco de Dados

## 📊 Descrição

O Modelo Físico representa a implementação concreta do banco de dados no SGBD MySQL, incluindo tipos de dados, índices, constraints e otimizações.

## 🗄️ Especificações Técnicas

### Engine
- **MySQL**: InnoDB (suporte a transações e chaves estrangeiras)
- **Charset**: utf8mb4
- **Collation**: utf8mb4_unicode_ci (suporte completo a caracteres especiais)

---

## 📋 Estrutura Física das Tabelas

### Tabela: `usuario`

```sql
CREATE TABLE usuario (
    idUsuario INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    idade INT NOT NULL,
    login VARCHAR(50) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    status BOOLEAN DEFAULT TRUE,
    interesses VARCHAR(255),
    
    CONSTRAINT chk_idade_positiva CHECK (idade > 0),
    CONSTRAINT chk_tipo_valido CHECK (tipo IN ('Administrador', 'Usuário comum', 'Admin', 'Comum')),
    CONSTRAINT uk_usuario_login UNIQUE (login)
) ENGINE=InnoDB 
  DEFAULT CHARSET=utf8mb4 
  COLLATE=utf8mb4_unicode_ci;
```

**Índices:**
- **PRIMARY KEY**: `idUsuario` (clustered index)
- **UNIQUE INDEX**: `uk_usuario_login` em `login`

**Tamanho Estimado:**
- Registro médio: ~500 bytes
- 1000 usuários: ~500 KB

**Otimizações:**
- Índice único em `login` para busca rápida no login
- Campo `status` para filtros de usuários ativos

---

### Tabela: `categoria`

```sql
CREATE TABLE categoria (
    idCategoria INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),
    
    CONSTRAINT uk_categoria_nome UNIQUE (nome)
) ENGINE=InnoDB 
  DEFAULT CHARSET=utf8mb4 
  COLLATE=utf8mb4_unicode_ci;
```

**Índices:**
- **PRIMARY KEY**: `idCategoria` (clustered index)
- **UNIQUE INDEX**: `uk_categoria_nome` em `nome`

**Tamanho Estimado:**
- Registro médio: ~200 bytes
- 3 categorias: ~600 bytes (tabela pequena)

**Otimizações:**
- Tabela pequena, sempre em memória
- Índice único garante integridade

---

### Tabela: `recurso`

```sql
CREATE TABLE recurso (
    idRecurso INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    autor VARCHAR(100) NOT NULL,
    idCategoria INT NOT NULL,
    idUsuario INT NOT NULL,
    dataCadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (idCategoria) REFERENCES categoria(idCategoria) 
        ON DELETE RESTRICT 
        ON UPDATE CASCADE,
    FOREIGN KEY (idUsuario) REFERENCES usuario(idUsuario) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE,
    
    INDEX idx_titulo (titulo),
    INDEX idx_categoria (idCategoria),
    INDEX idx_usuario (idUsuario),
    INDEX idx_dataCadastro (dataCadastro)
) ENGINE=InnoDB 
  DEFAULT CHARSET=utf8mb4 
  COLLATE=utf8mb4_unicode_ci;
```

**Índices:**
- **PRIMARY KEY**: `idRecurso` (clustered index)
- **INDEX**: `idx_titulo` em `titulo` (para ordenação alfabética)
- **INDEX**: `idx_categoria` em `idCategoria` (para JOINs)
- **INDEX**: `idx_usuario` em `idUsuario` (para JOINs e filtros)
- **INDEX**: `idx_dataCadastro` em `dataCadastro` (para ordenação temporal)

**Tamanho Estimado:**
- Registro médio: ~600 bytes
- 10.000 recursos: ~6 MB

**Otimizações:**
- Múltiplos índices para diferentes tipos de consulta
- Índice em `titulo` otimiza ORDER BY alfabético
- Foreign keys com índices automáticos

---

## 🔗 Chaves Estrangeiras

### 1. `recurso.idCategoria` → `categoria.idCategoria`

```sql
FOREIGN KEY (idCategoria) 
REFERENCES categoria(idCategoria) 
ON DELETE RESTRICT 
ON UPDATE CASCADE
```

**Comportamento:**
- **ON DELETE RESTRICT**: Impede exclusão de categoria com recursos
- **ON UPDATE CASCADE**: Atualiza automaticamente se ID da categoria mudar

### 2. `recurso.idUsuario` → `usuario.idUsuario`

```sql
FOREIGN KEY (idUsuario) 
REFERENCES usuario(idUsuario) 
ON DELETE CASCADE 
ON UPDATE CASCADE
```

**Comportamento:**
- **ON DELETE CASCADE**: Exclui recursos ao excluir usuário
- **ON UPDATE CASCADE**: Atualiza automaticamente se ID do usuário mudar

---

## 📊 Plano de Execução de Consultas

### Consulta: Listar recursos ordenados por título

```sql
SELECT r.*, c.nome AS categoria_nome 
FROM recurso r 
LEFT JOIN categoria c ON r.idCategoria = c.idCategoria 
ORDER BY r.titulo ASC;
```

**Plano de Execução:**
1. Escanear índice `idx_titulo` (ordenação pré-estabelecida)
2. JOIN com `categoria` usando índice primário
3. Retornar resultados já ordenados

**Custo Estimado:** O(n log n) onde n = número de recursos

---

## 🔧 Otimizações Implementadas

### 1. Índices Estratégicos
- Índice em `titulo` para ordenação alfabética (requisito)
- Índices em foreign keys para JOINs eficientes
- Índice único em `login` para autenticação rápida

### 2. Tipos de Dados Otimizados
- `VARCHAR` com tamanhos apropriados (não desperdiça espaço)
- `TIMESTAMP` com DEFAULT para data de cadastro automática
- `BOOLEAN` para status (mais eficiente que INT)

### 3. Constraints de Integridade
- CHECK constraints para validação de dados
- UNIQUE constraints para evitar duplicatas
- FOREIGN KEY constraints para integridade referencial

### 4. Engine InnoDB
- Suporte a transações ACID
- Row-level locking (melhor concorrência)
- Suporte completo a foreign keys

---

## 📈 Estimativas de Performance

### Tabela `usuario`
- **Busca por login**: O(log n) com índice único
- **Listagem completa**: O(n) scan completo
- **Inserção**: O(1) com auto-increment

### Tabela `recurso`
- **Listagem ordenada por título**: O(n log n) com índice
- **Busca por categoria**: O(log n) com índice em `idCategoria`
- **Busca por usuário**: O(log n) com índice em `idUsuario`
- **Inserção**: O(1) com auto-increment

### JOINs
- **recurso ↔ categoria**: O(n) com índice em foreign key
- **recurso ↔ usuario**: O(n) com índice em foreign key

---

## 🔐 Segurança e Integridade

### 1. Senhas
- Armazenadas como hash BCrypt (60 caracteres)
- Campo `senha` dimensionado para VARCHAR(255) (compatibilidade futura)

### 2. Validações
- CHECK constraint em `idade > 0`
- CHECK constraint em `tipo` (valores permitidos)
- UNIQUE constraint em `login` e `nome` (categoria)

### 3. Integridade Referencial
- Foreign keys garantem consistência
- RESTRICT previne exclusões acidentais
- CASCADE mantém consistência em cascata

---

## 📝 Script de Criação Completo

O script completo está disponível em `database/schema.sql` e inclui:
- Criação de todas as tabelas
- Definição de índices
- Constraints de integridade
- Dados iniciais (categorias e administrador padrão)

---

## 🔄 Manutenção

### Backup Recomendado
```sql
mysqldump -u root -p DB_CuradoriaIA > backup.sql
```

### Análise de Performance
```sql
ANALYZE TABLE usuario, categoria, recurso;
SHOW TABLE STATUS WHERE Name IN ('usuario', 'categoria', 'recurso');
```

### Otimização
```sql
OPTIMIZE TABLE usuario, categoria, recurso;
```

---

## 📊 Estatísticas Esperadas

| Tabela | Registros Esperados | Tamanho Estimado |
|--------|---------------------|------------------|
| `usuario` | 100 - 1.000 | ~500 KB - 5 MB |
| `categoria` | 3 (fixo) | ~600 bytes |
| `recurso` | 1.000 - 10.000 | ~6 MB - 60 MB |

**Total Estimado**: ~10-70 MB para um sistema de médio porte

