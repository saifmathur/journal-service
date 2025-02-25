# syntax=docker/dockerfile:1

################################################################################
# Create a stage for resolving and downloading dependencies.
FROM eclipse-temurin:17-jdk-jammy as deps

WORKDIR /build

# Copy pom.xml before downloading dependencies to avoid missing POM errors.
COPY pom.xml pom.xml

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

# Build the application and rename the JAR using the exact filename.
RUN ./mvnw package -DskipTests && \
    echo 'Listing target directory:' && ls -la target/ && \
    mv target/journal-service-0.0.1-SNAPSHOT.jar target/app.jar

################################################################################
# Create a stage for running the application.
FROM eclipse-temurin:17-jre-jammy AS final

# Create user using a more compatible approach for Railway
RUN useradd -m -u 10001 appuser
USER appuser

WORKDIR /app

# Copy the built JAR directly without extraction for simplicity.
COPY --from=package /build/target/app.jar ./

# Final check for app.jar existence
RUN ls -la /app

# Expose dynamic port for Railway compatibility
EXPOSE ${PORT}

# Adjust ENTRYPOINT for Railway compatibility
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]
