import java.util.Random;

public class Dice
{
    // declare field
    private int numberOfsides;

    // default constructor
    public Dice()
    {
        this.numberOfsides = 0;
    }

    //non-default constructor
    public Dice(int numberOfsides)
    {
        this.numberOfsides = numberOfsides;
    }

    //appropriate accessor
    public int getNumberOfsides()
    {
        return numberOfsides;
    }

    //mutator methods
    public void setNumberOfsides(int numberOfsides)
    {
        this.numberOfsides = numberOfsides;
    }

    //display methods 
    public void display()
    {
        System.out.println(toString());
    }

    //toString methods
    public String toString()
    {
        return "Number Of Sides: " + numberOfsides;
    }

    // 
    public int roll()
    {
        Random random = new Random();
        // nextInt(numberOfsides) = number from 0 to numberOfsides-1
        return random.nextInt(numberOfsides) + 1;// add 1 shifts the range to 1 to numberOfsides
    }

    
}
