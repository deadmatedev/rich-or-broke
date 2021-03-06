[![build](https://github.com/deadmatedev/rich-or-broke/actions/workflows/build.yml/badge.svg)](https://github.com/deadmatedev/rich-or-broke/actions/workflows/build.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=deadmatedev_rich-or-broke&metric=alert_status)](https://sonarcloud.io/dashboard?id=deadmatedev_rich-or-broke)
[![codecov](https://codecov.io/gh/deadmatedev/rich-or-broke/branch/master/graph/badge.svg?token=CB2G17JVDP)](https://codecov.io/gh/deadmatedev/rich-or-broke)
[![jacoco](https://img.shields.io/badge/jacoco-100%25-green)](https://github.com/deadmatedev/rich-or-broke/suites/3686775480/artifacts/89833041)
[![release](https://img.shields.io/github/v/release/deadmatedev/rich-or-broke)](https://github.com/deadmatedev/rich-or-broke/releases/tag/v1.0)
[![dockerhub](https://img.shields.io/docker/pulls/deadmate/rich-or-broke.svg)](https://hub.docker.com/repository/docker/deadmate/rich-or-broke)


# rich-or-broke

A simple Spring Boot microservice that returns random gifs based on exchange rates. It exposes just a single GET
endpoint. Given a 3-letter ISO currency code, returns some
random `.gif` file from https://giphy.com based on the exchange
rate trend calculated for that currency code. Exchange rates are provided by https://openexchangerates.org. Base currency is `USD` but it is customizable. In case of an uptrend, the animation is `rich`, otherwise it is `broke`.

## Live Demo

With this microservice you can, for example, visualize today's `RUB/USD` exchange rate trend in a funny `.gif` animation by simply fetching this URL: http://kakoo.hopto.org/trend/RUB 

[![RUB/USD](http://kakoo.hopto.org/trend/RUB)](http://kakoo.hopto.org/trend/RUB)

The app is deployed here, feel free to play around with it:

* http://kakoo.hopto.org/trend/USD
* http://kakoo.hopto.org/trend/EUR
* http://kakoo.hopto.org/trend/BTC
* http://kakoo.hopto.org/trend/SLL
* http://kakoo.hopto.org/trend/RUB

The list of supported currency codes:

* https://docs.openexchangerates.org/docs/supported-currencies

## Build

To build and test the app, run:

`./gradlew clean build`

Jacoco shows 100% code coverage, the report is located at:

`./build/reports/jacoco/test/html/index.html`

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

To build `rich-or-broke:1.0` app as a docker image and push it to https://hub.docker.com/, run:

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
* edit `docker-compose.yaml` (uncomment `environment` section)
* or run:

`export BASE_CURRENCY=RUB`

`export UPTREND_QUERY=happy`

`docker run -it -p 0.0.0.0:8080:8080 deadmate/rich-or-broke:1.0`

The list of supported customizations you can find here:

`./src/main/resources/application.yaml` 
