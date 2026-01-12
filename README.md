# GoHome - Swing Remake

This project is a Java Swing remake of the "GoHome" board game (educational sample). Features:

- 5×5 wrap-around grid, coin toss movement
- 48×48 pixel-art sprites (generator included)
- Single-player (AI) & multiplayer hotseat modes (toggle in settings)
- Runnable fat JAR (via `gradle fatJar`) and packaging script for Windows exe

Developer notes
- Use `tools/generate_sprites.py` to create the 48×48 PNG assets.
- Build a fat JAR with: `gradlew fatJar` (result: `build/libs/gohome-<version>-all.jar`).
- For Windows exe, use Launch4j (script provided `scripts/package_with_launch4j.bat`); edit `scripts/launch4j-config.xml` to set icon and JRE preferences.
- AI decisions are limited to 5 seconds max (configurable via `MinimaxAI` constructor).
