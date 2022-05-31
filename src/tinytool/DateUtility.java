/*
   @author  ©Copyright Anthonie Botha, All Rights Reserved
   @date    May 2022
   @name    tinytool.DateUtility

   @notes   convert date to a string
   @changes
*/

package tinytool;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtility {
  private static final String DATE_PATTERN = "yyyy-MM-dd";
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

  public static String format(LocalDate date) {
    if (date == null)
      return null;

    return DATE_FORMATTER.format(date);
  }

  public static LocalDate parse(String dateString) {
    return DATE_FORMATTER.parse(dateString, LocalDate::from);
  }

  public static boolean validateDate(String dateString) {
    return DateUtility.parse(dateString) != null;
  }
}