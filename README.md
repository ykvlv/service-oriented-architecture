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

* haproxy: порт 9200
* напрямую:
  * первые сервисы -- 9099, 9199 
  * вторые сервисы -- 9100, 9102

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

## Consul, Haproxy

Конфигурацию Haproxy можно найти в [backend/haproxy.cfg](backend/haproxy.cfg)

В нём настройка для server-template через консул для первых сервисов(экземпляров)

Также решить Service Discovery можно и через специальный скрипт который обращается к консулу и узнаёт о развёрнутых на нём экземплярах сервисов, далее фильтрует из них те, что относятся к первому сервису(в данном случае это все) и добавляет об этом информацию в haproxy.cfg. После надо перезапустить haproxy

Для просмотра статистики и мониторинга:

* Haproxy: http://localhost:8404/stats
* Consul: http://localhost:8500/
