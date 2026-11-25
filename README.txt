Папката съдържа папка с проекта и документацията на проекта.
Структура на проекта

src/main/java/

WebsocketChatApplication.java → main клас за стартиране на приложението.
config/ → WebSocket конфигурация.
controller/ → обработка на чат съобщения.
service/ → логика за стаи и потребители.
model/ → класове за данни.

src/main/resources/

static/ → HTML, CSS, JS (клиент).

application.properties → настройки.

Проeктът  се стартира от WebsocketChatApplication.java (main метода) или чрез Maven команда mvn spring-boot:run

Изготвил: Сибел Башова
Специалност: Разпределени системи и мобилни технологии
ФН: 2MI3400511