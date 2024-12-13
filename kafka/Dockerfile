# Use the official OpenJDK 17 image as a base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/kafka-service.jar /app/kafka-service.jar

# Expose the port your application listens on (optional)
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/kafka-service.jar"]
