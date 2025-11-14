# Especificação dos Casos de Uso

## 📋 Descrição Geral

Este documento especifica detalhadamente todos os casos de uso do Sistema de Curadoria e Compartilhamento de Recursos sobre IA.

---

## UC01: Realizar Login

### Descrição
Permite que um usuário autenticado acesse o sistema através de login e senha.

### Ator Principal
- Usuário Administrador
- Usuário Comum

### Pré-condições
- O usuário deve estar cadastrado no sistema
- A conta do usuário deve estar ativa (status = TRUE)

### Fluxo Principal
1. O sistema exibe a tela de login
2. O usuário informa seu login
3. O usuário informa sua senha
4. O usuário clica em "Entrar"
5. O sistema valida as credenciais
6. O sistema verifica se a conta está ativa
7. O sistema redireciona o usuário conforme seu tipo:
   - Se Administrador → TelaAdmin
   - Se Usuário Comum → TelaComum

### Fluxos Alternativos

#### 7a. Credenciais inválidas
7a.1. O sistema exibe mensagem: "Login ou senha incorretos / conta inativa"
7a.2. O sistema mantém a tela de login aberta

#### 7b. Conta inativa
7b.1. O sistema exibe mensagem: "Login ou senha incorretos / conta inativa"
7b.2. O sistema mantém a tela de login aberta

### Pós-condições
- Usuário autenticado e logado no sistema
- Tela apropriada exibida conforme tipo de usuário

### Regras de Negócio
- Senha é verificada usando BCrypt
- Apenas usuários com status = TRUE podem fazer login

---

## UC02: Cadastrar Usuário

### Descrição
Permite que um administrador cadastre um novo usuário no sistema.

### Ator Principal
- Usuário Administrador

### Pré-condições
- O administrador deve estar autenticado (UC01)
- O administrador deve estar na TelaAdmin

### Fluxo Principal
1. O administrador clica em "Novo"
2. O sistema exibe a tela de cadastro de usuário
3. O administrador preenche:
   - Nome (obrigatório)
   - Idade (obrigatório, > 0)
   - Login (obrigatório, único)
   - Senha (obrigatório)
   - Confirmar Senha (obrigatório)
   - Tipo: Administrador ou Usuário comum
   - Interesses: seleciona até 2 categorias
4. O administrador clica em "Cadastrar"
5. O sistema valida os dados
6. O sistema verifica se o login já existe
7. O sistema verifica se as senhas coincidem
8. O sistema verifica se não há mais de 2 interesses selecionados
9. O sistema criptografa a senha com BCrypt
10. O sistema salva o usuário no banco de dados
11. O sistema exibe mensagem de sucesso
12. O sistema atualiza a lista de usuários

### Fluxos Alternativos

#### 5a. Dados inválidos
5a.1. O sistema exibe mensagem de erro específica
5a.2. O sistema mantém a tela de cadastro aberta

#### 6a. Login já existe
6a.1. O sistema exibe mensagem: "Login já cadastrado"
6a.2. O sistema mantém a tela de cadastro aberta

#### 7a. Senhas não coincidem
7a.1. O sistema exibe mensagem: "Senhas não coincidem"
7a.2. O sistema mantém a tela de cadastro aberta

#### 8a. Mais de 2 interesses selecionados
8a.1. O sistema desmarca automaticamente o último interesse selecionado
8a.2. O sistema exibe mensagem: "Máximo de 2 interesses permitidos"

### Pós-condições
- Novo usuário cadastrado no sistema
- Lista de usuários atualizada

### Regras de Negócio
- Login deve ser único
- Idade deve ser positiva
- Máximo de 2 interesses por usuário
- Senha é armazenada como hash BCrypt
- Status padrão: TRUE (ativo)

---

## UC03: Editar Usuário

### Descrição
Permite que um administrador edite os dados de um usuário existente.

### Ator Principal
- Usuário Administrador

### Pré-condições
- O administrador deve estar autenticado (UC01)
- O administrador deve estar na TelaAdmin
- Deve haver pelo menos um usuário cadastrado

### Fluxo Principal
1. O administrador seleciona um usuário na tabela
2. O administrador clica em "Editar"
3. O sistema exibe a tela de edição com os dados do usuário
4. O administrador modifica os campos desejados:
   - Nome
   - Idade
   - Senha (opcional - se vazio, mantém a atual)
   - Tipo
   - Interesses
5. O administrador clica em "Salvar"
6. O sistema valida os dados
7. O sistema atualiza o usuário no banco de dados
8. O sistema exibe mensagem de sucesso
9. O sistema atualiza a lista de usuários

### Fluxos Alternativos

#### 1a. Nenhum usuário selecionado
1a.1. O sistema exibe mensagem: "Selecione um usuário para editar"
1a.2. O caso de uso termina

#### 6a. Dados inválidos
6a.1. O sistema exibe mensagem de erro específica
6a.2. O sistema mantém a tela de edição aberta

### Pós-condições
- Dados do usuário atualizados
- Lista de usuários atualizada

### Regras de Negócio
- Login não pode ser alterado
- Se senha estiver vazia, mantém a senha atual (hash)
- Se senha for informada, criptografa com BCrypt
- Máximo de 2 interesses

---

## UC04: Inativar Usuário

### Descrição
Permite que um administrador inative a conta de um usuário.

### Ator Principal
- Usuário Administrador

### Pré-condições
- O administrador deve estar autenticado (UC01)
- O administrador deve estar na TelaAdmin
- Deve haver pelo menos um usuário cadastrado

