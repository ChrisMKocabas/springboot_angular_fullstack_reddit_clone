# Java Spring Boot Backend for Full Stack Reddit Application

### Hosted at : https://zealous-wave-027e5c910.3.azurestaticapps.net

<img width="1715" alt="Screenshot 2023-08-05 at 11 40 36 PM" src="https://github.com/ChrisMKocabas/springboot_angular_fullstack_reddit_clone/assets/75855099/57f74b6a-106a-4654-bb2e-b5d1bc566e41">

#### For frontend code: https://github.com/ChrisMKocabas/springboot_angular_fullstack_reddit_clone_frontend

## Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Security Configuration](#security-configuration)
  - [JWT Security](#jwt-security)
  - [Scheduled Token Cleaning](#scheduled-token-cleaning)
- [Build and Deployment](#build-and-deployment)
  - [Dockerfile](#dockerfile)
- [Models](#models)
- [DTOs](#dtos)
- [Mappers](#mappers)
- [Controller Classes and Routes](#controller-classes-and-routes)
- [Contributing](#contributing)
- [License](#license)


## Overview
This documentation provides a detailed overview of the backend implementation of the Portfolio App, a full-stack web application developed using Java Spring Boot. The backend is responsible for handling various functionalities, including user authentication, subreddit and post management, commenting, voting, and more.

## Tech Stack
The backend of the Portfolio App is built using the following technologies and tools:

- Java: The backend is developed using Java, a widely-used programming language known for its robustness and versatility.
- Spring Boot: The application is built on the Spring Boot framework, which provides a powerful and flexible environment for developing Java-based applications with ease.
- Maven: The project uses Apache Maven for dependency management and project build processes, ensuring a well-structured and manageable codebase.
- Docker: The application is containerized using Docker, allowing for seamless deployment and scalability.
- JWT (JSON Web Token): JWT is utilized for secure authentication and authorization of users, ensuring data integrity and confidentiality.
- Render.com: The application is deployed on Render.com, a platform providing new and experimental container hosting, ensuring reliable performance and security.

## Project Structure
The backend project follows a well-organized package structure to enhance code readability and maintainability. Below is a brief overview of each package:

- **com.chriskocabas.redditclone**: The root package contains the main application class and other general configuration files.
- **com.chriskocabas.redditclone.config**: Contains configuration classes for different aspects of the application, such as CORS and security.
- **com.chriskocabas.redditclone.dto**: Holds Data Transfer Objects (DTOs) used to transfer data between the frontend and backend.
- **com.chriskocabas.redditclone.mapper**: Contains mapper classes responsible for converting entities to DTOs and vice versa.
- **com.chriskocabas.redditclone.model**: Contains entity classes representing the application's data model.
- **com.chriskocabas.redditclone.repository**: Contains repository interfaces responsible for database operations.
- **com.chriskocabas.redditclone.service**: Contains service classes handling business logic and data manipulation.

## Security Configuration
Security is of paramount importance in the Portfolio App. The backend uses Spring Security to secure the endpoints and protect sensitive data. The SecurityConfig class is responsible for defining the security configuration. It includes the following features:

### JWT Security
The application uses a top-notch 2048 RSA private/public key pair for JWT security. These keys are handled with utmost care and are never revealed within the application.yml file. All credentials are referenced from special environment files, ensuring the highest level of security. We are using a top-notch 2048 RSA private/public key pair for JWT security. These keys are handled with utmost care to ensure they are never revealed within the application.yml file. All credentials are referenced from special environment files that were never uploaded anywhere, ensuring the highest level of security.

### Scheduled Token Cleaning
The application includes a scheduled task that runs daily to clean all expired tokens. This task ensures that expired tokens are removed from the system, enhancing security and reducing the risk of unauthorized access.

## Build and Deployment
The application is built using Maven with the command `mvn clean build`, adhering to best practices for managing dependencies and project structure. After a successful build, the application is dockerized using the provided Dockerfile, creating a container image encapsulating the application and its dependencies.

### Dockerfile
```dockerfile
FROM openjdk:17
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target//redditclone-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

The Dockerfile sets up the base image using OpenJDK 17, exposes port 8080 for communication, and adds the Redditclone JAR file to the container. The application is launched with the entry point "java -jar /app.jar".

The docker image is then uploaded to a private docker repository, ensuring controlled access to the image. This private repository prevents unauthorized access and reduces the risk of potential security vulnerabilities.

Finally, the application is deployed on Render.com, a platform providing new and experimental container hosting. Render.com's container hosting offers a secure and reliable environment for running containerized applications, ensuring smooth performance and scalability.

## Models
The backend defines various entity classes that represent the application's data model. These classes are mapped to database tables using JPA (Java Persistence API) annotations. Key entity classes include:

- User: Represents a registered user of the application. It contains user details such as username, password, email, role, etc.

- Subreddit: Represents a community or subreddit in the application. It includes attributes like the subreddit name, description, posts, and user who created it.

- Post: Represents a post created by a user in a subreddit. It contains post details such as the title, URL, description, vote count, etc.

- Comment: Represents a comment made by a user on a post. It stores the comment text, the post it belongs to, the user who posted it, and the creation timestamp.

- Vote: Represents a user's vote (upvote or downvote) on a post. It holds the type of vote, the post voted on, and the user who cast the vote.


## DTOs

Data Transfer Objects (DTOs) are used to facilitate data transfer between the frontend and backend. The backend defines several DTO classes that correspond to entity classes but only contain relevant data for specific operations. For example:

- RegisterRequest: A DTO used for user registration. It contains fields like username, email, and password.

- AuthenticationResponse: A DTO used for returning authentication responses to the frontend. It includes fields like the access token, refresh token, and user details.

- LoginRequest: A DTO used for user login. It contains fields like username and password.

- RefreshTokenRequest: A DTO used for refreshing the access token. It includes the refresh token.

- CommentsDto: A DTO used for handling comments on posts. It contains fields like the comment text, the post ID, and the username of the comment author.

- PostRequest: A DTO used for creating new posts. It contains fields like the subreddit name, post title, URL, and description.

- PostResponse: A DTO used for retrieving post details. It includes information such as title, URL, description, vote count, comment count, etc.

- SubredditDto: A DTO used for subreddit management. It contains fields like subreddit name, description, and the number of posts in the subreddit.

- VoteDto: A DTO used for handling user votes on posts. It contains fields like post ID and vote type (UPVOTE or DOWNVOTE).

## Mappers

Mapper classes play a vital role in converting entity classes to DTOs and vice versa. These classes enable smooth data transfer between the frontend and backend without exposing unnecessary information. Key mapper classes include:

- PostMapper: Responsible for converting Post entities to PostResponse DTOs and vice versa. It handles fields like title, URL, description, vote count, comment count, and various status flags.

- SubredditMapper: Responsible for converting Subreddit entities to SubredditDto DTOs and vice versa. It handles fields like subreddit ID, name, description, number of posts, and duration since creation.

- CommentMapper: Responsible for converting Comment entities to CommentsDto DTOs and vice versa. It handles fields like comment text, creation timestamp, post, and user information.

## Controller Classes and Routes

<img width="2089" alt="Screenshot 2023-08-01 at 11 59 59 AM" src="https://github.com/ChrisMKocabas/springboot_angular_fullstack_reddit_clone/assets/75855099/0292a5bd-4908-42c6-93d2-c08b98b059e1">

Controller classes handle incoming HTTP requests, interact with the appropriate service classes, and return HTTP responses. The backend defines several controller classes, including:

- AuthController: Handles user registration, account verification, login, token refresh, and logout operations. It interacts with the AuthService and RefreshTokenService.

  - Route: /api/auth/signup (POST) - User registration
  - Route: /api/auth/verify/{verificationToken} (GET) - Account verification
  - Route: /api/auth/login (POST) - User login
  - Route: /api/auth/refresh/token (POST) - Token refresh
  - Route: /api/auth/logout (POST) - User logout

- CommentsController: Manages comments on posts. It handles comment creation, retrieval, updating, and deletion. It interacts with the CommentService.

  - Route: /api/comments/ (POST) - Create a new comment
  - Route: /api/comments/{commentId} (GET) - Get a comment by ID
  - Route: /api/comments/{commentId} (PUT) - Update a comment by ID
  - Route: /api/comments/{commentId} (DELETE) - Delete a comment by ID

- PostController: Handles post-related operations, such as creating new posts, retrieving posts, updating post details, deleting posts, and toggling notification status. It interacts with the PostService.

  - Route: /api/posts/ (POST) - Create a new post
  - Route: /api/posts/ (GET) - Get all posts
  - Route: /api/posts/{postId} (GET) - Get a post by ID
  - Route: /api/posts/{postId} (PUT) - Update a post by ID
  - Route: /api/posts/{postId} (DELETE) - Delete a post by ID
  - Route: /api/posts/notifications (POST) - Toggle post notification status

- SubredditController: Manages subreddit-related operations, such as creating new subreddits, retrieving subreddits, updating subreddit details, and deleting subreddits. It interacts with the SubredditService.

  - Route: /api/subreddits/ (POST) - Create a new subreddit
  - Route: /api/subreddits/ (GET) - Get all subreddits
  - Route: /api/subreddits/{subredditId} (GET) - Get a subreddit by ID
  - Route: /api/subreddits/{subredditId} (PUT) - Update a subreddit by ID
  - Route: /api/subreddits/{subredditId} (DELETE) - Delete a subreddit by ID

- VoteController: Handles user votes on posts (upvote or downvote). It interacts with the VoteService.

  - Route: /api/votes/ (POST) - Vote on a post
  - Route: /api/votes/{postId} (GET) - Get the vote status of a post by ID

## Contributing

I welcome contributions to the backend service. To contribute, follow these steps:

1. Fork the repository.
2. Create a new branch for your feature or bug fix.
3. Make your changes and commit them with descriptive commit messages.
4. Push your changes to your forked repository.
5. Submit a pull request to the main repository.

## License

This backend service is released under the MIT License. See [LICENSE](./LICENSE) for more details.
