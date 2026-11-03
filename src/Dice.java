import java.util.*;
public final class Dice {
    private final int sides;
    private final Random random;
    public Dice(int sides, Random random) {
        if (sides < 1) throw new IllegalArgumentException("Dice must have at least one side");
        this.sides = sides; this.random = Objects.requireNonNull(random);
    }
    public int roll() { return 1 + random.nextInt(sides); }
}
