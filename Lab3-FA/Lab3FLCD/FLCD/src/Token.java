public class Token {
    private int tokenCode;
    private Position positionInSymbolTable;

    public Token(int tokenCode) {
        this.tokenCode = tokenCode;
        this.positionInSymbolTable = null;
    }

    public Token(int tokenCode, Position positionInSymTab) {
        this.tokenCode = tokenCode;
        this.positionInSymbolTable = positionInSymTab;
    }

    public int getTokenCode() {
        return tokenCode;
    }

    public Position getPositionInSymbolTable() {
        return positionInSymbolTable;
    }

    @Override
    public String toString() {
        if (this.positionInSymbolTable != null)
            return tokenCode + " " + this.positionInSymbolTable;
        else
            return tokenCode + " -1 -1";
    }
}
