# JavaGameProtoype
# 🎮 Diablo-like Prototype Game Engine (Java + Rust + PHP + SQL)

**This is not a full game**, but a **prototype** of core mechanics and architecture inspired by Diablo 1. Built in just two weeks to test concepts of graphics, procedural generation, and cross-language interaction.

## 🧩 Key Components

### ☕ Java (Main Client)
- JavaFX interface and rendering
- Player control, event handling, canvas drawing
- Game loop (`GameLoop`) and save/load via `SaveGame`
- Support for **isometric** and **2D (Cartesian)** projections
- Modular architecture (MVC-like structure)

### 🦀 Rust (Dungeon Generator)
- Executable `mapGenerator.exe` built from Rust
- Algorithm: room and corridor generation like Diablo 1
- Communicates with Java via files (ASCII/text)

### 🐘 PHP + MySQL (Server API)
- Minimal backend for save/load
- JSON exchange via GSON
- Saves map data, player stats, positions

## 🎨 Graphics and Interface
- Switch between 2D and Isometric view
- Custom cursors, Diablo-like fonts and UI
- Pause menu, login, settings, save system

## 🛠 Technologies

| Language / Tech   | Used For           |
|-------------------|--------------------|
| Java 21           | Core game logic    |
| JavaFX            | GUI & rendering    |
| Rust              | Map generation     |
| PHP               | Server-side logic  |
| MySQL             | Data storage       |
| GSON              | JSON serialization |
| Maven             | Project build      |
| DIA               | Diagrams           |

## ⚙️ Setup

1. Install JDK 21+ and Maven
2. Place `config.properties` next to the JAR
3. Build project:
```bash
mvn clean install
```
4. Run the game:
```bash
java -jar target/projektObjektorientierung.jar
```

## 📁 Project Structure

```
progect/
├── JAVA/
│   └── src/
│   ├── external/mapGenerator.exe
│   └── config.properties
├── rust/
├── php/
```

## 🚧 In Development
- Sound & effects
- Combat mechanics
- Quests & dialog system
- Map editor
