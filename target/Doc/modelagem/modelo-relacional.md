# Modelo Relacional - Sistema de Curadoria

## 📊 Descrição

O Modelo Relacional representa a estrutura lógica do banco de dados em formato tabular, mostrando as relações entre as tabelas.

## 📋 Esquema Relacional

### Tabela: USUARIO

```
USUARIO (
    idUsuario INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    idade INT NOT NULL CHECK (idade > 0),
    login VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    tipo VARCHAR(50) NOT NULL CHECK (tipo IN ('Administrador', 'Usuário comum', 'Admin', 'Comum')),
    status BOOLEAN DEFAULT TRUE,
    interesses VARCHAR(255)
)
```

**Chaves:**
- **Chave Primária (PK)**: `idUsuario`
- **Chave Única (UK)**: `login`

**Restrições:**
- `idade > 0`
- `tipo` deve ser um dos valores permitidos
- `login` deve ser único

**Índices:**
- Índice primário em `idUsuario`
- Índice único em `login`

---

### Tabela: CATEGORIA

```
CATEGORIA (
    idCategoria INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL UNIQUE,
    descricao VARCHAR(255)
)
```

**Chaves:**
- **Chave Primária (PK)**: `idCategoria`
- **Chave Única (UK)**: `nome`

**Dados Iniciais:**
1. (1, 'IA Responsável', 'Categoria sobre IA Responsável')
2. (2, 'Cibersegurança', 'Categoria sobre Cibersegurança')
3. (3, 'Privacidade & Ética Digital', 'Categoria sobre Privacidade e Ética Digital')

---

### Tabela: RECURSO

```
RECURSO (
    idRecurso INT PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(255) NOT NULL,
    autor VARCHAR(100) NOT NULL,
    idCategoria INT NOT NULL,
    idUsuario INT NOT NULL,
    dataCadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idCategoria) REFERENCES CATEGORIA(idCategoria) ON DELETE RESTRICT,
    FOREIGN KEY (idUsuario) REFERENCES USUARIO(idUsuario) ON DELETE CASCADE
)
```

**Chaves:**
- **Chave Primária (PK)**: `idRecurso`
- **Chave Estrangeira (FK)**: `idCategoria` → `CATEGORIA(idCategoria)`
- **Chave Estrangeira (FK)**: `idUsuario` → `USUARIO(idUsuario)`

**Índices:**
- Índice primário em `idRecurso`
- Índice em `titulo` (para ordenação)
- Índice em `idCategoria` (para JOIN)
- Índice em `idUsuario` (para JOIN)

**Restrições de Integridade:**
- `idCategoria` deve existir em `CATEGORIA`
- `idUsuario` deve existir em `USUARIO`
- Não é possível excluir categoria que possui recursos (RESTRICT)
- Ao excluir usuário, seus recursos são excluídos automaticamente (CASCADE)

---

## 🔗 Relacionamentos

### 1. USUARIO → RECURSO (1:N)

**Tipo**: Um-para-Muitos

**Cardinalidade:**
- Um usuário pode cadastrar **zero ou mais** recursos
- Um recurso pertence a **exatamente um** usuário

**Representação:**
```
USUARIO (1) ────────< (N) RECURSO
```

**Chave Estrangeira:**
- `RECURSO.idUsuario` referencia `USUARIO.idUsuario`

---

### 2. CATEGORIA → RECURSO (1:N)

**Tipo**: Um-para-Muitos

**Cardinalidade:**
- Uma categoria pode classificar **zero ou mais** recursos
- Um recurso pertence a **exatamente uma** categoria

**Representação:**
```
CATEGORIA (1) ────────< (N) RECURSO
```

**Chave Estrangeira:**
- `RECURSO.idCategoria` referencia `CATEGORIA.idCategoria`

---

## 📊 Diagrama de Relacionamentos

```
┌─────────────────┐
│    USUARIO      │
├─────────────────┤
│ PK idUsuario    │
│    nome         │
│    idade        │
│    login (UK)   │
│    senha        │
│    tipo         │
│    status       │
│    interesses   │
└────────┬────────┘
         │
         │ 1
         │
         │ N
         │
┌────────▼────────┐
│    RECURSO      │
├─────────────────┤
│ PK idRecurso    │
│    titulo       │
│    autor        │
│ FK idCategoria  │──┐
│ FK idUsuario    │  │
│    dataCadastro │  │
└─────────────────┘  │
                     │
                     │ N
                     │
         ┌───────────┘
         │
         │ 1
         │
┌────────▼────────┐
│   CATEGORIA     │
├─────────────────┤
│ PK idCategoria  │
│    nome (UK)    │
│    descricao    │
└─────────────────┘
```

---

## 🔍 Consultas Típicas

### 1. Listar todos os recursos ordenados por título
```sql
SELECT r.*, c.nome AS categoria_nome, u.nome AS usuario_nome
FROM RECURSO r
LEFT JOIN CATEGORIA c ON r.idCategoria = c.idCategoria
LEFT JOIN USUARIO u ON r.idUsuario = u.idUsuario
ORDER BY r.titulo ASC;
```

### 2. Listar usuários com seus recursos
```sql
SELECT u.*, COUNT(r.idRecurso) AS total_recursos
FROM USUARIO u
LEFT JOIN RECURSO r ON u.idUsuario = r.idUsuario
GROUP BY u.idUsuario;
```

### 3. Listar recursos por categoria
```sql
SELECT c.nome AS categoria, COUNT(r.idRecurso) AS total
FROM CATEGORIA c
LEFT JOIN RECURSO r ON c.idCategoria = r.idCategoria
GROUP BY c.idCategoria, c.nome;
```

---

## ✅ Normalização

O modelo está em **3NF (Terceira Forma Normal)**:

1. ✅ **1NF**: Todos os atributos são atômicos
2. ✅ **2NF**: Não há dependências parciais (todas as chaves primárias são simples)
3. ✅ **3NF**: Não há dependências transitivas

**Observação**: Os interesses do usuário poderiam ser normalizados criando uma tabela `USUARIO_INTERESSE` para relacionamento N:N, mas a solução atual (string concatenada) atende aos requisitos funcionais.

---

## 🔐 Integridade Referencial

### Regras de Integridade

1. **Integridade de Entidade**: Todas as chaves primárias são NOT NULL e únicas
2. **Integridade Referencial**: Todas as chaves estrangeiras referenciam chaves primárias válidas
3. **Integridade de Domínio**: Valores respeitam os tipos e constraints definidos

### Ações de Integridade

- **ON DELETE RESTRICT** (Categoria → Recurso): Impede exclusão de categoria com recursos
- **ON DELETE CASCADE** (Usuario → Recurso): Exclui recursos ao excluir usuário

