package javaeetutorial.highlight;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Highlight {

    public static void main(String[] args) throws IOException {

        File dir = new File(".");
        String[] pages = dir.list();
        for (String fname : pages) {
            if (fname.endsWith(".htm")) {
                String cont = readFile(fname);
                cont = addSHHeaders(cont);
                cont = addCodeClass(cont);
                writeFile(fname, cont);
            }
        }
        String[] shFiles = {"shBrushJScript.js", "shBrushJava.js",
            "shBrushXml.js", "shBrushSql.js", "shBrushPlain.js", "shCore.js", 
            "shLegacy.js", "shCore.css", "shCoreDefault.css",
            "shThemeDefault.css"};
        (new File("sh")).mkdir();
        for (String shfile : shFiles) {
            copyShFile(shfile);
        }
    }

    public static String readFile(String fname) throws IOException {
        StringBuilder sb;
        try (BufferedReader br = new BufferedReader(new FileReader(fname))) {
            sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
        }
        return sb.toString();
    }

    public static void writeFile(String fname, String content) throws IOException {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(fname, false))) {
            out.write(content);
        }
    }

    public static String addSHHeaders(String cont) {
        String ncont;
        ncont = "<script type=\"text/javascript\" src=\"sh/shCore.js\"></script>\n";
        ncont += "<script type=\"text/javascript\" src=\"sh/shBrushJScript.js\"></script>\n";
        ncont += "<script type=\"text/javascript\" src=\"sh/shBrushJava.js\"></script>\n";
        ncont += "<script type=\"text/javascript\" src=\"sh/shBrushXml.js\"></script>\n";
        ncont += "<script type=\"text/javascript\" src=\"sh/shBrushSql.js\"></script>\n";
        ncont += "<script type=\"text/javascript\" src=\"sh/shBrushPlain.js\"></script>\n";
        ncont += "<link href=\"sh/shCore.css\" rel=\"stylesheet\" type=\"text/css\"/>\n";
        ncont += "<link href=\"sh/shThemeDefault.css\" rel=\"stylesheet\" type=\"text/css\"/>\n";
        ncont += "<script type=\"text/javascript\">SyntaxHighlighter.all();</script>\n";
        ncont += "<title>";
        int he = cont.indexOf("</head>");
        String head = cont.substring(0, he);
        String rest = cont.substring(he);
        head = head.replace("<title>", ncont);
        return head + rest;
    }

    public static String addCodeClass(String cont) {

        String[] preSplit = cont.split("<pre xml:space=\"preserve\" class=\"oac_no_warn\">");
        String sout = preSplit[0];
        for (int chunk = 1; chunk < preSplit.length; chunk++) {
            int end = preSplit[chunk].indexOf("</pre>");
            String code = preSplit[chunk].substring(0, end);
            if (code.contains("<span") || code.split("\\r?\\n").length < 4) {
                sout += "<pre xml:space=\"preserve\" class=\"oac_no_warn\">";
            } else {
                LanguageDetector det = new LanguageDetector(code);
                sout += "<pre xml:space=\"preserve\" class=\"brush: "+det.getLang()+"; toolbar: false;\">";
            }
            sout += preSplit[chunk];
        }
        return sout;
    }

    public static void copyShFile(String file) throws IOException {
        try (
                InputStream is = Highlight.class
                     .getResourceAsStream("/" + file); 
                OutputStream os = new FileOutputStream("sh/" + file)
            ) {
            byte[] buffer = new byte[4096];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }
    }
}