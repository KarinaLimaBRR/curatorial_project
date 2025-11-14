# Diagnóstico de Problemas - Por que o projeto não está rodando?

## 🔍 Problemas Mais Comuns

### 1. ❌ Banco de Dados MySQL não está rodando

**Sintoma**: Erro ao tentar conectar: `Communications link failure` ou `Connection refused`

**Solução**:
```bash
# Verificar se MySQL está rodando (macOS)
brew services list | grep mysql

# Iniciar MySQL (macOS)
brew services start mysql

# Ou verificar status
mysql.server status
mysql.server start
```

**Verificar no Linux**:
```bash
sudo systemctl status mysql
sudo systemctl start mysql
```

**Verificar no Windows**:
- Abrir "Serviços" (services.msc)
- Procurar "MySQL" e iniciar o serviço

---

### 2. ❌ Banco de dados não existe

**Sintoma**: Erro: `Unknown database 'DB_CuradoriaIA'`

**Solução**:
```bash
# Executar o script SQL
mysql -u root -p < database/schema.sql
```

Ou manualmente:
```sql
CREATE DATABASE IF NOT EXISTS DB_CuradoriaIA;
USE DB_CuradoriaIA;
-- Depois executar o resto do schema.sql
```

---

### 3. ❌ Credenciais incorretas

**Sintoma**: Erro: `Access denied for user 'root'@'localhost'`

**Solução**:
1. Abrir `src/main/java/com/curadoria/db/ConnectionFactory.java`
2. Ajustar as credenciais:
   ```java
   private static final String USER = "root";        // Seu usuário MySQL
   private static final String PASSWORD = "123456789"; // Sua senha MySQL
   ```

**Testar conexão**:
```bash
mysql -u root -p
# Digite sua senha
```

---

### 4. ❌ Porta MySQL incorreta

**Sintoma**: Erro de conexão na porta 3306

**Solução**:
1. Verificar porta do MySQL:
   ```bash
   mysql -u root -p -e "SHOW VARIABLES LIKE 'port';"
   ```
2. Ajustar em `ConnectionFactory.java`:
   ```java
   private static final String URL = "jdbc:mysql://localhost:PORTA/DB_CuradoriaIA";
   ```

---

### 5. ❌ Tabelas não foram criadas

**Sintoma**: Erro: `Table 'usuario' doesn't exist`

**Solução**:
```bash
# Executar script completo
mysql -u root -p < database/schema.sql
```

Ou verificar se as tabelas existem:
```sql
USE DB_CuradoriaIA;
SHOW TABLES;
```

Deve mostrar: `categoria`, `recurso`, `usuario`

---

### 6. ❌ Dependências Maven não baixadas

**Sintoma**: Erro: `ClassNotFoundException` ou `NoClassDefFoundError`

**Solução**:
```bash
# Limpar e baixar dependências
mvn clean install

# Ou forçar download
mvn dependency:resolve
```

---

### 7. ❌ Java não encontrado ou versão incorreta

**Sintoma**: Erro: `UnsupportedClassVersionError` ou `java: command not found`

**Solução**:
```bash
# Verificar versão do Java
java -version
# Deve ser Java 17 ou superior

# Verificar JAVA_HOME
echo $JAVA_HOME

# Se necessário, configurar JAVA_HOME (macOS/Linux)
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
```

---

### 8. ❌ Plugin exec-maven-plugin não configurado

**Sintoma**: Erro ao executar `mvn exec:java`

**Solução**: O plugin será baixado automaticamente, mas podemos adicionar ao pom.xml para garantir.

---

## 🧪 Testes de Diagnóstico

### Teste 1: Verificar Compilação
```bash
cd /Users/karinalima/Downloads/curadoria-db
mvn clean compile
```
**Esperado**: `BUILD SUCCESS`

---

### Teste 2: Verificar Conexão com Banco
```bash
mvn exec:java -Dexec.mainClass="com.curadoria.db.ConnectionFactory"
```
**Esperado**: `✅ Conexão bem-sucedida com o banco DB_CuradoriaIA!`

**Se falhar**: Problema de conexão com MySQL (ver itens 1-4 acima)

---

### Teste 3: Verificar se Banco Existe
```bash
mysql -u root -p -e "SHOW DATABASES;" | grep DB_CuradoriaIA
```
**Esperado**: `DB_CuradoriaIA`

