import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.math.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.Random;

class Event{

	public String eventName;
    public char eventCategory;
    public int minValue;
    public int maxValue;
    public int  weight;
	
	
    public Event (String eventName, char eventCategory, int minValue, int maxValue, int  weight)
	{
		this.eventName = eventName;
		this.eventCategory = eventCategory;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.weight = weight;
	}

    //getter example
	public String geteventName(){
		return eventName;
	}

    public char geteventCategory(){
		return eventCategory;
	}

    public int getMax(){
		return minValue;
	}

    public int getMin(){
		return minValue;
	}

    public int getWeight(){
		return weight;
	}
}

class Stats{

	public String eventName;
    public double mean;
    public double standardDeviation;
	
	public Stats (String eventName, double mean, double standardDeviation)
	{
		this.eventName = eventName;
		this.mean = mean;
        this.standardDeviation = standardDeviation;
	}

    public double getStatsMean(){
        return mean;
    }

    public double getStatsStandardDeviation(){
        return standardDeviation;
    }
}

class GeneratedEvent{

	public String eventName;
    public double mean=0;
    public double standardDeviation=0;
    public double varianceNumerator=0.0;
    public double variance=0.0;
    public double meanNumerator=0.0;
    public int count = 0; 

    public GeneratedEvent (String eventName){
		this.eventName = eventName;
	}

    public void setMeanNumerator (double val){
        this.count = this.count +1;
        this.meanNumerator = this.meanNumerator + val;
    }

    public void setGeneratedAverage(){
        this.mean = (this.meanNumerator/this.count);
    }

    public double getGeneratedAverage(){
        return this.mean;
    }

    public void setVarianceNumerator(double val){
        this.varianceNumerator = this.varianceNumerator + Math.pow( (this.mean-val) ,2);

	}

    public double getVarianceNumerator(){
		return this.varianceNumerator;
	}

    public void setVariance(){
		this.variance = varianceNumerator/count;
	}

    public double getVariance(){
		return variance;
	}

    public void setGeneratedStandardDev(){
		this.standardDeviation = Math.sqrt(this.variance);
	}  

    public double getGeneratedStandardDev(){
		return standardDeviation ;
	} 

    

    public String getGeneratedEventName(){
		return eventName;
	}  

}

class Assignment3{
    //below round function to round up double to 2 d.p. is from
    //https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static int RandomNumberGeneratorInt(int min, int max, double mean , double std){
        Random rand = new Random(); //instance of random class
        int upperbound ;
        int int_random ;
        
        //if there is no max specified for that event
        //get the mean and std to calculate the max
        if(max == 0 ){ 
            double data = (3*std)+mean;
            upperbound = (int)data; 
        }

