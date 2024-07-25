#Build
FROM maven:3.9.8-eclipse-temurin-17-alpine AS build
WORKDIR /home/app
COPY . /home/app
RUN mvn -f /home/app/pom.xml clean package

#Create image
FROM eclipse-temurin:17-alpine
EXPOSE 8080
COPY --from=build /home/app/target/ApiGetwayService-0.0.1-SNAPSHOT.jar ApiGetwayService-V-0.0.1.jar
ENTRYPOINT ["sh","-c","java -jar /ApiGetwayService-V-0.0.1.jar"]