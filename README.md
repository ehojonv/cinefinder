# CineFinder - Documentação Técnica Sprint 2
## Challenge Oracle - Java Advanced

---

## 📋 Informações do Projeto

**Nome da Aplicação:** CineFinder  
**Objetivo:** Sistema de descoberta e avaliação de filmes com recomendações personalizadas  
**Tecnologias:** Java 17, Spring Boot 3.5.7, Oracle Database, JPA/Hibernate  
**Sprint:** 2/4 - Segundo Semestre 2025

---

## 👥 Integrantes do Grupo

| Nome | RM |
|------|-----|
| Felipe Anselmo | 560661 |
| João Vinicius Alves | 559369 |
| Matheus Mariotto | 560276 |

---

## 🎯 Proposta Tecnológica

### Problema Identificado
No cenário atual de streaming, usuários enfrentam dificuldades para:
- Descobrir filmes relevantes em meio a milhares de opções
- Encontrar avaliações confiáveis de outros espectadores
- Organizar e compartilhar listas personalizadas de filmes
- Tomar decisões informadas sobre o que assistir

### Solução CineFinder
Uma plataforma centralizada que permite:
- **Avaliações Detalhadas:** Sistema de reviews com notas, comentários e localização
- **Organização Inteligente:** Flows (listas personalizadas) para categorizar filmes
- **Classificação por Gêneros:** Sistema flexível de múltiplos gêneros por filme
- **Cálculo Automático de Rating:** Média ponderada baseada em todas as avaliações

### Público-Alvo
- **Primário:** Cinéfilos e entusiastas de cinema (18-45 anos)
- **Secundário:** Usuários casuais de streaming buscando recomendações
- **Terciário:** Críticos e profissionais da indústria cinematográfica

---

### Diagrama de Classes de Entidade

![diagrama](/out/output/cinefinder/diagramaDeClasse.png)


### Relacionamentos e Cardinalidades

| Relação | Cardinalidade | Descrição | Constraint |
|---------|---------------|-----------|------------|
| User → Review | 1:N | Um usuário pode criar várias reviews | ON DELETE CASCADE |
| User → Flow | 1:N | Um usuário pode criar vários flows | ON DELETE CASCADE |
| Movie → Review | 1:N | Um filme pode ter várias reviews | ON DELETE CASCADE |
| Movie ↔ Genre | N:M | Um filme pode ter vários gêneros e vice-versa | Tabela Intermediária |
| Flow ↔ Movie | N:M | Um flow pode conter vários filmes e vice-versa | Tabela Intermediária |

### Justificativas de Design

**Por que Clean Architecture?**
- **Separação de Responsabilidades:** Cada camada tem um propósito específico
- **Testabilidade:** Facilita testes unitários e de integração
- **Manutenibilidade:** Mudanças em uma camada não afetam outras
- **Escalabilidade:** Permite crescimento organizado do sistema

**Por que JPA/Hibernate?**
- **Abstração do Banco:** Permite trocar de SGBD com facilidade
- **Mapeamento Objeto-Relacional:** Trabalhar com objetos Java ao invés de SQL
- **Otimizações Automáticas:** Lazy loading, cache de segundo nível
- **Validações:** Integração com Bean Validation

---

## 🔧 Implementação das Entidades (40 pontos)

### Evolução desde a Sprint 1

#### Melhorias Implementadas:
1. **Refatoração de DTOs:** Separação clara entre Input DTOs e Output DTOs
2. **Validações Aprimoradas:** Validador customizado de senha com @Password
3. **Cálculo Automático de Rating:** Método `calculateRating()` na entidade Movie
4. **Relacionamentos Bidirecionais:** Métodos auxiliares para manter consistência
5. **Normalização de Dados:** Método `normalizeName()` em Genre

### Entidades Implementadas

#### 1. AppUser (Usuário)

```java
@Entity
@Table(name = "cf_user")
public class AppUser {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    
    private String username;
    private String email;
    private String password; // BCrypt encrypted
    private LocalDate dateOfBirth;
    
    @OneToMany(mappedBy = "author")
    private List<Review> reviews;
    
    @OneToMany(mappedBy = "author")
    private List<Flow> flows;
}
```

