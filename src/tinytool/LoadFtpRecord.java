/*
   @author  ©Copyright Anthonie Botha, All Rights Reserved
   @date    June 2022
   @name    tinytool.LoadFtpRecord

   @notes   class to load XML file (FTP) records
   @changes
 */

package tinytool;

import tinytool.models.PersonnelRecord;
import tinytool.models.ServerRecord;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class LoadFtpRecord {
  private final Utilities utilities = new Utilities();

  public ServerRecord loadServerRecord(File xmlServers) {
    ServerRecord record;
    JAXBContext context;

    try {
      context = JAXBContext.newInstance(PersonnelRecord.class);
      Unmarshaller unmarshaller = context.createUnmarshaller();
      record = (ServerRecord) unmarshaller.unmarshal(xmlServers);
      return record;
    } catch (JAXBException e) {
      e.printStackTrace();
      utilities.showFieldError("FTP Servers","TinyTool could not load the XML data file\n" +
        xmlServers + '\n' + "Error: " + e.getCause());
      return null;
    }
  }
}