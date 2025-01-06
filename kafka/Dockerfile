# Use the official OpenJDK 17 image as a base image
FROM openjdk:17-jdk-slim

# Install netcat
RUN apt-get update && apt-get install -y netcat

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/kafka-service.jar /app/kafka-service.jar

# Expose the port your application listens on
EXPOSE 8080

# Run as a non-root user (optional but recommended for security)
RUN adduser --disabled-password --gecos "" myuser
USER myuser

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/kafka-service.jar"]