**Validações:**
- `username`: NotBlank
- `email`: Email válido
- `password`: Custom @Password (mínimo 8 chars, 1 maiúscula, 1 número)
- `dateOfBirth`: Data passada

#### 2. Movie (Filme)

```java
@Entity
@Table(name = "cf_movie")
public class Movie {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    
    private String title;
    private String synopsis;
    private LocalDate releaseDate;
    
    @Column(columnDefinition = "NUMERIC(4,2) DEFAULT 0")
    private Double rating;
    
    @ManyToMany
    @JoinTable(name = "cf_movie_genres")
    private List<Genre> genres;
    
    @OneToMany(mappedBy = "movie", cascade = CascadeType.MERGE)
    private List<Review> reviews;
    
    public void calculateRating() {
        this.reviews.stream()
            .filter(r -> r.getRate() != null && r.getRate() >= 0)
            .mapToDouble(Review::getRate)
            .average()
            .ifPresentOrElse(
                avg -> this.rating = new BigDecimal(avg)
                    .setScale(2, RoundingMode.CEILING).doubleValue(),
                () -> this.rating = 0.0
            );
    }
}
```

**Validações:**
- `title`: NotBlank
- `synopsis`: NotBlank, tamanho entre 10-2000 chars
- `releaseDate`: PastOrPresent
- `rating`: Auto-calculado (0.0-10.0)

#### 3. Review (Avaliação)

```java
@Entity
@Table(name = "cf_review")
public class Review {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    
    private String title;
    private String comments;
    private Double rate;
    private String localization;
    
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "movie_id")
    private Movie movie;
    
    @ManyToOne
    @JoinColumn(name = "author_id")
    private AppUser author;
    
    public void associateToMovie(Movie movie) {
        this.movie = movie;
        movie.addReview(this);
        movie.calculateRating();
    }
}
```

**Validações:**
- `title`: NotBlank
- `comments`: Máximo 2000 chars
- `rate`: NotNull, entre 0.0-10.0
- `localization`: NotBlank

#### 4. Genre (Gênero)

```java
@Entity
@Table(name = "cf_genres")
public class Genre {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    
    private String name;
    
    @ManyToMany(mappedBy = "genres")
    private List<Movie> movies;
    
    public String normalizeName() {
        return this.name.trim();
    }
}
```

#### 5. Flow (Lista Personalizada)

```java
@Entity
@Table(name = "cf_flow")
public class Flow {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    
    private String title;
    
    @ManyToOne
    private AppUser author;
    
    @ManyToMany
    @JoinTable(name = "cf_flow_movies")
    private List<Movie> movies;
}
```

### Mapeamento Objeto-Relacional

**Estratégias Utilizadas:**
- **GenerationType.IDENTITY:** Para auto-incremento de IDs
- **CascadeType.MERGE:** Para propagar atualizações
- **FetchType.LAZY:** (padrão) Para otimizar consultas
- **@JoinTable:** Para relacionamentos N:M

**Constraints Implementadas:**
- Primary Keys em todas as entidades
- Foreign Keys com integridade referencial
- Unique constraints em campos específicos
- Check constraints via validações Java

---

## 🌐 d) API RESTful - Nível de Maturidade 3 (15 pontos)

### Princípios REST Implementados

A API CineFinder segue os **princípios RESTful de Roy Fielding**:

1. **Identificação de Recursos:** URIs claras e consistentes
2. **Manipulação via Representações:** JSON como formato padrão
3. **Mensagens Auto-descritivas:** Headers HTTP apropriados
4. **HATEOAS:** Hypermedia as the Engine of Application State

### Modelo de Maturidade Richardson - Nível 3

#### Nível 0 ❌: POX (Plain Old XML)
- Não aplicável

#### Nível 1 ✅: Recursos
- `/users`, `/movies`, `/reviews`, `/genres`, `/flows`

#### Nível 2 ✅: Verbos HTTP
- GET (consulta), POST (criação), PUT (atualização), DELETE (exclusão)

