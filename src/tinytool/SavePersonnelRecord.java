/*
   @author  ©Copyright Anthonie Botha, All Rights Reserved
   @date    May 2022
   @name    tinytool.SavePersonnelRecord

   @notes   created a standalone class for saving the record
   @changes
*/

package tinytool;

import tinytool.models.PersonnelRecord;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SavePersonnelRecord {
  private final Utilities utilities = new Utilities();

  public boolean savePersonnelRecord(File xmlPersonnel, PersonnelRecord record) {
    PersonnelRecord personnelRecord = new PersonnelRecord();

    personnelRecord.setPersonnelNumber(record.getPersonnelNumber());
    personnelRecord.setSystemId(record.getSystemId());
    personnelRecord.setIdNumber(record.getIdNumber());
    personnelRecord.setSurname(record.getSurname());
    personnelRecord.setName(record.getName());
    personnelRecord.setEmail(record.getEmail());
    personnelRecord.setLineManagerSurname(record.getLineManagerSurname());
    personnelRecord.setLineManagerName(record.getLineManagerName());
    personnelRecord.setLineManagerEmail(record.getLineManagerEmail());
    personnelRecord.setOneUpSurname(record.getOneUpSurname());
    personnelRecord.setOneUpName(record.getOneUpName());
    personnelRecord.setOneUpEmail(record.getOneUpEmail());
    personnelRecord.setClientSurname(record.getClientSurname());
    personnelRecord.setClientName(record.getClientName());
    personnelRecord.setClientEmail(record.getClientEmail());
    personnelRecord.setDefaultDirectory(record.getDefaultDirectory());
    personnelRecord.setDeclarations(record.getDeclarations());
    personnelRecord.setTimeSheetLocal(record.getTimeSheetLocal());
    personnelRecord.setTimeSheetCopy(record.getTimeSheetCopy());
    personnelRecord.setAttendance(record.getAttendance());
    personnelRecord.setUserSignature(record.getUserSignature());
    personnelRecord.setUserInitials(record.getUserInitials());
    personnelRecord.setLineManagerSignature(record.getLineManagerSignature());
    personnelRecord.setLineManagerInitials(record.getLineManagerInitials());
    personnelRecord.setOneUpSignature(record.getOneUpSignature());
    personnelRecord.setOneUpInitials(record.getOneUpInitials());

    try {
      JAXBContext context;
      BufferedWriter writer;

      writer = new BufferedWriter(new FileWriter(xmlPersonnel));
      context = JAXBContext.newInstance(PersonnelRecord.class);
      Marshaller marshaller = context.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      marshaller.marshal(personnelRecord, writer);
      writer.close();
      return true;
    } catch (IOException | JAXBException e) {
      utilities.showFieldError("Saving XML File", "TinyTool could not save the data to the XML " +
        "file.\n" + "Error: " + e.getMessage());
      return false;
    }
  }
}