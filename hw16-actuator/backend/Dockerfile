# syntax=docker/dockerfile:1
ARG JDK_IMAGE="bellsoft/liberica-openjdk-alpine-musl:21@sha256:8eb384b8cf534a943e716378d089cfa67579f359eeddee7266bdca248e5b2b26"
ARG JRE_IMAGE="bellsoft/liberica-openjre-alpine-musl:21@sha256:e835f0dee6a30cab8c2a25363c78dfeb5a9d27307df96853889c43b6ab9ccfb6"
ARG APP_DIR="/app/book-lib-manager"

# compile artifact
FROM ${JDK_IMAGE} as builder

ARG APP_DIR
WORKDIR ${APP_DIR}

COPY .mvn .mvn
COPY mvnw pom.xml .
RUN ./mvnw -B dependency:go-offline

COPY src ./src

RUN ./mvnw -B clean package -DskipTests

# create app image
FROM ${JRE_IMAGE}

LABEL maintainer="Otus Spring Framework course student"
LABEL description="Book library manager demo image"
LABEL version="0.0.1-demo"

ARG APP_DIR
WORKDIR ${APP_DIR}
COPY --from=builder ${APP_DIR}/target/*.jar ${APP_DIR}/blm.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "blm.jar"]