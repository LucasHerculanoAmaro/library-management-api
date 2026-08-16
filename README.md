# Library Management API

*API REST* para gerenciamento de uma biblioteca, desenvolvida como parte do desafio técnico da pós-graduação em Arquitetura e Desenvolvimento Java da FIAP.

A aplicação permite o gerenciamento de usuários, livros, empréstimos e reservas, além de fornecer autenticação e autorização baseada em *JWT*.

---

## Objetivo

Desenvolver uma *API REST* para gerenciamento de uma biblioteca, contemplando:

- Cadastro e gerenciamento de usuários;
- Cadastro e gerenciamento de livros;
- Controle de empréstimos;
- Controle de devoluções;
- Sistema de reservas;
- Controle de disponibilidade dos livros;
- Autenticação de usuários;
- Autorização baseada em perfis;
- Persistência dos dados em banco relacional.

---

## Tecnologias utilizadas

- *Java*
- *Spring Boot*
- *Spring Web*
- *Spring Data JPA*
- *Spring Security*
- *JWT*
- *Jakarta Validation*
- *PostgreSQL*
- *Maven*
- *Docker*
- *Docker Compose*
- *Swagger / OpenAPI*

---

## Funcionalidades

### Usuários

- Cadastro de usuários;
- Consulta por ID;
- Consulta por CPF;
- Consulta por e-mail;
- Atualização;
- Ativação e desativação;
- Exclusão;
- Controle de perfil.

### Livros

- Cadastro de livros;
- Cadastro de livros em lote;
- Consulta por ID;
- Consulta por título;
- Consulta por autor;
- Consulta por ISBN;
- Listagem de livros disponíveis;
- Atualização;
- Exclusão.

### Empréstimos

- Cadastro de empréstimo;
- Cadastro de empréstimos em lote;
- Consulta por ID;
- Listagem paginada;
- Listagem de empréstimos ativos;
- Listagem de empréstimos devolvidos;
- Consulta por usuário;
- Consulta por livro;
- Devolução de livros.

### Reservas

- Cadastro de reserva;
- Consulta por ID;
- Listagem paginada;
- Cancelamento;
- Controle de fila de reservas;
- Disponibilização do livro para o próximo usuário após devolução.

---

## Regras de negócio

A aplicação possui regras para garantir a consistência das operações, incluindo:

- Usuários inativos não podem realizar empréstimos ou reservas;
- Um usuário não pode possuir simultaneamente um empréstimo ativo e uma reserva para o mesmo livro;
- Livros indisponíveis podem ser reservados;
- Um usuário não pode possuir mais de uma reserva ativa para o mesmo livro;
- Um livro emprestado não pode ser excluído;
- Um usuário com empréstimo ativo não pode ser excluído;
- A devolução altera o status do empréstimo;
- Quando existe uma reserva ativa, a devolução disponibiliza o livro para o próximo usuário da fila;
- Usuários comuns somente podem acessar seus próprios dados;
- Operações administrativas são restritas ao perfil ADMIN.

---

## Segurança

A aplicação utiliza *Spring Security* para autenticação e autorização.

A autenticação é realizada através de e-mail e senha, com geração de *token JWT* após o login.

As senhas são armazenadas utilizando *BCrypt*.

Os acessos aos endpoints são controlados de acordo com os perfis:

- `ADMIN`
- `USUARIO`

Além das regras definidas nos controllers, determinadas validações de propriedade dos recursos são realizadas na camada de serviço.

---

## Banco de dados

A aplicação utiliza *PostgreSQL* como banco de dados relacional.

O ambiente de desenvolvimento e execução pode ser iniciado utilizando *Docker Compose*, que disponibiliza:

- *PostgreSQL*;
- *API Spring Boot*.

O banco é criado automaticamente conforme a configuração da aplicação.

Ao iniciar um ambiente completamente novo, a aplicação também cria automaticamente o usuário administrador inicial.

---

## Execução com Docker

### Pré-requisitos

É necessário possuir:

- *Docker*
- *Docker Compose*

### Inicialização

Clone o projeto:

```bash
git clone <URL_DO_REPOSITORI>
```
Entre no diretório:

```bash
cd library-management-api
```

Execute:

```bash
docker compose up --build
```

A API será disponibilizada em:

```bash
http://localhost:8080
```

## Documentação da API

A aplicação disponibiliza documentação através do Swagger/OpenAPI.

Após iniciar a aplicação, acesse:

```bash
http://localhost:8080/swagger-ui/index.html
```

A documentação permite visualizar os endpoints disponíveis e realizar testes diretamente pela interface do Swagger.

### Autenticação

O login é realizado através do endpoint:

```bash
POST /api/auth/login
```

Exemplo:

```bash
{
"email": "admin@library.com",
"senha": "admin"
}
```

A resposta contém o token JWT:

```bash
{
"token": "TOKEN_JWT",
"tipo": "Bearer"
}
```

O token deve ser enviado nas requisições protegidas através do header:

```bash
Authorization: Bearer TOKEN_JWT
```

As credenciais do administrador inicial devem ser conferidas na configuração da aplicação antes da utilização em outro ambiente.

## Principais endpoints

### Autenticação
| Método | Endpoint          | Acesso  |
| ------ | ----------------- | ------- |
| POST   | `/api/auth/login` | Público |

## Usuários
| Método | Endpoint                       | Acesso          |
| ------ | ------------------------------ | --------------- |
| POST   | `/api/usuarios`                | ADMIN           |
| GET    | `/api/usuarios`                | ADMIN           |
| GET    | `/api/usuarios/{id}`           | ADMIN / USUARIO |
| PUT    | `/api/usuarios/{id}`           | ADMIN           |
| PATCH  | `/api/usuarios/{id}/ativar`    | ADMIN           |
| PATCH  | `/api/usuarios/{id}/desativar` | ADMIN           |
| DELETE | `/api/usuarios/{id}`           | ADMIN           |
| GET    | `/api/usuarios/cpf/{cpf}`      | ADMIN           |
| GET    | `/api/usuarios/email/{email}`  | ADMIN           |

## Livros
| Método | Endpoint                  | Acesso          |
| ------ | ------------------------- | --------------- |
| POST   | `/api/livros`             | ADMIN           |
| POST   | `/api/livros/lote`        | ADMIN           |
| GET    | `/api/livros`             | ADMIN / USUARIO |
| GET    | `/api/livros/{id}`        | ADMIN / USUARIO |
| PUT    | `/api/livros/{id}`        | ADMIN           |
| DELETE | `/api/livros/{id}`        | ADMIN           |
| GET    | `/api/livros/titulo`      | ADMIN / USUARIO |
| GET    | `/api/livros/autor`       | ADMIN / USUARIO |
| GET    | `/api/livros/isbn/{isbn}` | ADMIN / USUARIO |
| GET    | `/api/livros/disponiveis` | ADMIN / USUARIO |

## Empréstimos
| Método | Endpoint                         | Acesso      |
| ------ | -------------------------------- | ----------- |
| POST   | `/api/emprestimos`               | ADMIN       |
| POST   | `/api/emprestimos/lote`          | ADMIN       |
| GET    | `/api/emprestimos`               | Autenticado |
| GET    | `/api/emprestimos/{id}`          | Autenticado |
| PATCH  | `/api/emprestimos/{id}/devolver` | ADMIN       |
| GET    | `/api/emprestimos/ativos`        | ADMIN       |
| GET    | `/api/emprestimos/devolvidos`    | ADMIN       |
| GET    | `/api/emprestimos/usuario/{id}`  | Autenticado |
| GET    | `/api/emprestimos/livro/{id}`    | ADMIN       |

## Reservas
| Método | Endpoint                      | Acesso          |
| ------ | ----------------------------- | --------------- |
| POST   | `/api/reservas`               | ADMIN / USUARIO |
| GET    | `/api/reservas`               | ADMIN           |
| GET    | `/api/reservas/{id}`          | ADMIN / USUARIO |
| PATCH  | `/api/reservas/{id}/cancelar` | ADMIN / USUARIO |

## Testes

A aplicação foi submetida a testes funcionais e de segurança utilizando Postman e *Swagger*.

Foram validados, entre outros:

* Autenticação; 
* Autorização por perfil; 
* Cadastro e consulta de usuários;
* Cadastro e consulta de livros; 
* Empréstimos; 
* Devoluções; 
* Reservas; 
* Validação de regras de negócio; 
* Tratamento de recursos inexistentes; 
* Validação de dados; 
* Prevenção de exclusão de usuários com empréstimos ativos; 
* Prevenção de exclusão de livros com empréstimos ativos; 
* Acesso indevido a recursos; 
* Usuários inativos; 
* Respostas *HTTP* de erro.

Os testes e evidências detalhadas estão disponíveis no relatório técnico do projeto.

## Estrutura do projeto

```bash
src
└── main
    └── java
        └── br.com.library_management_api
            ├── controller
            ├── dto
            │   ├── request
            │   └── response
            ├── entity
            ├── enums
            ├── exception
            ├── repository
            ├── security
            └── service
```
A aplicação segue uma arquitetura organizada em camadas, separando responsabilidades entre controllers, services, repositories, entidades, DTOs e componentes de segurança.

## Documentação técnica

O projeto possui um relatório técnico contendo:

* Objetivos;
* Descrição da solução; 
* Requisitos; 
* Regras de negócio; 
* Arquitetura; 
* Persistência; 
* Segurança; 
* APIs; 
* Testes; 
* Evidências; 
* Referências técnicas.

## Autor

### Lucas Amaro

Projeto desenvolvido para o desafio técnico da pós-graduação em Arquitetura e Desenvolvimento Java — FIAP.


### Uma alteração importante

No README eu deixei:

```text
https://github.com/LucasHerculanoAmaro/library-management-api.git
