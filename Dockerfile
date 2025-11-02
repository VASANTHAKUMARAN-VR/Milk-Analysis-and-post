FROM openjdk:17-jdk-slim

WORKDIR /app

# Build arguments copy pannu
COPY .mvn/ .mvn
COPY mvnw .
COPY pom.xml .

# 👇 Maven Wrapper executable ah maaru
RUN chmod +x mvnw

# Dependencies download pannu
RUN ./mvnw dependency:go-offline

# Source code copy pannu
COPY src ./src

# Application build pannu
RUN ./mvnw clean package -DskipTests

# Run the application - CORRECT JAR NAME
CMD ["java", "-jar", "target/Milk-Analysis-and-post-0.0.1-SNAPSHOT.jar"]