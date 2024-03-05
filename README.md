# Задание

# Авторизация

В сервис `rates` была добавлена авторизация. Необходимо научить `converter` ее использовать

Вам необходимо добавить авторизацию в сервис `converter`. И научить `accounts` ее использовать

Сервис `accounts` оставить без авторизации

Для получения токена использовать `POST ${keycloak_url}/realms/${realm}/protocol/openid-connect/token`

Для проверки пришедшего токена `${keycloak_url}/realms/${realm}/protocol/openid-connect/certs`

Для авторизации используется **client credentials flow**. Все данные будут переданы через переменные окружения
# Тестирование

## Переменные окружения

Ваши приложения должны работать со следующими переменными окружения

Accounts:
* **DB_HOST** - хост БД
* **DB_PORT** - порт БД
* **DB_NAME** - название БД
* **DB_USER** - пользователь БД
* **DB_PASSWORD** - пароль для подключения к БД
* **CONVERTER_URL** - адрес конвертера вида http://smth:1234
* **KEYCLOAK_URL** - адрес киклоки вида http://smth:1234
* **KEYCLOAK_REALM** - realm, в котором живет клиент
* **CLIENT_ID** - client-id
* **CLIENT_SECRET** - client-secret

Converter:
* **RATES_URL** - адрес сервиса курсов валют вида http://smth:1234
* **KEYCLOAK_URL** - адрес киклоки вида http://smth:1234
* **KEYCLOAK_REALM** - realm, в котором живет клиент
* **CLIENT_ID** - client-id
* **CLIENT_SECRET** - client-secret

При установке хелм чарта переменные окружения будут передаваться через переменные чарта.
Для этого нужно добавить в свои чарты поддержку переменной extraEnv

```yml
extraEnv:
  - name: ENV_NAME
    value: some-value
```

Чарт должен уметь работать с переменной-массивом и все параметры передавать в переменные окружения пода

В свой воркфлоу сборки добавить новую джобу

```yaml
jobs:
  autotest:
    needs: $build_job_name # имя вашей основной джобы сборки
    uses: central-university-dev/hse-ab-cicd-hw/.github/workflows/autotests-hw4.yml@main
    with:
      chart-path: ./rates # путь к чарту из второй дз
      converter-image-name: foo/bar-converter # имя образа вашего приложения
      accounts-image-name: foo/bar-accounts # имя образа вашего приложения
      image-tag: $branch_name-$commit_hash # таг образа, который собран в рамках данного ПРа
```