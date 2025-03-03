# ⚙️ Sistema de Gerenciamento de Tarefas

> Este projeto implementa um sistema de gerenciamento de tarefas utilizando a arquitetura de microserviços com Spring Boot.

## 📝 Atividades solicitadas:
- [x] Ciração de serviço para cadastro de usuário
- [x] Ciração de serviço para cadastro de tarefas
- [x] Criação de teste para serviço de cadastro de usuário
- [x] Criação de teste para serviço de cadastro de de tarefas
- [x] Criação de teste de integração entre os servicos
- [ ] Ciração de front-end integrando os serviços criados
- [ ] Criação de teste de front-end
- [x] Ciração de imagem doecker para execução dos serviços
- [x] Ciração de documentação da API utilizando Swagger
- [x] Ciração de instruções para deploy da aplicação


## 🧩 Tecnologias Utilizadas

- Spring Boot 3.1.x
- Spring Data JPA
- PostgreSQL
- Docker & Docker Compose
- Swagger/OpenAPI para documentação


## 🏗️ Estrutura do Projeto

Backend composto por dois microserviços:

- **usuario-service**: Gerenciamento de usuários
- **tarefa-service**: Gerenciamento de tarefas

Utilizado conteiners docker para cada um dos serviços que estao associados em um Docker-compose

## 🛠️ Pré-requisitos

- Java 17+
- Docker e Docker Compose
- Maven (opcional, se não usar o wrapper)

## 👨‍💻 Como Executar

### 🐋 Usando Docker (Recomendado)
> Os comandos a seguir devem ser executados no bash
1. Clone o repositório:
```bash
git https://github.com/LucasNR22/sistema-gerenciamento-tarefas.git
cd sistema-gerenciamento-tarefas
```

2. Compíle os projetos:
```bash
chmod +x build-backend.sh
./build-backend.sh
```
3. Inicie os conteiners:
```bash
docker-compose up -d
```

4. Verifique se os containers estão rodando:
```bash
docker-compose ps
```

5. Verifique os serviços acessando:
- `Serviço de Usuários`: http://localhost:8081/swagger-ui.html
- `Serviço de Tarefas`: http://localhost:8082/swagger-ui.html

6. Teste a API:
- Endpoints Principais
    - Serviço de Usuários:

        - `GET /api/usuarios`: Lista todos os usuários
        - `GET /api/usuarios/{id}`: Obtém um usuário específico
        - `POST /api/usuarios`: Cria um novo usuário
        - `PUT /api/usuarios/{id}`: Atualiza um usuário existente
        - `DELETE /api/usuarios/{id}`: Remove um usuário

    - Serviço de Tarefas:
        - `GET /api/tarefas`: Lista todas as tarefas
        - `GET /api/tarefas/{id}`: Obtém uma tarefa específica
        - `POST /api/tarefas`: Cria uma nova tarefa
        - `PUT /api/tarefas/{id}`: Atualiza uma tarefa existente
        - `DELETE /api/tarefas/{id}`: Remove uma tarefa
        - `GET /api/tarefas/usuario/{usuarioId}`: Lista tarefas por usuário
        - `GET /api/tarefas/status/{status}`: Lista tarefas por status

## 📢 Considerações finais
> Agradeço a oportunidade e espero ter atendido aos requisitos minimos para a vaga. Sigo a disposição para eventuais duvidas e esclarecimentos.