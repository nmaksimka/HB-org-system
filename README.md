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
- unit tests для auth и групповых бизнес-правил;
- `giftService`: wishlist CRUD, видимость, бронирование и Kafka outbox;
- запрет редактирования чужого и бронирования собственного подарка;
- отдельная `gift_db`;
- `subscriptionService`: подписки на пользователей и группы;
- запрет подписки на себя и активных дублей;
- отдельная `subscription_db` и Kafka outbox;
- `notificationService`: Kafka consumers, read/unread API и WebSocket;
- идемпотентность по `sourceEventId` и отдельная `notification_db`;
- scheduler пользовательских birthday reminders;
- `chatService`: закрытые birthday-чаты, участники и история сообщений;
- JWT/STOMP WebSocket и публикация сообщения только после сохранения;
- запрет доступа именинника и отдельная `chat_db`;
- `fundraiserService`: сборы, участники, суммы и идемпотентная обработка оплат;
- `mockBankService`: мок-коллекции, идемпотентные платежи и `payment.succeeded`;
- `calendarService`: Google/Яндекс-интеграции, AES-GCM шифрование токенов;
- `adminService`: блокировка пользователей, CSV imports и Kafka audit log;
- React web-приложение с адаптивным интерфейсом;
- отдельная PostgreSQL БД и Flyway-миграции для каждого сервиса;
- transactional outbox для критичных Kafka producers.

## Быстрый запуск

```bash
copy .env.example .env
gradlew.bat clean test bootJar
docker compose up --build
```

Web UI: `http://localhost:5173`. Gateway: `http://localhost:6969`.

| Сервис | Порт | PostgreSQL host port |
|---|---:|---:|
| userService | 6700 | 5334 |
| groupService | 6701 | 5336 |
| giftService | 6702 | 5337 |
| subscriptionService | 6703 | 5338 |
| notificationService | 6704 | 5339 |
| chatService | 6705 | 5340 |
| fundraiserService | 6706 | 5341 |
| mockBankService | 6707 | 5342 |
| calendarService | 6708 | 5343 |
| adminService | 6709 | 5344 |

Swagger каждого сервиса: `http://localhost:<порт>/swagger-ui.html`.

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

## Проверка

```bash
gradlew.bat clean test bootJar
npm.cmd --prefix webApp ci
npm.cmd --prefix webApp run build
docker compose config --quiet
```