### Fluxo Principal
1. O administrador seleciona um usuário na tabela
2. O administrador clica em "Inativar"
3. O sistema exibe diálogo de confirmação
4. O administrador confirma a ação
5. O sistema atualiza o status do usuário para FALSE
6. O sistema atualiza a lista de usuários

### Fluxos Alternativos

#### 1a. Nenhum usuário selecionado
1a.1. O sistema exibe mensagem: "Selecione um usuário para inativar"
1a.2. O caso de uso termina

#### 4a. Administrador cancela
4a.1. O sistema fecha o diálogo
4a.2. O caso de uso termina

### Pós-condições
- Status do usuário alterado para inativo
- Usuário não pode mais fazer login
- Lista de usuários atualizada

### Regras de Negócio
- Usuário inativo não pode fazer login
- Recursos do usuário permanecem no sistema
- Não é possível excluir usuário, apenas inativar

---

## UC05: Listar Usuários

### Descrição
Permite que um administrador visualize todos os usuários cadastrados.

### Ator Principal
- Usuário Administrador

### Pré-condições
- O administrador deve estar autenticado (UC01)
- O administrador deve estar na TelaAdmin

### Fluxo Principal
1. O sistema carrega todos os usuários do banco de dados
2. O sistema ordena os usuários por nome
3. O sistema exibe os usuários em uma tabela com colunas:
   - ID
   - Nome
   - Login
   - Tipo
   - Status (Ativo/Inativo)

### Pós-condições
- Lista de usuários exibida ordenada por nome

### Regras de Negócio
- Ordenação alfabética por nome
- Exibe todos os usuários (ativos e inativos)

---

## UC06: Cadastrar Recurso

### Descrição
Permite que um usuário comum cadastre um recurso educacional que consumiu.

### Ator Principal
- Usuário Comum

### Pré-condições
- O usuário deve estar autenticado (UC01)
- O usuário deve estar na TelaComum

### Fluxo Principal
1. O usuário acessa a aba "Cadastrar Recurso"
2. O usuário preenche:
   - Título (obrigatório)
   - Autor (obrigatório)
   - Categoria: seleciona uma das três opções
3. O usuário clica em "Cadastrar"
4. O sistema valida os campos obrigatórios
5. O sistema associa o recurso ao usuário logado
6. O sistema salva o recurso no banco de dados
7. O sistema exibe mensagem de sucesso
8. O sistema limpa os campos do formulário
9. O sistema atualiza a lista de recursos

### Fluxos Alternativos

#### 4a. Campos obrigatórios vazios
4a.1. O sistema exibe mensagem: "Título, Autor e Categoria são obrigatórios!"
4a.2. O sistema mantém a tela de cadastro aberta

### Pós-condições
- Novo recurso cadastrado e associado ao usuário
- Lista de recursos atualizada

### Regras de Negócio
- Título e Autor são obrigatórios
- Categoria deve ser uma das três fixas
- Recurso é automaticamente associado ao usuário logado
- Data de cadastro é registrada automaticamente

---

## UC07: Visualizar Recursos (Ordenados Alfabeticamente)

### Descrição
Permite que um usuário comum visualize todos os recursos cadastrados, ordenados alfabeticamente por título.

### Ator Principal
- Usuário Comum

### Pré-condições
- O usuário deve estar autenticado (UC01)
- O usuário deve estar na TelaComum

### Fluxo Principal
1. O usuário acessa a aba "Listagem de Recursos"
2. O sistema busca todos os recursos no banco de dados
3. O sistema ordena os recursos alfabeticamente por título (ASC)
4. O sistema exibe os recursos em uma tabela com colunas:
   - Título
   - Autor
   - Categoria

### Pós-condições
- Lista de recursos exibida ordenada alfabeticamente

### Regras de Negócio
- Ordenação obrigatória: alfabética por título (A-Z)
- Exibe recursos de todos os usuários
- Ordenação case-insensitive

---

## UC08: Gerenciar Categorias

### Descrição
Sistema gerencia automaticamente as categorias fixas do sistema.

### Ator Principal
- Sistema (automático)

### Pré-condições
- Banco de dados inicializado

### Fluxo Principal
1. O sistema verifica se as categorias existem
2. Se não existirem, o sistema cria as três categorias padrão:
   - IA Responsável
   - Cibersegurança
   - Privacidade & Ética Digital

### Pós-condições
- Três categorias fixas disponíveis no sistema

### Regras de Negócio
- Categorias são fixas e não podem ser alteradas pelo usuário
- Categorias são usadas tanto para interesses quanto para recursos
- Categorias são criadas automaticamente na inicialização

---

## 📊 Matriz de Rastreabilidade

| Caso de Uso | Requisito Funcional | Prioridade |
|-------------|---------------------|------------|
| UC01 | Autenticação de usuários | Alta |
| UC02 | Admin: Cadastro de usuários | Alta |
| UC03 | Admin: Edição de usuários | Alta |
| UC04 | Admin: Inativação de usuários | Alta |
| UC05 | Admin: Listagem de usuários | Média |
| UC06 | Usuário: Cadastro de recursos | Alta |
| UC07 | Usuário: Visualização ordenada | Alta |
| UC08 | Sistema: Gerenciamento de categorias | Média |

---

## 🔗 Relacionamentos entre Casos de Uso

- **UC01** é pré-requisito para todos os outros casos de uso
- **UC02** e **UC06** estendem **UC08** (usam categorias)
- **UC03** e **UC04** dependem de **UC05** (listagem de usuários)

