public final class Player {
    private final String name;
    private int position;
    private int score;
    public Player(String name) {
        if (name == null || name.trim().isEmpty() || name.trim().length() > 10 || name.chars().anyMatch(Character::isISOControl))
            throw new IllegalArgumentException("Player name must be 1–10 characters");
        this.name = name.trim();
    }
    public String getName() { return name; }
    public int getPosition() { return position; }
    public int getScore() { return score; }
    public void move(int spaces, int trailLength) { position = Math.max(0, Math.min(trailLength-1, position + spaces)); }
    public void addScore(int points) { score += points; }
}
