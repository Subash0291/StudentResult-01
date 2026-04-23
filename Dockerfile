FROM tomcat:10.1-jdk17

# Remove default webapps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy project files
COPY WebContent/ /usr/local/tomcat/webapps/ROOT/
COPY src/ /tmp/src/

# Download MySQL connector
RUN mkdir -p /usr/local/tomcat/webapps/ROOT/WEB-INF/lib && \
    mkdir -p /usr/local/tomcat/webapps/ROOT/WEB-INF/classes

# Copy MySQL JAR if exists
COPY WebContent/WEB-INF/lib/ /usr/local/tomcat/webapps/ROOT/WEB-INF/lib/

# Compile Java files
RUN javac -cp "/usr/local/tomcat/lib/servlet-api.jar:/usr/local/tomcat/webapps/ROOT/WEB-INF/lib/*" \
    -d /usr/local/tomcat/webapps/ROOT/WEB-INF/classes \
    /tmp/src/*.java

# Copy web.xml
COPY WebContent/WEB-INF/web.xml /usr/local/tomcat/webapps/ROOT/WEB-INF/web.xml

EXPOSE 8080
CMD ["catalina.sh", "run"]
