import java.util.*;

public final class GameEngineTest {
    private static int checks;
    private static void check(boolean condition,String message){checks++;if(!condition)throw new AssertionError(message);}
    private static void rejects(Runnable action){try{action.run();throw new AssertionError("Expected input rejection");}catch(IllegalArgumentException expected){checks++;}}
    public static void main(String[] args){
        rejects(()->new Dice(0,new Random()));rejects(()->new Player(" "));rejects(()->new Player("12345678901"));
        rejects(()->new GameEngine("Explorer","invalid",42));rejects(()->GameWebServer.query("seed=1&seed=2"));
        check(GameWebServer.quote("\"\\\n").equals("\"\\\"\\\\\\u000a\""),"JSON quoting");
        check(GameWebServer.query("name=Test+Name&seed=42").get("name").equals("Test Name"),"URL decoding");
        Dice die=new Dice(4,new Random(42));for(int i=0;i<1000;i++){int roll=die.roll();check(roll>=1&&roll<=4,"four-sided die bounds");}
        boolean nonFinishingWinner=false,negativeScore=false;
        for(int seed=0;seed<200;seed++)for(String difficulty:List.of("easy","complex")){
            GameEngine a=new GameEngine("Explorer",difficulty,seed),b=new GameEngine("Explorer",difficulty,seed);
            char[] trail=a.getTrail();boolean easy=difficulty.equals("easy");
            check(trail.length>=(easy?12:15)&&trail.length<=(easy?20:30),"trail length");
            check(trail[0]=='_'&&trail[trail.length-1]=='_',"start and finish stay clear");
            for(char type:new char[]{'c','b','f','l'}){
                int count=0;for(int i=0;i<trail.length;i++)if(trail[i]==type){count++;if(easy)check(i+Landscape.action(type)>=1&&i+Landscape.action(type)<=trail.length-2,"easy features remain internal");}
                check(easy?count==1:count>=1&&count<=3,"feature distribution");
            }
            trail[0]='x';check(a.getTrail()[0]=='_',"defensive trail copy");
            int revision=a.getRevision();rejects(()->a.nextRound(revision+1));
            while(!a.isOver()){
                List<GameEngine.Turn> aa=a.nextRound(a.getRevision()),bb=b.nextRound(b.getRevision());
                check(aa.equals(bb),"seeded event replay");
                for(GameEngine.Turn turn:aa){
                    check(turn.to()>=0&&turn.to()<a.getTrail().length,"movement clamps to trail");
                    check(turn.landed()==Math.min(a.getTrail().length-1,turn.from()+turn.roll()),"dice movement");
                    check(turn.to()==turn.landed()+turn.shift(),"feature displacement accounting");
                }
            }
            check(a.getRevision()<=1000,"bounded expedition");
            check(GameWebServer.json(a,"test").equals(GameWebServer.json(b,"test")),"full response replay");
            for(int actor=0;actor<2;actor++){
                int total=0;for(GameEngine.Turn t:a.getTurns())if(t.actor()==actor)total+=t.points()+(t.finished()?10:0);
                check(total==a.getPlayer(actor).getScore(),"score equals encounter and bonus ledger");
                int[] sightings=a.getSightings(actor);
                for(int species=0;species<5;species++){
                    int count=0;for(GameEngine.Turn t:a.getTurns())if(t.actor()==actor&&t.animal().equals(GameEngine.ANIMALS[species]))count++;
                    check(count==sightings[species],"species ledger");
                }
                if(total<0)negativeScore=true;
            }
            int expected=a.getPlayer(0).getScore()==a.getPlayer(1).getScore()?-1:a.getPlayer(0).getScore()>a.getPlayer(1).getScore()?0:1;
            check(a.getWinner()==expected,"highest score determines winner");
            if(a.getWinner()>=0&&a.getWinner()!=a.getFinisher())nonFinishingWinner=true;
            check(a.getTurns().get(a.getRevision()-1).finished(),"game ends at first finish");
            rejects(()->a.nextRound(a.getRevision()));
        }
        check(nonFinishingWinner,"finisher need not win by score");check(negativeScore,"negative scores allowed");
        System.out.println("PASS: "+checks+" assertions across 400 seeded expeditions, replay, scoring and input rules");
    }
}
