# ============================================
# ETAPA 1: Construcción (Build Stage)
# ============================================
# Usamos una imagen con Gradle y JDK 21
FROM gradle:8.5-jdk21 AS build

# Establecer directorio de trabajo
WORKDIR /app

# Copiar archivos de configuración de Gradle
COPY build.gradle settings.gradle ./

# Copiar el wrapper de Gradle
COPY gradlew ./
COPY gradle ./gradle

# Dar permisos de ejecución al gradlew
RUN chmod +x gradlew

# Descargar dependencias (se cachea si no cambian los archivos .gradle)
RUN gradle dependencies --no-daemon || return 0

# Copiar el código fuente
COPY src ./src

# Compilar la aplicación y crear el JAR
# -x test: omite los tests para build más rápido
# --no-daemon: no deja procesos en segundo plano
RUN gradle clean build -x test --no-daemon

# ============================================
# ETAPA 2: Ejecución (Runtime Stage)
# ============================================
# Usamos solo el JRE (Java Runtime Environment) - más ligero
FROM eclipse-temurin:21-jre-alpine

# Crear un usuario no-root por seguridad
RUN addgroup -S spring && adduser -S spring -G spring

# Cambiar al usuario spring
USER spring:spring

# Directorio de trabajo
WORKDIR /app

# Copiar el JAR desde la etapa de build
# El * permite copiar sin saber el nombre exacto del JAR
COPY --from=build /app/build/libs/*.jar app.jar

# Exponer el puerto 8080 (puerto por defecto de Spring Boot)
EXPOSE 8080

# Variables de entorno por defecto
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Comando para ejecutar la aplicación
# ${JAVA_OPTS} permite configurar memoria desde docker-compose
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]
