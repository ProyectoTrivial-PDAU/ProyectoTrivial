# Etapa 1: compilar el proyecto con Maven
#FROM maven:3.9.6-eclipse-temurin-17 AS build
#WORKDIR /app

# Copiar pom.xml y descargar dependencias
#COPY pom.xml .
#RUN mvn -q dependency:go-offline

# Copiar el código fuente
#COPY src ./src

# Compilar el proyecto (sin tests)
#RUN mvn -q -DskipTests clean package

# Etapa 2: imagen final con solo el JAR
#FROM eclipse-temurin:17-jre
#WORKDIR /app

# Copiar el JAR generado
#COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto que usa Spring Boot
#EXPOSE 8080

# Comando de arranque
#ENTRYPOINT ["java", "-jar", "app.jar"]



# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-17 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml file first to leverage Docker cache
COPY pom.xml .

# Download dependencies - this layer will be cached unless pom.xml changes
RUN mvn dependency:go-offline -B

# Copy the source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:17-jre-alpine

# Install dumb-init for proper signal handling
RUN apk add --no-cache dumb-init

# Create a non-root user to run the application
RUN addgroup -g 1000 spring && adduser -u 1000 -G spring -s /bin/sh -D spring

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Change ownership of the application files
RUN chown -R spring:spring /app

# Switch to the non-root user
USER spring:spring

# Expose the port Spring Boot runs on
ENV PORT=8080
EXPOSE $PORT

# Use dumb-init to run the application
ENTRYPOINT ["dumb-init", "--"]

# Run the Spring Boot application
CMD ["java", "-Xmx512m", "-Dserver.port=${PORT}", "-jar", "app.jar"]
