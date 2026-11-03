    public class NatureFeature
    {
        // declare field
        private char featureType;
        private String featureName;
        private int spacePenalty;

    // default constructor
    public NatureFeature()
    {
        this.featureType = 'u';
        this.featureName = "unknown";
        this.spacePenalty = 0;
    }

    //non-default constructor
    public NatureFeature (char featureType,String featureName,int spacePenalty)
    {
        this.featureType = featureType;
        this.featureName = featureName;
        this.spacePenalty = spacePenalty;
    }

    //appropriate accessor
    public char getFeatureType()
    {
        return featureType;
    }

    public String getFeatureName()
    {
        return featureName;
    }

    public int getSpacePenalty()
    {
        return spacePenalty;
    }

    //mutator methods
    public void setFeatureType(char featureType)
    {
        this.featureType = featureType;
    }

    public void setFeatureName(String featureName)
    {
        this.featureName = featureName;
    }

    public void setSpacePenalty(int spacePenalty)
    {
        this.spacePenalty = spacePenalty;
    }

    //display methods 
    public void display()
    {
        System.out.println(toString());
    }

    //toString methods
    public String toString()
    {
        return "Feature Type: "+featureType+ ", Feature Name: "+featureName+ ",Space Penalty: "+spacePenalty;
    }

    }
