import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class LexicalAnalyzer {
    private String sourceCode;
    private List<Token> tokens;
    private SymbolTable symbolTable;

    private static Map<String, Integer> TOKEN_CODES;
    private static int IDENTIFIER_TOKEN_CODE;
    private static int CONSTANT_TOKEN_CODE;
    private static String[] RESERVED_WORDS;
    private static String SPECIAL_CHARACTERS;

    static {
        IDENTIFIER_TOKEN_CODE = 0;
        CONSTANT_TOKEN_CODE = 1;
        TOKEN_CODES = new HashMap<>();
        TOKEN_CODES.put("int", 2);
        TOKEN_CODES.put("string", 3);
        TOKEN_CODES.put("checkIf", 4);
        TOKEN_CODES.put("elseDo", 5);
        TOKEN_CODES.put("while", 6);
        TOKEN_CODES.put("read", 7);
        TOKEN_CODES.put("print", 8);
        TOKEN_CODES.put("char", 9);
        TOKEN_CODES.put("boolean", 10);
        TOKEN_CODES.put("readNumber", 11);
        TOKEN_CODES.put("do", 12);
        TOKEN_CODES.put("stop", 13);
        TOKEN_CODES.put("struct", 14);
        TOKEN_CODES.put(";", 15);
        TOKEN_CODES.put("{", 16);
        TOKEN_CODES.put("}", 17);
        TOKEN_CODES.put("(", 18);
        TOKEN_CODES.put(")", 19);
        TOKEN_CODES.put("[", 20);
        TOKEN_CODES.put("]", 21);
        TOKEN_CODES.put("+", 22);
        TOKEN_CODES.put("-", 23);
        TOKEN_CODES.put("*", 24);
        TOKEN_CODES.put("/", 25);
        TOKEN_CODES.put("<", 26);
        TOKEN_CODES.put("<=", 27);
        TOKEN_CODES.put(">", 28);
        TOKEN_CODES.put(">=", 29);
        TOKEN_CODES.put("==", 30);
        TOKEN_CODES.put("!=", 31);
        TOKEN_CODES.put("&&", 32);
        TOKEN_CODES.put("||", 33);
        TOKEN_CODES.put("=", 34);
        TOKEN_CODES.put("</", 35);
        TOKEN_CODES.put("/>", 36);
        TOKEN_CODES.put("%", 37);

        RESERVED_WORDS = new String[]{
                "int",
                "string",
                "checkIf",
                "elseDo",
                "while",
                "read",
                "print",
                "char",
                "readNumber",
                "boolean",
                "do",
                "stop",
                "struct"
        };
        SPECIAL_CHARACTERS = " _{}[];.,()|?!";
    }

    public LexicalAnalyzer() {
        tokens = new ArrayList<>();
        symbolTable = new SymbolTable(103);
    }

    public void readFromFile(String fileName) throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileInputStream(fileName));
        sourceCode = "";
        while (scanner.hasNext()) {
            sourceCode += scanner.nextLine();
        }
        scanner.close();
        sourceCode = eraseWhiteSpaces(sourceCode);
    }

    private String eraseWhiteSpaces(String text){
        char[] charText = text.toCharArray();
        StringBuilder result = new StringBuilder();
        boolean opened = false;
        for (char ch: charText) {
            if (ch == '\"') {
                result.append(ch);
                opened = !opened;
            } else if (!opened) {
                if (!Pattern.matches("[\\s]", "" + ch)) {
                    result.append(ch);
                }
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }

    public void analyze() throws LexicalException {
        char charSourceCode[] = this.sourceCode.toCharArray();
        int i=0;
        while(i < charSourceCode.length){
            if(isSeparator(charSourceCode[i])){
                tokens.add(getTokenFromSeparator(charSourceCode[i]));
                i++;
            } else if(isOperatorFirstChar(charSourceCode[i])){
                if(i+1 < sourceCode.length() && isOperatorSecondChar(charSourceCode[i + 1])){
                    tokens.add(getTokenFromOperator("" + charSourceCode[i] + charSourceCode[i+1]));
                    i++;
                } else{
                    tokens.add(getTokenFromOperator("" + charSourceCode[i]));
                }
                i++;
            } else{
                int j = i + 1;
                String word = "" + charSourceCode[i];
                while(j < charSourceCode.length && !isSeparator(charSourceCode[j]) && !isOperatorFirstChar(charSourceCode[j])){
                    word += charSourceCode[j];
                    j++;
                }
                i = j;
                if(isReservedWord(word)){
                    tokens.add(getTokenFromReservedWord(word));
                } else if(isIdentifier(word)){
                    if(word.length() > 256){
                        throw new LexicalException("Inavlid identifier : must have at most 256 characters: " + word);
                    }
                    tokens.add(new Token(IDENTIFIER_TOKEN_CODE, symbolTable.insert(word)));
                } else if(isConstant(word)){
                    tokens.add(new Token(CONSTANT_TOKEN_CODE, symbolTable.insert(word)));
                } else {
                    throw new LexicalException("Invalid token: " + word);
                }
            }
        }
    }

    private boolean isConstant(String token) {
        if (Pattern.matches("[\\+\\-]*(([1-9][0-9]*)|0)" , token)) {
            return true;
        }
        return Pattern.matches("\"[a-zA-Z0-9" + Pattern.quote(SPECIAL_CHARACTERS) + "]+\"" , token);

    }

    private boolean isOperatorFirstChar(char token) {
        return (Pattern.matches("[" + Pattern.quote("+-*/<>=!&&||=%") + "]", "" + token));
    }

    private boolean isOperatorSecondChar(char token) {
        return (Pattern.matches("[" + Pattern.quote("+-*/<>=!&&||=%") + "]", "" + token));
    }

    private Token getTokenFromSeparator(char token) throws LexicalException {
        if (!TOKEN_CODES.containsKey("" + token))
            throw new LexicalException("Invalid separator: " + token);
        return new Token(TOKEN_CODES.get("" + token));

    }

    private boolean isReservedWord(String token){
        for(String reservedWord: RESERVED_WORDS){
            if(token.equals(reservedWord)){
                return true;
            }
        }
        return false;
    }

    private boolean isSeparator(char token){
        return (Pattern.matches("[" + Pattern.quote(";{}()[]") + "]", "" + token));
    }

    private Token getTokenFromOperator(String token) throws LexicalException {
        if (!TOKEN_CODES.containsKey(token))
            throw new LexicalException("Invalid operator: " + token);
        return new Token(TOKEN_CODES.get(token));

    }

    private Token getTokenFromReservedWord(String token) throws LexicalException {
        if (!TOKEN_CODES.containsKey(token))
            throw new LexicalException("Invalid reserved word: " + token);
        return new Token(TOKEN_CODES.get(token));
    }

    private boolean isIdentifier(String token){
        return (Pattern.matches("[_a-zA-z][_a-zA-z0-9]*", token));
    }

    public void writePifToFile(String outFilePath) throws IOException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(outFilePath));
            writer.write("" + tokens.size() + System.lineSeparator());
            for (Token pifToken : tokens) {
                writer.write(pifToken.toString() + System.lineSeparator());
            }
            writer.write(symbolTable.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }


}
