# API Birthday Celebration System

Внешний base URL: `http://localhost:6969`.

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
| POST | `/api/v1/gifts` | JWT |
| GET | `/api/v1/gifts/me` | JWT |
| GET | `/api/v1/users/{userId}/gifts` | JWT |
| PUT | `/api/v1/gifts/{giftId}` | JWT, владелец |
| DELETE | `/api/v1/gifts/{giftId}` | JWT, владелец |
| POST | `/api/v1/gifts/{giftId}/reserve` | JWT, не владелец |
| POST | `/api/v1/subscriptions/users/{userId}` | JWT |
| POST | `/api/v1/subscriptions/groups/{groupId}` | JWT |
| GET | `/api/v1/subscriptions/me` | JWT |
| DELETE | `/api/v1/subscriptions/{subscriptionId}` | JWT, владелец |
| GET | `/api/v1/notifications` | JWT |
| PATCH | `/api/v1/notifications/{notificationId}/read` | JWT, владелец |
| PATCH | `/api/v1/notifications/read-all` | JWT |
| POST | `/api/v1/chats/birthday/{targetUserId}` | JWT |
| POST | `/api/v1/chats/{roomId}/join` | JWT, не именинник |
| GET | `/api/v1/chats/me` | JWT |
| GET | `/api/v1/chats/{roomId}/messages` | JWT, участник |
| POST | `/api/v1/chats/{roomId}/messages` | JWT, участник |
| POST | `/api/v1/fundraisers` | JWT, не для себя |
| POST | `/api/v1/fundraisers/{id}/activate` | JWT, владелец |
| POST | `/api/v1/fundraisers/{id}/participants` | JWT, не именинник |
| GET | `/api/v1/fundraisers/{id}` | JWT, не именинник |
| GET | `/api/v1/fundraisers/{id}/participants` | JWT, не именинник |
| GET | `/api/v1/mock-bank/collections/{fundraiserId}` | JWT |
| POST | `/api/v1/mock-bank/collections/{fundraiserId}/payments` | JWT |
| POST | `/api/v1/calendar/integrations` | JWT |
| GET | `/api/v1/calendar/integrations` | JWT |
| POST | `/api/v1/calendar/events` | JWT |
| GET | `/api/v1/calendar/events` | JWT |
| PUT | `/api/v1/calendar/events/{id}` | JWT, владелец |
| DELETE | `/api/v1/calendar/events/{id}` | JWT, владелец |
| PATCH | `/api/v1/admin/users/{id}/block` | JWT, ADMIN |
| GET | `/api/v1/admin/audit-logs` | JWT, ADMIN |
| POST | `/api/v1/admin/imports` | JWT, ADMIN |

WebSocket endpoint: `/ws/notifications`; персональная очередь:
`/user/queue/notifications`.

Chat WebSocket: `/ws/chats`, topic `/topic/chats/{roomId}`,
send destination `/app/chats/{roomId}/send`.

Internal endpoints пользователей, групп, подарков и mock bank не
маршрутизируются через Gateway и требуют `X-Internal-Api-Key`.

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
