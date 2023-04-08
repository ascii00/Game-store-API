# Game Store API

## Description

The Game Store API enables developers to perform essential **CRUD** (Create, Read, Update, and Delete) operations on games, 
game types, and game reviews. Additionally, the API supports user registration and authentication, providing a secure 
environment for users to access and interact with the game store's content.

## Table of Contents

* [Technologies](#technologies)
* [Getting Started](#getting-started)
* [Installation](#installation)
* [Domain Model](#domain-model)
* [API](#api)
* [Authentication](#authentication)
* [Email confirmation](#email-confirmation)
* [Exceptions handling](#exceptions-handling)
* [Testing](#testing)

## Technologies

* Language: **Java 17**
* Framework: **Spring Boot 3.0.5**
* Build Tool: **Maven**
* Database: **MySQL**
* ORM: **Spring Data JPA**
* Security: **Spring Security**
* Authentication & Authorization: **JWT**
* Testing: **JUnit 5, Mockito, Spring Test**
* Logging: **SLF4J**

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Requirements

* Java JDK 17
* Maven 3.8.6
* MySQL 8.0.30

### Installation

1. Clone the repository

```bash
https://github.com/ascii00/Game-store-API.git
```

* Create a database in MySQL server
* Create tables and insert data using the DDL and DML scripts in the resources folder
* Update application.properties file, or add environment variables

  * Database connection, example: <br/>`spring.datasource.url=jdbc:mysql://localhost:3306/{DatabaseName}?useSSL=false&serverTimezone=UTC`
  * Database user, example: <br/>`spring.datasource.username={username}`
  * Database user password, example: <br/>`spring.datasource.password={password}`
  * JWT secret key (at least 256 bit), example: <br/>`jwt.secret=58703273357638792F423F4428472B4B6250655368566D597133743677397A24`
  * if you want to send confirmation emails set email.sending.required to TRUE and provide your SendGrid API key and "email from" address

* After the following steps you are good to go.

## Domain Model

The **Game** table includes information about each game such as its name, description, price, and **game type**. **Reviews** are 
associated with specific games and include a rating and description. **Roles** are used to assign permissions to users, 
and the **User_Role** table associates users with their roles. **Tokens** are used for user authentication and include information 
such as the token string, type, and whether it has been revoked or expired.

![Domain Model](https://i.ibb.co/nj39wvd/Game-store-2023-04-08-14-50.png)

## API

### Game
| Request                          | Description | Access | Body                                    |
|----------------------------------| --- | --- |-----------------------------------------|
| `GET /api/v1/game/all`           | Retrieves a list of all games | everyone | -                                       |
| `GET /api/v1/game/byId/:id`      | Retrieves a game by its id | everyone | -                                       |
| `GET /api/v1/game/byName/:name`  | Retrieves a game by its name | everyone | -                                       |
| `PUT /api/v1/game/:id`           | Updates a game by its id | admin | ![json](https://i.ibb.co/Fht4C3M/1.png) |
| `POST /api/v1/game`              | Creates a new game | admin | ![json](https://i.ibb.co/9hNYDzb/1.png) |
| `DELETE /api/v1/game/:id`        | Deletes a game by its id | admin | -                                       |

### Game Type
| Request                          | Description | Access | Body                                    |
|----------------------------------| --- | --- |-----------------------------------------|
| `GET /api/v1/gameType/all`       | Retrieves a list of all game types | everyone | -                                       |
| `GET /api/v1/gameType/:id`       | Retrieves a game type by its id | everyone | -                                       |
| `POST /api/v1/gameType/:id`      | Creates a new game type | admin | ![json](https://i.ibb.co/Kx0FGPW/1.png) |
| `DELETE /api/v1/gameType/:id`    | Deletes a game type by its id | admin | -                                       |

### Game Review
| Request                          | Description | Access | Body                                    |
|----------------------------------| --- | --- |-----------------------------------------|
| `GET /api/v1/review`             | Retrieves a list of all game reviews | everyone | -                                       |
| `POST /api/v1/review`            | Creates a new game review | authenticated user | ![json](https://i.ibb.co/3Sbdy6z/1.png) |
| `DELETE /api/v1/review/:id`      | Deletes a game review by its id | admin | -                                       |
| `PUT /api/v1/review/:id`         | Updates a game review by its id | admin | ![json](https://i.ibb.co/Gn2Ps0W/1.png) |

### User
| Request                          | Description | Access | Body                                    |
|----------------------------------| --- | --- |-----------------------------------------|
| `POST /api/v1/auth/register`     | Registers a new user | everyone | ![json](https://i.ibb.co/MVYknyY/1.png) |
| `POST /api/v1/auth/authenticate` | Authenticates a user | everyone | ![json](https://i.ibb.co/MVYknyY/1.png) |
| `GET /api/v1/auth/logout`        | Logs out a user | authenticated user | -                                       |

### Success Response
All went well, and (usually) some data was returned.
```json
{
  "status": "success",
  "data": {
    "id": 1,
    "name": "Witcher 3",
    "description": "An action-packed open-world game.",
    "price": 24.99,
    "gameType": {
      "id": 1,
      "name": "RPG"
    },
    "reviews": []
  }
}
```

### Fail Response
There was a problem with the data submitted, or some pre-condition of the API call wasn't satisfied
```json
{
"status": "fail",
"data": "Password is not valid (Password must be at least 8 characters long, contain at least one number, one uppercase and one lowercase letter)"
}
```

### Error Response
An error occurred in processing the request, i.e. an exception was thrown
```json
{
"status": "error",
"data": "Internal server error"
}
```

## Authentication

The Game Store API uses JWT for authentication and authorization. The API supports user registration and authentication.
After a successful authentication or registration, the API returns a JWT token that is used for authorization. The token is sent in the 
Authorization header of each request. The API uses Spring Security to protect the endpoints. The API supports the following 
roles: USER and ADMIN. The USER role is assigned to all users by default. After logging out, the token is revoked and can no longer be used.

Whole process of authentication is described below:
![Authentication](https://i.ibb.co/C2bD1FZ/img-auth.png)

## Email confirmation

The Game Store API uses SendGrid for sending emails. After a successful registration, the API sends an email to the user
with a confirmation link. The user can confirm his email by clicking on the link. After clicking on the link, the user's email is confirmed.

## Exceptions handling

The Game Store API uses Spring Boot exception handling to handle exceptions. The API returns 500 code for internal 
server errors, 400 code for bad requests, and 401 code for unauthorized requests. Each exception is logged to the application.log file.

## Testing

The Game Store API uses JUnit 5, Mockito, and Spring Test for testing. It is covered with unit tests for services, and 
integration tests for controllers.

