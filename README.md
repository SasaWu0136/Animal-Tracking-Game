# Wild Trails 🐾
### An Australian wildlife board game, powered by Java.

**An interactive evolution of Animal Tracking Game — built with Java, JavaScript, HTML and CSS.**

Roll a four-sided die, explore an Australian nature trail and collect wildlife sightings before the expedition ends. Race a computer ranger through creeks, bridges and fallen trees — but remember: **the first to finish earns a bonus; the highest final score wins.**

[中文介紹](docs/PROJECT_ZH.md) · [Game rules](docs/RULES.md) · [Architecture](docs/ARCHITECTURE.md)

## Play your way

- **Two trail types:** a 12–20 tile meadow loop or a 15–30 tile complex trail.
- **Five animal encounters:** koala, emu, wombat, kangaroo and rabbit, each with its own score.
- **Terrain surprises:** bridges move you forward; creeks, fallen trees and landslides send you back.
- **Interactive board:** numbered trail tiles, player markers, scorecards and a four-sided die.
- **Wildlife journal:** track each player's sightings and review the complete turn history.
- **Repeatable expeditions:** replay the same seed with the same trail, rolls and encounters.
- **Journal export:** download the current expedition as a text report.
- **Shared Java engine:** browser and console play use the same rules.

The night-expedition interface uses a dark sector map, cyan route markers, orange explorer tokens and a wildlife index. It includes keyboard-operable controls and respects reduced-motion preferences. The ranger uses the same random rules as the human player; it is not an AI agent.

## Run locally

Requires **JDK 17+**, Git and a modern browser. No Node.js, database, API keys or third-party Java libraries are required.

```sh
git clone https://github.com/SasaWu0136/Animal-Tracking-Game.git
cd Animal-Tracking-Game
sh scripts/web.sh
```

Open **http://127.0.0.1:8082**. Keep the terminal running and press `Ctrl+C` to stop. If the port is busy, run `sh scripts/web.sh 8083` and open port 8083 instead.

The server is local to your computer. A public hosted demo is not included.

### Console edition

```sh
sh scripts/run.sh 42
```

Enter a name, select `1` for easy or `2` for complex, then press Enter to play each round. Enter `q` to leave. Omit the seed argument for a new random expedition.

### Windows / PowerShell

```powershell
New-Item -ItemType Directory -Force build
javac --release 17 -encoding UTF-8 -d build src/*.java
java -cp build GameWebServer
```

Use `java -cp build Game 42` for console play. Run commands from the repository root.

## How a turn works

1. Roll a die and move 1–4 spaces, clamped to the trail.
2. With a 50% chance, encounter one of five equally likely animals and adjust the score.
3. With a separate 50% chance, apply the feature on the landed tile. Features do not chain.
4. If the player reaches the final tile after movement, award 10 points and end the game immediately.
5. Otherwise, pass to the other player.

| Animal | Score | Terrain | Movement |
| --- | ---: | --- | ---: |
| Koala | +10 | Creek | −2 |
| Emu | +7 | Bridge | +4 |
| Wombat | +5 | Fallen tree | −3 |
| Kangaroo | +2 | Landslide | −5 |
| Rabbit | −5 | Open trail | 0 |

Negative scores are allowed. If final scores tie, the game is a draw. A player can reach the finish first and still lose on points. See [the full rules](docs/RULES.md).

## Design decisions

- **One engine, two interfaces:** the console and browser share the same Java rules.
- **Deterministic play:** a fixed seed reproduces trail generation, rolls and encounters.
- **Explicit state transitions:** revision checks reject stale or duplicate moves.
- **Inspectable results:** every score change is recorded in the expedition journal.
- **A small runtime:** the application uses the JDK HTTP server and a buildless frontend.

## Engineering

| Component | Responsibility |
| --- | --- |
| `GameEngine` | Turn order, encounter rolls, feature effects, scoring and finish conditions. |
| `Landscape` | Seeded trail generation with validated feature placement. |
| `Player`, `Dice` | Player state and injected random dice rolls. |
| `Game` | Console prompts and presentation. |
| `GameWebServer` | Local JDK HTTP server, in-memory sessions, input validation and JSON responses. |
| `web/` | Buildless HTML/CSS/JavaScript board, journal and export. |

The API uses revision numbers to reject duplicate or stale moves. Sessions are stored in server memory; restarting the server resets them. The server retains up to 100 expeditions. Seeds are sent as strings so JavaScript does not round Java long integers.

## Java update utility

The standalone update tool is included at `tools/AnimalTrackingGameUpdate.java`. It installs this edition into an original checkout without Python or third-party dependencies:

```sh
java tools/AnimalTrackingGameUpdate.java --check
java tools/AnimalTrackingGameUpdate.java
```

It verifies original files before writing, preserves custom changes, and archives the old game directory. Existing matching installations are left unchanged. The separate `tools/AnimalTrackingNightUpdate.java` applies the night-expedition interface to the original web edition. Normal project updates can use Git; both embedded installers are fixed snapshots. See [the update utility documentation](tools/README.md).

## Verify

```sh
sh scripts/test.sh
```

The Java suite exercises **400 complete seeded expeditions** across both difficulties, checking reproducibility, trail constraints, movement bounds, scoring and sighting ledgers, highest-score winners, finished-game rejection and invalid input. GitHub Actions runs the same command on Java 17.

The checks do not open a network port. Browser layout and end-to-end HTTP operation require a running local server.

## Project structure

| Path | Contents |
| --- | --- |
| `src/` | Maintainable Java source for the shared engine and both entry points. |
| `web/` | Responsive game interface. |
| `tests/` | Engine and adapter checks. |
| `scripts/` | Run, web and test commands. |
| `docs/` | Rules, architecture and Chinese overview. |
| `archive/original/` | Original project files preserved for provenance. |
| `build/` | Generated bytecode, excluded from Git. |

## From the original game

The original console project stored compiled class files and omitted `Landscape.java`. The maintained source restores the trail length ranges, feature counts and displacement rules documented by the supplied bytecode. It also consolidates randomness into one seeded stream, validates trail choices, removes competing console scanners and clarifies the finish bonus versus score-based winner.

The new seed sequence is not intended to reproduce old unseeded runs. The original files remain archived; the active application compiles entirely from source on Java 17.

## Future development

- Animated movement between intermediate tiles.
- Optional strategic choices or local two-player mode.
- Persistent expedition history.
- Java-capable hosting for a public demo.
