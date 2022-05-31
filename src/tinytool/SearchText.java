/*
   @author  &copy;Copyright Anthonie Botha, All Rights Reserved
   @date    October 2021
   @name    tinytool.SearchText

   @notes   class for searching text/csv files
   @changes 2021-10-09 - made this a more generic class
*/

package tinytool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SearchText {
  private final Searcher searcher = new Searcher();
  private File file;
  private String needle;
  private boolean exact;
  private int counter = 0;

  public int searchText(File file, String needle, boolean exact) {
    this.file = file;
    this.needle = needle;
    this.exact = exact;
    return getMatches();
  }

  private int getMatches() {
    try {
      String haystack;
      BufferedReader br = new BufferedReader(new FileReader(file));
      while ((haystack = br.readLine()) != null)
        counter = searcher.searcher(needle, haystack, exact);
    } catch (IOException e) {
      counter = -1;
    }

    return counter;
  }
}
