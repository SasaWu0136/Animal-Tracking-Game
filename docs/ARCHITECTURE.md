# Architecture

`GameEngine` owns the game state and deterministic random stream. `Game` renders that state in the console. `GameWebServer` exposes it to a browser without duplicating the rules in JavaScript.

## Local API

| Method | Endpoint | Parameters | Result |
| --- | --- | --- | --- |
| POST | `/api/new` | `name`, `difficulty` (easy/complex), `seed` | New session and complete game snapshot. |
| POST | `/api/turn` | `id`, `revision` | Human and optional ranger turn, followed by the updated snapshot. |
| GET | `/` and static assets | — | Browser interface. |

Snapshots contain trail tiles, player positions and scores, sighting counts, the event ledger, finish status and winner. Turn revisions reject stale/duplicate requests instead of silently rolling again. If a response is lost after the server accepts a move, the interface asks for a new expedition; automatic state resynchronization is not implemented.

The server binds to `127.0.0.1`. Sessions are in memory, with a maximum of 100 retained games; newer games evict the oldest. Restarting the server clears them. The browser displays one active expedition at a time.

## Frontend

The buildless interface uses CSS Grid for a serpentine numbered board. Player markers and trail features are rendered from API state. A four-sided die indicator shows the human's latest roll, while the turn journal records both players' rolls, encounters, actual terrain displacement and finish bonuses. The wildlife cards show sighting counts for each player.

Draft setup inputs do not change the current expedition until a new game is started. Export uses the completed game state rather than the draft settings. Player names are escaped before HTML rendering; seeds remain strings to preserve Java integer precision.

## Validation

Java tests run complete paired games across 200 seeds and two difficulties, checking deterministic event replay, movement bounds, feature placement, score and sighting accounting, winner calculation, JSON escaping and input rejection. Scripts compile with `--release 17` and no third-party dependencies.

Browser visual inspection and real HTTP checks are separate from the socket-free automated tests.
