import java.util.Random;
import java.util.Scanner;



public class Game
{   
    // declare field
    private Trail natureTrail;
    private Player playerOne;
    private Player playerTwo;
    private Dice dice;
    private Scanner scanner; 

    // default constructor
    public Game()
    {
        natureTrail = new Trail();
        //playerOne = new Player();
        //playerTwo = new Player();
        scanner = new Scanner(System.in); 
        dice = new Dice(4);
    }

    //non-default constructor
    public Game(Trail natureTrail,Player playerOne,Player playerTwo)
    {
        this.natureTrail = natureTrail;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        scanner = new Scanner(System.in);
        this.dice = new Dice(4);
    }

    //appropriate accessor
    public Trail getNatureTrail()
    {
        return this.natureTrail;
    }

    public Player getPlayerOne()
    {
        return this.playerOne;
    }

    public Player getPlayerTwo()
    {
        return this.playerTwo;
    }

    //mutator methods
    public void setNatureTrail(Trail natureTrail)
    {
        this.natureTrail = natureTrail;
    }

    public void setPlayerOne(Player playerOne)
    {
        this.playerOne = playerOne;
    }

    public void setPlayerTwo(Player playerTwo)
    {
        this.playerTwo = playerTwo;
    }

    //display methods 
    public void display()
    {
        System.out.println(toString());
    }

    //toString methods
    public String toString()
    {
        return "Nature Trail: "+natureTrail+ ",Player One: "+playerOne+",Player Two: "+playerTwo;
    }

     public static void main(String[] args){
        Random random = new Random();
        Game game = new Game();
        game.runGame(); //start the game rules

     } 
     //Roll the dice
     public int rollDice(){
        return dice.roll();
     }

     public void play() {

        Player[] players = { this.playerOne, this.playerTwo };
        //System.out.println("trail len: " + (natureTrail.getLength()-1));// debug
            
        for( int i = 0; ; i++) {
            if (0 == (i % 2)){
                System.out.println("*** Round " + (i/2 +1) + " ***");
                displayTrail();
            }
            
            Player current = players[i % 2]; //switch player

            playTurn(current);
            
            if (current.getPosition() >= natureTrail.getLength()-1){
                current.setPosition(natureTrail.getLength()-1);
                current.addScore(10);
                System.out.println("\n******************************************");
                System.out.println(current.getName()+" has reach the end of the trail");
                System.out.println(current.getName()+" has won the game and is awarded 10 points");
                System.out.println("Total score: "+ current.getScore());
                System.out.println("\n******************************************");
                displayTrail();
                break;
            }
            if (1 == (i % 2))
            {
                displayTrail();
                System.out.println("\nPress \"ENTER\" to continue...");
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
            }
        }
        displayResult(); //when the game over shows who is winner
     }

    // welcome message
     public void displayWelcomeMessage(){
         System.out.println("Welcome to the Animal Tracking game");
     }
    

    // enter the player Name
     public String getPlayerName() { 
        String name ="";

         System.out.println ("Enter player name: ");
         name = scanner.nextLine();

         while (name.length()<1 || name.length()>10 || name.startsWith(" ") || name.endsWith(" ")){
             System.out.println("Error: number of character is not in range: 1 and 10.\n");
             System.out.println ("Enter player name: ");
             name = scanner.nextLine();
        }

         return name.trim();
     }
    
    // Request an trail
     public void requestTrail() {
         System.out.println("Select trail:");
         System.out.println("======");
         System.out.println("1. Easy");
         System.out.println("2. Complex");
         System.out.println("Choice: ");
         int choice = scanner.nextInt();
         scanner.nextLine();

         switch(choice){
            case 1: 
                System.out.println("Choice: " + choice);
                this.natureTrail = new Trail(Landscape.getEasyTrail()); // char arr
                break;
            case 2:
                System.out.println("Choice: " + choice);
                this.natureTrail = new Trail(Landscape.getComplexTrail()); // char arr
                break;
            default:
                break;
         }
     }
     
    // playerOne start first and then playerTwo
    public void runGame(){
         // welcome message
         displayWelcomeMessage();
         // enter the player Name
         String playerName = getPlayerName();
         playerOne = new Player(playerName,0,0);
         playerTwo = new Player("Computer",0,0);
         requestTrail();
         play();
    }
        
