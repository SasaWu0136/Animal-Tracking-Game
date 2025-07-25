import java.util.Random;
import java.util.ArrayList;


public class Trail{
    // declare field
    private ArrayList<NatureFeature> natureTrail;
    private Random random;
    

    // default constructor
    public Trail()
    {
        this.natureTrail = new ArrayList<NatureFeature>();
        random = new Random();
    }

    //non-default constructor
    // public Trail(NatureFeature[] natureTrail)
    // {

    //     for(int i = 0;i<natureTrail.length; i++)
    //     {
    //         this.natureTrail.add(natureTrail[i]);
    //     }
    // }
// constructor that natureTrail based on charArray
    public Trail(char[] charArray){
        this.natureTrail = new ArrayList<NatureFeature>();
        //natureTrail based on charArray length
        // this.natureTrail = new NatureFeature[charArray.length];
        //System.out.println("Trail length: " + charArray.length);

        //loop go through each character in charArray
        for (int i=0;i < charArray.length;i++){
            //get the character at index i
            char rightChar = charArray[i];
            //System.out.println(rightChar);

            //switch based on the character
            switch(rightChar){
                //  NatureFeature tmp;
                 case 'c':
                     natureTrail.add(new NatureFeature('c',"Creek",-2));
                     break;
                 case 'b':
                     natureTrail.add(new NatureFeature('b',"Bridge",4));
                     break;
                 case 'f':
                     natureTrail.add(new NatureFeature('f',"Fallen Tree", -3));
                     break;
                 case 'l':
                    //  tmp = new NatureFeature('l',"Landslide", -5)
                     natureTrail.add(new NatureFeature('l',"Landslide", -5));
                     break;
                 default:
                    natureTrail.add(new NatureFeature('u',"unknown", 0));
                     break;
             }
         }
         // System.out.println("Trail length: " + this.natureTrail.size());
    }



    //appropriate accessor
    public ArrayList<NatureFeature> getNatureTrail()
    {
        return natureTrail;
    }

     //mutator methods
    // public void setNatureTrail (NatureFeature[] natureTrail)
    // {
    //     this.natureTrail = natureTrail;
    // }

    //display methods 
    public void display()   
    {
        System.out.println(toString());
    }

    //toString methods
    public String toString()
    {
        return "NatureTrail: " + natureTrail ;
    }
    

    public int getLength(){
        return natureTrail.size();
    }
    

}