        else {         
            upperbound = max; // else max will be that number specified
        }
        //generate random values depending on the mac
        return int_random = rand.nextInt(upperbound); 
    }

    public static double RandomNumberGeneratorDouble(int minInput, int maxInput, double mean , double std){       
        double upperbound ;
        double min = minInput;

        //if there is no max specified for that event
        //get the mean and std to calculate the max
        if(maxInput == 0 ){ 
            upperbound = (3*std)+mean;
        }
        
        else {
            upperbound = maxInput; // else max will be that number specified
        }
        //generate random values depending on the mac
        return round( ((Math.random() * (upperbound - min)) + min), 2);
    }

    public static void main(String[] args) 
    {

        int numEvents = 0;
        int numEvents2 = 0;
        ArrayList<Event> EventList = new ArrayList<Event>();
        ArrayList<Stats> StatsList = new ArrayList<Stats>();
        ArrayList<GeneratedEvent> BaselineStatTrainingList = new ArrayList<GeneratedEvent>();
        ArrayList<Stats> NewStatsList = new ArrayList<Stats>();
        ArrayList<GeneratedEvent> LiveStatList = new ArrayList<GeneratedEvent>();
        double thresholdSum = 0;
        ArrayList<ArrayList<Double> > dailyCounterLiveData = new ArrayList<ArrayList<Double> >(); //2d array for daily counter for simulated "Live" Data
        //List<List> dailyCounterLiveData = new ArrayList<List>();

        //----------------------------------------
        //part A: Extract and store all the events and each events' info from Events.txt file
        System.out.println("---part A: Extract and store all the events and each events' info from Events.txt file---");
        BufferedReader reader1;
        try 
        {
            reader1 = new BufferedReader(new FileReader("Events.txt"));
            String line = reader1.readLine();

            int lineCounter = 0;
            
            while (line != null) 
            {
                //System.out.println("lineCounter-" + lineCounter);

                if(lineCounter == 0){ //if it's the first line, it's the 'number of events being monitored'
                    //System.out.println("lineCounter-" + lineCounter);
                    numEvents = Integer.parseInt(line);
                    //System.out.println("if-" + line);
                }

                if(lineCounter>0) { //else extract each events' details
                    //System.out.println("lineCounterelse-" + lineCounter);
                    //System.out.println("else-" + line );

                    //----------------------------------------
                    //first parameter extraction 
                    //Returns index of the first occurrence of character ':'
                    int firstParamIndex = line.indexOf(':');
                    //System.out.println("firstParamIndex-" + firstParamIndex);
                    //extract first parameter without colon
                    String firstParam = line.substring(0,firstParamIndex);
                    //remove first parameter and first colon from line
                    line = line.substring(firstParamIndex+1);
                    //System.out.println("firstParam-" + firstParam );
                    //System.out.println("after firstparam extraction-" + line );

                    //----------------------------------------
                    //second parameter extraction 
                    //Returns index of the second occurrence of character ':'
                    int secondParamIndex = line.indexOf(':');
                    //extract second parameter without colon
                    String secondParamString = line.substring(0,secondParamIndex+1);
                    //System.out.println("secondParamString-" + secondParamString);
                    //convert extracted second parameter from String to char
                    char secondParam = secondParamString.charAt(0);
                    //remove second parameter from line
                    line = line.substring(secondParamIndex+1);
                    //System.out.println("secondParam-" + secondParam );
                    //System.out.println("after secondparam extraction-" + line );

                    //----------------------------------------
                    //Returns index of the third occurrence of character ':'
                    int thirdParamIndex = line.indexOf(':');

                    //extract third parameter without colon
                    String thirdParamString = line.substring(0,thirdParamIndex);
                    //System.out.println("thirdParamString-" + thirdParamString);

                    //convert extracted third parameter from String to int
                    int thirdParam = Integer.parseInt(thirdParamString); 

                    //remove third parameter from line
                    line = line.substring(thirdParamIndex+1);
                    //System.out.println("thirdParam-" + thirdParam );
                    //System.out.println("after thirdparam extraction-" + line );

                    //----------------------------------------
                    //Returns index of the fourth occurrence of character ':'
                    int fourthParamIndex = line.indexOf(':');
                    //System.out.println("fourthParamIndex-" + fourthParamIndex);

                    int fourthParam = 0;

                    if(fourthParamIndex != 0){ //if 4th parameter is not empty
                        //extract fourth parameter without colon
                        String fourthParamString = line.substring(0,fourthParamIndex);
                        //System.out.println("fourthParamString-" + fourthParamString);

                        //convert extracted fourth parameter from String to int
                        fourthParam = Integer.parseInt(fourthParamString); 
                    }

                    //remove fourth parameter from line
                    line = line.substring(fourthParamIndex+1);
                    //System.out.println("fourthParam-" + fourthParam );
                    //System.out.println("after fourthparam extraction-" + line );                     

                    //----------------------------------------
                    //Returns index of the fifth occurrence of character ':'
                    int fifthParamIndex = line.indexOf(':');
                    //System.out.println("fifthParamIndex-" + fifthParamIndex);

                    //extract fifth parameter without colon
                    String fifthParamString = line.substring(0,fifthParamIndex);
                    //System.out.println("fifthParamString-" + fifthParamString);

                    //convert extracted fourth parameter from String to int
                    int fifthParam = Integer.parseInt(fifthParamString);

                    //remove fifth parameter from line
                    line = line.substring(fifthParamIndex); 
                    //System.out.println("fifthParam-" + fifthParam );
                    //System.out.println("after fifthparam extraction-" + line );  

                    //----------------------------------------
                    //System.out.println("firstParam:" + firstParam + ", secondParam:" + secondParam + ", thirdParam:" + thirdParam + ", fourthParam:" +fourthParam + ", fifthParam:" + fifthParam);
                    EventList.add( new Event(firstParam, secondParam, thirdParam, fourthParam, fifthParam) );
                }
                lineCounter = lineCounter + 1; //when there is still more lines to read add 1 to linecounter.
                line = reader1.readLine();
            }
            
            reader1.close();
            System.out.print("Storing data from Events.txt file is successful");
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        
        //----------------------------------------
        //part B: Extract and store all the events' stats from Stats.txt file
        System.out.println("---part B: Extract and store all the events' stats from Stats.txt file---");
        BufferedReader reader2;
        try 
        {
            reader2 = new BufferedReader(new FileReader("Stats.txt"));
            String line2 = reader2.readLine();

            int lineCounter2 = 0;
            
            while (line2 != null) 
            {
               //System.out.println("lineCounter2-" + lineCounter2);

                if(lineCounter2 == 0){ //if it's the first line, it's the 'number of events being monitored'
                    //System.out.println("lineCounter2-" + lineCounter2);
                    numEvents2 = Integer.parseInt(line2);
                    //System.out.println("if2-" + line2);
                }

                if(lineCounter2>0) { //else extract each events' details
                    //System.out.println("lineCounterelse2-" + lineCounter2);
                    //System.out.println("else2-" + line2 );

                    //----------------------------------------
                    //first parameter extraction 
                    //Returns index of the first occurrence of character ':'
                    int firstParamIndex2 = line2.indexOf(':');
                    //System.out.println("firstParamIndex2-" + firstParamIndex2);
                    //extract first parameter without colon
                    String firstParam2 = line2.substring(0,firstParamIndex2);
                    //remove first parameter and first colon from line
                    line2 = line2.substring(firstParamIndex2+1);
                    //System.out.println("firstParam2-" + firstParam2 );
                    //System.out.println("after firstparam extraction2-" + line2 );

                    //----------------------------------------
                    //second parameter extraction 
                    //Returns index of the second occurrence of character ':'
                    int secondParamIndex2 = line2.indexOf(':');
                    //extract second parameter without colon
                    String secondParamString2 = line2.substring(0,secondParamIndex2);
                    //System.out.println("secondParamString2-" + secondParamString2);
                    //convert extracted second parameter from String to char
                    double secondParam2 = Double.parseDouble(secondParamString2); 
                    //remove second parameter from line
                    line2 = line2.substring(secondParamIndex2+1);
                    //System.out.println("secondParam2-" + secondParam2 );
                    //System.out.println("after secondparam extraction2-" + line2 );

                    //----------------------------------------
                    //third parameter extraction 
                    //Returns index of the third occurrence of character ':'
                    int thirdParamIndex2 = line2.indexOf(':');

                    //extract third parameter without colon
                    String thirdParamString2 = line2.substring(0,thirdParamIndex2);
                    //System.out.println("thirdParamString2-" + thirdParamString2);

                    //convert extracted third parameter from String to int
                    double thirdParam2 = Double.parseDouble(thirdParamString2); 

                    //remove third parameter from line
                    line2 = line2.substring(thirdParamIndex2+1);
                    //System.out.println("thirdParam2-" + thirdParam2 );
                    //System.out.println("after thirdparam extraction2-" + line2 ); 

                    //----------------------------------------
                    //System.out.println("firstParam2:" + firstParam2 + ", secondParam2:" + secondParam2 + ", thirdParam2:" + thirdParam2 );
                    StatsList.add( new Stats(firstParam2, secondParam2, thirdParam2) );
                }
                lineCounter2 = lineCounter2 + 1; //when there is still more lines to read add 1 to linecounter.
                line2 = reader2.readLine();
            }
            System.out.println("Storing data from Stats.txt file is successful");
            reader2.close();

        } 
        catch (IOException e) {
            e.printStackTrace();
        }

        //----------------------------------------
        //part C: Get input for the number of days and generate events
        // Getting days input from user
        Scanner input = new Scanner(System.in);

        System.out.println("Enter number of days: ");
        int numDays = input.nextInt();

        //System.out.println("number of days= " + numDays);

        //----------------------------------------
        //Part D: Generate events, and store each day's event's to a file depending on the number of days user have input
        for(int i = 1 ; i <= numDays; i++){
            try {
                FileWriter myWriter = new FileWriter("Day"+ i +".txt");
                
                //writing to the file

                for(int e= 0; e<numEvents ; e++ ){

                    //extract event name
                    String writeLine =  EventList.get(e).geteventName(); //extract each event
                    
                    //extract if D or C
                    char extractEventCat = EventList.get(e).geteventCategory();
                    int randInt = 0;
                    double  randDouble= 0.0;

                    //random number generated based on the min and max of that event
                    if (extractEventCat == 'C'){
                       randDouble = RandomNumberGeneratorDouble(EventList.get(e).getMin(), EventList.get(e).getMax(), StatsList.get(e).getStatsMean(), StatsList.get(e).getStatsStandardDeviation());
                       writeLine = writeLine + ":C:"+ randDouble + "\n";
                    }

                    else if(extractEventCat == 'D'){
                        randInt = RandomNumberGeneratorInt(EventList.get(e).getMin(), EventList.get(e).getMax(), StatsList.get(e).getStatsMean(), StatsList.get(e).getStatsStandardDeviation());
                        writeLine = writeLine + ":D:" + randInt + "\n";
                    }
                    //store each generated event in the 
                    myWriter.write(writeLine);
                }
                myWriter.close();
                System.out.println("Successfully written Day" + i +".txt");                    
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.
                printStackTrace();
            }
            System.out.println("Successfully generated events");
        }

        //----------------------------------------
        //part E: Get the Baseline Stats (Training) from the generated events which is in the BaselineStatTrainingList

        double mean = 0;
        double standardDeviation = 0;

        //create the new stats events for BaselineStatTrainingList arraylist
        for (int e = 0; e<numEvents ;e++ ){
            BaselineStatTrainingList.add( new GeneratedEvent(EventList.get(e).geteventName()));          
        }

        //open every days file to add up all the values for all the days in that event
        BufferedReader reader3;
        for(int n = 1 ; n <= numDays; n++){       

            try {
                reader3 = new BufferedReader(new FileReader("Day"+n+".txt"));
                //System.out.println("Day= "+ n);
                String line3 = reader3.readLine();
                //System.out.println("line3="+line3 );
                int lineCounter3 = 0;
           
                while (line3 != null) {
                    //get the index of the first ':' in the line and remove the characters till that ':'
                    int valIndex1 = line3.indexOf(':');
                    //System.out.println("valIndex1= "+valIndex1);

                    //extract the remaining string  by using substring
                    String valString1 = line3.substring(valIndex1+1);
                    //System.out.println("valString1= "+valString1 );

                    //get the index of the second ':' in the line and remove the characters till that ':'
                    int valIndex2 = valString1.indexOf(':');
                    //System.out.println("valIndex2= "+valIndex2 );

                    //extract again the remaining string by using substring
                    String valString2 = valString1.substring(valIndex2+1);
                    //System.out.println("valString2= "+valString2);
                    
                    //convert extracted  String to double
                    double val = Double.parseDouble(valString2); 
                    //System.out.println("val= "+val);

                    //System.out.println("lineCounter3= "+lineCounter3);

                    BaselineStatTrainingList.get(lineCounter3).setMeanNumerator(val);
                    BaselineStatTrainingList.get(lineCounter3).setGeneratedAverage();
                    lineCounter3 ++;
                    line3 = reader3.readLine();
                    //System.out.println("-----------------------");
                }
                      
            //System.out.println("----------------------------------------------");
            }
            
            catch (IOException e) {
                System.out.println("An error occurred.");
                e.
                printStackTrace();
            }
        }


        //----------------------------------------
        //part F: calculate the variance numerator for each events in the Baseline Stats (Training) 
        System.out.println("---part F: calculate the variance numerator for each events in the Baseline Stats (Training) ---");
        //calculate for variance numerator
        for(int nd = 1 ; nd <= numDays; nd++){ 

            //get each day 
            BufferedReader reader5;
            try 
            {
                reader5 = new BufferedReader(new FileReader("Day"+nd+".txt"));
                String line5 = reader5.readLine();

                //add each event's val of the day to the  variance 
                
                int lineCounter4 = 0; //to determine which event it is in each file
                while (line5 != null) { //checker if there is a line when reading each line at the file

                    // do the extraction again
                    //get the index of the first ':' in the line and remove the characters till that ':'
                    int bstIndex = line5.indexOf(':');
                    //System.out.println("bstIndex= "+bstIndex);

                    //extract the remaining string  by using substring
                    String bstString1 = line5.substring(bstIndex+1);
                    //System.out.println("bstString1= "+bstString1 );

                    //get the index of the second ':' in the line and remove the characters till that ':'
                    int bstIndex2 = bstString1.indexOf(':');
                    //System.out.println("bstIndex2= "+bstIndex2 );

                    //extract again the remaining string by using substring
                    String bstString2 = bstString1.substring(bstIndex2+1);
                    //System.out.println("bstString2= "+bstString2);
                    
                    //convert extracted  String to double
                    double bstVal = Double.parseDouble(bstString2); 
                    //System.out.println("bst= "+ lineCounter4 +", bstVal= "+bstVal);

                    BaselineStatTrainingList.get(lineCounter4).setVarianceNumerator(bstVal);
                    //System.out.println("BaselineStatTrainingList.get("+lineCounter4+").getVarianceNumerator()= " + BaselineStatTrainingList.get(lineCounter4).getVarianceNumerator());
                    line5 = reader5.readLine();
                    lineCounter4 ++;
                } 

            } 
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        //----------------------------------------
        //part G: calculate for mean and standard dev for each event Baseline Stats (Training) then finally store all to a file
        System.out.println("---part G: calculate for mean and standard dev for each event then finally store all to a file---");
        //after settling the variance numerator, finally calculate the variance then the standard dev
        try {
            
            FileWriter myWriter2 = new FileWriter("Baseline Statistics.txt");
            String writeLine2;
            for (int bst = 0; bst< BaselineStatTrainingList.size() ; bst++ ){
                //System.out.println("Event=" + BaselineStatTrainingList.get(bst).getGeneratedEventName() + " ,varianceNumerator=" + BaselineStatTrainingList.get(bst).getVarianceNumerator());
               

                //finally calculate the variance then the standard dev
                BaselineStatTrainingList.get(bst).setVariance();
                BaselineStatTrainingList.get(bst).setGeneratedStandardDev();

                ///write to a file the mean and standard dev
                writeLine2 = BaselineStatTrainingList.get(bst).getGeneratedEventName() + ":" + round(BaselineStatTrainingList.get(bst).getGeneratedAverage(),2) + ":" + round(BaselineStatTrainingList.get(bst).getGeneratedStandardDev(),2)+": \n";
                myWriter2.write(writeLine2);

                //add the event's mean for the threshold calculation
                thresholdSum = thresholdSum + round(EventList.get(bst).getWeight(),2);
            }
        
            myWriter2.close();
            System.out.println("Successfully written file Baseline Statistics.txt");                    
        } 
        
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.
            printStackTrace();
        }
        System.out.println("Successfully created new statistic file");
        
        //----------------------------------------
        //part H: extract the new stats from the new stats file
        System.out.println("---part H: extract the new stats from the new stats file---");
        BufferedReader reader6;
        try 
        {
            reader6 = new BufferedReader(new FileReader("NewStats.txt"));
            String line6 = reader6.readLine();

            int lineCounter6 = 0;
            
            while (line6 != null) 
            {
                //System.out.println("lineCounter6-" + lineCounter6);

                if(lineCounter6 == 0){ //if it's the first line, it's the 'number of events being monitored'
                    //System.out.println("lineCounter6-" + lineCounter6);
                    numEvents2 = Integer.parseInt(line6);
                    //System.out.println("if6-" + line6);
                }

                if(lineCounter6>0) { //else extract each events' details
                    //System.out.println("lineCounterelse6-" + lineCounter6);
                    //System.out.println("else6-" + line6 );

                    //----------------------------------------
                    //first parameter extraction 
                    //Returns index of the first occurrence of character ':'
                    int firstParamIndex6 = line6.indexOf(':');
                    //System.out.println("firstParamIndex6-" + firstParamIndex6);
                    //extract first parameter without colon
                    String firstParam6 = line6.substring(0,firstParamIndex6);
                    //remove first parameter and first colon from line
                    line6 = line6.substring(firstParamIndex6+1);
                    //System.out.println("firstParam6-" + firstParam6 );
                    //System.out.println("after firstparam extraction6-" + line6 );

                    //----------------------------------------
                    //second parameter extraction 
                    //Returns index of the second occurrence of character ':'
                    int secondParamIndex6 = line6.indexOf(':');
                    //extract second parameter without colon
                    String secondParamString6 = line6.substring(0,secondParamIndex6);
                    //System.out.println("secondParamString6-" + secondParamString6);
                    //convert extracted second parameter from String to char
                    double secondParam6 = Double.parseDouble(secondParamString6); 
                    //remove second parameter from line
                    line6 = line6.substring(secondParamIndex6+1);
                    //System.out.println("secondParam6-" + secondParam6 );
                    //System.out.println("after secondparam extraction6-" + line6 );

                    //----------------------------------------
                    //third parameter extraction 
                    //Returns index of the third occurrence of character ':'
                    int thirdParamIndex6 = line6.indexOf(':');

                    //extract third parameter without colon
                    String thirdParamString6 = line6.substring(0,thirdParamIndex6);
                    //System.out.println("thirdParamString6-" + thirdParamString6);

                    //convert extracted third parameter from String to int
                    double thirdParam6 = Double.parseDouble(thirdParamString6); 

                    //remove third parameter from line
                    line6 = line6.substring(thirdParamIndex6+1);
                    //System.out.println("thirdParam6-" + thirdParam6 );
                    //System.out.println("after thirdparam extraction6-" + line6 );

                    //add each stat to a new arraylist for stats
                    NewStatsList.add( new Stats(firstParam6, secondParam6, thirdParam6) ); 
                }
                lineCounter6 = lineCounter6 + 1; //when there is still more lines to read add 1 to linecounter.
                line6 = reader6.readLine();
            }
            System.out.print("Storing data from Stats.txt file is successful");
            reader6.close(); 

        } 
        catch (IOException e) {
            e.printStackTrace();
        }

        //----------------------------------------
        //part I: Get another input for new number of days and generate events
        System.out.println("---part I: Get another input for new number of days and generate events---");
        // Getting days input from user
        Scanner input2 = new Scanner(System.in);

        System.out.println("Enter number of days: ");
        int numDays2 = input2.nextInt();

        //System.out.println("number of days= " + numDays);

        //----------------------------------------
        //Part J: Generate new events again, and store each day's event's to a file depending on the number of days user have input
         System.out.println("---Part J: Generate new events again, and store each day's event's to a file depending on the number of days user have input---");
        for(int i = 1 ; i <= numDays2; i++){
            try {
                FileWriter myWriter3 = new FileWriter("NewDay"+ i +".txt");
                
                //writing to the file

                for(int e= 0; e<numEvents2 ; e++ ){

                    //extract event name
                    String writeLine =  EventList.get(e).geteventName(); //extract each event
                    
                    //extract if D or C
                    char extractEventCat = EventList.get(e).geteventCategory();
                    int randInt = 0;
                    double  randDouble= 0.0;

                    //random number generated based on the min and max of that event
                    if (extractEventCat == 'C'){
                       randDouble = RandomNumberGeneratorDouble(EventList.get(e).getMin(), EventList.get(e).getMax(), NewStatsList.get(e).getStatsMean(), NewStatsList.get(e).getStatsStandardDeviation());
                       writeLine = writeLine + ":C:"+ randDouble + "\n";
                    }

                    else if(extractEventCat == 'D'){
                        randInt = RandomNumberGeneratorInt(EventList.get(e).getMin(), EventList.get(e).getMax(), NewStatsList.get(e).getStatsMean(), NewStatsList.get(e).getStatsStandardDeviation());
                        writeLine = writeLine + ":D:" + randInt + "\n";
                    }
                    //store each generated event in the file
                    myWriter3.write(writeLine);
                }
                myWriter3.close();
                System.out.println("Successfully written file NewDay" + i +".txt");                    
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.
                printStackTrace();
            }
            System.out.println("Successfully generated events");
        }

        //----------------------------------------
        //part K: Get new Stats (Live) from the simulated events which is in the LiveStatList
        System.out.println("---part K: Get new Stats (Live) from the simulated events which is in the LiveStatList---");
        double mean2 = 0;
        double standardDeviation2 = 0;

        //create the new stats events for LiveStatList arraylist
        for (int en = 0; en<numEvents ; en++ ){
            LiveStatList.add( new GeneratedEvent(EventList.get(en).geteventName()));          
        }

        //open every days file to add up all the values for all the days in that event
        BufferedReader reader7;
        for(int nn = 1 ; nn <= numDays2; nn++){      

            try {
                reader7 = new BufferedReader(new FileReader("NewDay"+nn+".txt"));
                //System.out.println("Day= "+ n);
                String line7 = reader7.readLine();
                //System.out.println("line7="+line7 );
                int lineCounter7 = 0;
           
                while (line7 != null) {
                    //get the index of the first ':' in the line and remove the characters till that ':'
                    int valIndex7 = line7.indexOf(':');
                    //System.out.println("valIndex7= "+valIndex7);

                    //extract the remaining string  by using substring
                    String valString7 = line7.substring(valIndex7+1);
                    //System.out.println("valString7= "+valString7 );

                    //get the index of the second ':' in the line and remove the characters till that ':'
                    valIndex7 = valString7.indexOf(':');
                    //System.out.println("valIndex7= "+valIndex7 );

                    //extract again the remaining string by using substring
                    valString7 = valString7.substring(valIndex7+1);
                    //System.out.println("valString7= "+valString7);
                    
                    //convert extracted  String to double
                    double val = Double.parseDouble(valString7); 
                    //System.out.println("val= "+val);

                    //System.out.println("lineCounter3= "+lineCounter3);

                    LiveStatList.get(lineCounter7).setMeanNumerator(val);
                    LiveStatList.get(lineCounter7).setGeneratedAverage();
                    lineCounter7 ++;
                    line7 = reader7.readLine();
                    //System.out.println("-----------------------");
                }
                      
            //System.out.println("----------------------------------------------");
            }
            
            catch (IOException e) {
                System.out.println("An error occurred.");
                e.
                printStackTrace();
            }
        }
    
        //----------------------------------------
        //part L: calculate the variance numerator for each events in the LiveStatList
        System.out.println("---part L: calculate the variance numerator for each events in the LiveStatList---");
        //calculate for variance numerator
        for(int nd = 1 ; nd <= numDays2; nd++){ 

            //get each day 
            BufferedReader reader8; 
            try 
            {
                reader8 = new BufferedReader(new FileReader("NewDay"+nd+".txt"));
                String line8 = reader8.readLine();

                //add each event's val of the day to the  variance 
                
                int lineCounter8 = 0; //to determine which event it is in each file
                while (line8 != null) { //checker if there is a line when reading each line at the file

                    // do the extraction again
                    //get the index of the first ':' in the line and remove the characters till that ':'
                    int bstIndex = line8.indexOf(':');
                    //System.out.println("bstIndex= "+bstIndex);

                    //extract the remaining string  by using substring
                    String bstString1 = line8.substring(bstIndex+1);
                    //System.out.println("bstString1= "+bstString1 );

                    //get the index of the second ':' in the line and remove the characters till that ':'
                    int bstIndex2 = bstString1.indexOf(':');
                    //System.out.println("bstIndex2= "+bstIndex2 );

                    //extract again the remaining string by using substring
                    String bstString2 = bstString1.substring(bstIndex2+1);
                    //System.out.println("bstString2= "+bstString2);
                    
                    //convert extracted  String to double
                    double bstVal = Double.parseDouble(bstString2); 
                    //System.out.println("bst= "+ lineCounter4 +", bstVal= "+bstVal);

                    LiveStatList.get(lineCounter8).setVarianceNumerator(bstVal);
                    //System.out.println("LiveStatList.get("+lineCounter8+").getVarianceNumerator()= " + LiveStatList.get(lineCounter8).getVarianceNumerator());
                    line8 = reader8.readLine();
                    lineCounter8 ++;
                } 

            } 
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        //----------------------------------------
        //part M: calculate for mean and standard dev for each event in the LiveStatList then finally store all to a file
        System.out.println("---part M: calculate for mean and standard dev for each event in the LiveStatList then finally store all to a file---");
        //after settling the variance numerator, finally calculate the variance then the standard dev
        try { 
            
            FileWriter myWriter4 = new FileWriter("Live Statistics.txt");
            String writeLine4;
            for (int bst = 0; bst< LiveStatList.size() ; bst++ ){
                //System.out.println("Event=" + LiveStatList.get(bst).getGeneratedEventName() + " ,varianceNumerator=" + LiveStatList.get(bst).getVarianceNumerator());
               

                //finally calculate the variance then the standard dev
                LiveStatList.get(bst).setVariance();
                LiveStatList.get(bst).setGeneratedStandardDev();

                ///write to a file the mean and standard dev
                writeLine4 = LiveStatList.get(bst).getGeneratedEventName() + ":" + round(LiveStatList.get(bst).getGeneratedAverage(),2) + ":" + round(LiveStatList.get(bst).getGeneratedStandardDev(),2)+": \n";
                myWriter4.write(writeLine4);
            }
        
            myWriter4.close();
            System.out.println("Successfully written file Live Statistics.txt");                    
        } 
        
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.
            printStackTrace();
        }


        //----------------------------------------
        //part N: calculate threshold
        System.out.println("---part N: calculate threshold---");
        double threshold = 2*thresholdSum;

        System.out.println("Threshold: " + round(threshold,2)); 
     
        //----------------------------------------
        //part O: calculate daily counter for simulated "Live" Data
        System.out.println("---part O: calculate daily counter for simulated Live Data---");
        //get each event's value in each NewDay file
        for(int nd = 1 ; nd <= numDays2; nd++){ 

            //read each NewDay file
            BufferedReader reader9; 
            try 
            { 
                reader9 = new BufferedReader(new FileReader("NewDay"+nd+".txt"));
                String line9 = reader9.readLine();

                //create a list for all events' counters for each day
                //List list1=new ArrayList();
                ArrayList<Double> List1 = new ArrayList<Double>();

                int lineCounter9 = 0; //to determine which event it is in each file
                while (line9 != null) { //checker if there is a line when reading each line at the file

                    // do the extraction again
                    //get the index of the first ':' in the line and remove the characters till that ':'
                    int bstIndex = line9.indexOf(':');
                    //System.out.println("bstIndex= "+bstIndex);

                    //extract the remaining string  by using substring
                    String bstString1 = line9.substring(bstIndex+1);
                    //System.out.println("bstString1= "+bstString1 );

                    //get the index of the second ':' in the line and remove the characters till that ':'
                    int bstIndex2 = bstString1.indexOf(':');
                    //System.out.println("bstIndex2= "+bstIndex2 );

                    //extract again the remaining string by using substring
                    String bstString2 = bstString1.substring(bstIndex2+1);
                    //System.out.println("bstString2= "+bstString2);
                    
                    //convert extracted  String to double
                    double bstVal = Double.parseDouble(bstString2); 
                    //System.out.println("bst= "+ lineCounter4 +", bstVal= "+bstVal);

                    //calculate the counter
                    double counter = ( Math.abs(bstVal-BaselineStatTrainingList.get(lineCounter9).getGeneratedAverage()) / BaselineStatTrainingList.get(lineCounter9).getGeneratedStandardDev())* EventList.get(lineCounter9).getWeight();
                    //System.out.println();
                    //store the every event counter for each day inside a list
                    List1.add(counter);

                    line9 = reader9.readLine();
                    lineCounter9 ++;
                } 

                //store the daily counter for simulated "Live" Data into a 2D array
                dailyCounterLiveData.add(List1);

            } 
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        //----------------------------------------
        //part P: calculate daily counter for each day in simulated "Live" Data and determine if that day exceeded threshold
        System.out.println("---part P: calculate daily counter for each day in simulated Live Data and determine if that day exceeded threshold---");
        double DailyTotalCounter = 0;
        //for each day
        for(int nd = 1 ; nd <= numDays2; nd++){
            
            DailyTotalCounter = 0; // for each reset to 0;

            //for each event of that day
            for(int ne = 0 ; ne < numEvents; ne++){
                DailyTotalCounter = DailyTotalCounter + dailyCounterLiveData.get(nd-1).get(ne);
                //System.out.println("Day=" + nd + ", Event=" + ne + ", DailyTotalCounter="+ DailyTotalCounter);
            }

            if(DailyTotalCounter > threshold){ //if DailyTotalCounter is more than threshold, flag that day as abnormal
                System.out.println("Day"+ nd +" is abnormal. Daily Total Counter= " + round(DailyTotalCounter,2));
            }

            else if (DailyTotalCounter < threshold){ //if DailyTotalCounter is less than threshold, flag that day as normal
                System.out.println("Day"+ nd +" is normal. Daily Total Counter= " + round(DailyTotalCounter,2));
            }
        }

    }   
}