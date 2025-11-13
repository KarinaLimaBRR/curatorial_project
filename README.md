# Sistema de Curadoria - DB

Projeto Java Swing para gerenciamento de curadoria com conexão ao banco de dados MySQL.

## 📋 Pré-requisitos

Antes de abrir o projeto, certifique-se de ter instalado:

- **Java 17** ou superior
- **Maven** 3.6+ 
- **MySQL** 8.0+ (com o banco de dados `DB_CuradoriaIA` criado)
- **IDE** (IntelliJ IDEA, Eclipse, VS Code, etc.)

## 🚀 Como Abrir o Projeto

### Opção 1: Usando uma IDE (Recomendado)

#### IntelliJ IDEA
1. Abra o IntelliJ IDEA
2. File → Open → Selecione a pasta `curadoria-db`
3. Aguarde o Maven baixar as dependências automaticamente
4. Configure o JDK 17: File → Project Structure → Project → SDK: 17

#### Eclipse
1. Abra o Eclipse
2. File → Import → Maven → Existing Maven Projects
3. Selecione a pasta `curadoria-db`
4. Clique em Finish

#### VS Code
1. Abra o VS Code
2. File → Open Folder → Selecione a pasta `curadoria-db`
3. Instale a extensão "Extension Pack for Java" se necessário
4. O VS Code detectará automaticamente o projeto Maven

### Opção 2: Via Terminal

```bash
# Navegue até a pasta do projeto
cd /Users/karinalima/Downloads/curadoria-db

# Compile o projeto
mvn clean compile

# Execute o projeto
mvn exec:java -Dexec.mainClass="com.curadoria.view.TelaLogin"
```

### Opção 3: Executar o JAR já compilado

```bash
# Navegue até a pasta do projeto
cd /Users/karinalima/Downloads/curadoria-db

# Execute o JAR
java -cp target/curadoria-db-1.0.0.jar:target/classes com.curadoria.view.TelaLogin
```

## ⚙️ Configuração do Banco de Dados

Antes de executar, configure a conexão com o MySQL no arquivo:
`src/main/java/com/curadoria/db/ConnectionFactory.java`

As configurações padrão são:
- **URL**: `jdbc:mysql://localhost:3306/DB_CuradoriaIA`
- **Usuário**: `root`
- **Senha**: `123456789`

**⚠️ Importante**: Ajuste essas credenciais conforme seu ambiente MySQL.

### Criar o Banco de Dados

**Opção 1: Usar o script SQL completo (Recomendado)**

Execute o script `database/schema.sql` que cria todas as tabelas, insere as categorias padrão e um administrador inicial:

```bash
mysql -u root -p < database/schema.sql
```

Ou no MySQL Workbench/CLI:
```sql
SOURCE database/schema.sql;
```

**Opção 2: Criar manualmente**

```sql
CREATE DATABASE IF NOT EXISTS DB_CuradoriaIA;
USE DB_CuradoriaIA;
```

**Nota**: O script SQL completo está em `database/schema.sql` e inclui:
- Criação de todas as tabelas (usuario, recurso, categoria)
- Inserção das 3 categorias padrão
- Inserção de um administrador padrão (login: `admin`, senha: `admin123`)

## 🧪 Testar Conexão

Para testar se a conexão com o banco está funcionando:

```bash
mvn exec:java -Dexec.mainClass="com.curadoria.db.ConnectionFactory"
```

## 📦 Estrutura do Projeto

```
curadoria-db/
├── src/main/java/com/curadoria/
│   ├── dao/          # Data Access Objects
│   ├── db/           # Conexão com banco de dados
│   ├── model/        # Modelos de dados
│   └── view/        # Interfaces gráficas (Swing)
├── target/           # Arquivos compilados
└── pom.xml           # Configuração Maven
```

## 🎯 Executar a Aplicação

A classe principal é `TelaLogin.java`. Ao executar, a tela de login será aberta.

**Classe principal**: `com.curadoria.view.TelaLogin`

## 🔧 Comandos Úteis

```bash
# Compilar o projeto
mvn clean compile

# Compilar e gerar JAR
mvn clean package

# Limpar arquivos compilados
mvn clean

# Executar testes (se houver)
mvn test
```

## 📝 Dependências

- MySQL Connector/J 8.4.0
- SLF4J Simple 2.0.13
- BCrypt 0.4

Todas as dependências são gerenciadas automaticamente pelo Maven.

## 📊 Artefatos de Modelagem

O projeto inclui todos os artefatos de modelagem solicitados pela UC Modelagem de Software:

### Diagramas UML
- ✅ Diagrama de Casos de Uso (`modelagem/casos-de-uso.puml`)
- ✅ Diagrama de Classes (`modelagem/diagrama-classes.puml`)
- ✅ Diagrama de Sequência (`modelagem/diagrama-sequencia.puml`)
- ✅ Diagrama de Atividades (`modelagem/diagrama-atividades.puml`)

### Modelagem de Banco de Dados
- ✅ DER - Diagrama Entidade-Relacionamento (`modelagem/der.puml`)
- ✅ Modelo Relacional (`modelagem/modelo-relacional.md`)
- ✅ Modelo Físico (`modelagem/modelo-fisico.md`)

### Documentação
- ✅ Especificação detalhada de Casos de Uso (`modelagem/especificacao-casos-de-uso.md`)
- ✅ Documentação do DER (`modelagem/DER.md`)
- ✅ Resumo da Modelagem (`modelagem/RESUMO_MODELAGEM.md`)

**Para visualizar os diagramas**: Veja o arquivo `modelagem/README.md` para instruções detalhadas.

## 📚 Documentação para Apresentação

- **Documentação Completa para Banca**: `DOCUMENTACAO_BANCA.md`
- **Guia Rápido de Apresentação**: `GUIA_APRESENTACAO.md`
- **Perguntas e Respostas Detalhadas**: `PERGUNTAS_RESPOSTAS_DETALHADAS.md`
