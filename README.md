# Kravbank backend README

## Prerequisites

* In order to run the application locally, you need a .env file
* Create (or copy from someone else in the team) the .env file in the kravbank-backend root directory:

#API postgreSQL\
DB_DATABASE = ""\
DB_USER = ""\
DB_PASSWORD = ""\
#Keycloak server\
KC_USER = ""\
KC_PASSWORD = ""\
KC_CRED_SECRET = ""\
#Keycloak postgreSQL\
KC_DB_USER = ""\
KC_DB_PASSWORD = ""

## Installation

* Make sure you have added a SSH key to your GitHub settings to be able to clone the repository securely.
* Clone the repository
  ``git clone git@github.com:dfo-no/krb-backend.git``
* Make sure you have installed Maven and Java 17 with relevant PATH environment variables. If you use
  powershell you could install Java JDK/openJDK and Maven with Chocolatey
  ``choco install openjdk``
  ``choco install maven``
* Launch application from CLI
  ``quarkus dev``
* Launch application from IntelliJ
  Run kravbank-backend (Shift+F10)

## Kravbank API

* URL: https://krb-backend-api.azurewebsites.net
* The API is deployed as an Azure App Service with github workflow pipelines

## Kravbank API database (PostreSQL)

* URL: krb-auth-postgres.postgres.database.azure.com
* The API uses a postgresql database, hosted as an Azure Database for PostgreSQL

## Keycloak server

* URL: https://krb-backend-auth.azurewebsites.net/
* Resources are secured by our keycloak server, deployed as an Azure App service
* Keycloak is an OpenID Connect (OIDC) for Identity and access management (IAM)
* The server is based on a docker container, spun up by a Keycloak docker image on Azure Container Registry (ACR):
    * krbkeycloakauth.azurecr.io
    * Instructions how to pull and push Docker images to ACR are I.e found on "Quick Start" in the Azure Portal resource
    * Credentials can be found under "Access Keys" in the Azure Portal resource

## Keycloak database (PostgreSQL)

* URL: krb-auth-postgres.postgres.database.azure.com
* Keycloak uses an external database, hosted as an Azure Database for PostgreSQL