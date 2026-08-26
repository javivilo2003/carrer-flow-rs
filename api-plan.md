# API Planning for CareerFlow

## API conventions

- Base path: `/api`
- Request body format: JSON
- Response body format: JSON
- JSON fields use camelCase. SQL columns should stay snake_case.
- IDs are UUID strings.
- Dates use ISO-8601 format: `YYYY-MM-DD`.
- Timestamps use ISO-8601 date-time format.
- `GET` and `DELETE` endpoints do not need request bodies.
- `POST` endpoints create records and return `201 Created`.
- `PUT` endpoints replace/update records and return `200 OK`.
- `DELETE` endpoints return `204 No Content` when successful.

## Error response format

All error responses should use this shape:

```json
{
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "One or more fields are invalid.",
    "details": [
      {
        "field": "companyId",
        "message": "companyId is required."
      }
    ]
  }
}
```

Common error cases:

| Status code | Error code | When it happens |
| ----------- | ---------- | --------------- |
| 400 | `INVALID_REQUEST` | Malformed JSON, invalid UUID, invalid date, or invalid enum value. |
| 400 | `VALIDATION_ERROR` | Required fields are missing or field values do not pass validation. |
| 404 | `NOT_FOUND` | The requested resource or referenced resource does not exist. |
| 409 | `CONFLICT` | A unique field already exists, such as company name or website. |
| 500 | `INTERNAL_ERROR` | Unexpected server/database failure. |

Example `404 Not Found` body:

```json
{
  "error": {
    "code": "NOT_FOUND",
    "message": "Application was not found."
  }
}
```

Example `409 Conflict` body:

```json
{
  "error": {
    "code": "CONFLICT",
    "message": "Company website already exists.",
    "details": [
      {
        "field": "website",
        "message": "website must be unique."
      }
    ]
  }
}
```

## Shared enums

`applicationStatus`:

```json
[
  "SAVED",
  "PREPARING",
  "APPLIED",
  "INTERVIEWING",
  "OFFER",
  "REJECTED",
  "WITHDRAWN"
]
```

`interviewStatus`:

```json
[
  "SCHEDULED",
  "COMPLETED",
  "CANCELLED",
  "RESCHEDULED",
  "PASSED",
  "FAILED",
  "WAITING_FEEDBACK"
]
```

## Job applications

### Endpoint chart

| Operation | Endpoint | Description |
| --------- | -------- | ----------- |
| GET | `/api/applications` | Return all job applications with company name and status. |
| GET | `/api/applications/{id}` | Return one application with details, notes, interviews, and followups. |
| POST | `/api/applications` | Create a new job application. |
| PUT | `/api/applications/{id}` | Update an existing job application. |
| DELETE | `/api/applications/{id}` | Delete an existing job application. |

### `GET /api/applications`

Success status code: `200 OK`

Success response body:

```json
{
  "applications": [
    {
      "id": "7f1b72e1-83e5-4e16-9b35-2b31f0e16964",
      "userId": "0c6d7204-3d5f-43ec-9b38-4960bf9d2c41",
      "companyId": "3fb07ed5-d376-424d-8d38-90dcf359bd7c",
      "companyName": "NVIDIA",
      "position": "Software Engineer, Infrastructure - DGX Cloud",
      "jobType": "Full-time",
      "salary": 124000,
      "status": "PREPARING",
      "appliedAt": "2026-08-20",
      "source": "LinkedIn",
      "postingLink": "https://www.linkedin.com/jobs/search-results/",
      "createdAt": "2026-08-20T10:30:00Z"
    }
  ]
}
```

Error cases:

| Status code | Error code | Response body |
| ----------- | ---------- | ------------- |
| 500 | `INTERNAL_ERROR` | `{"error":{"code":"INTERNAL_ERROR","message":"Unable to list applications."}}` |

### `GET /api/applications/{id}`

Success status code: `200 OK`

Success response body:

```json
{
  "application": {
    "id": "7f1b72e1-83e5-4e16-9b35-2b31f0e16964",
    "userId": "0c6d7204-3d5f-43ec-9b38-4960bf9d2c41",
    "company": {
      "id": "3fb07ed5-d376-424d-8d38-90dcf359bd7c",
      "companyName": "NVIDIA",
      "companyAddress": "Santa Clara, CA",
      "bio": "Computer Hardware Manufacturing",
      "website": "https://www.nvidia.com/en-us/"
    },
    "position": "Software Engineer, Infrastructure - DGX Cloud",
    "jobType": "Full-time",
    "description": "Automating portions of the software development lifecycle.",
    "salary": 124000,
    "status": "PREPARING",
    "appliedAt": "2026-08-20",
    "source": "LinkedIn",
    "postingLink": "https://www.linkedin.com/jobs/search-results/",
    "createdAt": "2026-08-20T10:30:00Z",
    "notes": [
      {
        "id": "3dd51287-92b7-49c5-a942-ad9973c5a7ce",
        "content": "Prepare DGX Cloud for interview.",
        "createdAt": "2026-08-20T10:45:00Z"
      }
    ],
    "interviews": [
      {
        "id": "34ebbe6e-2b9b-4e8f-9d56-c38dce4285e6",
        "stage": "1st contact with HR",
        "status": "SCHEDULED",
        "interviewDate": "2026-08-25",
        "notes": "Prepare for a very technical interview"
      }
    ],
    "followups": [
      {
        "id": "98f89422-2f53-43e0-a9fd-57c3e83af7da",
        "title": "Technical Interview Appointment",
        "dueDate": "2026-08-26",
        "completed": false
      }
    ]
  }
}
```

Error cases:

| Status code | Error code | Response body |
| ----------- | ---------- | ------------- |
| 400 | `INVALID_REQUEST` | `{"error":{"code":"INVALID_REQUEST","message":"Application id must be a valid UUID."}}` |
| 404 | `NOT_FOUND` | `{"error":{"code":"NOT_FOUND","message":"Application was not found."}}` |
| 500 | `INTERNAL_ERROR` | `{"error":{"code":"INTERNAL_ERROR","message":"Unable to fetch application."}}` |

### `POST /api/applications`

Request body:

```json
{
  "userId": "0c6d7204-3d5f-43ec-9b38-4960bf9d2c41",
  "companyId": "3fb07ed5-d376-424d-8d38-90dcf359bd7c",
  "position": "Software Engineer, Infrastructure - DGX Cloud",
  "jobType": "Full-time",
  "description": "Automating portions of the software development lifecycle.",
  "salary": 124000,
  "status": "PREPARING",
  "appliedAt": "2026-08-20",
  "source": "LinkedIn",
  "postingLink": "https://www.linkedin.com/jobs/search-results/"
}
```

Required fields: `userId`, `companyId`, `position`, `jobType`, `status`, `source`

Success status code: `201 Created`

Success response body:

```json
{
  "application": {
    "id": "7f1b72e1-83e5-4e16-9b35-2b31f0e16964",
    "userId": "0c6d7204-3d5f-43ec-9b38-4960bf9d2c41",
    "companyId": "3fb07ed5-d376-424d-8d38-90dcf359bd7c",
    "position": "Software Engineer, Infrastructure - DGX Cloud",
    "jobType": "Full-time",
    "description": "Automating portions of the software development lifecycle.",
    "salary": 124000,
    "status": "PREPARING",
    "appliedAt": "2026-08-20",
    "source": "LinkedIn",
    "postingLink": "https://www.linkedin.com/jobs/search-results/",
    "createdAt": "2026-08-20T10:30:00Z"
  }
}
```

Error cases:

| Status code | Error code | Response body |
| ----------- | ---------- | ------------- |
| 400 | `INVALID_REQUEST` | `{"error":{"code":"INVALID_REQUEST","message":"Request body must be valid JSON."}}` |
| 400 | `VALIDATION_ERROR` | `{"error":{"code":"VALIDATION_ERROR","message":"One or more fields are invalid.","details":[{"field":"position","message":"position is required."}]}}` |
| 404 | `NOT_FOUND` | `{"error":{"code":"NOT_FOUND","message":"Referenced company was not found.","details":[{"field":"companyId","message":"No company exists for this id."}]}}` |
| 500 | `INTERNAL_ERROR` | `{"error":{"code":"INTERNAL_ERROR","message":"Unable to create application."}}` |

### `PUT /api/applications/{id}`

Request body:

```json
{
  "userId": "0c6d7204-3d5f-43ec-9b38-4960bf9d2c41",
  "companyId": "3fb07ed5-d376-424d-8d38-90dcf359bd7c",
  "position": "Software Engineer, Infrastructure - DGX Cloud",
  "jobType": "Full-time",
  "description": "Updated job description.",
  "salary": 130000,
  "status": "APPLIED",
  "appliedAt": "2026-08-22",
  "source": "LinkedIn",
  "postingLink": "https://www.linkedin.com/jobs/search-results/"
}
```

Required fields: `userId`, `companyId`, `position`, `jobType`, `status`, `source`

Success status code: `200 OK`

Success response body:

```json
{
  "application": {
    "id": "7f1b72e1-83e5-4e16-9b35-2b31f0e16964",
    "userId": "0c6d7204-3d5f-43ec-9b38-4960bf9d2c41",
    "companyId": "3fb07ed5-d376-424d-8d38-90dcf359bd7c",
    "position": "Software Engineer, Infrastructure - DGX Cloud",
    "jobType": "Full-time",
    "description": "Updated job description.",
    "salary": 130000,
    "status": "APPLIED",
    "appliedAt": "2026-08-22",
    "source": "LinkedIn",
    "postingLink": "https://www.linkedin.com/jobs/search-results/",
    "createdAt": "2026-08-20T10:30:00Z"
  }
}
```

Error cases:

| Status code | Error code | Response body |
| ----------- | ---------- | ------------- |
| 400 | `INVALID_REQUEST` | `{"error":{"code":"INVALID_REQUEST","message":"Application id must be a valid UUID."}}` |
| 400 | `VALIDATION_ERROR` | `{"error":{"code":"VALIDATION_ERROR","message":"One or more fields are invalid.","details":[{"field":"status","message":"status must be a valid applicationStatus value."}]}}` |
| 404 | `NOT_FOUND` | `{"error":{"code":"NOT_FOUND","message":"Application was not found."}}` |
| 404 | `NOT_FOUND` | `{"error":{"code":"NOT_FOUND","message":"Referenced company was not found.","details":[{"field":"companyId","message":"No company exists for this id."}]}}` |
| 500 | `INTERNAL_ERROR` | `{"error":{"code":"INTERNAL_ERROR","message":"Unable to update application."}}` |

### `DELETE /api/applications/{id}`

Success status code: `204 No Content`

Success response body: none

Error cases:

| Status code | Error code | Response body |
| ----------- | ---------- | ------------- |
| 400 | `INVALID_REQUEST` | `{"error":{"code":"INVALID_REQUEST","message":"Application id must be a valid UUID."}}` |
| 404 | `NOT_FOUND` | `{"error":{"code":"NOT_FOUND","message":"Application was not found."}}` |
| 409 | `CONFLICT` | `{"error":{"code":"CONFLICT","message":"Application cannot be deleted while interviews, notes, or followups reference it."}}` |
| 500 | `INTERNAL_ERROR` | `{"error":{"code":"INTERNAL_ERROR","message":"Unable to delete application."}}` |

## Companies

### Endpoint chart

| Operation | Endpoint | Description |
| --------- | -------- | ----------- |
| GET | `/api/companies` | Return all companies with company name and bio. |
| GET | `/api/companies/{id}` | Return one company with name, address, bio, and website url. |
| POST | `/api/companies` | Create a new company. |
| PUT | `/api/companies/{id}` | Update an existing company. |
| DELETE | `/api/companies/{id}` | Delete an existing company. |

### `GET /api/companies`

Success status code: `200 OK`

Success response body:

```json
{
  "companies": [
    {
      "id": "3fb07ed5-d376-424d-8d38-90dcf359bd7c",
      "companyName": "NVIDIA",
      "bio": "Computer Hardware Manufacturing",
      "website": "https://www.nvidia.com/en-us/",
      "createdAt": "2026-08-20T10:00:00Z"
    }
  ]
}
```

Error cases:

| Status code | Error code | Response body |
| ----------- | ---------- | ------------- |
| 500 | `INTERNAL_ERROR` | `{"error":{"code":"INTERNAL_ERROR","message":"Unable to list companies."}}` |

### `GET /api/companies/{id}`

Success status code: `200 OK`

Success response body:

```json
{
  "company": {
    "id": "3fb07ed5-d376-424d-8d38-90dcf359bd7c",
    "companyName": "NVIDIA",
    "companyAddress": "Santa Clara, CA",
    "bio": "Computer Hardware Manufacturing",
    "website": "https://www.nvidia.com/en-us/",
    "createdAt": "2026-08-20T10:00:00Z"
  }
}
```