#### Nível 3 ✅: HATEOAS
- Links de navegação em todas as respostas
- Descoberta de recursos relacionados
- Self-links para identificação

### Implementação HATEOAS

#### Exemplo - UserService

```java
private static EntityModel<GetUserDto> toModel(AppUser user) {
    var resource = EntityModel.of(GetUserDto.fromAppUser(user));
    
    resource.add(
        linkTo(methodOn(AppUserController.class)
            .getUserById(user.getId()))
            .withSelfRel(),
        linkTo(methodOn(AppUserController.class)
            .getAllUsers(null, Pageable.unpaged()))
            .withRel("all-users")
    );
    
    return resource;
}
```

#### Resposta JSON com HATEOAS

```json
{
  "id": 1,
  "username": "johndoe",
  "email": "john@example.com",
  "birthDate": "1990-05-15",
  "_links": {
    "self": {
      "href": "http://localhost:8080/users/1"
    },
    "all-users": {
      "href": "http://localhost:8080/users"
    }
  }
}
```

### Paginação e Filtros

#### Parâmetros de Paginação
- `page`: Número da página (default: 0)
- `size`: Tamanho da página (default: 10)
- `sort`: Campo e direção (ex: `id,DESC`)

#### Filtros Implementados

**UserFilter:**
- `username`: Busca parcial (case-insensitive)
- `minAge`: Idade mínima
- `maxAge`: Idade máxima

**ReviewFilter:**
- `title`: Busca parcial no título
- `username`: Autor da review
- `minRating`: Nota mínima
- `maxRating`: Nota máxima
- `localization`: Localização
- `movieTitle`: Título do filme

**GenreFilter:**
- `name`: Nome do gênero
- `moviesIds`: IDs de filmes

#### Exemplo de Uso

```
GET /reviews?username=john&minRating=8&page=0&size=20&sort=rate,DESC
```

---

## 📄 f) README.md do Projeto (10 pontos)

### Conteúdo Completo

```markdown
# 🎬 CineFinder

Sistema de descoberta e avaliação de filmes com recomendações personalizadas

## 👥 Integrantes

- **[Seu Nome]** (RM [RM]) - Backend Development & Database
- **[Nome 2]** (RM [RM]) - API Development & HATEOAS
- **[Nome 3]** (RM [RM]) - Documentation & Testing

## 🎯 Proposta Tecnológica

CineFinder é uma plataforma que resolve o problema da sobrecarga de opções em serviços de streaming, oferecendo:

- Sistema robusto de avaliações com notas e comentários
- Organização personalizada através de Flows (listas)
- Classificação inteligente por múltiplos gêneros
- Cálculo automático de ratings baseado em reviews

**Público-Alvo:** Cinéfilos, usuários de streaming e críticos de cinema

## 🚀 Como Rodar a Aplicação

### Pré-requisitos

- Java 17+
- Maven 3.8+
- Oracle Database (ou utilizar Oracle Cloud Free Tier)

### Configuração

1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/cinefinder.git
cd cinefinder
```

2. Configure o banco de dados em `application.properties`:
```properties
spring.datasource.url=jdbc:oracle:thin:@seu-host:1521:ORCL
spring.datasource.username=seu-usuario
spring.datasource.password=sua-senha
```

3. Execute a aplicação:
```bash
mvn spring-boot:run
```

4. Acesse: `http://localhost:8080`

## 📊 Diagramas

### Diagrama de Classes
![Diagrama de Classes](docs/diagrams/class-diagram.png)

### Diagrama Entidade-Relacionamento
![DER](docs/diagrams/er-diagram.png)

## 📡 Endpoints da API

### Users
- `GET /users` - Lista todos os usuários (com filtros)
- `GET /users/{id}` - Busca usuário por ID
- `POST /users` - Cria novo usuário
- `PUT /users/{id}` - Atualiza usuário
- `DELETE /users/{id}` - Remove usuário

### Movies
- `GET /movies` - Lista todos os filmes
- `GET /movies/{id}` - Busca filme por ID
- `POST /movies` - Cria novo filme
- `PUT /movies/{id}` - Atualiza filme
- `DELETE /movies/{id}` - Remove filme

