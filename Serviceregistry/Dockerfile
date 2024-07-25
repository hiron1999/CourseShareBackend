#Build
FROM maven:3.9.8-eclipse-temurin-17-alpine AS build
WORKDIR /home/app
COPY . /home/app
RUN mvn -f /home/app/pom.xml clean package

#Create image
FROM eclipse-temurin:17-alpine
EXPOSE 8761
COPY --from=build /home/app/target/Serviceregistry-0.0.1-SNAPSHOT.jar Serviceregistry-V-0.0.1.jar
ENTRYPOINT ["sh","-c","java -jar /Serviceregistry-V-0.0.1.jar"]