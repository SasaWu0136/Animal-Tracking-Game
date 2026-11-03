public class JsonFixture {
    public static void main(String[] args){GameEngine game=new GameEngine("Explorer","easy",42);while(!game.isOver())game.nextRound(game.getRevision());System.out.println(GameWebServer.json(game,"fixture"));}
}