### Reviews
- `GET /reviews` - Lista todas as reviews (com filtros)
- `GET /reviews/{id}` - Busca review por ID
- `POST /reviews` - Cria nova review
- `PUT /reviews/{id}` - Atualiza review
- `DELETE /reviews/{id}` - Remove review

### Genres
- `GET /genres` - Lista todos os gêneros (com filtros)
- `GET /genres/{id}` - Busca gênero por ID
- `POST /genres` - Cria novo gênero
- `PUT /genres/{id}` - Atualiza gênero
- `DELETE /genres/{id}` - Remove gênero

### Flows
- `GET /flows` - Lista todos os flows
- `GET /flows/{id}` - Busca flow por ID
- `POST /flows` - Cria novo flow
- `PUT /flows/{id}` - Atualiza flow
- `DELETE /flows/{id}` - Remove flow

## 🎥 Vídeo de Apresentação

[Link para o vídeo no YouTube](seu-link-aqui)

O vídeo demonstra:
- Proposta tecnológica e público-alvo
- Arquitetura da aplicação
- Demonstração de todos os endpoints
- HATEOAS em funcionamento
- Filtros e paginação
- Evolução desde Sprint 1

## 🛠️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.5.7**
- **Spring Data JPA**
- **Spring HATEOAS**
- **Oracle Database**
- **Lombok**
- **Bean Validation**
- **BCrypt** (criptografia de senhas)

## 📚 Documentação Adicional

- [Documentação Sprint 1](docs/Sprint1_Documentation.pdf)
- [Documentação Sprint 2](docs/Sprint2_Documentation.pdf)
- [Collection Postman](postman/CineFinder_API.postman_collection.json)
```

---

### Validação de Persistência

Todos os testes foram realizados com persistência no Oracle Database:

1. **Criação:** Dados inseridos com sucesso
2. **Leitura:** Dados recuperados corretamente
3. **Atualização:** Modificações refletidas no banco
4. **Deleção:** Registros removidos com integridade referencial
5. **Relacionamentos:** Associações N:M e 1:N funcionando

---

## 🎯 Evolução desde Sprint 1

### Principais Melhorias

1. **HATEOAS Completo (Nível 3 Richardson)**
   - Todos os services retornam EntityModel
   - Links de navegação self e collection
   - Cliente pode descobrir recursos dinamicamente

2. **Sistema de Filtros**
   - UserFilter (username, idade)
   - ReviewFilter (título, autor, rating, localização, filme)
   - GenreFilter (nome, filmes)
   - Implementado com Spring Data Specifications

3. **Refatoração de DTOs**
   - Separação clara: Input DTOs vs Output DTOs
   - GetUserDto, GetMovieDto, GetReviewDto, GetGenreDto
   - MovieRefDto para referências leves

4. **Validações Customizadas**
   - @Password: Validador de senha forte
   - Regex: Mínimo 8 chars, 1 maiúscula, 1 número
   - Mensagens de erro personalizadas

5. **Cálculo Automático de Rating**
   - Método `calculateRating()` otimizado
   - BigDecimal para precisão
   - Atualização em cascata via associação

6. **Tratamento de Exceções**
   - GlobalExceptionHandler
   - Respostas padronizadas com timestamp e path
   - Validação com mensagens detalhadas

### Comparativo Sprint 1 vs Sprint 2

| Aspecto | Sprint 1 | Sprint 2 |
|---------|----------|----------|
| Maturidade REST | Nível 1 | Nível 3 (HATEOAS) |
| Filtros | Não implementado | 3 filtros completos |
| DTOs | Misturados | Separados (In/Out) |
| Validações | Básicas | Customizadas |
| Rating | Manual | Automático |
| Paginação | Simples | Completa (sort, size) |

---

## 🔍 Detalhes Técnicos Adicionais

### Padrões de Projeto Utilizados

1. **Repository Pattern**
   - Abstração do acesso a dados
   - Interfaces JpaRepository + JpaSpecificationExecutor

2. **DTO Pattern**
   - Transferência de dados entre camadas
   - Proteção de entidades internas
   - Controle de serialização JSON

3. **Builder Pattern**
   - Construção de entidades complexas
   - Lombok @Builder para código limpo

4. **Specification Pattern**
   - Filtros dinâmicos e compostos
   - Reutilização de critérios de busca

5. **Service Layer Pattern**
   - Lógica de negócio isolada
   - Transações gerenciadas

### Segurança Implementada

#### Criptografia de Senhas
```java
@Bean
PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}

