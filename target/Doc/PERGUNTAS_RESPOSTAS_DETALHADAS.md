# Perguntas e Respostas Detalhadas para Banca

Este documento contém respostas detalhadas para perguntas que podem ser feitas durante a apresentação.

---

## 🔐 SEGURANÇA

### P1: Como vocês garantem a segurança das senhas?

**Resposta Detalhada**:
Utilizamos o algoritmo **BCrypt** para criptografar senhas. BCrypt é superior a algoritmos como MD5 ou SHA porque:

1. **Algoritmo Lento**: BCrypt é intencionalmente lento, tornando ataques de força bruta impraticáveis
2. **Salt Automático**: Cada hash inclui um salt único, então mesmo senhas idênticas geram hashes diferentes
3. **Ajustável**: O fator de custo pode ser aumentado conforme hardware evolui
4. **Padrão da Indústria**: Amplamente aceito e utilizado em sistemas de produção

**Implementação**:
- No cadastro: `BCrypt.hashpw(senha, BCrypt.gensalt())`
- No login: `BCrypt.checkpw(senhaDigitada, hashArmazenado)`
- Senhas nunca são armazenadas em texto plano
- Impossível recuperar senha original do hash

**Exemplo de Hash BCrypt**:
```
$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
```

---

### P2: Por que não implementaram recuperação de senha?

**Resposta**:
Recuperação de senha não estava nos requisitos do projeto. É uma **melhoria futura** importante que incluiria:

1. Sistema de tokens temporários
2. Envio de email (requer servidor SMTP)
3. Interface para redefinição
4. Expiração de tokens

**Justificativa**: Focamos em implementar todos os requisitos especificados. Recuperação de senha seria uma funcionalidade adicional valiosa, mas fora do escopo inicial.

---

### P3: Como vocês previnem SQL Injection?

**Resposta**:
Utilizamos **PreparedStatements** em todas as consultas SQL, nunca concatenamos strings diretamente.

**Exemplo Correto**:
```java
String sql = "SELECT * FROM usuario WHERE login = ?";
PreparedStatement pstm = conn.prepareStatement(sql);
pstm.setString(1, login); // Parâmetro seguro
```

**Exemplo Incorreto (NÃO fazemos)**:
```java
String sql = "SELECT * FROM usuario WHERE login = '" + login + "'"; // VULNERÁVEL!
```

**Benefícios**:
- Parâmetros são escapados automaticamente
- Previne SQL Injection
- Melhor performance (queries preparadas são reutilizáveis)

---

## 🏗️ ARQUITETURA

### P4: Por que escolheram o padrão DAO?

**Resposta Detalhada**:
O padrão **DAO (Data Access Object)** foi escolhido porque:

1. **Separação de Responsabilidades**: Lógica de acesso a dados isolada da lógica de negócio
2. **Manutenibilidade**: Mudanças no banco não afetam outras camadas
3. **Testabilidade**: Fácil criar mocks para testes
4. **Reutilização**: Métodos de acesso podem ser reutilizados
5. **Troca de Banco**: Facilita migração futura (ex: MySQL → PostgreSQL)

**Estrutura**:
```
View (Tela) → Model (Usuario) → DAO (UsuarioDAO) → Database
```

**Exemplo**:
```java
// Na View
UsuarioDAO dao = new UsuarioDAO();
Usuario u = dao.buscarPorLogin(login, senha);

// No DAO
public Usuario buscarPorLogin(String login, String senha) {
    // Lógica de acesso ao banco isolada aqui
}
```

---

### P5: Por que não usaram JPA/Hibernate?

**Resposta**:
Decisão tomada considerando:

1. **Requisito do Projeto**: Especificava uso de JDBC
2. **Escopo Acadêmico**: JDBC oferece aprendizado mais profundo de SQL
3. **Controle Total**: JDBC dá controle completo sobre queries
4. **Simplicidade**: Menos abstrações, código mais direto
5. **Performance**: JDBC pode ser mais rápido em casos específicos

**Quando JPA seria melhor**:
- Projetos maiores com muitas entidades
- Necessidade de ORM complexo
- Equipe acostumada com JPA

**Melhoria Futura**: Migração para JPA seria uma evolução natural do projeto.

---

### P6: Como funciona a ConnectionFactory?

**Resposta**:
`ConnectionFactory` implementa o padrão **Factory** para centralizar criação de conexões:

**Vantagens**:
1. **Configuração Centralizada**: Credenciais em um único lugar
2. **Reutilização**: Método estático pode ser chamado de qualquer lugar
3. **Manutenção**: Mudanças de configuração em um só lugar
4. **Try-with-resources**: Garante fechamento automático

**Implementação**:
```java
public static Connection getConnection() {
    return DriverManager.getConnection(URL, USER, PASSWORD);
}
```

**Uso**:
```java
try (Connection conn = ConnectionFactory.getConnection()) {
    // Usa conexão
} // Fecha automaticamente
```

