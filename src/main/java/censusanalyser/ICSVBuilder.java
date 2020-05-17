package censusanalyser;

import java.io.Reader;
import java.util.Iterator;

public interface ICSVBuilder {
    public Iterator getCSVFileIterator(Reader reader, Class csvClass) throws CensusAnalyserException;
}
