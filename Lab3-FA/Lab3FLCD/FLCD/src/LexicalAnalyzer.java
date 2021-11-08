import fa.FAGenerators;
import fa.FiniteAutomata;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class LexicalAnalyzer {
    private String sourceCode;
    private final List<Token> tokens;
    private final SymbolTable symbolTable;
    //TODO declare FAs
    private final FiniteAutomata identifierFA;
    private final FiniteAutomata constantFA;

    private static final Map<String, Integer> TOKEN_CODES;
    private static final int IDENTIFIER_TOKEN_CODE;
    private static final int CONSTANT_TOKEN_CODE;
    private static final String[] RESERVED_WORDS;
    private static final String SPECIAL_CHARACTERS;

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
        TOKEN_CODES.put("write", 38);

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
                "struct",
                "write"
        };
        SPECIAL_CHARACTERS = " _{}[];.,()|?!";
    }

    public LexicalAnalyzer(String constantFAFileName, String identifierFAFileName)throws FileNotFoundException {
        tokens = new ArrayList<>();
        symbolTable = new SymbolTable(103);
        //TODO initialize FAs
//        identifierFA = FAGenerators.generateIdentifierFA();
//        constantFA = FAGenerators.generateConstantFA(Arrays.asList(SPECIAL_CHARACTERS.split("")));
        identifierFA = new FiniteAutomata();
        identifierFA.readFiniteAutomata(identifierFAFileName);
        constantFA = new FiniteAutomata();
        constantFA.readFiniteAutomata(constantFAFileName);
    }

    public void readFromFile(String fileName) throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileInputStream(fileName));
        sourceCode = "";
        while (scanner.hasNext()) {
            sourceCode += scanner.nextLine();
            sourceCode += '\n';
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
                if(ch == '\n'){
                    result.append(ch);
                }
                else if (!Pattern.matches("[\\s]", "" + ch)) {
                    result.append(ch);
                }
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }

    public void analyze() throws LexicalException {
        char[] charSourceCode = this.sourceCode.toCharArray();
        int line = 1;
        int i=0;
        while(i < charSourceCode.length){
            if(charSourceCode[i] == '\n'){
                line++;
                i++;
            }else if(isSeparator(charSourceCode[i])){
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
                        throw new LexicalException("Invalid identifier : must have at most 256 characters: " + word + " on line: " + line);
                    }
                    tokens.add(new Token(IDENTIFIER_TOKEN_CODE, symbolTable.insert(word)));
                } else if(isConstant(word)){
                    tokens.add(new Token(CONSTANT_TOKEN_CODE, symbolTable.insert(word)));
                } else {
                    throw new LexicalException("Invalid token: " + word + " on line: " + line);
                }
            }
        }
        System.out.println("Lexically correct");
    }

    private boolean isConstant(String token) {
//        if (Pattern.matches("[\\+\\-]?(([1-9][0-9]*)|0)" , token)) {
//            return true;
//        }
//        if(Pattern.matches("\"[a-zA-Z0-9" + Pattern.quote(SPECIAL_CHARACTERS) + "]+\"" , token)){
//            return true;
//        }
//        return Pattern.matches("\'[a-zA-Z0-9" + Pattern.quote(SPECIAL_CHARACTERS) + "]\'" , token);
        // TODO changed to FA
        return constantFA.checkSequence(token);

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
        return (Pattern.matches("[" + Pattern.quote(";{}()[]\n") + "]", "" + token));
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
//        return (Pattern.matches("[a-zA-z][a-zA-z0-9]*", token));
        // TODO changed to FA
        return identifierFA.checkSequence(token);
    }

    public void writePifToFile(String outFilePath) throws IOException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(outFilePath));
            writer.write("" + tokens.size() + System.lineSeparator());
            for (Token pifToken : tokens) {
                writer.write(pifToken.toString() + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public void writeSymbolTableToFile(String outFilePath) throws IOException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(outFilePath));
            int i = 0;
            int numberOfBuckets = symbolTable.getNumberOfBuckets();
            while (i < numberOfBuckets){
                if(symbolTable.getSymbols().get(i).size() != 0){
                    for(int j = 0 ; j < symbolTable.getSymbols().get(i).size(); j ++){
                        writer.write(symbolTable.getSymbols().get(i).get(j) + " "+ i + " " + j + "\n");
                    }
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }


}
