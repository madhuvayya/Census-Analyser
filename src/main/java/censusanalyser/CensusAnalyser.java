package censusanalyser;

import com.google.gson.Gson;

import java.util.*;
import java.util.stream.Collectors;

public class CensusAnalyser {

    enum Country {
        INDIA,US
    }

    Map<String, CensusDAO> censusStateMap = null;

    public CensusAnalyser(Country country) {
    }

    public int loadCensusData(Country country, String... csvFilePath) throws CensusAnalyserException {
        censusStateMap = CensusAdapterFactory.getCensusData(country, csvFilePath);
        return censusStateMap.size();
    }

    public String getSortedCensusDataAccordingToStateName(Country country) throws CensusAnalyserException {
        Comparator<CensusDAO> censusComparator = Comparator.comparing(census -> census.state);
        return this.getSortedCensusData(censusComparator,country);
    }

    public String getSortedCensusDataAccordingToStateCode(Country country) throws CensusAnalyserException {
        Comparator<CensusDAO> censusComparator = Comparator.comparing(census -> census.stateCode);
        return this.getSortedCensusData(censusComparator,country);
    }

    public String getSortedCensusDataAccordingToPopulation(Country country) throws CensusAnalyserException {
        Comparator<CensusDAO> censusComparator = Comparator.comparing(census -> census.population);
        return this.getSortedCensusData(censusComparator.reversed(),country);
    }

    public String getSortedCensusDataAccordingToPopulationDensity(Country country) throws CensusAnalyserException {
        Comparator<CensusDAO> censusComparator = Comparator.comparing(census -> census.populationDensity);
        return this.getSortedCensusData(censusComparator.reversed(),country);
    }

    public String getSortedCensusDataAccordingToArea(Country country) throws CensusAnalyserException {
        Comparator<CensusDAO> censusComparator = Comparator.comparing(census -> census.totalArea);
        return this.getSortedCensusData(censusComparator.reversed(),country);
    }

    private String getSortedCensusData(Comparator<CensusDAO> censusComparator, Country country) throws CensusAnalyserException {
        if(censusStateMap == null || censusStateMap.size() ==0 ) {
                throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        List sortedCensusData = censusStateMap.values().stream().
                sorted(censusComparator).
                map(censusDAO -> censusDAO.getCensusDTO(country)).
                collect(Collectors.toList());
        return new Gson().toJson(sortedCensusData);
    }
}
