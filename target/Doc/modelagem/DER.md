# DER - Diagrama Entidade-Relacionamento

## 📊 Descrição

O Diagrama Entidade-Relacionamento (DER) representa a estrutura conceitual do banco de dados do Sistema de Curadoria.

## 🗂️ Entidades

### 1. Usuario
Representa os usuários do sistema (Administradores e Usuários Comuns).

**Atributos:**
- `idUsuario` (PK): Identificador único do usuário
- `nome`: Nome completo do usuário
- `idade`: Idade do usuário (deve ser > 0)
- `login`: Login único do usuário
- `senha`: Hash da senha (BCrypt)
- `tipo`: Tipo do usuário ("Administrador" ou "Usuário comum")
- `status`: Status da conta (TRUE = ativo, FALSE = inativo)
- `interesses`: String com até 2 interesses separados por vírgula

**Regras de Negócio:**
- Login deve ser único
- Idade deve ser positiva
- Interesses limitados a 2 categorias
- Pelo menos um administrador deve existir no sistema

### 2. Categoria
Representa as categorias fixas dos recursos.

**Atributos:**
- `idCategoria` (PK): Identificador único da categoria
- `nome`: Nome da categoria (único)
- `descricao`: Descrição da categoria

**Categorias Fixas:**
1. IA Responsável
2. Cibersegurança
3. Privacidade & Ética Digital

### 3. Recurso
Representa os recursos educacionais cadastrados pelos usuários.

**Atributos:**
- `idRecurso` (PK): Identificador único do recurso
- `titulo`: Título do recurso
- `autor`: Autor do recurso
- `idCategoria` (FK): Referência à categoria
- `idUsuario` (FK): Referência ao usuário que cadastrou
- `dataCadastro`: Data e hora do cadastro

**Regras de Negócio:**
- Ordenação alfabética por título
- Um usuário pode cadastrar vários recursos
- Um recurso pertence a uma categoria

## 🔗 Relacionamentos

### Usuario → Recurso (1:N)
- **Cardinalidade**: Um usuário pode cadastrar vários recursos
- **Tipo**: Relacionamento obrigatório
- **Ação**: CASCADE DELETE (se usuário for excluído, seus recursos também são)

### Categoria → Recurso (1:N)
- **Cardinalidade**: Uma categoria pode classificar vários recursos
- **Tipo**: Relacionamento obrigatório
- **Ação**: RESTRICT DELETE (não permite excluir categoria com recursos)

## 📋 Observações

1. **Normalização**: O sistema está em 3NF (Terceira Forma Normal)
2. **Interesses**: Atualmente armazenados como string concatenada. Ideal seria criar tabela `usuario_interesse` para relacionamento N:N
3. **Segurança**: Senhas são armazenadas como hash BCrypt
4. **Integridade**: Chaves estrangeiras garantem integridade referencial

## 🎨 Visualização

O diagrama visual está disponível em `der.puml` e pode ser renderizado com PlantUML.

