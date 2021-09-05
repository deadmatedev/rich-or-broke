[![gradle build](https://github.com/deadmatedev/rich-or-broke/actions/workflows/gradle-publish.yml/badge.svg)](https://github.com/deadmatedev/rich-or-broke/actions/workflows/gradle-publish.yml)
[![jacoco](https://img.shields.io/badge/coverage-100%25-green)(https://github.com/deadmatedev/rich-or-broke/suites/3686775480/artifacts/89833041)]
[![dockerhub](https://img.shields.io/docker/pulls/deadmate/rich-or-broke.svg)](https://hub.docker.com/repository/docker/deadmate/rich-or-broke)

# rich-or-broke

A simple Spring Boot microservice that returns random gifs based on exchange rates. It exposes just a single GET
endpoint. Given a 3-letter ISO currency code, returns some
random `.gif` file from https://giphy.com based on the exchange
rate trend calculated for that currency code. Exchange rates are provided by https://openexchangerates.org. Base currency is `RUB` but it is customizable. In case of an uptrend, the animation is `rich`, otherwise it is `broke`.

## Live Demo

The app is deployed here:

* http://kakoo.hopto.org/trend/USD

The list of supported currency codes:

* https://docs.openexchangerates.org/docs/supported-currencies

## Build

To build and test the app, run:

`./gradlew clean build`

Jacoco shows 100% code coverage, the report is located at:

`./build/reports/jacoc/test/html/index.html`

## Run

To run the app:

`java -jar ./build/libs/rich-or-broke-1.0.jar`

Open your browser and head over to:

* http://localhost:8080/trend/USD
* http://localhost:8080/trend/EUR
* http://localhost:8080/trend/RUB
* http://localhost:8080/trend/BTC
* http://localhost:8080/trend/ABC

Try refreshing the page with Ctrl+F5...

# Docker

## Build

To build `rich-or-broke:1.0` app as a docker image and it push to https://hub.docker.com/, run:

`export DOCKERHUB_USER=<user>`

`export DOCKERHUB_PWD=<password>`

`./gradlew clean build jib`

## Run

To run a pre-built docker image:

`docker compose up`

or

`docker run -it -p 0.0.0.0:8080:8080 deadmate/rich-or-broke:1.0`

## Customize 

To customize app's behaviour there's no need to recompile it. Before running the app simply pass new settings to it via environment variables.

You can either:
* edit `docker-compse.yaml` (uncomment `environment` section)
* or run:

`export BASE_CURRENCY=RUB`

`export UPTREND_QUERY=happy`

`docker run -it -p 0.0.0.0:8080:8080 deadmate/rich-or-broke:1.0`

The list of supported customizations you can find here:

`./src/main/resources/application.yaml` 
