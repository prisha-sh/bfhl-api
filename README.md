# Bajaj Finserv Health API (BFHL)

> Backend Engineering Hiring Round — REST API Challenge

---

## 👤 Candidate Details

| Field       | Value                    |
|-------------|--------------------------|
| Name        | Prisha Sharma            |
| Email       | prishas0301@gmail.com    |
| Roll Number | 0827CS231192             |
| User ID     | prisha_sharma_03012005   |

---

## 🚀 Tech Stack

- **Java 17**
- **Spring Boot 3.2.5**
- **Maven**
- **Lombok**
- **Spring Validation**
- **JUnit 5 + MockMvc**

---

## 📁 Project Structure

```
bfhl-api/
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/com/bajaj/bfhl/
    │   │   ├── BfhlApplication.java
    │   │   ├── controller/
    │   │   │   └── BfhlController.java
    │   │   ├── dto/
    │   │   │   ├── BfhlRequest.java
    │   │   │   └── BfhlResponse.java
    │   │   ├── service/
    │   │   │   ├── BfhlService.java
    │   │   │   └── BfhlServiceImpl.java
    │   │   └── exception/
    │   │       ├── BfhlException.java
    │   │       └── GlobalExceptionHandler.java
    │   └── resources/
    │       └── application.properties
    └── test/
        └── java/com/bajaj/bfhl/
            ├── BfhlApplicationTests.java
            ├── controller/
            │   └── BfhlControllerTest.java
            └── service/
                └── BfhlServiceImplTest.java
```

---

## 🛠️ How to Build & Run

### Prerequisites
- Java 17+
- Maven 3.8+

### Run Tests
```bash
mvn clean test
```

### Run Locally
```bash
mvn spring-boot:run
```

Server starts at: `http://localhost:8080`

### Build JAR
```bash
mvn clean package -DskipTests
java -jar target/bfhl-api-1.0.0.jar
```

---

## 📡 API Contract

### `POST /bfhl`

**Request**
```json
{
  "data": ["a", "1", "334", "4", "R", "$"]
}
```

**Response (200 OK)**
```json
{
  "is_success": true,
  "user_id": "prisha_sharma_03012005",
  "email": "prishas0301@gmail.com",
  "roll_number": "0827CS231192",
  "odd_numbers": ["1"],
  "even_numbers": ["334", "4"],
  "alphabets": ["A", "R"],
  "special_characters": ["$"],
  "sum": "339",
  "concat_string": "Ra"
}
```

**Error Response (400 Bad Request)**
```json
{
  "is_success": false,
  "message": "Validation failed",
  "errors": {
    "data": "'data' field must not be empty"
  }
}
```

---

## 🧠 Business Logic

### Classification Rules
| Token Type       | Rule                                          |
|-----------------|-----------------------------------------------|
| Number           | Matches `-?\d+`                              |
| Even number      | `value % 2 == 0`                             |
| Odd number       | `value % 2 != 0`                             |
| Alphabet         | Matches `[a-zA-Z]+`, returned in **UPPERCASE** |
| Special character| Anything that is not a number or alphabet    |

### Sum
Sum of all numeric values, returned as a **string**.

### concat_string Algorithm
1. Take all classified alphabets (already uppercased)
2. Join them into a single string
3. **Reverse** the combined string
4. Apply **alternating case** starting with UPPERCASE at index 0

**Example:**
```
Input:     ["a", "b", "c", "d", "e"]
Uppercase: ["A", "B", "C", "D", "E"]
Joined:    "ABCDE"
Reversed:  "EDCBA"
Result:    "EdCbA"
```

---

## 🧪 Testing

### curl Examples

**Valid Request:**
```bash
curl -X POST http://localhost:8080/bfhl \
  -H "Content-Type: application/json" \
  -d '{"data": ["a","1","334","4","R","$"]}'
```

**All Numbers:**
```bash
curl -X POST http://localhost:8080/bfhl \
  -H "Content-Type: application/json" \
  -d '{"data": ["2","3","10","7"]}'
```

**Alphabets Only:**
```bash
curl -X POST http://localhost:8080/bfhl \
  -H "Content-Type: application/json" \
  -d '{"data": ["a","b","c","d","e"]}'
```

**Error — Empty Array:**
```bash
curl -X POST http://localhost:8080/bfhl \
  -H "Content-Type: application/json" \
  -d '{"data": []}'
```

**Error — Missing Field:**
```bash
curl -X POST http://localhost:8080/bfhl \
  -H "Content-Type: application/json" \
  -d '{}'
```

---

### Postman Collection

Import the following JSON into Postman:

```json
{
  "info": {
    "name": "BFHL API",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "POST /bfhl - Mixed Input",
      "request": {
        "method": "POST",
        "header": [
          { "key": "Content-Type", "value": "application/json" }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"data\": [\"a\", \"1\", \"334\", \"4\", \"R\", \"$\"]\n}"
        },
        "url": { "raw": "http://localhost:8080/bfhl" }
      }
    },
    {
      "name": "POST /bfhl - Alphabets Only",
      "request": {
        "method": "POST",
        "header": [
          { "key": "Content-Type", "value": "application/json" }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"data\": [\"a\", \"b\", \"c\", \"d\", \"e\"]\n}"
        },
        "url": { "raw": "http://localhost:8080/bfhl" }
      }
    },
    {
      "name": "POST /bfhl - Error: Empty Array",
      "request": {
        "method": "POST",
        "header": [
          { "key": "Content-Type", "value": "application/json" }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"data\": []\n}"
        },
        "url": { "raw": "http://localhost:8080/bfhl" }
      }
    }
  ]
}
```

---

## ☁️ Deployment

You can deploy this Spring Boot API to Render instantly using the button below:

[![Deploy to Render](https://render.com/images/deploy-to-render-button.svg)](https://render.com/deploy?repo=https://github.com/prisha-sh/bfhl-api)

### Manual Deployment Steps:
1. Log into [Render](https://render.com).
2. Click **New +** and select **Blueprint**.
3. Connect your GitHub repository `bfhl-api`.
4. Click **Approve/Apply** to start the build.
5. Once deployed, the API will be live at `https://bfhl-api-xxxx.onrender.com/bfhl` (Render will generate a unique sub-domain if `bfhl-api` is taken).

