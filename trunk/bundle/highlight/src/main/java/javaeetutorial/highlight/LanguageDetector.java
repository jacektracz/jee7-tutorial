package javaeetutorial.highlight;

import java.util.regex.Pattern;

public class LanguageDetector {
    
    private int javaScore;
    private int jsScore;
    private int xmlScore;
    private int sqlScore;
    
    private String code;
    
    public LanguageDetector(String code) {
        this.code = code;
        getJavaScore();
        getJsScore();
        getXmlScore();
        getSqlScore();
    }
    
    public String getLang() {
        int min = 2;
        if (javaScore > 2 && javaScore > jsScore && javaScore > xmlScore && javaScore > sqlScore)
            return "java";
        else if (jsScore > 2 && jsScore > javaScore && jsScore > xmlScore && jsScore > sqlScore)
            return "js";
        else if (xmlScore > 2 && xmlScore > javaScore && xmlScore > jsScore && xmlScore > sqlScore)
            return "xml";
        else if (sqlScore > 2 && sqlScore > javaScore && sqlScore > jsScore && sqlScore > xmlScore)
            return "sql";
        else
            return "plain";
    }
    
    private int getJavaScore() {
        int matches = 0;
        String[] p = {"new\\s[A-Z]", "public\\s[A-Za-z]", "void\\s[A-Za-z]",
                      "private\\s[A-Za-z]", "volatile\\s[A-Za-z]",
                      "synchronized\\s[A-Za-z]", "this.[A-Za-z]",
                      "int\\s[A-Za-z]", "double\\s[A-Za-z]", "enum\\s[A-Za-z]",
                      "import\\s[A-Za-z]", "@[A-Z][a-zA-Z]*", "\\stry\\s\\{",
                      "[A-Z][A-Za-z]*&lt;[A-Z][a-zA-Z]*&gt;\\s[a-z][A-Za-z0-9]*\\s=",
                      "[A-Z][A-Za-z]*&lt;[A-Z][a-zA-Z]*&gt;\\s[a-z][A-Za-z0-9]*;",
                      "[A-Z][a-zA-Z]*\\s[a-z][a-zA-Z0-9]*\\s=\\s[a-zA-Z0-9]*.[a-zA-Z]*\\([^\\)]*\\);"};
        for (String pi : p)
            matches += 7 * (code.split(pi, -1).length - 1);
        String[] p2 = {"[^t];"};
        for (String pi : p2)
            matches += 1 * (code.split(pi, -1).length - 1);
        String[] p3 = {"{", "}", "return ", "for (", "if (", "while ("};
        for (String pi : p3) {
            matches += 1 * (code.split(Pattern.quote(pi), -1).length - 1);
        }
        javaScore = matches;
        return matches;
    }
    
    private int getJsScore() {
        int matches = 0;
        String[] p = {"var\\s*[A-Za-z]", "function\\s*[A-Za-z]"};
        for (String pi : p)
            matches += 7 * (code.split(pi, -1).length - 1);
        String[] p2 = {"[^t];"};
        for (String pi : p2)
            matches += 1 * (code.split(pi, -1).length - 1);
        String[] p3 = {"{", "}", "return ", "for ", "if ", "while "};
        for (String pi : p3) {
            matches += 1 * (code.split(Pattern.quote(pi), -1).length - 1);
        }
        jsScore = matches;
        return matches;
    }
    
    private int getXmlScore() {
        int matches = 0;
        String[] p = {"[^\"]&lt;html\\s", "^&lt;html\\s"};
        for (String pi : p)
            matches += 7 * (code.split(pi, -1).length - 1);
        String[] p2 = {"&lt;web-app", "ns/javaee", "?xml", "&lt;h:", "&lt;f:"};
        for (String pi : p2)
            matches += 5 * (code.split(Pattern.quote(pi), -1).length - 1);
        String[] p3 = {"&lt;[a-z][^&]*&gt;"};
        for (String pi : p3) {
            matches += 3 * (code.split(pi, -1).length - 1);
        }
        xmlScore = matches;
        return matches;
    }
    
    private int getSqlScore() {
        String[] p = {"SELECT ", "FROM ", "WHERE ", "DISTINCT ", "AND ", "IN ",
            "LIKE ", "DELETE ", "IS ", "UPDATE ", "SET ", "CASE ", "ELSE ",
            "WHEN ", "THEN ", "JOIN "};
        int matches = 0;
        for (String pi : p) {
            matches += code.split(Pattern.quote(pi), -1).length - 1;
        }
        sqlScore = matches;
        return matches;
    }
}

