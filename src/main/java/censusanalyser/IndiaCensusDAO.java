package censusanalyser;

public class IndiaCensusDAO {
    public int population;
    public int densityPerSqKm;
    public String state;
    public int areaInSqKm;
    public String stateCode;

    public IndiaCensusDAO(IndiaCensusCSV indiaCensusCSV) {
        state = indiaCensusCSV.state;
        areaInSqKm = indiaCensusCSV.areaInSqKm;
        densityPerSqKm = indiaCensusCSV.densityPerSqKm;
        population = indiaCensusCSV.population;
    }

    public int getPopulation() {
        return population;
    }

    public int getAreaInSqKm() {
        return areaInSqKm;
    }

    public String getState() {
        return state;
    }

    public int getDensityPerSqKm() {
        return densityPerSqKm;
    }

}
