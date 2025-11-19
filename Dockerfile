# ============================================
# ETAPA 1: Construcción
# ============================================
# Imagen base estable con JDK 21
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copiar archivos de Gradle
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY gradlew ./

# Permisos
RUN chmod +x gradlew

# Actualizar certificados SSL (CRÍTICO)
RUN apt-get update && apt-get install -y ca-certificates && update-ca-certificates

# Forzar TLS moderno
ENV JAVA_TOOL_OPTIONS="-Djdk.tls.client.protocols=TLSv1.2,TLSv1.3"

# ============================================
# ETAPA 1: Construcción
# ============================================
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copiar gradle wrapper y archivos
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle settings.gradle ./

# Dar permisos al wrapper
RUN chmod +x gradlew

# Actualizar certificados SSL
RUN apt-get update && apt-get install -y ca-certificates && update-ca-certificates

# Forzar protocolo TLS moderno
ENV JAVA_TOOL_OPTIONS="-Djdk.tls.client.protocols=TLSv1.2,TLSv1.3"

# Descargar dependencias
RUN ./gradlew dependencies --no-daemon || true

# Copiar código fuente
COPY src ./src

# Build sin tests
RUN ./gradlew clean build -x test --no-daemon


# ============================================
# ETAPA 2: Runtime
# ============================================
FROM eclipse-temurin:21-jre-alpine

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app*
