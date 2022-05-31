/*
   @author  ©Copyright Anthonie Botha, All Rights Reserved
   @date    May 2022
   @name    tinytool.LoadXmlRecord

   @notes   created a standalone class for loading the data
   @changes
*/

package tinytool;

import tinytool.models.PersonnelRecord;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class LoadPersonnelRecord {
  private final Utilities utilities = new Utilities();

  public PersonnelRecord loadPersonnelRecord(File xmlPersonnel) {
    PersonnelRecord record;
    JAXBContext context;

    try {
      context = JAXBContext.newInstance(PersonnelRecord.class);
      Unmarshaller unmarshaller = context.createUnmarshaller();
      record = (PersonnelRecord) unmarshaller.unmarshal(xmlPersonnel);
      return record;
    } catch (JAXBException e) {
      e.printStackTrace();
      utilities.showFieldError("Personnel Record","TinyTool could not load the XML data file\n" +
        xmlPersonnel + '\n' + "Error: " + e.getCause());
      return null;
    }
  }
}