**Melhoria Futura**: Implementar pool de conexões (HikariCP) para melhor performance.

---

## 💾 BANCO DE DADOS

### P7: Por que interesses como string e não tabela separada?

**Resposta Detalhada**:

**Decisão Atual**: String concatenada (ex: "IA Responsável, Cibersegurança")

**Justificativa**:
1. **Requisito Simples**: "Até 2 interesses" - limitação simples
2. **Atende Requisitos**: Solução atual funciona perfeitamente
3. **Simplicidade**: Menos complexidade no código
4. **Over-engineering**: Normalização seria desnecessária para o escopo

**Quando Normalizar**:
- Se precisássemos consultar "todos usuários interessados em IA"
- Se interesses pudessem ser mais de 2 no futuro
- Se precisássemos estatísticas por interesse

**Melhoria Futura**: Criar tabela `usuario_interesse`:
```sql
CREATE TABLE usuario_interesse (
    idUsuario INT,
    idCategoria INT,
    PRIMARY KEY (idUsuario, idCategoria)
);
```

**Trade-off**: Complexidade vs. Necessidade real

---

### P8: Por que não excluem usuários fisicamente?

**Resposta**:
Usuários são **inativados** (status = FALSE), não excluídos.

**Razões**:
1. **Histórico**: Preserva histórico de recursos cadastrados
2. **Integridade**: Mantém integridade referencial
3. **Reativação**: Permite reativar conta no futuro
4. **Auditoria**: Rastreabilidade de ações
5. **Boas Práticas**: Padrão em sistemas de produção

**Exemplo**:
```sql
-- Inativação
UPDATE usuario SET status = FALSE WHERE idUsuario = 1;

-- Usuário inativo não pode fazer login
SELECT * FROM usuario WHERE login = ? AND status = TRUE;
```

**Alternativa**: Soft delete com campo `deletedAt` seria ainda melhor.

---

### P9: Como funciona a ordenação alfabética?

**Resposta**:
Ordenação é feita no **banco de dados** usando SQL:

```sql
SELECT * FROM recurso 
ORDER BY titulo ASC;
```

**Vantagens**:
1. **Performance**: Ordenação no banco é mais eficiente
2. **Índice**: Índice em `titulo` otimiza a consulta
3. **Case-insensitive**: MySQL ordena ignorando maiúsculas/minúsculas
4. **Consistência**: Sempre ordenado, não depende de código Java

**Índice Criado**:
```sql
CREATE INDEX idx_titulo ON recurso(titulo);
```

**Resultado**: Lista sempre ordenada alfabeticamente, conforme requisito.

---

### P10: O banco está normalizado?

**Resposta**:
Sim, o banco está em **3NF (Terceira Forma Normal)**:

**1NF (Primeira Forma Normal)**: ✅
- Todos os atributos são atômicos
- Não há grupos repetitivos

**2NF (Segunda Forma Normal)**: ✅
- Todas as chaves primárias são simples (não compostas)
- Não há dependências parciais

**3NF (Terceira Forma Normal)**: ✅
- Não há dependências transitivas
- Cada atributo não-chave depende apenas da chave primária

**Exceção**: Interesses como string (discutido anteriormente) - mas isso não quebra 3NF, apenas não está totalmente normalizado.

---

## 🎨 INTERFACE

### P11: Por que Swing e não JavaFX?

**Resposta**:

1. **Requisito**: Especificava explicitamente `javax.swing`
2. **Nativo**: Swing vem com Java, sem dependências
3. **Simplicidade**: Adequado para o escopo do projeto
4. **Maturidade**: Framework maduro e estável

**JavaFX seria melhor se**:
- Precisássemos de interface mais moderna
- Quiséssemos CSS para estilização
- Precisássemos de animações complexas

**Melhoria Futura**: Migração para JavaFX ou uso de temas modernos no Swing.

---

### P12: Como funciona a validação de interesses (máximo 2)?

**Resposta**:
Validação em **tempo real** usando `ItemListener`:

```java
ItemListener limitador = ev -> {
    int cnt = 0;
    if (chkIA.isSelected()) cnt++;
    if (chkCyber.isSelected()) cnt++;
    if (chkEtica.isSelected()) cnt++;
    if (cnt > 2) {
        JCheckBox src = (JCheckBox) ev.getItemSelectable();
        src.setSelected(false); // Desmarca automaticamente
        JOptionPane.showMessageDialog(...);
    }
};
```

**Fluxo**:
1. Usuário tenta selecionar 3º interesse
2. Listener detecta que já há 2 selecionados
3. Desmarca automaticamente o 3º
4. Exibe mensagem informativa

**Vantagem**: Feedback imediato, não precisa clicar em "Salvar" para descobrir erro.

---

## 🧪 TESTES E QUALIDADE

### P13: O sistema foi testado?

**Resposta**:
Sim, o sistema foi **testado manualmente**:

1. **Testes Funcionais**: Todas as funcionalidades foram testadas
2. **Testes de Validação**: Validações foram verificadas
3. **Testes de Integração**: Integração com banco testada
4. **Testes de Segurança**: Login e criptografia validados

**O que falta**:
- Testes automatizados (JUnit)
- Testes unitários
- Testes de integração automatizados

**Melhoria Futura**: Implementar suite de testes automatizados.

---

### P14: Como vocês tratam erros?

**Resposta**:
Tratamento de erros em múltiplas camadas:

1. **Validação de Entrada**: Antes de processar
2. **Mensagens ao Usuário**: Erros claros e informativos
3. **Logging**: `printStackTrace()` para debug (pode ser melhorado)
4. **Try-catch**: Tratamento de exceções SQL

**Exemplo**:
```java
try {
    usuarioDAO.cadastrar(usuario);
    JOptionPane.showMessageDialog(..., "Sucesso!");
} catch (SQLException e) {
    e.printStackTrace();
    JOptionPane.showMessageDialog(..., "Erro ao salvar");
}
```

**Melhoria Futura**: Sistema de logging robusto (Log4j) e exceções customizadas.

---

## 📊 MODELAGEM

### P15: Todos os diagramas UML foram criados?

**Resposta**:
Sim, **todos os artefatos** foram criados:

✅ **Diagrama de Casos de Uso**: 8 casos de uso documentados
✅ **Diagrama de Classes**: Todas as classes e relacionamentos
✅ **Diagrama de Sequência**: Fluxos de interação
✅ **Diagrama de Atividades**: Fluxo principal do sistema
✅ **DER**: Diagrama Entidade-Relacionamento
✅ **Modelo Relacional**: Estrutura tabular completa
✅ **Modelo Físico**: Implementação no MySQL

**Localização**: Pasta `modelagem/`
**Formato**: PlantUML (.puml) e Markdown (.md)

---

### P16: Como visualizar os diagramas?

**Resposta**:
Diagramas estão em formato **PlantUML**:

1. **Online**: https://www.plantuml.com/plantuml/uml/
2. **VS Code**: Extensão "PlantUML"
3. **IntelliJ**: Plugin "PlantUML integration"

**Vantagens do PlantUML**:
- Texto puro (versionável)
- Fácil de editar
- Renderização automática
- Exporta para PNG/SVG

---

## 🚀 MELHORIAS FUTURAS

### P17: Quais melhorias vocês sugerem?

**Resposta**:
Lista completa em `DOCUMENTACAO_BANCA.md`, seção "Melhorias Futuras". Principais:

**Alta Prioridade**:
- Sistema de busca e filtros
- Testes automatizados
- Recuperação de senha

**Média Prioridade**:
- Normalização de interesses
- Paginação
- Logging robusto

**Baixa Prioridade**:
- Interface mais moderna
- Sistema de avaliação
- API REST

---

## 💻 CÓDIGO

### P18: Como está organizado o código?

**Resposta**:
Estrutura em **pacotes** seguindo padrão MVC:

```
com.curadoria/
├── view/     # Interface gráfica (Swing)
├── model/    # Entidades de domínio
├── dao/      # Acesso a dados
└── db/       # Configuração de conexão
```

**Benefícios**:
- Separação clara de responsabilidades
- Fácil localizar código
- Escalável
- Manutenível

---

### P19: Há comentários no código?

**Resposta**:
Código está **autoexplicativo** com nomes descritivos:

- Métodos com nomes claros: `buscarPorLogin()`, `cadastrarRecurso()`
- Variáveis descritivas: `usuarioLogado`, `txtTitulo`
- Estrutura lógica

**Melhoria Futura**: Adicionar Javadoc completo para documentação formal.

---

## 🎯 REQUISITOS

### P20: Todos os requisitos foram atendidos?

**Resposta**:
Sim, **todos os requisitos** foram atendidos:

✅ Aplicação desktop com interface gráfica
✅ Banco de dados MySQL
✅ Autenticação de usuários
✅ Dois perfis (Admin/Comum)
✅ Cadastro/edição/inativação de usuários
✅ Cadastro de recursos
✅ Visualização ordenada alfabeticamente
✅ Categorias fixas
✅ Interesses limitados a 2
✅ Todos os artefatos de modelagem

**Documentação**: `ANALISE_REQUISITOS.md` contém checklist completo.

---

## 📝 CONCLUSÃO

### P21: O que vocês aprenderam com este projeto?

**Resposta**:
Aprendizados principais:

1. **Desenvolvimento Completo**: Do requisito à implementação
2. **Arquitetura**: Padrões de projeto (DAO, Factory)
3. **Segurança**: Criptografia de senhas
4. **Banco de Dados**: Normalização, integridade referencial
5. **Modelagem**: UML completo
6. **Integração**: Java + MySQL + Swing

**Desafios Superados**:
- Integração inicial com MySQL
- Validações complexas
- Ordenação eficiente
- Organização do código

---

**Preparação**: Revise este documento antes da apresentação para estar preparado para qualquer pergunta! 🎯

