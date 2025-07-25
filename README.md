# 📚 API de Livros, Autores e Categorias - Biblioteca Digital

Este projeto é uma API REST desenvolvida com Spring Boot para gerenciar uma biblioteca digital que também coleta informações de livros de sites externos.

---

## 🚀 Como executar o projeto

### Pré-requisitos

- Java 17+
- IDE IntelliJ

### Execução

1. Clone o projeto:

```bash
git clone https://github.com/jvitorserrao/case-share-api.git
```

2. Acesse a interface de documentação Swagger/OpenAPI:

```
http://localhost:8080/swagger-ui.html
```

---

## 📌 Endpoints Disponíveis

### 📖 Livros

- `GET /api/livro` → Listar livros (filtros: `categoria`, `ano`, `autor`)
- `GET /api/livro/{id}` → Buscar livro por ID
- `POST /api/livro` → Criar novo livro
- `PUT /api/livro/{id}` → Atualizar livro existente
- `DELETE /api/livro/{id}` → Deletar livro
- `GET /api/livro/search?titulo={titulo}` → Buscar livro por título

### 👤 Autores

- `GET /api/autor` → Listar todos os autores (com paginação)
- `GET /api/autor/{id}` → Buscar autor por ID
- `POST /api/autor` → Criar novo autor
- `PUT /api/autor/{id}` → Atualizar autor
- `DELETE /api/autor/{id}` → Deletar autor
- `GET /api/autor/{id}/livros` → Listar livros de um autor

### 🗂 Categorias

- `GET /api/categoria` → Listar categorias
- `POST /api/categoria` → Criar categoria
- `GET /api/categoria/search?idCategoria={id}` → Listar livros por categoria

---

## 📥 Exemplos de Requisição

### Criar Autor

```http
POST /api/autor
Content-Type: application/json

{
  "nome": "Machado de Assis",
  "email": "machado@literatura.com",
  "dataNascimento": "1839-06-21"
}
```

### Criar Categoria

```http
POST /api/categoria
Content-Type: application/json

{
"nome": "Romance"
"descricao": "Livros de Romance"
}
```

### Criar Livro

```http
POST /api/livro
Content-Type: application/json

{
  "titulo": "Dom Casmurro",
  "isbn": "978-85-00-12345-6",
  "anoPublicacao": 1899,
  "idAutor": 1,
  "idCategoria": 2
}
```

### Buscar Livro por Título

```http
GET /api/livro/search?titulo=Dom Casmurro
```

---

## 🌐 URL de Scraping

A API também permite importar livros via scraping da seguinte URL ou de qualquer outra da Livraria Digital da Amazon:

```
https://www.amazon.com.br/Dom-Casmurro-Machado-Assis/dp/859431860X
```

Endpoint correspondente:
```http
POST /api/importar
{
  "url": "https://www.amazon.com.br/Dom-Casmurro-Machado-Assis/dp/859431860X"
}
```

---

## 🧪 Testes da API

Você pode testar a API usando:

- Postman (importando o arquivo `Livros.postman_collection.json`)
- IntelliJ `testes.http`

---

## 🧰 Tecnologias Utilizadas

- Java 17
- Spring Boot (Web, Data JPA)
- WebClient (WebFlux)
- H2
- JSoup (Web Scraping)
- Lombok
- Maven
- Swagger / Springdoc OpenAPI