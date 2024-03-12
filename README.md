## Домашнее задание по лекции "Основные компоненты"

### Выполнено:
- На старте открывается первая Activity.
- Из первой Activity открывается вторая.
- На второй Activity запускается Service.
- Service получает список контактов через ContentProvider.
- После завершения работы Service возвращает данные вторую Activity с помощью LocalBroadcastReceiver.
- Вторая Activity после получения ответа из LocalBroadcastReceiver передает данные в первую Activity.
- Первая Activity после получения ответа от второй Activity отображает результат.
- Задание в вынесено в отдельный модуль.

[*Видео работы приложения*](https://gitlab.com/mi9moq/tfs_spring_2024/-/blob/homework_1/homework_1/src/main/res/video/homework1.webm?ref_type=heads)