# Архитектура Birthday Celebration System

## Выбранный подход

Система строится по принципу database-per-service. Исходная
`birthday_database_schema.sql` полезна как концептуальная ER-модель, но не
может применяться целиком: в ней все домены находятся в одной PostgreSQL БД
и связаны междоменными foreign key.

В рабочей реализации схема делится так:

| Сервис | Собственные таблицы | Внешние ссылки |
|---|---|---|
| userService | users, user_profiles, friendships | — |
| groupService | groups, group_members | user_id без FK |
| giftService | gift_wishes, gift_reservations | user_id без FK |
| subscriptionService | birthday_subscriptions | user_id/group_id без FK |
| notificationService | notifications, notification_delivery_logs | user_id без FK |
| chatService | chat_rooms, chat_participants, chat_messages | user/gift/group ID без FK |
| fundraiserService | fundraisers, fundraiser_participants | user/gift ID без FK |
| mockBankService | mock_collections, mock_payments | fundraiser/user ID без FK |
| calendarService | calendar_integrations, calendar_events | user_id без FK |
| adminService | admin_imports, audit_logs | actor_user_id без FK |

Существование внешней сущности проверяется через internal REST API, когда
ответ нужен в текущем запросе. Изменения, уведомления, аудит и фоновые
действия передаются Kafka events.

## Первый вертикальный срез

Текущая версия реализует:

```text
Client
  -> apiGateway :6767
      -> userService :6701
          -> user_db :5432 (host :5334)
      -> groupService :6702
          -> group_db :5432 (host :5336)
          -> userService internal API
      -> giftService :6703
          -> gift_db :5432 (host :5337)
          -> userService internal API
          -> Kafka via transactional outbox
      -> subscriptionService :6704
          -> subscription_db :5432 (host :5338)
          -> userService/groupService internal API
          -> Kafka via transactional outbox
```

Gateway:

- создаёт/прокидывает `X-Correlation-Id`;
- валидирует access JWT;
- удаляет недоверенные `X-User-*` headers и формирует их из JWT;
- не публикует `/internal/**`;
- централизует CORS и routing.

`userService`:

- регистрирует пользователя и BCrypt-hash пароля;
- выдаёт и проверяет HMAC JWT;
- запрещает вход неактивным пользователям;
- предоставляет profile/users API;
- защищает internal API отдельным ключом;
- использует DTO, mapper, service/repository layers и Flyway.

`groupService`:

- владеет таблицами `groups` и `group_members`;
- хранит внешние `user_id` без FK на `user_db`;
- проверяет пользователей синхронно через OpenFeign;
- при создании назначает создателю роль `OWNER`;
- запрещает повторное вступление и выход владельца;
- принимает identity headers только с внутренним секретом Gateway.

`giftService`:

- владеет `gift_wishes`, `gift_reservations` и `outbox_events`;
- не создаёт FK на пользователей;
- скрывает личность бронирующего из обычного `GiftResponse`;
- блокирует строку подарка при бронировании и допускает только одну бронь;
- сохраняет бизнес-изменение и Kafka event в одной транзакции.

`subscriptionService`:

- владеет `birthday_subscriptions` и собственным outbox;
- проверяет пользователей и группы через защищённые internal API;
- запрещает подписку на себя и дубли активных подписок;
- публикует `SubscriptionCreatedEvent` и `SubscriptionDeletedEvent`.

## Решения по исходной схеме

1. PostgreSQL enum заменены на `VARCHAR + CHECK`. Это упрощает JPA mapping и
   эволюцию enum новой Flyway-миграцией.
2. Временные поля хранятся как `TIMESTAMPTZ`, а Java использует `Instant`.
3. Для email/username добавлены case-insensitive unique indexes.
4. Секреты и connection strings берутся из environment variables.
5. Межсервисные FK исключаются; согласованность обеспечивается API,
   событиями, идемпотентными consumers и, на следующих этапах, outbox.

## Порядок дальнейшей реализации

1. groupService и giftService.
2. subscriptionService и notificationService + Kafka/outbox.
3. chatService + WebSocket и проверка запрета доступа именинника.
4. fundraiserService + mockBankService.
5. calendarService и adminService.
6. React frontend после стабилизации MVP API.
