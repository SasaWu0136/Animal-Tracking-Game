import java.util.*;

/** Shared deterministic rules for CLI and browser play. No rendering or networking. */
public final class GameEngine {
    public static final String[] ANIMALS = {"Koala", "Emu", "Wombat", "Kangaroo", "Rabbit"};
    public static final int[] POINTS = {10,7,5,2,-5};
    public record Turn(int actor, int roll, int from, int landed, int to, String animal,
                       int points, String feature, int shift, boolean finished) {}
    private final Random random;
    private final Dice dice;
    private final char[] trail;
    private final Player[] players;
    private final long seed;
    private final String difficulty;
    private final List<Turn> turns = new ArrayList<>();
    private boolean over;
    private int finisher = -1;
    private final int[][] sightings = new int[2][5];
    public GameEngine(String name, String difficulty, long seed) {
        this.seed = seed; this.difficulty = difficulty;
        this.players = new Player[]{new Player(name), new Player("Ranger")};
        this.random = new Random(seed);
        this.trail = Landscape.generate(difficulty, random);
        this.dice = new Dice(4,random);
    }
    /** Rolls one human turn and, unless the game ends, one computer turn. */
    public synchronized List<Turn> nextRound(int expectedRevision) {
        if (expectedRevision != turns.size()) throw new IllegalArgumentException("Game has advanced. Start a new game to resync.");
        if (over) throw new IllegalArgumentException("This expedition is complete");
        List<Turn> result = new ArrayList<>();
        result.add(playTurn(0));
        if (!over) result.add(playTurn(1));
        return List.copyOf(result);
    }
    private Turn playTurn(int actor) {
        Player player = players[actor];
        int from = player.getPosition(), roll = dice.roll();
        player.move(roll, trail.length);
        int landed = player.getPosition();
        String animal = ""; int points = 0;
        if (random.nextBoolean()) {
            int index = random.nextInt(ANIMALS.length);
            animal = ANIMALS[index]; points = POINTS[index]; sightings[actor][index]++;
            player.addScore(points);
        }
        String feature = ""; int shift = 0;
        if (random.nextBoolean() && trail[landed] != '_') {
            feature = Landscape.name(trail[landed]);
            player.move(Landscape.action(trail[landed]), trail.length);
            shift = player.getPosition() - landed;
        }
        boolean finished = player.getPosition() == trail.length-1;
        if (finished) {player.addScore(10); finisher = actor; over = true;}
        Turn turn = new Turn(actor,roll,from,landed,player.getPosition(),animal,points,feature,shift,finished);
        turns.add(turn);
        if (turns.size() >= 1000) over = true;
        return turn;
    }
    public Player getPlayer(int i) { return players[i]; }
    public char[] getTrail() { return trail.clone(); }
    public List<Turn> getTurns() { return List.copyOf(turns); }
    public int[] getSightings(int actor) { return sightings[actor].clone(); }
    public int getRevision() { return turns.size(); }
    public boolean isOver() { return over; }
    public int getFinisher() { return finisher; }
    public int getWinner() { if (!over) return -2; return Integer.compare(players[0].getScore(),players[1].getScore()) > 0 ? 0 : players[0].getScore()==players[1].getScore() ? -1 : 1; }
    public long getSeed() { return seed; }
    public String getDifficulty() { return difficulty; }
}
