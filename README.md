# LibCRM

## Система управления библиотекой

## Patterns

### Model

Book - модель книг

Reader - модель чителей

Transaction - модель передачи и возврата книг

### Repository

BookRepositry - хранение книг

ReaderRepository - хранение читателей

TransactionRepository - хранение информации о выдачи книг

### Service

BookRepository - управление книгами

ReaderService - управление читателями

TransactionRepository - управление выдачей книг

### Command

команды для консольного приложения

# Build
```bash
$ ./gradlew clean bootJar
```

# Run

```bash
$ docker compose up -d

$ java -jar build/libs/libcrm-1.0-SNAPSHOT.jar
```

@ Made by Filimonov Artem