#FROM openjdk:17-jdk-slim
#
#WORKDIR /app
#
## Build arguments copy pannu
#COPY .mvn/ .mvn
#COPY mvnw .
#COPY pom.xml .
#
## 👇 Maven Wrapper executable ah maaru
#RUN chmod +x mvnw
#
## Dependencies download pannu
#RUN ./mvnw dependency:go-offline
#
## Source code copy pannu
#COPY src ./src
#
## Application build pannu
#RUN ./mvnw clean package -DskipTests
#
## Run the application - CORRECT JAR NAME
#CMD ["java", "-jar", "target/Milk-Analysis-and-post-0.0.1-SNAPSHOT.jar"]



# ✅ Use maintained image — Eclipse Temurin instead of openjdk
FROM eclipse-temurin:17-jdk-jammy AS build

WORKDIR /app

# Copy Maven wrapper & config files
COPY .mvn/ .mvn
COPY mvnw .
COPY pom.xml .

# Make Maven wrapper executable
RUN chmod +x mvnw

# Pre-download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy project source
COPY src ./src

# Build the application JAR
RUN ./mvnw clean package -DskipTests

# =======================================================
# ✅ Final lightweight runtime image (multi-stage build)
# =======================================================
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /app/target/Milk-Analysis-and-post-0.0.1-SNAPSHOT.jar app.jar

# Expose the app port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
