<a id="readme-top"></a>

# 🎓 Global Solution - MentoraX - Java API + IA

![Static Badge](https://img.shields.io/badge/build-passing-brightgreen) ![Static Badge](https://img.shields.io/badge/Version-1.0.0-black) ![License](https://img.shields.io/badge/license-MIT-lightgrey)

## 🧑‍🤝‍🧑 Informações dos Contribuintes

| Nome | Matrícula | Turma |
| :------------: | :------------: | :------------: |
| Marcus Vinicius de Souza Calazans | 556620 | 2TDSPH |
| Felipe Nogueira Ramon | 555335 | 2TDSPH |
| Pedro Henrique Vasco Antonieti | 556253 | 2TDSPH |

<p align="right"><a href="#readme-top">Voltar ao topo</a></p>

## 🚩 Características

API RESTful desenvolvida em Java com Spring Boot, focada em **mentoria profissional inteligente** com integração de **Inteligência Artificial (OpenAI GPT-4o-mini)**. A plataforma MentoraX conecta mentores e mentorados, oferecendo recursos como:

- ✅ **Gestão de Perfis Profissionais** (skills, objetivos, experiência)
- ✅ **Sistema de Mentorias** (relação mentor-mentorado com sessões e feedbacks)
- ✅ **Integração OpenAI** para geração de conteúdo personalizado:
  - Recomendações de carreira baseadas no perfil
  - Planos de estudos personalizados
  - Planos de mentoria gerados automaticamente com IA
  - Histórico completo de interações com IA
- ✅ **Autenticação JWT** com recuperação de senha
- ✅ **Cache inteligente** para otimização de consultas
- ✅ **Auditoria completa** via triggers SQL
- ✅ **Documentação completa** via Swagger/OpenAPI
- ✅ **Migrations automáticas** com Flyway

<p align="right"><a href="#readme-top">Voltar ao topo</a></p>

## 🛠️ Tecnologias Utilizadas

![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![SQL Server](https://img.shields.io/badge/Microsoft_SQL_Server-CC2927?style=for-the-badge&logo=microsoft-sql-server&logoColor=white)
![Azure](https://img.shields.io/badge/Microsoft_Azure-0078D4?style=for-the-badge&logo=microsoft-azure&logoColor=white)
![OpenAI](https://img.shields.io/badge/OpenAI-412991?style=for-the-badge&logo=openai&logoColor=white)
![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)

**Stack Técnico:**
- Java 17+
- Spring Boot 3.5.8
- Spring Security (JWT)
- Spring AI (OpenAI Integration)
- Spring Data JPA + Hibernate
- Flyway (migrations)
- SQL Server / Azure SQL
- Lombok
- Swagger/OpenAPI 3.0

<p align="right"><a href="#readme-top">Voltar ao topo</a></p>

## 💻 Inicializar Projeto

### 📝 Pré-requisitos

- **Java 17+**
- **Maven 3.1.0+**
- **SQL Server** (local ou Azure)
- **OpenAI API Key** (para funcionalidades de IA)
- IDE (IntelliJ IDEA, Eclipse ou VS Code)

### 🔑 Configuração de Variáveis de Ambiente

Crie um arquivo `.env` na raiz do projeto com as seguintes variáveis:

```env
# Banco de Dados
SPRING_DATASOURCE_DRIVER=com.microsoft.sqlserver.jdbc.SQLServerDriver
SPRING_DATASOURCE_URL=jdbc:sqlserver://localhost:1433;databaseName=db-mentorax;encrypt=true;trustServerCertificate=true
SPRING_DATASOURCE_USERNAME=seu_usuario
SPRING_DATASOURCE_PASSWORD=sua_senha
SPRING_JPA_DATABASE_PLATFORM=org.hibernate.dialect.SQLServerDialect
SPRING_JPA_HIBERNATE_DDL_AUTO=validate

# OpenAI API
OPENAI_API_KEY=sk-proj-xxxxxxxxxxxxxxxxxx

# Email (para recuperação de senha)
SPRING_MAIL_HOST=smtp.gmail.com
SPRING_MAIL_PORT=465
SPRING_MAIL_USERNAME=seu_email@gmail.com
SPRING_MAIL_PASSWORD=sua_senha_app
APP_MAIL_FROM=seu_email@gmail.com
```

### 🗃️ Instalação

1. Clone o repositório:
    ```sh
    git clone https://github.com/seu-usuario/MentoraX.git
    ```

2. Acesse a pasta do projeto:
    ```sh
    cd MentoraX
    ```

3. Configure as variáveis de ambiente (arquivo `.env`)

4. Execute as migrations do Flyway (automático ao iniciar):
    ```sh
    ./mvnw flyway:migrate
    ```

5. Compile e execute o projeto:
    ```sh
    ./mvnw spring-boot:run
    ```
    
    **Ou no Windows:**
    ```cmd
    mvnw.cmd spring-boot:run
    ```

6. Acesse o Swagger para testar os endpoints:
    ```
    http://localhost:8080/swagger-ui.html
    ```

<p align="right"><a href="#readme-top">Voltar ao topo</a></p>

## 🌐 Acesso pela Rede Local (Mobile)

Para testar com aplicativo mobile na mesma rede Wi-Fi:

1. Configure `server.address=0.0.0.0` no `application.properties`

2. Libere a porta 8080 no Firewall (Windows):
    ```powershell
    New-NetFirewallRule -DisplayName "MentoraX API" -Direction Inbound -Protocol TCP -LocalPort 8080 -Action Allow
    ```

3. Descubra seu IP local:
    ```powershell
    ipconfig
    ```

4. Acesse do celular:
    ```
    http://SEU_IP_LOCAL:8080/swagger-ui.html
    ```

<p align="right"><a href="#readme-top">Voltar ao topo</a></p>

## 📋 Principais Endpoints da API

### 🔐 Autenticação
- `POST /api/auth/register` - Registrar novo usuário
- `POST /api/auth/login` - Login e obter JWT
- `POST /api/auth/validarToken` - Validar token
- `POST /api/auth/recuperarSenha` - Solicitar recuperação de senha

### 👤 Perfil Profissional
- `POST /api/perfil/criar` - Criar perfil (autenticado)
- `GET /api/perfil/meuPerfil` - Ver meu perfil (autenticado)
- `PUT /api/perfil/atualizar` - Atualizar perfil (autenticado)
- `DELETE /api/perfil/deletar` - Deletar perfil (autenticado)
- `GET /api/perfil/todos` - Listar todos os perfis

### 🤝 Mentorias
- `POST /api/mentoria/criar` - Criar mentoria (mentor)
- `GET /api/mentoria/minhasMentorias/comoMentor` - Minhas mentorias como mentor
- `GET /api/mentoria/minhasMentorias/comoMentorado` - Minhas mentorias como mentorado
- `PUT /api/mentoria/atualizar/{id}` - Atualizar mentoria (status/nota)

### 📅 Sessões de Mentoria
- `POST /api/sessao/criar` - Criar sessão (mentor)
- `GET /api/sessao/mentoria/{id}` - Listar sessões de uma mentoria

### ⭐ Feedbacks
- `POST /api/feedback/criar` - Criar feedback (mentorado)
- `GET /api/feedback/mentoria/{id}` - Listar feedbacks de uma mentoria

### 🤖 Inteligência Artificial (OpenAI)
- `POST /api/ia/gerar` - Prompt livre para IA
- `POST /api/ia/recomendacoes/meuPerfil` - Recomendações baseadas no perfil
- `POST /api/ia/planoEstudos/meuPerfil` - Gerar plano de estudos personalizado
- `GET /api/ia/historico/meu` - Ver histórico de interações com IA

### 📝 Plano de Mentoria
- `POST /api/plano/criar` - Criar plano (manual ou com IA)
- `GET /api/plano/mentoria/{id}` - Listar planos de uma mentoria
- `GET /api/plano/mentoria/{id}/atual` - Ver plano mais recente

<p align="right"><a href="#readme-top">Voltar ao topo</a></p>

## 🗄️ Estrutura do Banco de Dados

O projeto utiliza **Flyway** para migrations automáticas. As tabelas criadas são:

- `T_MENTORAX_USUARIO` - Usuários (mentor/mentorado/admin)
- `T_MENTORAX_PERFIL_PROFISSIONAL` - Perfis profissionais
- `T_MENTORAX_MENTORIA` - Relações de mentoria
- `T_MENTORAX_SESSAO_MENTORIA` - Sessões de mentoria
- `T_MENTORAX_FEEDBACK` - Feedbacks das sessões
- `T_MENTORAX_PLANO_MENTORIA` - Planos de mentoria
- `T_MENTORAX_HISTORICO_IA` - Histórico de interações com IA
- `T_MENTORAX_AUDITORIA` - Auditoria de operações

**Migrations disponíveis em:** `src/main/resources/db/migration/`

<p align="right"><a href="#readme-top">Voltar ao topo</a></p>

## 🎯 Funcionalidades Especiais

### 🤖 Integração OpenAI (GPT-4o-mini)

A plataforma utiliza o modelo **GPT-4o-mini** para economizar créditos, configurado com:
- **Temperatura**: 0.7 (equilíbrio entre criatividade e precisão)
- **Max Tokens**: 800 (otimizado para respostas concisas)

### 📊 Sistema de Cache

Cache inteligente implementado com Spring Cache para:
- Listagem de perfis profissionais
- Mentorias por mentor/mentorado
- Sessões de mentoria
- Invalidação automática após modificações

### 🔒 Segurança

- Autenticação via **JWT** (JSON Web Token)
- Senha criptografada com **BCrypt**
- Recuperação de senha com código temporário via e-mail
- Proteção contra múltiplas tentativas de recuperação
- Filtro JWT personalizado para todas as rotas protegidas

<p align="right"><a href="#readme-top">Voltar ao topo</a></p>

## 📱 Integração Mobile

O backend está preparado para comunicação com apps mobile (Android/iOS/React Native):

1. Configure CORS para permitir requisições do mobile
2. Use `server.address=0.0.0.0` para acesso na rede local
3. Todas as rotas retornam JSON padronizado
4. Documentação Swagger disponível para referência

**Base URL exemplo:** `http://192.168.0.10:8080/api`

<p align="right"><a href="#readme-top">Voltar ao topo</a></p>

## 📄 Licença

Este projeto está sob a licença MIT.

<p align="right"><a href="#readme-top">Voltar ao topo</a></p>

---

**Desenvolvido com ❤️ por:** Marcus Calazans, Felipe Ramon e Pedro Antonieti | FIAP - 2TDSPH | Global Solution 2025
