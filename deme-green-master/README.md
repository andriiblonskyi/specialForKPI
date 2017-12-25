Instructions to run in Docker

1. Execute a jar using ./mvnw package && java -jar target/"jar-name".jar

2. Create an image using ./mvnw install dockerfile:build

3. Run docker run -p 8090:8090 -t "image-name"