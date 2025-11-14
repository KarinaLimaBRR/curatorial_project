# Correção: ComboBox de Categoria mostrando caminho do objeto

## 🔍 Problema Identificado

O ComboBox de categoria estava mostrando o caminho do objeto Java ao invés do nome da categoria:
- **Antes**: `com.curadoria.model.Categoria@344a075c`
- **Esperado**: `IA Responsável`, `Cibersegurança`, `Privacidade & Ética Digital`

**Causa**: A classe `Categoria` não tinha o método `toString()` sobrescrito, então o Java usava o `toString()` padrão da classe `Object`, que retorna o nome da classe + hashcode.

---

## ✅ Solução Aplicada

Foi adicionado o método `toString()` na classe `Categoria` para retornar o nome da categoria:

```java
@Override
public String toString() {
    return nome != null ? nome : "";
}
```

**Localização**: `src/main/java/com/curadoria/model/Categoria.java:39-42`

---

## 🧪 Como Testar

1. **Recompilar o projeto** (se necessário):
   ```bash
   mvn clean compile
   ```

2. **Executar a aplicação**:
   ```bash
   mvn exec:java
   ```

3. **Fazer login como usuário comum**

4. **Ir para aba "Cadastrar Recurso"**

5. **Clicar no ComboBox de Categoria**

6. **Verificar**: Agora deve mostrar:
   - ✅ IA Responsável
   - ✅ Cibersegurança
   - ✅ Privacidade & Ética Digital

---

## 📝 Explicação Técnica

### Por que aconteceu?

Quando você adiciona objetos customizados a um `JComboBox`, o Swing usa o método `toString()` do objeto para exibir o texto. Se o método não estiver sobrescrito, usa o padrão de `Object`, que retorna algo como `ClassName@hashcode`.

### Solução

Sobrescrever o método `toString()` na classe `Categoria` para retornar o campo `nome`, que é o que queremos exibir.

### Alternativa (não usada)

Também seria possível criar um `ListCellRenderer` customizado, mas o `toString()` é mais simples e direto.

---

## ✅ Status

**Problema**: ✅ **RESOLVIDO**

O ComboBox agora mostra corretamente os nomes das categorias:
- IA Responsável
- Cibersegurança
- Privacidade & Ética Digital

---

**Data da Correção**: 2024
**Arquivo Modificado**: `src/main/java/com/curadoria/model/Categoria.java`

