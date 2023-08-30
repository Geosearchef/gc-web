#FROM gradle:7.5.1-jdk18-alpine AS build
FROM gradle:7.5.1-jdk18-focal AS build

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle shadowJar --no-daemon


FROM amazoncorretto:18

EXPOSE 12240

RUN mkdir /app
RUN mkdir /app/static

COPY --from=build /home/gradle/src/build/libs/*.jar /app/gc-web.jar
COPY --from=build /home/gradle/src/build/distributions/ /app/static/

# Development only
COPY gcweb-keystore.jks /app/gcweb-keystore.jks

WORKDIR /app
ENTRYPOINT ["java", "-jar", "gc-web.jar"]
