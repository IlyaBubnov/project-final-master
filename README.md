## [REST API](http://localhost:8080/doc)

## Концепция:

- Spring Modulith
    - [Spring Modulith: достигли ли мы зрелости модульности](https://habr.com/ru/post/701984/)
    - [Introducing Spring Modulith](https://spring.io/blog/2022/10/21/introducing-spring-modulith)
    - [Spring Modulith - Reference documentation](https://docs.spring.io/spring-modulith/docs/current-SNAPSHOT/reference/html/)

```
  url: jdbc:postgresql://localhost:5432/jira
  username: jira
  password: JiraRush
```

- Есть 2 общие таблицы, на которых не fk
    - _Reference_ - справочник. Связь делаем по _code_ (по id нельзя, тк id привязано к окружению-конкретной базе)
    - _UserBelong_ - привязка юзеров с типом (owner, lead, ...) к объекту (таска, проект, спринт, ...). FK вручную будем
      проверять

## Аналоги

- https://java-source.net/open-source/issue-trackers

## Тестирование

- https://habr.com/ru/articles/259055/
  
## Список выполненных задач:
1. Разобраться со структурой проекта (onboarding). 
2. Сделать рефакторинг метода com.javarush.jira.bugtracking.attachment.FileUtil#upload, чтобы он использовал современный подход для работы с файловой системмой.
3. Удалить социальные сети: vk, yandex.
4. Вынести чувствительную информацию в отдельный проперти файл:
   * логин
   * пароль БД
   * идентификаторы для OAuth регистрации/авторизации
   * настройки почты
   Значения этих проперти должны считываться при старте сервера из переменных окружения машины.

   * Создание и коннект с базой данных на PostgreSQL. Изменил порт "5432" на "5433"
   и наименование БД "jira" на "postgres", т.к. не мог подключиться к БД и запуститься
   в браузере.
   
   * Пытался переопределить значения свойств почты по умолчанию, установив переменные среды,
   но тесты посыпались, поэтому оставил так, как есть.
   
   * Переменные среды:
   - CLIENT_REGISTRATION_GITHUB_CLIENTID=3d0d8738e65881fff266;
   - CLIENT_REGISTRATION_GITHUB_CLIENTSECRET=0f97031ce6178b7dfb67a6af587f37e222a16120;
   - CLIENT_REGISTRATION_GITLAB_CLIENTID=b8520a3266089063c0d8261cce36971defa513f5ffd9f9b7a3d16728fc83a494;
   - CLIENT_REGISTRATION_GITLAB_CLIENTSECRET=e72c65320cf9d6495984a37b0f9cc03ec46be0bb6f071feaebbfe75168117004;
   - CLIENT_REGISTRATION_GOOGLE_CLIENTID=329113642700-f8if6pu68j2repq3ef6umd5jgiliup60.apps.googleusercontent.com;
   - CLIENT_REGISTRATION_GOOGLE_CLIENTSECRET=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;
   - DB_HOST=jdbc:postgresql://localhost;
   - DB_PASSWORD=JiraRush;
   - DB_PORT=5433;
   - DB_USERNAME=jira