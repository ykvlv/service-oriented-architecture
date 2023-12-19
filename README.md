# service-oriented-architecture

## Бэкенд

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

## Фронтенд

https://se.ifmo.ru/~s311727/soa/public/

Собрать

```shell
rm -rf .shadow-cljs && npx shadow-cljs release app
```

Деплой на хелиос

```shell
scp -P 2222 -r ./public s311727@se.ifmo.ru:~/soa/public
```

Чтобы норм подключиться

```shell
/Applications/Chromium.app/Contents/MacOS/Chromium --ignore-certificate-errors
```