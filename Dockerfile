FROM openjdk:17-jdk-slim

WORKDIR /app

# Build arguments copy pannu
COPY .mvn/ .mvn
COPY mvnw .
COPY pom.xml .

# Dependencies download pannu
RUN ./mvnw dependency:go-offline

# Source code copy pannu
COPY src ./src

# Application build pannu
RUN ./mvnw clean package -DskipTests

# Run the application
CMD ["java", "-jar", "target/milkanalysis-0.0.1-SNAPSHOT.jar"]