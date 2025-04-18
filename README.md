
# 🛡️ contratacao-service — Microserviço de Contratação de Seguros Automotivos

Este microserviço é responsável por processar contratações de seguros automotivos e publicar eventos de nova venda em uma fila SQS. A solução foi desenvolvida com foco em escalabilidade, separação de responsabilidades e aderência a boas práticas de arquitetura moderna.

## 🚀 Tecnologias e Padrões Utilizados

- **Java 17**
- **Spring Boot 3.3.10**
- **Arquitetura Hexagonal (Ports & Adapters)**
- **Integração assíncrona com AWS SQS (LocalStack para ambiente local)**
- **Banco de dados PostgreSQL**
- **Testcontainers para testes de integração realistas**
- **Lombok para redução de boilerplate**
- **Swagger/OpenAPI para documentação**
- **Checkstyle (Google style) para padronização de código**
- **Log estruturado com SLF4J**
- **Perfis segregados por ambiente (`local`, `docker`, `test`)**


## 📁 Estrutura de Pastas

Este microserviço foi desenvolvido com base na **Clean Architecture**, fortemente inspirado nos princípios da **Arquitetura Hexagonal**, **DDD** e **SOLID**.

```
contratacao-service
├── adapter/
│   ├── in/
│   │   └── web/
│   │       ├── controller/
│   │       └── dto/
│   └── out/
│       ├── messaging/sqs/
│       └── persistence/
├── application/
│   ├── config/
│   ├── exception/
│   ├── port/
│   │   ├── in/
│   │   └── out/
│   └── usecase/
├── config/
│   └── GlobalExceptionHandler
├── domain/
│   └── entity/
├── resources/
│   ├── application.yml
│   ├── application-local.yml
│   ├── application-docker.yml
│   └── application-test.yml
```

> **🧠 Organização por responsabilidade**:
>
> - `adapter/`: Camada de entrada e saída — expõe a API e integra com SQS/PostgreSQL.
> - `application/`: Regras de negócio — define ports (interfaces), use cases e exceções da aplicação.
> - `config/`: Configurações e tratamento global de exceções.
> - `domain/`: Entidades centrais da regra de negócio.
> - `resources/`: Arquivos de configuração por ambiente.

## 🐳 Docker

### Build do JAR
```bash
mvn clean package -DskipTests
```

### Build da imagem Docker
```bash
docker build -t contratacao-service .
```

### Dockerfile utilizado
```dockerfile
FROM eclipse-temurin:17-jdk
VOLUME /tmp
WORKDIR /app
COPY target/contratacao-service.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]
```

## 🔗 Docker Compose: Banco, Fila e Serviço
> Se você preferir rodar o serviço manualmente (via terminal ou IDE), basta subir apenas os containers `postgres` e `localstack` via Docker Compose, conforme abaixo.

```bash
docker compose up -d postgres localstack
```

Para subir tudo (inclusive o microserviço):

```bash
docker compose up -d
```

## 📩 Criando a Fila no LocalStack

```bash
AWS_ACCESS_KEY_ID=test AWS_SECRET_ACCESS_KEY=test aws sqs create-queue \
  --queue-name nova-venda-criada \
  --region us-east-1 \
  --endpoint-url=http://localhost:4566
```

## 🧪 Testes
> A cobertura total supera 90%, validando cenários positivos e negativos de forma automatizada.

- Testes unitários e de integração separados por perfil Maven:
  - `unit-tests` (ativo por padrão)
  - `integration-tests`

Execute testes de integração com:

```bash
mvn test -Pintegration-tests
```

> ⚠️ Para executar os testes de integração com sucesso, é necessário que os containers do PostgreSQL e LocalStack estejam rodando com a fila `nova-venda-criada` previamente criada.

## 📚 Swagger (OpenAPI)
Acesse a documentação interativa:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## 🧰 Ferramentas e Qualidade

- **Checkstyle**: executa validação automática com base no `google_checks.xml`.  

```bash
mvn checkstyle:check
```

Caso enfrente problemas ao baixar a dependência, execute:

```bash
mvn dependency:get -Dartifact=com.puppycrawl.tools:checkstyle:10.3.1
```

- **Actuator**: endpoints de saúde e métricas expostos em `/actuator`.

---

## 📈 Observabilidade e Logs

- Foi adicionada uma camada básica de logs com `Slf4j` nas principais classes de serviço, controller e exception handler.
- O serviço está preparado para monitoramento básico via **Spring Boot Actuator**, expondo endpoints de `/actuator/health` e métricas para futura integração com ferramentas como Prometheus, Grafana ou CloudWatch.


---

## 📓 Observações

- A arquitetura aplicada é fortemente inspirada na Clean Architecture e segue os princípios de separação de responsabilidades entre camadas.


- ℹ️ **Aviso sobre o AWS SDK 1.x**:  

Durante a execução local, pode ser exibido um aviso relacionado à descontinuação do AWS SDK para Java 1.x.  
Isso não impacta o funcionamento do projeto com o LocalStack e é seguro ignorar durante testes e desenvolvimento.

📌 Em uma futura evolução, recomenda-se a migração para o AWS SDK v2 para garantir aderência às versões mais recentes e melhor suporte oficial.  
Mais informações: [AWS Blog - Java SDK 1.x em manutenção](https://aws.amazon.com/blogs/developer/the-aws-sdk-for-java-1-x-is-in-maintenance-mode-effective-july-31-2024/)

---

## 🎯 Sobre o Desafio

Este microserviço foi desenvolvido como parte de um desafio técnico com foco em modernização de sistemas legados na jornada de contratação de seguros automotivos. A proposta visa demonstrar domínio sobre:

- Arquitetura distribuída moderna
- Comunicação assíncrona via mensageria
- Princípios 12 Factors
- Alta qualidade de código, testes e observabilidade

Este projeto também demonstra domínio e aplicação prática de:

- Arquitetura hexagonal e Clean Architecture
- Princípios SOLID e DDD
- TDD, testes automatizados e validação de contratos
- Comunicação assíncrona e mensageria resiliente

---

## 👤 Autor

**Henrique Witzel**  
Desenvolvedor backend e líder técnico com foco em:

- Arquitetura moderna
- Qualidade de código
- Boas práticas de engenharia de software

[LinkedIn](https://www.linkedin.com/in/henrique-witzel-2aa883149/)
