# Birthday Celebration System

Проект развивается как набор независимых Spring Boot микросервисов.

## Обязательные правила

- Один сервис обслуживает одну бизнес-зону и владеет отдельной БД.
- Сервис не читает БД другого сервиса и не создаёт FK на его таблицы.
- Синхронные проверки выполняются через REST/OpenFeign, события и фоновые
  действия — через Kafka.
- Внешний трафик проходит через API Gateway. Gateway валидирует JWT,
  добавляет `X-Correlation-Id` и не содержит бизнес-логики.
- Controller принимает и возвращает DTO, вызывает service и не обращается
  к repository/Kafka напрямую.
- Service содержит бизнес-правила и границы транзакций.
- Entity не возвращается из API. Преобразование выполняет mapper.
- Каждая БД версионируется собственными неизменяемыми Flyway-миграциями.
- Идентификатор текущего пользователя берётся из проверенного JWT/Gateway
  headers, а не из request body.
- Пароли хранятся только как BCrypt hash; секреты задаются через env.
- Kafka events называются в прошедшем времени, содержат metadata и
  `correlationId`. Для надёжной публикации предпочтителен outbox.
- Все сервисы возвращают единый формат API errors и имеют unit/integration
  tests для бизнес-правил.

## Слои сервиса

`Controller -> Service -> Repository -> Entity`, при этом API использует
`DTO <-> Mapper`. Интеграции размещаются в `client`, события — в `kafka`,
настройки — в `config`, ошибки — в `exception`, security — в `security`.

## Definition of Done

Код компилируется, тесты проходят, миграции добавлены, Swagger отображает
endpoint, Docker Compose не сломан, документация обновлена, секретов нет.

## Порты сервисов

- API Gateway: `6767`.
- userService: `6701`.
- groupService: `6702`.
- giftService: `6703`.
- subscriptionService: `6704`.
- notificationService: `6705`.
- chatService: `6706`.
- fundraiserService: `6707`.
- mockBankService: `6708`.
- calendarService: `6709`.
- adminService: `6710`.