    //Display the nature trail with the position of each player
     public void displayTrail(){
         String trailDisplay = "\nS";

         if (playerOne.getPosition() == 0 && playerTwo.getPosition() == 0){
            trailDisplay +="HC";
         } else if (playerOne.getPosition()==0){
            trailDisplay +="H";
         } else if (playerTwo.getPosition()==0){
            trailDisplay +="C";
         }/*else{
            trailDisplay +="S";
         }*/
         for (int i = 1 ; i< natureTrail.getLength()-1; i++){
            if ( i == (playerOne.getPosition()) && i == (playerTwo.getPosition()) ){
                 trailDisplay +="HC"; //player&computer at the same trail
            } else if (i == playerOne.getPosition()){
                trailDisplay +="H";//playerOne
            } else if (i == playerTwo.getPosition()){
                trailDisplay +="C";//playerTwo
            } else {
                trailDisplay +="_";//trail
            }
         }
        if (playerOne.getPosition() == natureTrail.getLength()-1)
            trailDisplay += "H";
        if (playerTwo.getPosition() == natureTrail.getLength()-1)
            trailDisplay += "C";
        trailDisplay +="F";
        System.out.println(trailDisplay.toString());
     }
     
    //Move the player a number of places
     public void movePlayer(Player player , int steps){
         int newPosition = player.getPosition()+ steps;
         if (newPosition >= natureTrail.getLength()){
             newPosition = natureTrail.getLength()-1;
         }
         else if (newPosition < 0)
            newPosition = 0;
         player.setPosition(newPosition);
     }

        public String getRandomAnimal(){
        String[] animals = {"Koala", "Emu", "Wombat", "Kangaroo", "Rabbit" };
        return animals[new Random().nextInt(animals.length)];
    } 

    public int getAnimalPoints(String animal){
        switch(animal){
            case "Koala": 
                System.out.println(" Animal sighted: Koala , point +10");
                return 10;
            case "Emu":
                System.out.println(" Animal sighted: Emu , point +7");
                return 7;
            case "Wombat": 
                System.out.println(" Animal sighted: Wombat , point +5");
                return 5;
            case "Kangaroo": 
                System.out.println(" Animal sighted: Kangaroo , point +2");
                return 2;
            case "Rabbit":
                System.out.println(" Animal sighted: Rabbit , point -5");
                return -5;
            default: return 0;
        }
    }  
    

   //Request the player object to adjust their score
     public void Score(Player player,int points){
         player.addScore(points);
     }

     /*
     Animal sighted: xxx , point +-xxx
     Reach a nature feature: xxx
     */
     public void playTurn(Player player){
        int aRand = 0;
        int nRand = 0;
        System.out.println( player.getName()+" is playing...");
        System.out.println(" Current position: "+player.getPosition());

        int roll = rollDice();
        System.out.println(" Throwing the dice..." +roll);
        movePlayer(player,roll);
        System.out.println(" New position: "+player.getPosition());

        //animal
        aRand = new Random().nextInt(2);
        if(aRand == 1)
            Score(player, getAnimalPoints(getRandomAnimal()));
        else
            System.out.println(" No animal found.");

        //feature
        nRand = new Random().nextInt(2);
        if (nRand == 1){
            int pTmp = (this.getNatureTrail().getNatureTrail()).get(player.getPosition()).getSpacePenalty();
            String sTmp = (this.getNatureTrail().getNatureTrail()).get(player.getPosition()).getFeatureName();
            if( sTmp != "unknown")
            {
                System.out.println(" Reach a nature feature: " + sTmp);
                System.out.println(" Position change: " + pTmp);
                movePlayer(player,pTmp);
                System.out.println(" New position: "+player.getPosition());
            }
            else
            {
                System.out.println(" No natureFeature met.");
            }
        }

        System.out.println(" Current score: " + player.getScore());
            
     }
     public void displayResult(){
        System.out.println("\n******************************************");
        System.out.println( playerOne.getName() + ": " +playerOne.getScore());
        System.out.println( playerTwo.getName() + ": " +playerTwo.getScore() + "\n");

        if (playerOne.getScore() > playerTwo.getScore())
            System.out.println( playerOne.getName() + " won the game.");
        else if (playerOne.getScore() < playerTwo.getScore())
            System.out.println(" Computer won the game.");
        else
            System.out.println(" The game is draw");
         System.out.println("\n******************************************");


         System.out.println("Press \"ENTER\" to continue...");
         scanner.nextLine();

         System.out.println("GOODBYE");
     }

     
/*
    public void test()
    {
        char[]nfArray = Landscape.getEasyTrail();
        NatureFeature[] nfs = new NatureFeature[nfArray.length];

        for(int index = 0; index < nfArray.length; index++)
        {
            char nfc = nfArray[index];
        }  
    } 
*/ 
}
