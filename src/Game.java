import java.util.*;
/** Console entry point for the same engine used by the web game. */
public final class Game {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            long seed = args.length == 0 ? new Random().nextLong() : Long.parseLong(args[0]);
            System.out.println("WILD TRAILS — Animal Tracking Game | Seed: " + seed);
            String name;
            while (true) {
                System.out.print("Player name (1–10 characters): ");
                if (!scanner.hasNextLine()) return;
                name = scanner.nextLine();
                try {new Player(name);break;} catch(IllegalArgumentException e){System.out.println(e.getMessage());}
            }
            String difficulty;
            while (true) {
                System.out.print("Trail: 1 = Easy, 2 = Complex: ");
                if (!scanner.hasNextLine()) return;
                String input = scanner.nextLine().trim();
                if(input.equals("1") || input.equals("2")){difficulty=input.equals("1")?"easy":"complex";break;}
                System.out.println("Please select 1 or 2.");
            }
            GameEngine game = new GameEngine(name,difficulty,seed);
            System.out.println("First to finish receives +10; highest final score wins.");
            while (!game.isOver()) {
                System.out.println("Press Enter to roll, or q to quit.");
                if (!scanner.hasNextLine() || scanner.nextLine().trim().equalsIgnoreCase("q")) return;
                for (GameEngine.Turn turn : game.nextRound(game.getRevision())) {
                    System.out.printf("%s rolled %d: %d → %d. %s %s%s%n",game.getPlayer(turn.actor()).getName(),turn.roll(),turn.from(),turn.to(),
                        turn.animal().isEmpty()?"No animal sighted.":turn.animal()+" "+(turn.points()>0?"+":"")+turn.points()+" points.",
                        turn.feature().isEmpty()?"":turn.feature()+" ("+turn.shift()+" spaces). ",turn.finished()?"Finish bonus +10.":"");
                }
                for(int i=0;i<2;i++)System.out.printf("%s: %d points | tile %d/%d%n",game.getPlayer(i).getName(),game.getPlayer(i).getScore(),game.getPlayer(i).getPosition()+1,game.getTrail().length);
            }
            System.out.println(game.getWinner()==-1?"Draw!":game.getPlayer(game.getWinner()).getName()+" wins by score!");
        }
    }
}
