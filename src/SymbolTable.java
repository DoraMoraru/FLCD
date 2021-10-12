import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SymbolTable {
    private static int DEFAULT_NUMBER_OF_BUCKETS = 10007;
    private int numberOfBuckets;
    private List<List<String>> symbols;


    public SymbolTable(int numberOfBuckets) {
        this.numberOfBuckets = numberOfBuckets;
        symbols = Stream.generate((Supplier<ArrayList<String>>) ArrayList::new)
                .limit(numberOfBuckets)
                .collect(Collectors.toList());
    }

    public SymbolTable() {
        this(DEFAULT_NUMBER_OF_BUCKETS);
    }

    private int getHashValue(String symbol){
        int hashValue = 0;
        for (char ch: symbol.toCharArray()){
            hashValue += ch;
        }
        return hashValue % numberOfBuckets;
    }

    private Position findSymbol(int bucket, String symbol){
        for(int i = 0; i < symbols.get(bucket).size(); i++){
            if(symbols.get(bucket).get(i).equals(symbol)){
                return new Position(bucket, i);
            }
        }
        return null;
    }

    public Position insert(String symbol){
        int bucket = getHashValue(symbol);
        var value = findSymbol(bucket, symbol);
        if(value != null){
            return value;
        }
        symbols.get(bucket).add(symbol);
        return new Position(bucket, symbols.get(bucket).size() - 1);
    }

    @Override
    public String toString() {
        return "SymbolTable{" +
                "numberOfBuckets=" + numberOfBuckets +
                ", symbols=" + symbols +
                '}';
    }
}
