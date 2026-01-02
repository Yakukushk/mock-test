
# GitHub Repositories API

## Description
This is a Spring Boot application that exposes a REST API for retrieving a list of GitHub repositories
for a given user **excluding forked repositories**.  
For each repository, the API returns information about its branches along with the latest commit SHA.

The application integrates with the GitHub REST API v3.

---

## API Endpoint

### Get user repositories
```

GET /github/{username}/repositories

````

### Example response (200 OK)
```json
[
  {
    "repositoryName": "repo1",
    "ownerLogin": "testuser",
    "branches": [
      {
        "name": "main",
        "lastCommitSha": "abc123"
      }
    ]
  }
]
````

---

## Error Handling

### User not found (404)

If the specified GitHub user does not exist, the API returns:

```json
{
  "responseCode": 404,
  "message": "User not found: testuser"
}
```

---

## Technologies Used

* Java 25
* Spring Boot 4
* Spring Web (RestTemplate)
* Maven
* JUnit 5
* WireMock (integration testing)

---

## Testing

The project includes **integration tests** that:

* Mock GitHub API using WireMock
* Verify full HTTP request/response flow
* Ensure forked repositories are excluded
* Validate returned branches and commit SHA values

To run tests:

```bash
mvn clean test
```

---

## Running the Application

### Run locally

```bash
mvn spring-boot:run
```

The application will be available at:

```
http://localhost:8080
```

---

## Configuration

By default, the application uses the public GitHub API:

```
https://api.github.com
```

The API base URL can be overridden using the following property:

```
github.api.url
```

Example (for tests):

```properties
github.api.url=http://localhost:8089
```

---

## Assumptions & Notes

* Pagination is not implemented
* No authentication is used
