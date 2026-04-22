# IMAGEN MODELO
FROM eclipse-temurin:21.0.10_7-jdk

# PUERTO DONDE SE EJECUTA EL CONTENEDOR(INFORMATIVO)
EXPOSE 8080

#DEFINIR DIRECTORIO RAIZ DEL CONTENEDOR
WORKDIR /root

# COPIAR Y PEGAR ARCHIVOS DENTRO DEL CONTENEDOR
COPY ./pom.xml /root
COPY ./.mvn /root/.mvn
COPY ./mvnw /root

# Asegurar permisos de ejecución
RUN chmod +x mvnw

# DESCARGAR LAS DEPENDECIAS
RUN ./mvnw dependency:go-offline

# COPIAR CODIGO FUENTE
COPY ./src /root/src

# CONSTRUIR APLICACION
RUN ./mvnw clean install -DskipTests

# LEVANTAR APLICACION CUANDO EL CONTENEDOR SE INICIE
ENTRYPOINT ["java", "-jar", "/root/target/backend-clintec-0.0.1-SNAPSHOT.jar"]
