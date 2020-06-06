FROM openjdk:8
ADD target/survey-0.0.1-SNAPSHOT.jar survey-0.0.1-SNAPSHOT.jar
EXPOSE 8085
ENTRYPOINT java -jar -Dspring.profiles.active=dev survey-0.0.1-SNAPSHOT.jar

