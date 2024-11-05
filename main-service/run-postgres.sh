#!/bin/bash

docker stop db || true
docker rm db || true

docker run -d \
  --name main-db \
  -e POSTGRES_DB=main_db \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  postgres:16.1

if [ $? -eq 0 ]; then
  echo "База данных 'db' успешно создана и запущена."
else
  echo "Не удалось создать или запустить базу данных."
fi