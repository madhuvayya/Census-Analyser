package censusanalyser;

import com.google.gson.Gson;

import java.util.*;
import java.util.stream.Collectors;

public class CensusAnalyser {

    enum Country {
        INDIA,US
    }

    Map<String, CensusDAO> censusStateMap = null;

    public int loadCensusData(Country country,String... csvFilePath) throws CensusAnalyserException {
        censusStateMap = CensusAdapterFactory.getCensusData(country,csvFilePath);
        return censusStateMap.size();
    }

    public String getStateWiseSortedCensusData() throws CensusAnalyserException {
        if(censusStateMap == null || censusStateMap.size() ==0 ) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        List<CensusDAO> sortedCensusData = censusStateMap.values().stream().sorted(Comparator.comparing(CensusDAO::getState)).collect(Collectors.toList());
        String sortedStateCensusDataInJson = new Gson().toJson(sortedCensusData);
        return sortedStateCensusDataInJson;
    }

    public String getSortedCensusDataAccordingToPopulation() throws CensusAnalyserException {
        if(censusStateMap == null || censusStateMap.size() ==0 ) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        List<CensusDAO> sortedCensusData = censusStateMap.values().stream().sorted(Comparator.comparing(CensusDAO::getPopulation).reversed()).collect(Collectors.toList());
        String sortedCensusDataInJson = new Gson().toJson(sortedCensusData);
        return sortedCensusDataInJson;
    }

    public String getSortedCensusDataAccordingToPopulationDensity() throws CensusAnalyserException {
        if(censusStateMap == null || censusStateMap.size() ==0 ) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        List<CensusDAO> sortedCensusData = censusStateMap.values().stream().sorted(Comparator.comparing(CensusDAO::getPopulationDensity).reversed()).collect(Collectors.toList());
        String sortedCensusDataInJson = new Gson().toJson(sortedCensusData);
        return sortedCensusDataInJson;
    }

    public String getSortedCensusDataAccordingToArea() throws CensusAnalyserException {
        if(censusStateMap == null || censusStateMap.size() ==0 ) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        List<CensusDAO> sortedCensusData = censusStateMap.values().stream().sorted(Comparator.comparing(CensusDAO::getTotalArea).reversed()).collect(Collectors.toList());
        String sortedCensusDataInJson = new Gson().toJson(sortedCensusData);
        return sortedCensusDataInJson;
    }
}
