FROM openjdk:23-jdk-oracle AS builder

ARG COMPILE_DIR=/compiledir

WORKDIR ${COMPILE_DIR}

COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .
COPY .mvn .mvn
COPY src src

# RUN chmod a+x ./mvnw && ./mvnw package -Dmaven.test.skip=true
RUN ./mvnw package -Dmaven.test.skip=true

# ENV PORT=4000
# EXPOSE ${PORT}
ENV SERVER_PORT=4000
EXPOSE ${SERVER_PORT}

ENV SPRING_DATA_REDIS_HOST=localhost SPRING_DATA_REDIS_PORT=6379
ENV SPRING_DATA_REDIS_USERNAME="" SPRING_DATA_REDIS_PASSWORD=""
ENV FILEPATH=

# Not needed as app will run in 2nd stage
# ENTRYPOINT java -jar target/practice-test-0.0.1-SNAPSHOT.jar

## Second stage
FROM openjdk:23-jdk-oracle

ARG WORKDIR=/app
WORKDIR ${WORKDIR}

COPY --from=builder /compiledir/target/practice-test-0.0.1-SNAPSHOT.jar practice-test.jar

# ENV PORT=4000
# EXPOSE ${PORT}
ENV SERVER_PORT=4000
EXPOSE ${SERVER_PORT}

ENV SPRING_DATA_REDIS_HOST=localhost SPRING_DATA_REDIS_PORT=6379
ENV SPRING_DATA_REDIS_USERNAME="" SPRING_DATA_REDIS_PASSWORD=""
ENV FILEPATH=

# SHELL [ "/bin/sh", "-c" ]

# ENTRYPOINT SERVER_PORT=${PORT} java -jar practice-test.jar
ENTRYPOINT java -jar practice-test.jar
