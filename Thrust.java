import java.util.Scanner;

public class Thrust
{
    double R = .08206; // gas constant, L atm K-1 mol-1
    
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
        setVolume(); // L
        setDensity(); // kg/L
        setCPressure(); // atm
        setExitPressure(); // atm
        setExitVelocity();
        setMassFlowRate();
        setThrust();
        printAll();
    }
    
    public void setVolume() // in: cm, cm, out: liters
    {
        volume = Math.PI * Math.pow(radius, 2) * length;
        volume *= .001; // cm^3 to liters
    }
    public void setDensity() // in: g, L, out: g/cm^3
    {
        totalMass = 0;
        for(int i = 0; i < 5; i++)
        {
            totalMass += gramsOfGases[i];
        }
        density = totalMass / (volume / .001); // g/cm^3
    }
    public void setCPressure() // out: atm
    {
        double[] pPressures = new double[5]; // partial pressures
        for(int i = 0; i < 5; i++)
        {
            pPressures[i] = getPressure(molesOfGases[i]);
        }
        cPressure = 0;
        for(int i = 0; i < 5; i++)
        {
            cPressure += pPressures[i];
        }
        System.out.println("cPressure: " + cPressure);
    }
        private double getPressure(double n) // in: L, g/(g/mol) = mol, K out: atm
        {
            double pressure = n * R * temp / volume; // gas constant, L atm K-1 mol-1
            return pressure;
        }
    public void setExitPressure() // in: kg/m^3, J/kg K, K out: atm note: g/cm^3 == kg/L
    {
        System.out.println("http://www.umutaksoy.com/tools/nozzle/pressureexit.php\nDensity: " + density + "Rspecific: " + rSpecific + "Temperature: " + temp);
        //exitPressure = in.nextDouble();
        exitPressure = 1.7384548856672;
    }
    public void setExitVelocity() // out: m/s
    {
        double exp = (gamma - 1)/gamma;
        /*double total = 0;
        for(int i = 0; i < 5; i++) // gets RMS of each gas.
        {
            RMS[i] = RMS(molesOfGases[i]); //out: m/s
        }
        for(int i = 0; i < 5; i++)
        {
            total += molesOfGases[i]/totalMass * RMS[i];
        }
        rmsAVG = total / 5; */
        exitVelocity = Math.sqrt( (temp * 8.314 / (25.3*.001)) * ((2 * gamma) / (gamma - 1)) * (1 - Math.pow( (exitPressure/cPressure), exp) ) );
        System.out.println("exit^2: " + (temp * 8.314 / (25.3/1000)) * ((2 * gamma) / (gamma - 1)) * (1 - Math.pow( (exitPressure/cPressure), exp) ));
        System.out.println("\t" + "Temp: " + temp);
        System.out.println("\t exp: " + exp);
        System.out.println("\t" + "Gamma: " + gamma);
    }
        public double RMS(double mol) // in: g/mol, out: m/s
        {
            double rms = Math.sqrt((3 * 8.31446 * temp )/ (mol*.001));
            return rms;
        }
    public void setMassFlowRate() // in: kg/L, m/s, cm^2 out: kg/s
    {
        massFlowRate = (density * (exitVelocity/100) * exitArea)/1000;
    }
    public void setThrust() // kg/s * m/s + cm^2 * (atm) to Newtons
    {
        thrust = massFlowRate * exitVelocity + (exitArea * .0001) * ((exitPressure - atm) * 1.01325 * Math.pow(10,5));
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
    
}
