package censusanalyser;

public class CensusDAO {
    public int population;
    public double totalArea;
    public String state;
    public double populationDensity;
    public String stateCode;

    public CensusDAO(IndiaCensusCSV indiaCensusCSV) {
        state = indiaCensusCSV.state;
        population = indiaCensusCSV.population;
        totalArea = indiaCensusCSV.areaInSqKm;
        populationDensity = indiaCensusCSV.densityPerSqKm;
    }

    public CensusDAO(USCensusCSV usCensusCSV) {
        state = usCensusCSV.state;
        stateCode = usCensusCSV.stateId;
        population = usCensusCSV.population;
        totalArea = usCensusCSV.totalArea;
        populationDensity = usCensusCSV.populationDensity;
    }

    public IndiaCensusCSV getIndiaCensusCSV() {
        return new IndiaCensusCSV(state,population,(int) populationDensity, (int) totalArea);
    }

    public int getPopulation() {
        return population;
    }

    public int getTotalArea() {
        return (int)totalArea;
    }

    public String getState() {
        return state;
    }

    public int getPopulationDensity() {
        return (int)populationDensity;
    }
    
}