**Se não aparecer**: Executar `database/schema.sql`

---

### Teste 4: Verificar Tabelas
```bash
mysql -u root -p DB_CuradoriaIA -e "SHOW TABLES;"
```
**Esperado**: 
```
Tables_in_DB_CuradoriaIA
categoria
recurso
usuario
```

---

### Teste 5: Verificar Administrador Padrão
```bash
mysql -u root -p DB_CuradoriaIA -e "SELECT login, tipo FROM usuario WHERE tipo = 'Administrador';"
```
**Esperado**: Pelo menos um registro com login `admin`

---

## 🚀 Solução Passo a Passo

### Passo 1: Verificar MySQL
```bash
# Verificar se MySQL está rodando
mysql -u root -p -e "SELECT 1;"
```

Se não funcionar:
- Iniciar MySQL
- Verificar credenciais

---

### Passo 2: Criar Banco de Dados
```bash
cd /Users/karinalima/Downloads/curadoria-db
mysql -u root -p < database/schema.sql
```

**Importante**: Digite a senha quando solicitado

---

### Passo 3: Verificar Credenciais
Editar `src/main/java/com/curadoria/db/ConnectionFactory.java`:
```java
private static final String USER = "root";           // Seu usuário
private static final String PASSWORD = "SUA_SENHA"; // Sua senha
```

---

### Passo 4: Testar Conexão
```bash
mvn exec:java -Dexec.mainClass="com.curadoria.db.ConnectionFactory"
```

---

### Passo 5: Executar Aplicação
```bash
mvn exec:java -Dexec.mainClass="com.curadoria.view.TelaLogin"
```

Ou na IDE:
- IntelliJ: Botão Run na classe `TelaLogin`
- Eclipse: Run As → Java Application
- VS Code: F5 ou Run

---

## 🔧 Comandos Úteis de Diagnóstico

### Verificar Status do MySQL
```bash
# macOS
brew services list | grep mysql

# Linux
sudo systemctl status mysql

# Windows
# Abrir "Serviços" e procurar MySQL
```

### Ver Logs de Erro
```bash
# macOS
tail -f /usr/local/var/mysql/*.err

# Linux
sudo tail -f /var/log/mysql/error.log
```

### Testar Conexão Manual
```bash
mysql -u root -p -h localhost -P 3306
```

### Verificar Porta MySQL
```bash
mysql -u root -p -e "SHOW VARIABLES LIKE 'port';"
```

---

## 📋 Checklist de Diagnóstico

Execute este checklist na ordem:

- [ ] MySQL está rodando?
- [ ] Banco de dados `DB_CuradoriaIA` existe?
- [ ] Tabelas foram criadas? (usuario, recurso, categoria)
- [ ] Credenciais em ConnectionFactory estão corretas?
- [ ] Java 17+ está instalado?
- [ ] Projeto compila? (`mvn clean compile`)
- [ ] Dependências foram baixadas? (`mvn dependency:resolve`)
- [ ] Conexão com banco funciona? (Teste ConnectionFactory)
- [ ] Administrador padrão existe? (login: admin, senha: admin123)

---

## 🆘 Se Nada Funcionar

1. **Verificar logs completos**:
   ```bash
   mvn exec:java -Dexec.mainClass="com.curadoria.view.TelaLogin" -X
   ```

2. **Executar com mais verbosidade**:
   ```bash
   java -cp "target/classes:$(mvn dependency:build-classpath -q -Dmdep.outputFile=/dev/stdout)" com.curadoria.view.TelaLogin
   ```

3. **Verificar se todas as classes foram compiladas**:
   ```bash
   ls -la target/classes/com/curadoria/
   ```

4. **Limpar e recompilar tudo**:
   ```bash
   mvn clean
   mvn compile
   mvn package
   ```

---

## 💡 Dicas

- **Sempre teste a conexão primeiro** com `ConnectionFactory.main()`
- **Verifique os logs** para mensagens de erro específicas
- **Use a IDE** para executar - geralmente mostra erros mais claros
- **Confirme as credenciais** - são o problema mais comum

---

## 📞 Informações para Suporte

Se precisar de ajuda, forneça:

1. Mensagem de erro completa
2. Resultado de `java -version`
3. Resultado de `mysql --version`
4. Resultado do teste de conexão
5. Sistema operacional
6. Logs de erro (se houver)

---

**Última atualização**: 2024

