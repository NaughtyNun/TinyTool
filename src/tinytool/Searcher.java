/*
   @author  ©Copyright Anthonie Botha, All Rights Reserved
   @date    January 2022
   @name    tinytool.Searcher

   @notes   generic class for searching plain text files.
            can also search Word and CSV files.
   @changes
 */

package tinytool;

public class Searcher {
  private int counter = 0;
  private String needle;
  private String haystack;
  private boolean exact;

  public int searcher(String needle, String haystack, boolean exact) {
    this.needle = needle;
    this.haystack = haystack;
    this.exact = exact;

    if (exact)
      counter = exactSearch();
    else
      counter = counter + fussySearch();

    return counter;
  }

  private int exactSearch() {
    int pos = haystack.indexOf(String.valueOf(needle));

    if (pos > -1)
      counter++;

    return counter;
  }

  private int fussySearch() {
    int matches = 0;
    int index = 0;

    while (true) {
      index = haystack.toLowerCase().indexOf(needle.toLowerCase(), index);

      if (index != -1) {
        matches++;
        index += needle.length();
      } else
        break;
    }

    return matches;
  }
}