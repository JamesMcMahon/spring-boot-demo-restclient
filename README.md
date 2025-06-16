# Spring RestClient vs RestTemplate – side-by-side demo

This small Spring Boot app shows the same HTTP calls written twice — once with the
modern [RestClient](https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-restclient) API
and once with the
classic [RestTemplate](https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-resttemplate).  
Both versions talk to a local [HTTPBin](https://httpbin.org/) instance that Docker Compose
starts for you.

Jump straight to the two implementation classes and compare them line-by-line:

* [`RestClientHttpBinClient`](src/main/java/sh/jfm/springbootdemos/restclient/RestClientHttpBinClient.java)
* [`RestTemplateHttpBinClient`](src/main/java/sh/jfm/springbootdemos/restclient/RestTemplateHttpBinClient.java)

Everything else (controller, adaptor, DTOs) only exists to make the app runnable.

## Requirements

Docker (Docker Engine / Docker Desktop) must be installed and running.  
When you start the Spring Boot application, the `compose.yaml` file is picked up
automatically by Spring Boot’s Docker-Compose integration and the supporting
`httpbin` container is started for you.

## Run the app

The command below launches both the Spring Boot application **and** the Docker
Compose services in one step:

```bash
./mvnw spring-boot:run
# server starts at http://localhost:8080
```

## Try it out

### Hello

```bash
curl -G 'http://localhost:8080/restClient/hello' \
     --data-urlencode 'name=Spring'
```

```bash
curl -G 'http://localhost:8080/restTemplate/hello' \
     --data-urlencode 'name=Spring'
```

### Forced 500

```bash
curl http://localhost:8080/restClient/error
```

```bash
curl http://localhost:8080/restTemplate/error
```

### POST JSON

```bash
curl -X POST 'http://localhost:8080/restClient/post' \
     -H 'Content-Type: application/json' \
     -d '{"message":"Hi"}'
```

```bash
curl -X POST 'http://localhost:8080/restTemplate/post' \
     -H 'Content-Type: application/json' \
     -d '{"message":"Hi"}'
```

### Auth (success / failure)

```bash
curl http://localhost:8080/restClient/auth
```

```bash
curl http://localhost:8080/restClient/auth?fail
```

```bash
curl http://localhost:8080/restTemplate/auth
```

```bash
curl http://localhost:8080/restTemplate/auth?fail
```

## Tests

Run `./mvnw test` to exercise every route in one go; the assertions inside the
tests show the exact response bodies, so you can see what each endpoint should
return without reaching for `curl`.
