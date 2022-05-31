/*
   @author  ©Copyright Anthonie Botha, All Rights Reserved
   @date    October 2021
   @name    tinytool.SearchWord

   @notes   class for searching Word documents, both doc and docx
   @changes 2022-01-03 - using the Searcher class for the search
*/

package tinytool;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SearchWord {
  private final tinytool.Utilities utilities = new tinytool.Utilities();
  private final tinytool.Searcher searcher = new tinytool.Searcher();
  private final TinyTool tinyTool = new TinyTool();
  private File file;
  private String needle;
  private boolean exact;
  private FileInputStream fis;

  public int searchWord(File file, String needle, boolean exact) {
    this.file = file;
    this.needle = needle;
    this.exact = exact;
    return getMatches();
  }

  private int getMatches() {
    int counter;

    if (utilities.getFileExtension(file).equals("docx"))
      counter = searchNewFormat(file);
    else
      counter = searchOldFormat(file);

    return counter;
  }

  private int searchNewFormat(File file) {
    String text;
    int matches;

    try {
      fis = new FileInputStream(file);
      XWPFDocument document = new XWPFDocument(fis);
      XWPFWordExtractor extractor = new XWPFWordExtractor(document);
      text = extractor.getText();
      fis.close();
    } catch (IOException e) {
      return -1;
    }

    assert text != null;
    matches = searchFile(text);
    return matches;
  }

  private int searchOldFormat(File file) {
    String text;
    int matches;

    try {
      fis = new FileInputStream(file);
      HWPFDocument document = new HWPFDocument(fis);
      text = document.getDocumentText();
      fis.close();
    } catch (IOException e) {
      return -1;
    }

    assert text != null;
    matches = searchFile(text);
    return matches;
  }

  private int searchFile(String text) {
    String tempFile = tinyTool.WORKDIR + "tempdocsearch.txt";
    BufferedWriter bw;
    BufferedReader br;
    String haystack;
    int matches = 0;

    try {
      bw = new BufferedWriter(new FileWriter(tempFile));
      bw.write(text);
      bw.close();
      br = new BufferedReader(new FileReader(tempFile));

      while ((haystack = br.readLine()) != null) {
        matches = searcher.searcher(needle, haystack, exact);
      }
      br.close();
    } catch (IOException e) {
      matches = -1;
    }

    Path path = Paths.get(tempFile);
    try {
      Files.deleteIfExists(path);
    } catch (IOException e) {
      utilities.showFieldError("Temporary Search File", "Could not delete the temporary text" +
        " file used for the Word Search\n" + "File: " + path);
    }

    return matches;
  }
}