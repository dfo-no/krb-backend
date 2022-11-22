# Kravbank backend README

## Prerequisites

* In order to run the application locally, you need a .env file
* Create (or copy from someone else in the team) the .env file in the kravbank-backend root directory:
  DB_USER=<LIVE USERNAME>
  DB_PASSWORD=<LIVE PASSWORD>
  KC_CRED_SECRET=<KEYCLOAK SECRET>

## Installation

* Make sure you have added a SSH key to your GitHub settings to be able to clone the repository securely.
* Clone the repository
  ``git clone git@github.com:dfo-no/krb-backend.git``
* Make sure you have installed Maven and Java 16 or higher with relevant PATH environment variables. If you use
  powershell you could install Java JDK/openJDK and Maven with Chocolatey
  ``choco install openjdk``
  ``choco install maven``
* Launch application from CLI
  ``quarkus dev``
* Launch application from IntelliJ
  Run kravbank-backend (Shift+F10)

## Information regarding live version on Azure Portal

## API

* URL: https://krb-backend-api.azurewebsites.net

## API PostreSQL

* URL: krb-auth-postgres.postgres.database.azure.com

## Keycloak

* URL: https://krb-backend-auth.azurewebsites.net/
* The keycloak server is build by a Keycloak docker image on Azure Container Registry (ACR) server:
    * krbkeycloakauth.azurecr.io
    * Instructions how to pull and push Docker images to ACR are I.e found on "Quick Start" in the Azure Portal resource
    * Credentials can be found under "Access Keys" in the Azure Portal resource

## Keycloak PostreSQL Live

* URL: krb-auth-postgres.postgres.database.azure.com