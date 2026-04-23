# Setup Instructions

## Requirements
- Java JDK 17+
- Apache Tomcat 10+
- MySQL 8+
- MySQL Connector JAR (mysql-connector-j-8.x.jar)

## Steps

### 1. Database Setup
- Open MySQL Workbench or terminal
- Run: `sql/schema.sql`

### 2. Project Setup (Eclipse/IntelliJ)
- Create a new Dynamic Web Project
- Copy src/ files into src/
- Copy WebContent/ files into WebContent/
- Add MySQL connector JAR to WEB-INF/lib/

### 3. Configure DB Password
- Open `DBConnection.java`
- Set your MySQL password in the PASSWORD field

### 4. Run
- Deploy on Tomcat
- Open: http://localhost:8080/StudentResult/index.html