Error cases:

| Status code | Error code | Response body |
| ----------- | ---------- | ------------- |
| 400 | `INVALID_REQUEST` | `{"error":{"code":"INVALID_REQUEST","message":"Company id must be a valid UUID."}}` |
| 404 | `NOT_FOUND` | `{"error":{"code":"NOT_FOUND","message":"Company was not found."}}` |
| 500 | `INTERNAL_ERROR` | `{"error":{"code":"INTERNAL_ERROR","message":"Unable to fetch company."}}` |

### `POST /api/companies`

Request body:

```json
{
  "companyName": "NVIDIA",
  "companyAddress": "Santa Clara, CA",
  "bio": "Computer Hardware Manufacturing",
  "website": "https://www.nvidia.com/en-us/"
}
```

Required fields: `companyName`

Success status code: `201 Created`

Success response body:

```json
{
  "company": {
    "id": "3fb07ed5-d376-424d-8d38-90dcf359bd7c",
    "companyName": "NVIDIA",
    "companyAddress": "Santa Clara, CA",
    "bio": "Computer Hardware Manufacturing",
    "website": "https://www.nvidia.com/en-us/",
    "createdAt": "2026-08-20T10:00:00Z"
  }
}
```

Error cases:

| Status code | Error code | Response body |
| ----------- | ---------- | ------------- |
| 400 | `INVALID_REQUEST` | `{"error":{"code":"INVALID_REQUEST","message":"Request body must be valid JSON."}}` |
| 400 | `VALIDATION_ERROR` | `{"error":{"code":"VALIDATION_ERROR","message":"One or more fields are invalid.","details":[{"field":"companyName","message":"companyName is required."}]}}` |
| 409 | `CONFLICT` | `{"error":{"code":"CONFLICT","message":"Company name already exists.","details":[{"field":"companyName","message":"companyName must be unique."}]}}` |
| 500 | `INTERNAL_ERROR` | `{"error":{"code":"INTERNAL_ERROR","message":"Unable to create company."}}` |

### `PUT /api/companies/{id}`

Request body:

```json
{
  "companyName": "NVIDIA",
  "companyAddress": "Santa Clara, CA",
  "bio": "Computer Hardware Manufacturing",
  "website": "https://www.nvidia.com/en-us/"
}
```

Required fields: `companyName`

Success status code: `200 OK`

Success response body:

```json
{
  "company": {
    "id": "3fb07ed5-d376-424d-8d38-90dcf359bd7c",
    "companyName": "NVIDIA",
    "companyAddress": "Santa Clara, CA",
    "bio": "Computer Hardware Manufacturing",
    "website": "https://www.nvidia.com/en-us/",
    "createdAt": "2026-08-20T10:00:00Z"
  }
}
```

Error cases:

| Status code | Error code | Response body |
| ----------- | ---------- | ------------- |
| 400 | `INVALID_REQUEST` | `{"error":{"code":"INVALID_REQUEST","message":"Company id must be a valid UUID."}}` |
| 400 | `VALIDATION_ERROR` | `{"error":{"code":"VALIDATION_ERROR","message":"One or more fields are invalid.","details":[{"field":"website","message":"website must be a valid URL."}]}}` |
| 404 | `NOT_FOUND` | `{"error":{"code":"NOT_FOUND","message":"Company was not found."}}` |
| 409 | `CONFLICT` | `{"error":{"code":"CONFLICT","message":"Company website already exists.","details":[{"field":"website","message":"website must be unique."}]}}` |
| 500 | `INTERNAL_ERROR` | `{"error":{"code":"INTERNAL_ERROR","message":"Unable to update company."}}` |

### `DELETE /api/companies/{id}`

Success status code: `204 No Content`

Success response body: none

Error cases:

| Status code | Error code | Response body |
| ----------- | ---------- | ------------- |
| 400 | `INVALID_REQUEST` | `{"error":{"code":"INVALID_REQUEST","message":"Company id must be a valid UUID."}}` |
| 404 | `NOT_FOUND` | `{"error":{"code":"NOT_FOUND","message":"Company was not found."}}` |
| 409 | `CONFLICT` | `{"error":{"code":"CONFLICT","message":"Company cannot be deleted while applications or contacts reference it."}}` |
| 500 | `INTERNAL_ERROR` | `{"error":{"code":"INTERNAL_ERROR","message":"Unable to delete company."}}` |