// No service
user.setPassword(encoder.encode(nUser.password()));
```

#### Validação de Senha Forte
```java
@Password
private String password;

// Regex: ^(?=.*[A-Z])(?=.*\d).{8,}$
// Requer: 8+ chars, 1 maiúscula, 1 número
```

#### Configuração de Segurança
```java
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .csrf(csrf -> csrf.disable());
        return http.build();
    }
}
```

*Nota: Segurança básica para desenvolvimento. OAuth2 será implementado na Sprint 3.*

### Performance e Otimizações

1. **Lazy Loading**
   - Relacionamentos carregados sob demanda
   - Evita N+1 queries

2. **Cascade Operations**
   - CascadeType.MERGE em relacionamentos críticos
   - Propagação eficiente de atualizações

3. **Indexação**
   - IDs como Primary Keys indexados
   - Foreign Keys com índices automáticos

4. **Connection Pool**
   - HikariCP (padrão Spring Boot)
   - Reutilização de conexões

### Tratamento de Erros

#### Exception Handler Global
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestError> handleValidation(
        MethodArgumentNotValidException e) {
        
        var errors = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(f -> f.getField() + ": " + f.getDefaultMessage())
            .collect(Collectors.toList());
            
        return ResponseEntity
            .status(BAD_REQUEST)
            .body(new RestError(errors, 400, now(), path));
    }
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<RestError> handleRuntime(
        RuntimeException e) {
        
        return ResponseEntity
            .status(NOT_FOUND)
            .body(new RestError(
                List.of(e.getMessage()),
                404,
                now(),
                path
            ));
    }
}
```

#### Exemplo de Resposta de Erro
```json
{
  "messages": [
    "password: must match ^(?=.*[A-Z])(?=.*\\d).{8,}$"
  ],
  "status": 400,
  "timestamp": "2025-11-06T14:30:00",
  "path": "/users"
}
```

---

## 📊 Endpoints Detalhados com Exemplos

### 1. Users Endpoints

#### GET /users
**Descrição:** Lista todos os usuários com paginação e filtros

**Parâmetros:**
- `username` (optional): Busca parcial por nome de usuário
- `minAge` (optional): Idade mínima
- `maxAge` (optional): Idade máxima
- `page` (optional): Número da página (default: 0)
- `size` (optional): Tamanho da página (default: 10)
- `sort` (optional): Campo e direção (ex: username,ASC)

**Exemplo Request:**
```
GET /users?username=john&minAge=25&page=0&size=5
```

**Exemplo Response:**
```json
{
  "content": [
    {
      "id": 1,
      "username": "johndoe",
      "email": "john@example.com",
      "birthDate": "1990-05-15",
      "_links": {
        "self": {
          "href": "http://localhost:8080/users/1"
        },
        "all-users": {
          "href": "http://localhost:8080/users"
        }
      }
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 5
  },
  "totalElements": 1,
  "totalPages": 1
}
```

#### POST /users
**Descrição:** Cria novo usuário

**Request Body:**
```json
{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "Senha123",
  "dateOfBirth": "1990-05-15"
}
```

**Response:** 201 Created com EntityModel

#### PUT /users/{id}
**Descrição:** Atualiza usuário existente

**Request Body:**
```json
{
  "username": "johnupdated",
  "email": "john.updated@example.com",
  "password": "NovaSenha456",
  "dateOfBirth": "1990-05-15"
}
```

**Response:** 200 OK com EntityModel atualizado

#### DELETE /users/{id}
**Descrição:** Remove usuário

**Response:** 204 No Content

---

### 2. Movies Endpoints

