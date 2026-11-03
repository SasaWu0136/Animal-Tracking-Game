public class Player
    {
    // declare field
        private String name;
        private int position;
        private int score;

    // default constructor
    public Player()
    {
        this.name = "";
        this.position = 0;
        this.score = 0;
    }

    //non-default constructor
    public Player (String PlayerName,int PlayerPosition,int PlayerScore)
    {
        this.name = PlayerName;
        this.position = PlayerPosition;
        this.score = PlayerScore;
    }

    //appropriate accessor
    public String getName()
    {
        return name;
    }

    public int getPosition()
    {
        return position;
    }

    public int getScore()
    {
        return score;
    }

    //mutator methods
    public void setName (String playerName)
    {
        this.name = playerName;
    }

    public void setPosition (int playerPosition)
    {
        this.position = playerPosition;
    }

    public void setScore(int playerScore)
    {
        this.score = playerScore;
    }

    public void addScore(int points){
        this.score += points;
    }

    //display methods  
    public void display()
    {
        System.out.println(toString());
    }

    //toString methods
    public String toString()
    {
        return "Player Name: "+name+ ", Player Position: "+position+ ", Player Score: "+score;
    }
    }

