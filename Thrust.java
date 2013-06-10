import java.util.Scanner;

public class Thrust
{
    double R = .08206; // gas constant, atm
    
    // Data associated with exhaust gases.
    // H20, N2, CO2, HCN, SO2
    double molarMass[] = {18, 28, 44, 27, 64}; 
    double gramsOfGases[] = new double[5];
    double molesOfGases[] = new double[5];
    double proportionOfGases[] = {88, 68, 23, 2, 1};
    double totalMass; // of gases
    double RMS[] = new double[5];
    double rmsAVG;
    
    // Ammonium Nitrate (s) data
    double ANmolarMass = 80.052;
    double gramsOfAN = 10.25;
    double molesOfAN = .13;
    double ANproportion = 34;
    // Environment Data
    double temp = 473; // 200 C (decomposition temperature of AN)
    double atm = 1; // atmospheric pressure at sea level
    double gamma = 1.310; // approximated: water @ 200 C
    double rSpecific = 372.847; // J kg-1 K-1, used avg. molar mass
    
    // Engine Parameters
    double length;
    double radius;
    double volume;
    double exitArea = .495; // cm^2
    double density; // in combustion chamber
    double cPressure; //combustion chamber pressure
    
    // Thrust Parameters
    double thrust;
    double massFlowRate;
    double exitVelocity;
    double exitPressure;
    
    Scanner in = new Scanner(System.in);
    
    public Thrust()
    {
        for(int i = 0; i < 5; i++)
        {
            molesOfGases[i] = proportionOfGases[i] * molesOfAN / ANproportion;
        }
        for(int i = 0; i < 5; i++)
        {
            gramsOfGases[i] = molesOfGases[i] * molarMass[i];
        }
        for(int i = 0; i < 5; i++)
        {
            System.out.print(molarMass[i] + " " + proportionOfGases[i] + " " + molesOfGases[i] + " " + gramsOfGases[i] + "\n");
        }
        for(int i = 0; i < 5; i++)
        {
            RMS[i] = RMS(molarMass[i]);
        }
        radius = .635;
        length = 14;
        setVolume();
        setDensity();
        setCPressure();
        setExitPressure();
        setExitVelocity();
        setMassFlowRate();
        setThrust();
        printAll();
    }
    
    public void setVolume()
    {
        // put your code here
        volume = Math.PI * Math.pow(radius, 2) * length;
    }
    public void setDensity()
    {
        totalMass = 0;
        for(int i = 0; i < 5; i++)
        {
            totalMass += gramsOfGases[i];
        }
        density = totalMass / volume; // g/cm^3
    }
    public void setCPressure()
    {
        double[] pPressures = new double[5]; // partial pressures
        for(int i = 0; i < 5; i++)
        {
            getPressure(molesOfGases[i],gramsOfGases[i]);
        }
        cPressure = 0;
        for(int i = 0; i < 5; i++)
        {
            cPressure += pPressures[i];
        }
        
    }
        private double getPressure(double molmass, double totmass)
        {
            double n = totmass / molmass;
            double pressure = n * R * temp / volume;
            return pressure;
        }
    public void setExitPressure()
    {
        System.out.println("http://www.umutaksoy.com/tools/nozzle/pressureexit.php\nDensity: " + density + "Rspecific: " + rSpecific + "Temperature: " + temp);
        exitPressure = in.nextDouble();
    }
    public void setExitVelocity()
    {
        double exp = (gamma - 1)/gamma;
        double rms = 0;
        double total = 0;
        for(int i = 0; i < 5; i++) // gets RMS of each gas.
        {
            RMS[i] = RMS(molarMass[i]);
        }
        for(int i = 0; i < 5; i++)
        {
            total += molesOfGases[i]/totalMass * RMS[i];
        }
        rms = total / 5; 
        exitVelocity = rms * Math.sqrt((2 * gamma) / (gamma - 1)) * Math.sqrt(1 - Math.pow((exitPressure/cPressure), gamma));
    }
    public void setMassFlowRate()
    {
        massFlowRate = density * exitVelocity * exitArea;
    }
    public void setThrust()
    {
        thrust = massFlowRate * exitVelocity + exitArea * (exitPressure - atm);
    }
    public void printAll()
    {
        System.out.println("CHEMISTRY IS EASY, ANYONE CAN DO IT!" + "\n" +
        "Mass Flow Rate, m-dot: " + massFlowRate + "\n" +
        "Density, rho: " + density + "\n" +
        "Exit Velocity: " + exitVelocity + "\n" +
        "Exit Area: " + exitArea + "\n" +
        "Exit Pressure: " + exitPressure + "\n" +
        "Atmospheric Pressure: " + atm + "\n" +
        "\nThrust: " + thrust);
    }
    public double RMS(double molMass)
    {
        double rms = Math.sqrt((3 * R * temp )/ molMass);
        return rms;
    }
}
