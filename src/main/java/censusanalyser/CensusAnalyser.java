package censusanalyser;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CensusAnalyser {

    Map<String,IndiaCensusDAO> censusStateMap = null;

    public CensusAnalyser() {
        censusStateMap = new HashMap<>();
    }

    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath))){
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaCensusCSV> csvFileIterator = csvBuilder.getCSVFileIterator(reader, IndiaCensusCSV.class);
            Iterable<IndiaCensusCSV> csvIterable = () -> csvFileIterator;
            StreamSupport.stream(csvIterable.spliterator(),false)
                    .forEach(censusCSV -> censusStateMap.put(censusCSV.state,new IndiaCensusDAO(censusCSV)));
            return this.censusStateMap.size();
        } catch (IOException ioException) {
            throw new CensusAnalyserException(ioException.getMessage(),
                                                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (RuntimeException runtimeException) {
            throw new CensusAnalyserException(runtimeException.getMessage(),
                                                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (CSVBuilderException csvBuilderException) {
            throw new CensusAnalyserException(csvBuilderException.getMessage(),csvBuilderException.type.name());
        }
    }

    public int loadIndiaStateCode(String csvFilePath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath))){
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaStateCodeCSV> csvFileIterator = csvBuilder.getCSVFileIterator(reader, IndiaStateCodeCSV.class);
            Iterable<IndiaStateCodeCSV> csvIterable = () -> csvFileIterator;
            StreamSupport.stream(csvIterable.spliterator(),false)
                    .filter(csvState -> censusStateMap.get(csvState.state) != null)
                    .forEach(csvState -> censusStateMap.get(csvState.state).stateCode = csvState.stateCode);
            return this.censusStateMap.size();
        } catch (IOException ioException) {
            throw new CensusAnalyserException(ioException.getMessage(),
                                                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (RuntimeException runtimeException) {
            throw new CensusAnalyserException(runtimeException.getMessage(),
                                                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (CSVBuilderException csvBuilderException) {
            throw new CensusAnalyserException(csvBuilderException.getMessage(),csvBuilderException.type.name());
        }
    }

    public String getStateWiseSortedCensusData() throws CensusAnalyserException {
        if(censusStateMap == null || censusStateMap.size() ==0 ) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        List<IndiaCensusDAO> sortedCensusData = censusStateMap.values().stream().sorted(Comparator.comparing(IndiaCensusDAO::getState)).collect(Collectors.toList());
        String sortedStateCensusDataInJson = new Gson().toJson(sortedCensusData);
        return sortedStateCensusDataInJson;
    }

    public String getSortedCensusDataAccordingToPopulation() throws CensusAnalyserException {
        if(censusStateMap == null || censusStateMap.size() ==0 ) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        List<IndiaCensusDAO> sortedCensusData = censusStateMap.values().stream().sorted(Comparator.comparing(IndiaCensusDAO::getPopulation).reversed()).collect(Collectors.toList());
        String sortedCensusDataInJson = new Gson().toJson(sortedCensusData);
        return sortedCensusDataInJson;
    }

    public String getSortedCensusDataAccordingToPopulationDensity() throws CensusAnalyserException {
        if(censusStateMap == null || censusStateMap.size() ==0 ) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        List<IndiaCensusDAO> sortedCensusData = censusStateMap.values().stream().sorted(Comparator.comparing(IndiaCensusDAO::getDensityPerSqKm).reversed()).collect(Collectors.toList());
        String sortedCensusDataInJson = new Gson().toJson(sortedCensusData);
        return sortedCensusDataInJson;
    }

    public String getSortedCensusDataAccordingToArea() throws CensusAnalyserException {
        if(censusStateMap == null || censusStateMap.size() ==0 ) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        List<IndiaCensusDAO> sortedCensusData = censusStateMap.values().stream().sorted(Comparator.comparing(IndiaCensusDAO::getAreaInSqKm).reversed()).collect(Collectors.toList());
        String sortedCensusDataInJson = new Gson().toJson(sortedCensusData);
        return sortedCensusDataInJson;
    }


}
