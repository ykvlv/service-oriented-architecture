# service-oriented-architecture

Собрать

```shell
mvn clean install
```

Запустить первый сервис

```shell
java -jar ticket-service/target/ticket-service.jar
```

Запустить второй сервис

```shell
java -jar ~/payara-micro-5.2022.5.jar --port 8888 --sslport 9090 booking-service/target/booking-service.war
```

Чтобы норм подключиться

```shell
/Applications/Chromium.app/Contents/MacOS/Chromium --ignore-certificate-errors
```