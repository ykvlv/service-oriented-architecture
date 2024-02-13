# service-oriented-architecture

## Бэкенд

Запускать сервисы через

```shell
mvn spring-boot:run
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

Скачиваем Mule Standalone с официального сайта, запускаем

```shell
sudo ./bin/mule
```

Деплоим артефакт прямо в работающий Mule

```shell
export MULE_HOME=/Users/ykvlv/Downloads/mule-enterprise-standalone-4.5.2/
mvn clean deploy -DmuleDeploy
```

Конфигурация находится в [mule-booking-service.xml](mule-booking-service/src/main/mule/mule-booking-service.xml). Сервис поднимается на порту 9101
