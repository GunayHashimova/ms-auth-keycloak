FROM openjdk:11.0.5

WORKDIR /code/
COPY ./build/libs/ms-auth-keycloak.jar .

EXPOSE 80

ENTRYPOINT ["java", "-Xmx1500m", "-jar", "ms-auth-keycloak.jar", "--server.port=80"]
