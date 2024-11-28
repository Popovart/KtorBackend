FROM openjdk:17-jdk-slim

ADD cloud-sql-proxy /cloud-sql-proxy
RUN chmod +x /cloud-sql-proxy

# work directory
WORKDIR /app

COPY build/libs/KtorBackEnd-all.jar app.jar

COPY secrets/key.json /secrets/key.json

EXPOSE 8080

CMD [ "sh", "-c", "./cloud-sql-proxy -instances=my-project:us-central1:fancyquizzes=tcp:5432 -credential_file=/secrets/key.json & java -jar app.jar" ]
