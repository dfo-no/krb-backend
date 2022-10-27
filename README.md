## Dev mode

Kjør quarkus applikasjonen i dev mode

```shell script
./mvnw compile quarkus:dev
```

## Bruk av opp lokal PostgreSQL docker som database

Spin opp en postgres container som samsvarer application.properties configs
Eksempelvis:

```shell script
docker run --name my_db -e POSTGRES_USER=username -e POSTGRES_PASSWORD=password -e POSTGRES_DB=my_db -p 5432:5432 postgres:10.5
```

## Bruk av Azure Database for PostgreSQL

Opprett .env fil i root med credentials i henhold til applications.properties configs
