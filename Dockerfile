FROM openjdk:11

VOLUME /tmp

EXPOSE 8000

ADD ./target/com.case.study.java-0.0.1.jar case-study.jar

ENTRYPOINT ["java","-jar","/case-study.jar"]

