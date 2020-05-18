package censusanalyser;

public class IndiaStateCodeDAO {
    public String state;
    public String stateCode;

    public IndiaStateCodeDAO(IndiaStateCodeCSV indiaStateCodeCSV) {
        state = indiaStateCodeCSV.state;
        stateCode = indiaStateCodeCSV.stateCode;
    }
}