#### GET /movies
**Descrição:** Lista todos os filmes com paginação

**Exemplo Response:**
```json
{
  "content": [
    {
      "id": 1,
      "title": "Inception",
      "synopsis": "A thief who steals corporate secrets...",
      "releaseDate": "2010-07-16",
      "rating": 8.8,
      "genres": ["Action", "Sci-Fi"],
      "numberOfReviews": 2,
      "_links": {
        "self": {
          "href": "http://localhost:8080/movies/1"
        },
        "all-movies": {
          "href": "http://localhost:8080/movies"
        }
      }
    }
  ]
}
```

#### POST /movies
**Request Body:**
```json
{
  "title": "The Matrix",
  "synopsis": "A computer hacker learns about the true nature of reality...",
  "releaseDate": "1999-03-31",
  "genresIds": [1, 2]
}
```

---

### 3. Reviews Endpoints

#### GET /reviews
**Descrição:** Lista reviews com filtros avançados

**Parâmetros:**
- `title`: Busca no título da review
- `username`: Busca por autor
- `minRating`: Nota mínima
- `maxRating`: Nota máxima
- `localization`: Localização
- `movieTitle`: Título do filme

**Exemplo Request:**
```
GET /reviews?username=john&minRating=8&movieTitle=inception
```

**Exemplo Response:**
```json
{
  "content": [
    {
      "id": 1,
      "title": "Amazing Movie!",
      "comments": "One of the best sci-fi movies ever made",
      "movie": {
        "id": 1,
        "title": "Inception",
        "rating": 8.8
      },
      "author": {
        "id": 1,
        "username": "johndoe",
        "email": "john@example.com",
        "birthDate": "1990-05-15"
      },
      "localization": "USA",
      "rate": 9.5,
      "_links": {
        "self": {
          "href": "http://localhost:8080/reviews/1"
        },
        "all-reviews": {
          "href": "http://localhost:8080/reviews"
        }
      }
    }
  ]
}
```

#### POST /reviews
**Request Body:**
```json
{
  "title": "Great movie!",
  "comments": "Loved every minute of it",
  "rate": 9.0,
  "localization": "Brazil",
  "authorId": 1,
  "movieId": 1
}
```

**Efeito Colateral:** O rating do filme é automaticamente recalculado

---

### 4. Genres Endpoints

#### GET /genres
**Parâmetros:**
- `name`: Busca parcial por nome
- `moviesIds`: Filtra por filmes associados

**Exemplo Response:**
```json
{
  "content": [
    {
      "id": 1,
      "name": "Action",
      "movie": [
        {
          "id": 1,
          "title": "Inception",
          "rating": 8.8
        },
        {
          "id": 2,
          "title": "The Matrix",
          "rating": 8.7
        }
      ],
      "_links": {
        "self": {
          "href": "http://localhost:8080/genres/1"
        },
        "all-genres": {
          "href": "http://localhost:8080/genres"
        }
      }
    }
  ]
}
```

#### POST /genres
**Request Body:**
```json
{
  "name": "Horror",
  "moviesIds": [3, 4, 5]
}
```

---

### 5. Flows Endpoints

#### GET /flows
**Descrição:** Lista flows (listas personalizadas)

**Exemplo Response:**
```json
{
  "content": [
    {
      "id": 1,
      "title": "My Sci-Fi Favorites",
      "author": {
        "id": 1,
        "username": "johndoe"
      },
      "movies": [
        {
          "id": 1,
          "title": "Inception",
          "rating": 8.8
        },
        {
          "id": 2,
          "title": "The Matrix",
          "rating": 8.7
        }
      ],
      "_links": {
        "self": {
          "href": "http://localhost:8080/flows/1"
        },
        "all-flows": {
          "href": "http://localhost:8080/flows"
        }
      }
    }
  ]
}
```

#### POST /flows
**Request Body:**
```json
{
  "title": "Weekend Watch List",
  "authorId": 1,
  "movieIds": [1, 2, 3]
}
```

---

## 🎬 Video Demonstração
[Video Cinefinder](https://www.youtube.com/watch?v=NYCNEdujqZQ)

---