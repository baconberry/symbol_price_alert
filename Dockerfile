FROM eclipse-temurin:21-alpine as builder

WORKDIR /app

COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .

RUN ./mvnw -B dependency:resolve

COPY . .

RUN ./mvnw -B package


FROM eclipse-temurin:21-jre-alpine as runner

COPY --from=builder /app/target/*.jar /app/app.jar

CMD ["java", "-jar", "/app/app.jar"]