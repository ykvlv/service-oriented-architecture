# service-oriented-architecture

## Бэкенд

Собрать

```shell
mvn clean install
```

Запустить все сервисы

```shell
docker compose up
```

Обращение к сервисам

* ticket сервис – 9099
* booking сервис – 9100

## Фронтенд

https://se.ifmo.ru/~s311727/soa/

Собрать

```shell
npm install
rm -rf .shadow-cljs && npx shadow-cljs release app
```

Деплой на хелиос

```shell
rsync -av -e "ssh -p 2222" ./public/ s311727@se.ifmo.ru:~/public_html/soa/
```

Чтобы норм подключиться

```shell
/Applications/Chromium.app/Contents/MacOS/Chromium --ignore-certificate-errors
```

## Mule ESB

что это вообще такое
