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

    public Object getCensusDTO(CensusAnalyser.Country country) {
        if(country.equals(CensusAnalyser.Country.INDIA))
            return new IndiaCensusCSV(state,population,(int)populationDensity,(int)totalArea);
        return new USCensusCSV(state,stateCode,population,populationDensity,totalArea);
    }
    
}
