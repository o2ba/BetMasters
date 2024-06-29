# User Registration Service Documentation

## Overview

The User Registration Service (`RegisterServiceImpl`) manages the process of registering new users in the system. It validates user input, ensures age eligibility, stores user data securely in the database, and issues JWT tokens for authenticated access.

## Functionality

### User Registration Process

1. **Input Validation**: Validates user-provided details such as name, email, password, and date of birth to ensure they meet specified criteria.

2. **Age Validation**: Checks that the user meets the minimum age requirement configured in the system.

3. **Database Persistence**: Stores validated user information securely in the database, recording the creation timestamp.

4. **JWT Token Generation**: Generates a JWT (JSON Web Token) for the registered user to facilitate secure authentication and authorization.

### Security Features

- **Sensitive Data Handling**: Utilizes `SensitiveData` class for securely managing password information.

- **Error Handling**: Implements robust error handling mechanisms to manage exceptions such as duplicate email, invalid age, and unhandled errors during token generation.

- **JWT Token Security**: Ensures JWT tokens are securely generated and encrypted to prevent tampering and unauthorized access.

- **Configuration Management**: Uses Spring `@Value` annotations to configure parameters like JWT token lifetime and minimum user age, ensuring flexibility and security through centralized configuration management.

- **Logging**: Utilizes SLF4J (`LoggerFactory.getLogger`) for logging critical events and errors related to user registration processes.

## Components

### `RegisterServiceImpl`

- **Dependencies**:
    - `JwtTokenService`: Handles JWT token generation and encryption.
    - `RegisterServiceDb`: Interface for database operations related to user registration.
    - `Validation`: Performs input validation checks.

- **Methods**:
    - `addUser`: Initiates the user registration process, validating inputs, checking age eligibility, persisting data, and generating a JWT token upon successful registration.

### `RegisterServiceDb`

- **Interface Purpose**: Defines database operations for storing user registration data securely.

- **Methods**:
    - `addUser`: Persists user details (first name, last name, email, password, date of birth) in the database, ensuring data integrity and security.

### Annotations

- `@Service`, `@Component`: Spring annotations indicating that `RegisterServiceImpl` and `RegisterServiceDb` are managed components within the Spring application context.

- `@DatabaseOperation`: Custom annotation indicating that the `addUser` method performs database operations.

## Exception Handling

- **Handled Exceptions**:
    - `DuplicateEmailException`: Thrown if the email provided during registration already exists in the database.
    - `InvalidAgeException`: Raised when the user's age does not meet the minimum age requirement.
    - `InvalidInputException`: Indicates invalid input data (e.g., invalid name format, email format, or password complexity).
    - `UnhandledErrorException`: Captures unexpected errors during JWT token generation.

- **Logging**: Utilizes SLF4J logging to record errors and events, ensuring visibility into registration process issues.

## Configuration

- **Properties**:
    - `jwt.token.lifetime`: Configures the lifetime of JWT tokens issued upon successful registration.
    - `user.minimum.age`: Defines the minimum age required for users to register.
