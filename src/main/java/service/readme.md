# Service Layer

The service layer in this project is responsible for the business logic and interacts with the data access layer to perform CRUD operations.

## Structure

The service layer is divided into several packages:

- `emailService`: Handles email verification functionality.
- `fixtureService`: Contains services related to fixtures.
- `tokenService`: Manages the creation, encryption, and storage of JWT and refresh tokens.
- `userService`: Handles user-related operations and is further divided into:
    - `privateRequests`: Contains services for operations that require user authentication.
    - `publicRequests`: Contains services for operations that do not require user authentication.

## Key Classes

- `RefreshTokenService`: Handles the generation, encryption, and storage of refresh tokens. It also provides functionality to delete all refresh tokens for a specific user and retrieve a valid refresh token for a user.
- `UserLoginImpl`: Implements the `UserLogin` interface and contains the logic for user login.
- `UserCreationImpl`: Implements the `UserCreation` interface and contains the logic for user registration.

## Usage

Each service class provides methods that perform specific operations related to their function. For example, the `RefreshTokenService` provides methods to generate a new refresh token, encrypt and save the token to the database, delete all refresh tokens for a user, and retrieve a valid refresh token for a user.

## Dependencies

The service layer depends on the data access layer for database operations and the utility classes in the `common` package for various functionalities like token generation and encryption.

## Testing

Unit tests for the service layer are located in the `test/java/service` directory. Each service class has a corresponding test class that tests its methods.
(TODO!)