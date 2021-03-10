FROM amazoncorretto:11 as builder

RUN yum install -y vim wget unzip

# install gradle
RUN wget -nv https://services.gradle.org/distributions/gradle-6.8-bin.zip -P /tmp && unzip -q -d /opt/gradle /tmp/gradle-*.zip
ENV GRADLE_HOME=/opt/gradle/gradle-6.8
ENV PATH=${GRADLE_HOME}/bin:${PATH}

RUN mkdir /app
WORKDIR /app

COPY . /app

RUN gradle clean build && gradle jar

FROM amazoncorretto:11 as runner

WORKDIR /app

COPY --from=builder /opt/gradle /opt/gradle

ENV GRADLE_HOME=/opt/gradle/gradle-6.8
ENV PATH=${GRADLE_HOME}/bin:${PATH}

COPY --from=builder /app/app/build/libs/app.jar /bin/app.jar
CMD ["java", "-jar", "/bin/app.jar"]
