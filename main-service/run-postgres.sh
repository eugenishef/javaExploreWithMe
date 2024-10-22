#!/bin/bash

docker stop main-db || true
docker rm main-db || true

docker run -d \
  --name main-db \
  -e POSTGRES_DB=main_db \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5433:5432 \
  postgres:16.1

if [ $? -eq 0 ]; then
  echo "База данных 'main_db' успешно создана и запущена."
else
  echo "Не удалось создать или запустить базу данных."
fi