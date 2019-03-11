FROM openjdk:8-alpine

COPY target/uberjar/pweb-app.jar /pweb-app/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/pweb-app/app.jar"]
