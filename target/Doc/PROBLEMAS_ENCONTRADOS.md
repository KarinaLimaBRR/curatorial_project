# Problemas Encontrados e Soluções

## ✅ Problemas Corrigidos

### 1. Plugin exec-maven-plugin não estava no pom.xml
**Status**: ✅ **CORRIGIDO**
- Plugin adicionado ao `pom.xml`
- Agora pode executar com `mvn exec:java`

---

## ⚠️ Problemas Identificados

### 1. Inconsistência no Tipo de Usuário no Banco

**Problema**: 
- No banco: tipo = `"ADMIN"` (maiúsculas)
- No código: verifica `"administrador"` ou `"admin"` (minúsculas)
- No ComboBox: usa `"Administrador"` e `"Usuário comum"`

**Impacto**: 
- Login pode não funcionar corretamente
- Usuário admin pode não ser reconhecido como administrador

**Solução**:

**Opção 1: Corrigir no banco (Recomendado)**
```sql
UPDATE usuario SET tipo = 'Administrador' WHERE tipo = 'ADMIN';
```

**Opção 2: Ajustar código para aceitar maiúsculas**
O código já tem `toLowerCase()`, então deveria funcionar, mas vamos garantir.

**Verificação**:
```sql
SELECT login, tipo FROM usuario;
-- Deve mostrar tipo como "Administrador" ou "Usuário comum"
```

---

## 🔍 Análise Completa

### O que está funcionando:
- ✅ Compilação do projeto
- ✅ MySQL rodando e acessível
- ✅ Banco de dados `DB_CuradoriaIA` existe
- ✅ Tabelas criadas (usuario, recurso, categoria)
- ✅ Administrador existe no banco (login: `admin`)
- ✅ Plugin exec-maven-plugin adicionado

### O que precisa verificar:
- ⚠️ Tipo do usuário no banco (pode estar como "ADMIN" em vez de "Administrador")
- ⚠️ Senha do administrador (verificar se hash BCrypt está correto)

---

## 🚀 Como Executar Agora

### Passo 1: Corrigir tipo do usuário (se necessário)
```bash
mysql -u root -p123456789 DB_CuradoriaIA -e "UPDATE usuario SET tipo = 'Administrador' WHERE tipo = 'ADMIN';"
```

### Passo 2: Verificar se correção funcionou
```bash
mysql -u root -p123456789 DB_CuradoriaIA -e "SELECT login, tipo FROM usuario;"
```

### Passo 3: Executar aplicação
```bash
cd /Users/karinalima/Downloads/curadoria-db
mvn exec:java
```

**Ou na IDE**: Executar a classe `TelaLogin`

---

## 🧪 Teste de Login

**Credenciais padrão** (se schema.sql foi executado):
- **Login**: `admin`
- **Senha**: `admin123`

**Se não funcionar**:
1. Verificar se hash BCrypt está correto no banco
2. Verificar se tipo está como "Administrador"
3. Verificar se status = 1 (ativo)

---

## 📋 Checklist de Execução

Execute na ordem:

1. [x] Projeto compila
2. [x] MySQL está rodando
3. [x] Banco existe
4. [x] Tabelas existem
5. [ ] Tipo do usuário corrigido (se necessário)
6. [ ] Testar conexão: `mvn exec:java -Dexec.mainClass="com.curadoria.db.ConnectionFactory"`
7. [ ] Executar aplicação: `mvn exec:java`

---

## 🔧 Comandos Rápidos

### Verificar tipo do usuário
```bash
mysql -u root -p123456789 DB_CuradoriaIA -e "SELECT login, tipo, status FROM usuario;"
```

### Corrigir tipo do usuário
```bash
mysql -u root -p123456789 DB_CuradoriaIA -e "UPDATE usuario SET tipo = 'Administrador' WHERE tipo = 'ADMIN' OR tipo = 'admin';"
```

### Testar conexão
```bash
mvn exec:java -Dexec.mainClass="com.curadoria.db.ConnectionFactory"
```

### Executar aplicação
```bash
mvn exec:java
```

---

## 💡 Dicas

1. **Sempre verifique o console** quando executar - erros aparecem lá
2. **Use a IDE** para executar - mostra erros mais claros
3. **Teste a conexão primeiro** antes de executar a aplicação completa
4. **Verifique os logs** se algo não funcionar

---

**Status**: ✅ **Principal problema corrigido** (plugin adicionado)
**Ação necessária**: Verificar/corrigir tipo do usuário no banco

