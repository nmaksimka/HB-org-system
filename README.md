# Birthday Celebration System

Микросервисная система для напоминаний о днях рождения, wishlist, групп и
закрытого обсуждения подарков.

Для сокращения шаблонного Java-кода во всех модулях подключены Lombok и
MapStruct: entity используют Lombok, преобразования DTO выполняются
генерируемыми Spring mapper-компонентами MapStruct.

## Реализовано

- Gradle multi-project;
- общие API/event contracts;
- API Gateway с маршрутизацией и correlation ID;
- `userService`: регистрация, логин, JWT, профиль и internal user API;
- отдельные DTO для собственного и публичного профиля (email не раскрывается);
- отдельная PostgreSQL БД и Flyway-миграции пользователя;
- `groupService`: создание и просмотр групп, вступление, выход и участники;
- роли участников `OWNER / ADMIN / MEMBER`;
- синхронная проверка пользователей через защищённый OpenFeign internal API;
- Swagger/OpenAPI;
- отдельные `user_db` и `group_db`;
- Docker healthchecks и упорядоченный запуск сервисов;
- unit tests для auth и групповых бизнес-правил.
- `giftService`: wishlist CRUD, видимость, бронирование и Kafka outbox;
- запрет редактирования чужого и бронирования собственного подарка;
- отдельная `gift_db`.
- `subscriptionService`: подписки на пользователей и группы;
- запрет подписки на себя и активных дублей;
- отдельная `subscription_db` и Kafka outbox.
- `notificationService`: Kafka consumers, read/unread API и WebSocket;
- идемпотентность по `sourceEventId` и отдельная `notification_db`;
- scheduler пользовательских birthday reminders.
- `chatService`: закрытые birthday-чаты, участники и история сообщений;
- JWT/STOMP WebSocket и публикация сообщения только после сохранения;
- запрет доступа именинника и отдельная `chat_db`.

Следующие этапы: `fundraiserService` и `mockBankService`.

## Быстрый запуск

```bash
copy .env.example .env
gradlew.bat clean test bootJar
docker compose up --build
```

Gateway: `http://localhost:1488`. Swagger user service:
`http://localhost:6700/swagger-ui.html`; group service:
`http://localhost:6701/swagger-ui.html`; gift service:
`http://localhost:6702/swagger-ui.html`; subscription service:
`http://localhost:6703/swagger-ui.html`; notification service:
`http://localhost:6704/swagger-ui.html`; chat service:
`http://localhost:6705/swagger-ui.html`.

## Примеры

```http
POST /api/v1/auth/register
Content-Type: application/json

{
  "email": "max@example.com",
  "username": "maksim",
  "password": "password123",
  "firstName": "Максим",
  "lastName": "Никонов",
  "birthDate": "2008-04-15"
}
```

Все защищённые запросы используют `Authorization: Bearer <token>`.
