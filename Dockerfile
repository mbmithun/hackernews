FROM maven:3.6.3-jdk-8 as maven

COPY ./pom.xml ./pom.xml

RUN mvn dependency:go-offline -B

COPY ./src ./src

RUN mvn package -DskipTests

FROM openjdk:8

WORKDIR /hackernews

COPY --from=maven target/hackernews-1.0.jar ./

CMD ["java", "-jar", "./hackernews-1.0.jar"]