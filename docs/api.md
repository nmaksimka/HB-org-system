# API первого вертикального среза

Внешний base URL: `http://localhost:6767`.

| Метод | Endpoint | Доступ |
|---|---|---|
| POST | `/api/v1/auth/register` | публичный |
| POST | `/api/v1/auth/login` | публичный |
| GET | `/api/v1/auth/me` | JWT |
| GET | `/api/v1/users` | JWT |
| GET | `/api/v1/users/{userId}` | JWT |
| PUT | `/api/v1/users/me/profile` | JWT |
| POST | `/api/v1/groups` | JWT |
| GET | `/api/v1/groups` | JWT |
| GET | `/api/v1/groups/{groupId}` | JWT |
| POST | `/api/v1/groups/{groupId}/join` | JWT |
| DELETE | `/api/v1/groups/{groupId}/leave` | JWT |
| GET | `/api/v1/groups/{groupId}/members` | JWT |

Internal endpoint `GET /internal/users/{userId}` не маршрутизируется через
Gateway и требует `X-Internal-Api-Key`.

`groupService` доверяет `X-User-*` только при наличии внутреннего
`X-Gateway-Secret`. Gateway удаляет клиентские identity headers и формирует
их заново из проверенного JWT.

## Ошибки

```json
{
  "timestamp": "2026-07-03T12:00:00Z",
  "status": 409,
  "error": "Conflict",
  "message": "Email is already registered",
  "path": "/api/v1/auth/register",
  "correlationId": "d9387932-2695-4ccd-8fd1-f3bb52fd92d0"
}
```
