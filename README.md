# 🐾 Java Animal Tracking Game

This is a turn-based console game written in Java for a university assignment. Two players (a human and the computer) take turns rolling a dice and moving along a randomly generated nature trail. Players encounter obstacles and animals that affect their score and position.

## 🎮 Game Features
- Dice roll system (1–4 steps per turn)
- Random animal encounters:
  - 🐨 Koala (+10)
  - 🐦 Emu (+7)
  - 🐾 Wombat (+5)
  - 🦘 Kangaroo (+2)
  - 🐇 Rabbit (−5)
- Nature features (Creek, Bridge, Fallen Tree, Landslide)
- Position and score tracking
- Victory condition when a player reaches the end of the trail

## 📂 Project Structure
```
src/
├── Game.java
├── Player.java
├── Trail.java
├── NatureFeature.java
├── Dice.java
└── Landscape.java
```

## 🚀 How to Run
1. Clone this repo:
```bash
git clone https://github.com/YOUR_USERNAME/AnimalTrackingGame.git
```
2. Compile all Java files:
```bash
javac *.java
```
3. Run the game:
```bash
java Game
```

## 📝 Sample Output
```
Welcome to the Animal Tracking game
Enter player name: KIKI
Select trail:
1. Easy
2. Complex
Choice: 1

*** Round 1 ***
S_HC__________F
...
KIKI has won the game and is awarded 10 points
KIKI: 17
Computer: 7
KIKI won the game.
GOODBYE
```

## 📌 Notes
- Input validation prevents empty or overly long player names.
- Trail is randomly
