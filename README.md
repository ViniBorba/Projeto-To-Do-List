# 📌 ToDo App – Backend (Spring Boot 3 + JWT + H2)

Este é o **backend** da aplicação de gerenciamento de tarefas.  
Desenvolvido em **Spring Boot 3**, fornece uma API REST segura com **JWT** e persistência em **H2 Database**.

---

## 🚀 Tecnologias
- [Java 21](https://openjdk.org/)
- [Spring Boot 3](https://spring.io/projects/spring-boot)
- [Spring Security 6](https://spring.io/projects/spring-security)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [H2 Database](https://www.h2database.com/)
- [JWT (jjwt)](https://github.com/jwtk/jjwt)
- [Lombok](https://projectlombok.org/)

---

## 📦 Estrutura
```
backend/
 ├── src/main/java/com/example/demo/
 │   ├── auth/          # JWT, filtros e serviços de autenticação
 │   ├── task/          # Entidade Task, DTOs, Controller e Service
 │   ├── user/          # Usuário e UserDetailsService
 │   └── DemoApplication.java
 ├── src/main/resources/
 │   ├── application.properties
 │   └── data.sql (opcional para carga inicial)
 ├── pom.xml
```

---

## ⚙️ Pré-requisitos
- Java 21
- Maven 3.9+

Verifique:
```bash
java -version
mvn -v
```

---

## 🔧 Configuração
Arquivo `src/main/resources/application.properties`:

```properties
# Porta
server.port=8080

# JWT
jwt.secret=uma-chave-super-secreta-com-32-caracteres
jwt.expirationMillis=3600000

# H2 Database
spring.datasource.url=jdbc:h2:mem:todo
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update

# Console H2
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

---

## ▶️ Executar
```bash
mvn spring-boot:run
```

Acesse:
- API: [http://localhost:8080/api](http://localhost:8080/api)  
- Swagger (se configurado): `http://localhost:8080/swagger-ui.html`  
- H2 Console: `http://localhost:8080/h2-console` (JDBC: `jdbc:h2:mem:todo`)

---

## 🔑 Endpoints principais

### Autenticação
- `POST /api/auth/register` → criar usuário
- `POST /api/auth/login` → autenticar e gerar token

### Tarefas
> Todos requerem `Authorization: Bearer <token>`

- `GET /api/tasks` → lista todas as tarefas do usuário
- `POST /api/tasks` → cria nova tarefa
- `PATCH /api/tasks/{id}/done` → marca como concluída
- `PUT /api/tasks/{id}` → atualiza tarefa
- `DELETE /api/tasks/{id}` → remove tarefa
- `PATCH /api/tasks/{id}/status` → atualiza status (novo endpoint)

---

## 🧪 Testes
```bash
mvn test
```

---

## 🌍 Banco de dados
- Usa **H2 em memória** (reinicia a cada execução).
- Acesse via console em `/h2-console`.

Exemplo de login:
- **JDBC URL**: `jdbc:h2:mem:todo`
- **User**: `sa`
- **Password**: (vazio)

---

## 📜 Licença
MIT
