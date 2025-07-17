diablo-clone-client/
│
├─ src/
│  ├─ main/
│  │  ├─ GameApp.java                     // Точка входа
│  │  ├─ game/                            // Основной игровой цикл и логика
│  │  │  ├─ GameLoop.java                 // Игровой цикл (AnimationTimer)
│  │  │  ├─ GameEngine.java          // Обновления и системы
│  │  │  ├─ GameState.java                // Состояние игры (модель)


│  │  │  └─ InputHandler.java             // Обработка пользовательского ввода
│  │  ├─ graphics/                        // Графика и визуализация
│  │  │  ├─ IsoRenderer.java              // Отрисовка изометрической карты
│  │  │  ├─ EntityRenderer.java           // Отрисовка игроков, врагов, предметов
│  │  │  └─ Assets.java                   // Загрузка и кеширование текстур
│  │  ├─ model/                           // Игровые сущности и модели
│  │  │  ├─ entities/                     // Игроки, враги, NPC
│  │  │  │  ├─ Player.java                // Игрок
│  │  │  │  ├─ Monster.java               // Монстр
│  │  │  │  └─ Entity.java                // Абстрактная сущность
│  │  │  ├─ map/                          // Плитки и карты

│  │  │  │  ├─ MapGenerator.java          // Процедурная генерация карт
│  │  │  │  ├─ MapLoader.java             // Загрузка карты из JSON
│  │  │  │  ├─ MapManager.java            // Обработчик карты
│  │  │  │  ├─ Tile.java                  // Плитка
│  │  │  │  ├─ GameMap.java               // Модель карты
│  │  │  │  └─ TileType.java              // Типы плиток
│  │  │  ├─ items/                        // Игровые предметы
│  │  │  │  ├─ Item.java                  // Предмет
│  │  │  │  └─ ItemType.java              // Типы предметов
│  │  │  └─ stats/                        // Статы и параметры
│  │  │     ├─ Stats.java                 // Общая модель статов
│  │  │     └─ Attributes.java            // Сила, ловкость, интеллект
│  │  ├─ network/                         // Сетевое взаимодействие (позже)
│  │  │  ├─ NetworkClient.java            // Клиент HTTP / WebSocket
│  │  │  └─ ApiService.java               // REST API вызовы
│  │  ├─ util/                            // Утилиты
│  │  │  ├─ CursorSpritesheetManager.java // Курсор
│  │  │  └─ FontManager.java              // Шрифт
│  │  ├─ ui/                              // Пользовательский интерфейс
│  │  │  ├─ Hud.java                      // Панель здоровья/маны
│  │  │  ├─ InventoryView.java            // Отображение инвентаря игрока
│  │  │  └─ screens/                      // Экраны главного меню
│  │  │     ├─ AboutScreen.java           // Экран "О игре"
│  │  │     ├─ GameScreen.java            // Экран самой игры
│  │  │     ├─ MenuScreen.java            // Главное меню
│  │  │     └─ SettingsScreen.java        // Настройки
│  │  │     ├─ LoginScreen.java           // Логин/пароль
│  │  │     └─ SelectGameScreen.java      // Выбор игры/сессии
│  │  └─ menu/                            // устарело, заменено на ui/screens/
│
└─ resources/
├─ map.json                       // Карта (тестовая)
├─ tiles/                         // Спрайты тайлов
│  ├─ floor.png
│  ├─ wall.png
│  └─ ...
└─ sprites/                       // Спрайты персонажей и врагов
├─ player.png
├─ monster.png
└─ ...

├─ map.json                       // Карта (тестовая)
├─ tiles/                         // Спрайты тайлов
│  ├─ floor.png
│  ├─ wall.png
│  └─ ...
└─ sprites/                       // Спрайты персонажей и врагов
├─ player.png
├─ monster.png
└─ ...