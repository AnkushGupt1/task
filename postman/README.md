# Training Platform — Postman Collection

## Import

1. Open Postman → **Import**
2. Select both files:
   - `Training-Platform.postman_collection.json`
   - `Training-Platform.local.postman_environment.json`
3. Top-right: select environment **Training Platform — Local**

## Run the app first

```bash
./mvnw -pl app spring-boot:run
```

API base: `http://localhost:8080`  
Swagger: `http://localhost:8080/swagger-ui.html`

## Happy path (run in order)

| # | Request | Notes |
|---|---------|-------|
| 1 | **Create Course** | Auto-saves `courseId` |
| 2 | **Publish Course** | Uses `{{courseId}}` |
| 3 | **Get Course by ID** | Verify `published: true` |
| 4 | **Enroll Visitor** | Auto-saves `enrollmentId` |
| 5 | **List Enrollments by Course** | Filter by `courseId` |

Or use **Collection Runner** on folders `1. Catalog` then `2. Enrollments`.

## Endpoints covered

| Method | Path |
|--------|------|
| `POST` | `/api/v1/courses` |
| `POST` | `/api/v1/courses/{id}/publish` |
| `GET` | `/api/v1/courses/{id}` |
| `POST` | `/api/v1/enrollments` |
| `GET` | `/api/v1/enrollments?courseId=` |

No auth required.

## Variables

| Variable | Default | Set by |
|----------|---------|--------|
| `baseUrl` | `http://localhost:8080` | Environment |
| `courseId` | *(empty)* | Create Course test script |
| `enrollmentId` | *(empty)* | Enroll Visitor test script |
| `visitorName` | `Ada Lovelace` | Environment |
| `visitorEmail` | `ada@example.com` | Environment |
