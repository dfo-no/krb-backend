# README KRAVBANK-BACKEND project


## Kjør applikasjon i dev mode

Kjør quarkus applikasjonen i dev mode

```shell script
./mvnw compile quarkus:dev
```


## Set opp postgres docker container

Spin opp en postgres container database med samsvarende application.properties fil

```shell script
docker run --name my_db -e POSTGRES_USER=username -e POSTGRES_PASSWORD=password -e POSTGRES_DB=my_db -p 5432:5432 postgres:10.5
```


## Bruk av postman 

For å teste endpunktene i Postman: Importer DFØ.postman_collection.json fra root i prosjektet
