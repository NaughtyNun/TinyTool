/*
   @author  ©Copyright Anthonie Botha, All Rights Reserved
   @date    October 2021
   @name    tinytool.SearchPdf

   @notes   class for searching PDF documents
   @changes
*/

package tinytool;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchPdf {
  private File file;
  private String needle;
  private boolean exact;

  public int searchPdf(File file, String needle, boolean exact) {
    this.file = file;
    this.needle = needle;
    this.exact = exact;
    return getMatches();
  }

  private int getMatches() {
    int counter = 0;
    int index;

    try {
      PDDocument document = PDDocument.load(file);
      PDFTextStripper stripper = new PDFTextStripper();
      document.getClass();

      if (!document.isEncrypted()) {
        String haystack = stripper.getText(document);
        Pattern pattern = Pattern.compile(needle.toLowerCase());
        Matcher matcher = pattern.matcher(haystack.toLowerCase());

        if (exact) {
          index = haystack.split(needle, -1).length - 1;
          counter = index;
        } else
          while (matcher.find())
            counter++;

        document.close();
      } else
        counter = -2;
    } catch (IOException e) {
      counter = -1;
    }

    return counter;
  }
}
