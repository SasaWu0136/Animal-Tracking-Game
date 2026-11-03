# Game rules

## Goal

Accumulate more points than the computer ranger. The first player to reach the last tile receives +10 and immediately ends the expedition. The final score decides the winner, not the finishing order. Equal scores are a draw.

## Turn order

The human starts. One browser click plays the human turn and, if the game has not ended, the ranger turn. The computer makes no strategic choices; both players follow identical rules.

For each turn:

1. Roll a uniform four-sided die, moving 1–4 tiles.
2. Clamp the position to the trail bounds.
3. Flip a fair encounter coin. On a sighting, select uniformly from Koala (+10), Emu (+7), Wombat (+5), Kangaroo (+2) and Rabbit (−5).
4. Flip a separate fair terrain coin. If triggered and the landed tile contains a feature, apply Creek (−2), Bridge (+4), Fallen tree (−3) or Landslide (−5). Clamp again. Do not chain a second feature.
5. Check for the finish, award the +10 bonus and stop immediately if reached.

Negative scores are allowed. An exceptional 1,000-turn safety limit ends the game by score without awarding a finish bonus unless the last turn actually finishes.

## Trails

Easy trails contain 12–20 tiles with exactly one of each feature. Features are placed so their destinations remain strictly between start and finish.

Complex trails contain 15–30 tiles and 1–3 of each feature. Effects may reach a boundary; positions are clamped. Start and finish tiles contain no feature.

The maintained Landscape implementation follows the original compiled class's length ranges, feature count rules and displacement values. Candidate-based placement and a shared seeded Java Random replace unseeded placement retries, so old random outcomes are not replayed.

## Replay

A signed 64-bit seed determines the trail layout, dice rolls, animal encounters and terrain triggers. The same name, difficulty, seed and sequence of rounds reproduce the same expedition in this version.

This is a chance-based board game. It does not provide real animal locations, wildlife identification or navigation.
