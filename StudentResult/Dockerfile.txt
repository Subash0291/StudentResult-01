FROM tomcat:10.1-jdk17

RUN rm -rf /usr/local/tomcat/webapps/*

RUN mkdir -p /usr/local/tomcat/webapps/ROOT/WEB-INF/lib && \
    mkdir -p /usr/local/tomcat/webapps/ROOT/WEB-INF/classes && \
    mkdir -p /usr/local/tomcat/webapps/ROOT/css

RUN curl -L "https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar" \
    -o /usr/local/tomcat/webapps/ROOT/WEB-INF/lib/mysql-connector-j-8.0.33.jar

COPY StudentResult/WebContent/index.html /usr/local/tomcat/webapps/ROOT/
COPY StudentResult/WebContent/css/style.css /usr/local/tomcat/webapps/ROOT/css/
COPY StudentResult/WebContent/WEB-INF/web.xml /usr/local/tomcat/webapps/ROOT/WEB-INF/
COPY StudentResult/src/ /tmp/src/

RUN javac -cp "/usr/local/tomcat/lib/servlet-api.jar:/usr/local/tomcat/webapps/ROOT/WEB-INF/lib/*" \
    -d /usr/local/tomcat/webapps/ROOT/WEB-INF/classes \
    /tmp/src/*.java

EXPOSE 8080
CMD ["catalina.sh", "run"]
