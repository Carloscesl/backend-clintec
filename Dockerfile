# ── ETAPA 1: Compilar ──────────────────────────────
FROM eclipse-temurin:21.0.10_7-jdk AS builder

WORKDIR /root

COPY ./pom.xml .
COPY ./.mvn .mvn
COPY ./mvnw .

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

COPY ./src ./src
RUN ./mvnw clean package -DskipTests

# ── ETAPA 2: Imagen final liviana ──────────────────
FROM eclipse-temurin:21-jre

WORKDIR /root

COPY --from=builder /root/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/root/app.jar"]