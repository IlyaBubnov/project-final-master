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

## Перед запуском приложения

Для запуска приложения необходимо задать значения переменных среды.
В папке doc в файле "read-before-start.md" прописаны наименование и значения этих переменных. 
Edit configurations/Environment variables/User environment variables - здесь необходимо прописать 
все переменные из файла "read-before-start.md".

## Запуск приложения на Docker через консоль

    mvn clean install
    docker build .
    docker images
    docker run ID

Билдим приложение, используя команду "mvn clean install".
Затем командой "docker build ." собираем образ (image). Вместо точки можно прописать полный путь к файлу Dockerfile.
Для запуска используем команду "docker run ID". Вместо ID необходимо указать ID вашего созданного контейнера. Для
этого командой "docker images" находим все образы и оттуда берем ID.
  
## Список выполненных задач:
- 1). Разобраться со структурой проекта (onboarding).
- 2). Удалить социальные сети: vk, yandex.
- 3). Вынести чувствительную информацию в отдельный проперти файл:
    * логин
    * пароль БД
    * идентификаторы для OAuth регистрации/авторизации
    * настройки почты
  
    Значения этих проперти должны считываться при старте сервера из переменных окружения машины.
- 5). Написать тесты для всех публичных методов контроллера ProfileRestController. Хоть методов только 2, 
но тестовых методов должно быть больше, т.к. нужно проверить success and unsuccess path.
- 6). Сделать рефакторинг метода com.javarush.jira.bugtracking.attachment.FileUtil#upload, 
чтобы он использовал современный подход для работы с файловой системмой.
- 9). Написать Dockerfile для основного сервера.

* Создание и коннект с базой данных на PostgreSQL. Изменил порт "5432" на "5433"
и наименование БД "jira" на "postgres", т.к. не мог подключиться к БД и запуститься
в браузере.

* Пытался переопределить значения свойств почты по умолчанию, установив переменные среды,
но тесты посыпались, поэтому оставил так, как есть.