## Interviews

### Endpoint chart

| Operation | Endpoint | Description |
| --------- | -------- | ----------- |
| GET | `/api/interviews` | Return all interviews with company name, position name, status, interview date, and stage of the interview process. |
| GET | `/api/interviews/{id}` | Return one interview with company name, position name, status, interview date, and stage of the interview process. |
| POST | `/api/interviews` | Create a new interview. |
| PUT | `/api/interviews/{id}` | Update an existing interview. |
| DELETE | `/api/interviews/{id}` | Delete an existing interview. |

### `GET /api/interviews`

Success status code: `200 OK`

Success response body:

```json
{
  "interviews": [
    {
      "id": "34ebbe6e-2b9b-4e8f-9d56-c38dce4285e6",
      "jobApplicationId": "7f1b72e1-83e5-4e16-9b35-2b31f0e16964",
      "companyName": "NVIDIA",
      "position": "Software Engineer, Infrastructure - DGX Cloud",
      "stage": "1st contact with HR",
      "status": "SCHEDULED",
      "interviewDate": "2026-08-25",
      "notes": "Prepare for a very technical interview"
    }
  ]
}
```

Error cases:

| Status code | Error code | Response body |
| ----------- | ---------- | ------------- |
| 500 | `INTERNAL_ERROR` | `{"error":{"code":"INTERNAL_ERROR","message":"Unable to list interviews."}}` |

### `GET /api/interviews/{id}`

Success status code: `200 OK`

Success response body:

```json
{
  "interview": {
    "id": "34ebbe6e-2b9b-4e8f-9d56-c38dce4285e6",
    "jobApplicationId": "7f1b72e1-83e5-4e16-9b35-2b31f0e16964",
    "companyName": "NVIDIA",
    "position": "Software Engineer, Infrastructure - DGX Cloud",
    "stage": "1st contact with HR",
    "status": "SCHEDULED",
    "interviewDate": "2026-08-25",
    "notes": "Prepare for a very technical interview"
  }
}
```

Error cases:

| Status code | Error code | Response body |
| ----------- | ---------- | ------------- |
| 400 | `INVALID_REQUEST` | `{"error":{"code":"INVALID_REQUEST","message":"Interview id must be a valid UUID."}}` |
| 404 | `NOT_FOUND` | `{"error":{"code":"NOT_FOUND","message":"Interview was not found."}}` |
| 500 | `INTERNAL_ERROR` | `{"error":{"code":"INTERNAL_ERROR","message":"Unable to fetch interview."}}` |

### `POST /api/interviews`

Request body:

```json
{
  "jobApplicationId": "7f1b72e1-83e5-4e16-9b35-2b31f0e16964",
  "stage": "1st contact with HR",
  "status": "SCHEDULED",
  "interviewDate": "2026-08-25",
  "notes": "Prepare for a very technical interview"
}
```

Required fields: `jobApplicationId`, `stage`, `status`

Success status code: `201 Created`

Success response body:

```json
{
  "interview": {
    "id": "34ebbe6e-2b9b-4e8f-9d56-c38dce4285e6",
    "jobApplicationId": "7f1b72e1-83e5-4e16-9b35-2b31f0e16964",
    "stage": "1st contact with HR",
    "status": "SCHEDULED",
    "interviewDate": "2026-08-25",
    "notes": "Prepare for a very technical interview"
  }
}
```

Error cases:

| Status code | Error code | Response body |
| ----------- | ---------- | ------------- |
| 400 | `INVALID_REQUEST` | `{"error":{"code":"INVALID_REQUEST","message":"Request body must be valid JSON."}}` |
| 400 | `VALIDATION_ERROR` | `{"error":{"code":"VALIDATION_ERROR","message":"One or more fields are invalid.","details":[{"field":"stage","message":"stage is required."}]}}` |
| 404 | `NOT_FOUND` | `{"error":{"code":"NOT_FOUND","message":"Referenced application was not found.","details":[{"field":"jobApplicationId","message":"No application exists for this id."}]}}` |
| 500 | `INTERNAL_ERROR` | `{"error":{"code":"INTERNAL_ERROR","message":"Unable to create interview."}}` |

### `PUT /api/interviews/{id}`

Request body:

```json
{
  "jobApplicationId": "7f1b72e1-83e5-4e16-9b35-2b31f0e16964",
  "stage": "Technical interview",
  "status": "RESCHEDULED",
  "interviewDate": "2026-08-28",
  "notes": "Review backend API design."
}
```

Required fields: `jobApplicationId`, `stage`, `status`

Success status code: `200 OK`

Success response body:

```json
{
  "interview": {
    "id": "34ebbe6e-2b9b-4e8f-9d56-c38dce4285e6",
    "jobApplicationId": "7f1b72e1-83e5-4e16-9b35-2b31f0e16964",
    "stage": "Technical interview",
    "status": "RESCHEDULED",
    "interviewDate": "2026-08-28",
    "notes": "Review backend API design."
  }
}
```

Error cases:

| Status code | Error code | Response body |
| ----------- | ---------- | ------------- |
| 400 | `INVALID_REQUEST` | `{"error":{"code":"INVALID_REQUEST","message":"Interview id must be a valid UUID."}}` |
| 400 | `VALIDATION_ERROR` | `{"error":{"code":"VALIDATION_ERROR","message":"One or more fields are invalid.","details":[{"field":"status","message":"status must be a valid interviewStatus value."}]}}` |
| 404 | `NOT_FOUND` | `{"error":{"code":"NOT_FOUND","message":"Interview was not found."}}` |
| 404 | `NOT_FOUND` | `{"error":{"code":"NOT_FOUND","message":"Referenced application was not found.","details":[{"field":"jobApplicationId","message":"No application exists for this id."}]}}` |
| 500 | `INTERNAL_ERROR` | `{"error":{"code":"INTERNAL_ERROR","message":"Unable to update interview."}}` |

### `DELETE /api/interviews/{id}`

Success status code: `204 No Content`

Success response body: none

Error cases:

| Status code | Error code | Response body |
| ----------- | ---------- | ------------- |
| 400 | `INVALID_REQUEST` | `{"error":{"code":"INVALID_REQUEST","message":"Interview id must be a valid UUID."}}` |
| 404 | `NOT_FOUND` | `{"error":{"code":"NOT_FOUND","message":"Interview was not found."}}` |
| 500 | `INTERNAL_ERROR` | `{"error":{"code":"INTERNAL_ERROR","message":"Unable to delete interview."}}` |

## Followups

### Endpoint chart

| Operation | Endpoint | Description |
| --------- | -------- | ----------- |
| GET | `/api/followups` | Return all followups with job application id, title of the followup, and the due date. |
| GET | `/api/followups/{id}` | Return one followup with job application id, title of the followup, and the due date. |
| POST | `/api/followups` | Create a new followup. |
| PUT | `/api/followups/{id}` | Update an existing followup. |
| DELETE | `/api/followups/{id}` | Delete an existing followup. |

### `GET /api/followups`

Success status code: `200 OK`

Success response body:

```json
{
  "followups": [
    {
      "id": "98f89422-2f53-43e0-a9fd-57c3e83af7da",
      "jobApplicationId": "7f1b72e1-83e5-4e16-9b35-2b31f0e16964",
      "companyName": "NVIDIA",
      "position": "Software Engineer, Infrastructure - DGX Cloud",
      "title": "Technical Interview Appointment",
      "dueDate": "2026-08-26",
      "completed": false
    }
  ]
}
```

Error cases:

| Status code | Error code | Response body |
| ----------- | ---------- | ------------- |
| 500 | `INTERNAL_ERROR` | `{"error":{"code":"INTERNAL_ERROR","message":"Unable to list followups."}}` |

### `GET /api/followups/{id}`

Success status code: `200 OK`

Success response body:

```json
{
  "followup": {
    "id": "98f89422-2f53-43e0-a9fd-57c3e83af7da",
    "jobApplicationId": "7f1b72e1-83e5-4e16-9b35-2b31f0e16964",
    "companyName": "NVIDIA",
    "position": "Software Engineer, Infrastructure - DGX Cloud",
    "title": "Technical Interview Appointment",
    "dueDate": "2026-08-26",
    "completed": false
  }
}
```

Error cases:

| Status code | Error code | Response body |
| ----------- | ---------- | ------------- |
| 400 | `INVALID_REQUEST` | `{"error":{"code":"INVALID_REQUEST","message":"Followup id must be a valid UUID."}}` |
| 404 | `NOT_FOUND` | `{"error":{"code":"NOT_FOUND","message":"Followup was not found."}}` |
| 500 | `INTERNAL_ERROR` | `{"error":{"code":"INTERNAL_ERROR","message":"Unable to fetch followup."}}` |

