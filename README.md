# Go Home - Board Game

A classic "Go Home" board game implementation in Java with Swing UI.

## Overview

This project implements the "Go Home" board game (similar to Ludo/Parcheesi) with a graphical user interface using Java Swing. The game is based on the educational concepts from https://inf-schule.de/oop/java/beziehungen/gohome/.

## Features

- ✅ 2-4 player support
- ✅ Circular board visualization
- ✅ Dice rolling mechanism
- ✅ Interactive figure movement
- ✅ Player turn management
- ✅ Win condition detection
- ✅ German UI with menu and help
- ✅ New game functionality

## Screenshots

![Player Selection](https://github.com/user-attachments/assets/c85b7a30-a4cc-4e6b-aa22-0b178fce638a)

## Quick Start

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- No additional libraries required

### Compilation

```bash
cd "Go Home/scr"
javac *.java
```

### Run

```bash
java Main
```

## Game Rules

**Objective**: Get all 4 figures to the home position!

**How to Play**:
1. Roll the dice to get a number (1-6)
2. You need a 6 to start a figure from the starting position
3. Move a figure forward by the rolled number
4. If you land on a field with an opponent's figure, it gets sent back to start
5. Reach home with all 4 figures to win
6. Click "Next Player" after your turn

## Project Structure

```
Go Home/
└── scr/
    ├── Main.java          - Entry point
    ├── Game.java          - Game controller
    ├── Board.java         - Game board management
    ├── Field.java         - Board field
    ├── Figure.java        - Game piece
    ├── Player.java        - Player management
    ├── Dice.java          - Dice functionality
    ├── GameUI.java        - Main UI window
    └── BoardPanel.java    - Board visualization
```

## Architecture

The project follows object-oriented principles with clear separation of concerns:

- **Model**: `Game`, `Board`, `Field`, `Figure`, `Player`, `Dice`
- **View**: `GameUI`, `BoardPanel`
- **Controller**: Event handlers in `GameUI`

## Documentation

For detailed documentation in German, see [ANLEITUNG.md](ANLEITUNG.md).

## License

Educational project based on concepts from inf-schule.de.
