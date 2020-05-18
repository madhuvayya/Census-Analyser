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

    Map<String, CensusDAO> censusStateMap = null;

    public CensusAnalyser() {
        censusStateMap = new HashMap<>();
    }

    public int loadUsCensusData(String csvFilePath) throws CensusAnalyserException {
        return this.loadCensusData(csvFilePath,USCensusCSV.class);
    }

    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        return loadCensusData(csvFilePath,IndiaCensusCSV.class);
    }

    private <E> int loadCensusData(String csvFilePath, Class censusCSVClass) throws CensusAnalyserException {
        try ( Reader reader = Files.newBufferedReader(Paths.get(csvFilePath)))
        {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<E> csvFileIterator = csvBuilder.getCSVFileIterator(reader, censusCSVClass);
            Iterable<E> csvIterable = () -> csvFileIterator;
            if(censusCSVClass.getName().equals("censusanalyser.IndiaCensusCSV")) {
                StreamSupport.stream(csvIterable.spliterator(), false)
                        .map(IndiaCensusCSV.class::cast)
                        .forEach(censusCSV -> censusStateMap.put(censusCSV.state, new CensusDAO(censusCSV)));
            } else if(censusCSVClass.getName().equals("censusanalyser.USCensusCSV")) {
                StreamSupport.stream(csvIterable.spliterator(), false)
                        .map(USCensusCSV.class::cast)
                        .forEach(censusCSV -> censusStateMap.put(censusCSV.state, new CensusDAO(censusCSV)));
            }
            return this.censusStateMap.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }catch (RuntimeException e) {
            throw new CensusAnalyserException(e.getMessage(),CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),e.type.name());
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