### `POST /api/followups`

Request body:

```json
{
  "jobApplicationId": "7f1b72e1-83e5-4e16-9b35-2b31f0e16964",
  "title": "Technical Interview Appointment",
  "dueDate": "2026-08-26",
  "completed": false
}
```

Required fields: `jobApplicationId`, `title`, `dueDate`

Success status code: `201 Created`

Success response body:

```json
{
  "followup": {
    "id": "98f89422-2f53-43e0-a9fd-57c3e83af7da",
    "jobApplicationId": "7f1b72e1-83e5-4e16-9b35-2b31f0e16964",
    "title": "Technical Interview Appointment",
    "dueDate": "2026-08-26",
    "completed": false
  }
}
```

Error cases:

| Status code | Error code | Response body |
| ----------- | ---------- | ------------- |
| 400 | `INVALID_REQUEST` | `{"error":{"code":"INVALID_REQUEST","message":"Request body must be valid JSON."}}` |
| 400 | `VALIDATION_ERROR` | `{"error":{"code":"VALIDATION_ERROR","message":"One or more fields are invalid.","details":[{"field":"dueDate","message":"dueDate is required."}]}}` |
| 404 | `NOT_FOUND` | `{"error":{"code":"NOT_FOUND","message":"Referenced application was not found.","details":[{"field":"jobApplicationId","message":"No application exists for this id."}]}}` |
| 500 | `INTERNAL_ERROR` | `{"error":{"code":"INTERNAL_ERROR","message":"Unable to create followup."}}` |

### `PUT /api/followups/{id}`

Request body:

```json
{
  "jobApplicationId": "7f1b72e1-83e5-4e16-9b35-2b31f0e16964",
  "title": "Send thank-you email",
  "dueDate": "2026-08-29",
  "completed": true
}
```

Required fields: `jobApplicationId`, `title`, `dueDate`, `completed`

Success status code: `200 OK`

Success response body:

```json
{
  "followup": {
    "id": "98f89422-2f53-43e0-a9fd-57c3e83af7da",
    "jobApplicationId": "7f1b72e1-83e5-4e16-9b35-2b31f0e16964",
    "title": "Send thank-you email",
    "dueDate": "2026-08-29",
    "completed": true
  }
}
```

Error cases:

| Status code | Error code | Response body |
| ----------- | ---------- | ------------- |
| 400 | `INVALID_REQUEST` | `{"error":{"code":"INVALID_REQUEST","message":"Followup id must be a valid UUID."}}` |
| 400 | `VALIDATION_ERROR` | `{"error":{"code":"VALIDATION_ERROR","message":"One or more fields are invalid.","details":[{"field":"completed","message":"completed must be a boolean."}]}}` |
| 404 | `NOT_FOUND` | `{"error":{"code":"NOT_FOUND","message":"Followup was not found."}}` |
| 404 | `NOT_FOUND` | `{"error":{"code":"NOT_FOUND","message":"Referenced application was not found.","details":[{"field":"jobApplicationId","message":"No application exists for this id."}]}}` |
| 500 | `INTERNAL_ERROR` | `{"error":{"code":"INTERNAL_ERROR","message":"Unable to update followup."}}` |

### `DELETE /api/followups/{id}`

Success status code: `204 No Content`

Success response body: none

Error cases:

| Status code | Error code | Response body |
| ----------- | ---------- | ------------- |
| 400 | `INVALID_REQUEST` | `{"error":{"code":"INVALID_REQUEST","message":"Followup id must be a valid UUID."}}` |
| 404 | `NOT_FOUND` | `{"error":{"code":"NOT_FOUND","message":"Followup was not found."}}` |
| 500 | `INTERNAL_ERROR` | `{"error":{"code":"INTERNAL_ERROR","message":"Unable to delete followup."}}` |

## Planned nested endpoints

| Operation | Endpoint | Description |
| --------- | -------- | ----------- |
| GET | `/api/applications/{id}/interviews` | Return interviews for one application. |
| GET | `/api/applications/{id}/notes` | Return notes for one application. |
| GET | `/api/applications/{id}/followups` | Return followups for one application. |
