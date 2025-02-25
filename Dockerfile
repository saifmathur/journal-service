# syntax=docker/dockerfile:1

################################################################################
# Create a stage for resolving and downloading dependencies.
FROM eclipse-temurin:17-jdk-jammy as deps

WORKDIR /build

# Copy the mvnw wrapper with executable permissions.
COPY --chmod=0755 mvnw mvnw
COPY .mvn/ .mvn/

# Download dependencies without cache mounts for Railway compatibility.
RUN ./mvnw dependency:go-offline -DskipTests

################################################################################
# Create a stage for building the application based on the stage with downloaded dependencies.
FROM deps as package

WORKDIR /build

COPY ./src src/
COPY pom.xml pom.xml

# Build the application and rename the JAR using a shell form to avoid parsing issues.
RUN /bin/sh -c "./mvnw package -DskipTests && mv target/*.jar target/app.jar"

################################################################################
# Create a stage for extracting the application into separate layers.
FROM package as extract

WORKDIR /build

RUN java -Djarmode=layertools -jar target/app.jar extract --destination target/extracted

################################################################################
# Create a new stage for running the application.
FROM eclipse-temurin:17-jre-jammy AS final

# Create user using a more compatible approach for Railway
RUN useradd -m -u 10001 appuser
USER appuser

WORKDIR /app

COPY --from=extract /build/target/extracted/dependencies/ ./
COPY --from=extract /build/target/extracted/spring-boot-loader/ ./
COPY --from=extract /build/target/extracted/snapshot-dependencies/ ./
COPY --from=extract /build/target/extracted/application/ ./

# Expose dynamic port for Railway compatibility
EXPOSE ${PORT}

# Adjust ENTRYPOINT for Railway compatibility
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]
