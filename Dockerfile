FROM amazoncorretto:11

RUN yum install -y vim wget unzip

RUN wget -nv https://services.gradle.org/distributions/gradle-6.8-bin.zip -P /tmp \
  && unzip -q -d /opt/gradle /tmp/gradle-*.zip

ENV GRADLE_HOME=/opt/gradle/gradle-6.8
ENV PATH=${GRADLE_HOME}/bin:${PATH}

RUN mkdir /app
WORKDIR /app
