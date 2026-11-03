import java.util.*;

/** Source restoration based on the original Landscape.class rules. */
public final class Landscape {
    public static char[] generate(String difficulty, Random random) {
        boolean easy = difficulty.equals("easy");
        if (!easy && !difficulty.equals("complex")) throw new IllegalArgumentException("Choose easy or complex");
        int length = easy ? 12 + random.nextInt(9) : 15 + random.nextInt(16);
        char[] cells = new char[length];
        Arrays.fill(cells, '_');
        for (char type : new char[]{'c','b','f','l'}) {
            int count = easy ? 1 : 1 + random.nextInt(3);
            for (int n = 0; n < count; n++) {
                List<Integer> candidates = new ArrayList<>();
                for (int i = 1; i < length-1; i++) {
                    int destination = i + action(type);
                    if (cells[i] == '_' && (!easy || destination >= 1 && destination <= length-2)) candidates.add(i);
                }
                if (candidates.isEmpty()) throw new IllegalStateException("Cannot place trail feature");
                cells[candidates.get(random.nextInt(candidates.size()))] = type;
            }
        }
        return cells;
    }
    public static int action(char type) {
        return switch(type) {case 'c' -> -2; case 'b' -> 4; case 'f' -> -3; case 'l' -> -5; default -> 0;};
    }
    public static String name(char type) {
        return switch(type) {case 'c' -> "Creek"; case 'b' -> "Bridge"; case 'f' -> "Fallen tree"; case 'l' -> "Landslide"; default -> "Open trail";};
    }
}
