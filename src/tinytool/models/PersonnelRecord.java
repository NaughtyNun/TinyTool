/*
   @author  ©Copyright Anthonie Botha, All Rights Reserved
   @date    January 2022
   @name    tinytool.models.PersonnelRecord

   @notes   wrapper class for reading and saving Personnel Record to XML file
   @changes 2022-03-20 - added default file locations
            2022-04-09 - XML file was changed a little to create a more orderly structure
            2022-04-09 - added more signatories for automation purposes
*/

package tinytool.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.File;

@XmlRootElement(name = "personnelRecord")
@XmlType(propOrder = { "personnelNumber",
                       "systemId",
                       "surname",
                       "name",
                       "idNumber",
                       "email",
                       "lmSurname",
                       "lmName",
                       "lmEmail",
                       "oneUpSurname",
                       "oneUpName",
                       "oneUpEmail",
                       "clientSurname",
                       "clientName",
                       "clientEmail",
                       "defaultDirectory",
                       "declarations",
                       "timeSheetLoad",
                       "timeSheetCopy",
                       "attendance",
                       "userSignature",
                       "userInitial",
                       "lmSignature",
                       "lmInitial",
                       "oneUpSignature",
                       "oneUpInitial"})
@XmlAccessorType(XmlAccessType.FIELD)

public class PersonnelRecord {
  private String personnelNumber;
  private String systemId;
  private String surname;
  private String name;
  private String idNumber;
  private String email;
  private String lmSurname;
  private String lmName;
  private String lmEmail;
  private String oneUpSurname;
  private String oneUpName;
  private String oneUpEmail;
  private String clientSurname;
  private String clientName;
  private String clientEmail;
  private String defaultDirectory;
  private String declarations;
  private String timeSheetLoad;
  private String timeSheetCopy;
  private String attendance;
  private File userSignature;
  private File userInitial;
  private File lmSignature;
  private File lmInitial;
  private File oneUpSignature;
  private File oneUpInitial;

  public PersonnelRecord() { super(); }

  // Setters
  public void setPersonnelNumber(String personnelNumber) { this.personnelNumber = personnelNumber; }
  public void setSystemId(String systemId) { this.systemId = systemId; }
  public void setSurname(String surname) { this.surname = surname; }
  public void setName(String name) { this.name = name; }
  public void setIdNumber(String idNumber) { this.idNumber = idNumber; }
  public void setEmail(String email) { this.email = email; }
  public void setLineManagerSurname(String lmSurname) { this.lmSurname = lmSurname; }
  public void setLineManagerName(String lmName) { this.lmName = lmName; }
  public void setLineManagerEmail(String lmEmail) { this.lmEmail = lmEmail; }
  public void setOneUpSurname(String oneUpSurname) { this.oneUpSurname = oneUpSurname; }
  public void setOneUpName(String oneUpName) { this.oneUpName = oneUpName; }
  public void setOneUpEmail(String oneUpEmail) { this.oneUpEmail = oneUpEmail; }
  public void setClientName(String clientName) { this.clientName = clientName; }
  public void setClientSurname(String clientSurname) { this.clientSurname = clientSurname; }
  public void setClientEmail(String clientEmail) { this.clientEmail = clientEmail; }
  public void setDefaultDirectory(String defaultDirectory) { this.defaultDirectory = defaultDirectory; }
  public void setDeclarations(String declarations) { this.declarations = declarations; }
  public void setTimeSheetLocal(String timeSheetLocal) { this.timeSheetLoad = timeSheetLocal; }
  public void setTimeSheetCopy(String timeSheetCopy) { this.timeSheetCopy = timeSheetCopy; }
  public void setAttendance(String attendance) { this.attendance = attendance; }
  public void setUserSignature(File userSignature) { this.userSignature = userSignature; }
  public void setUserInitials(File userInitials) { this.userInitial = userInitials; }
  public void setLineManagerSignature(File lmSignature) { this.lmSignature = lmSignature; }
  public void setLineManagerInitials(File lmInitials) { this.lmInitial = lmInitials; }
  public void setOneUpSignature(File oneUpSignature) { this.oneUpSignature = oneUpSignature; }
  public void setOneUpInitials(File oneUpInitials) { this.oneUpInitial = oneUpInitials; }

  // Getters
  public String getPersonnelNumber() { return personnelNumber; }
  public String getSystemId() { return systemId; }
  public String getSurname() { return surname; }
  public String getName() { return name; }
  public String getIdNumber() { return idNumber; }
  public String getEmail() { return email; }
  public String getLineManagerName() { return lmName; }
  public String getLineManagerSurname() { return lmSurname; }
  public String getLineManagerEmail() { return lmEmail; }
  public String getOneUpName() { return oneUpName; }
  public String getOneUpSurname() { return oneUpSurname; }
  public String getOneUpEmail() { return oneUpEmail; }
  public String getClientName() { return clientName; }
  public String getClientSurname() { return clientSurname; }
  public String getClientEmail() { return clientEmail; }
  public String getDefaultDirectory() { return defaultDirectory; }
  public String getDeclarations() { return declarations; }
  public String getTimeSheetLocal() { return timeSheetLoad; }
  public String getTimeSheetCopy() { return timeSheetCopy; }
  public String getAttendance() { return attendance; }
  public File getUserSignature() { return userSignature; }
  public File getUserInitials() { return userInitial; }
  public File getLineManagerSignature() { return lmSignature; }
  public File getLineManagerInitials() { return lmInitial; }
  public File getOneUpSignature() { return oneUpSignature; }
  public File getOneUpInitials() { return oneUpInitial; }

  @Override
  public String toString() {
    return "personnelRecord{ \n" +
           personnelNumber + '\n' +
           systemId + '\n' +
           name  + '\n' +
           surname + '\n' +
           idNumber + '\n' +
           email + '\n' +
           lmName + '\n' +
           lmSurname + '\n' +
           lmEmail + '\n' +
           oneUpName + '\n' +
           oneUpSurname + '\n' +
           oneUpEmail + '\n' +
           clientName + '\n' +
           clientSurname + '\n' +
           clientEmail + '\n' +
           defaultDirectory + '\n' +
           declarations + '\n' +
           timeSheetLoad + '\n' +
           timeSheetCopy + '\n' +
           attendance + '\n' +
           userSignature + '\n' +
           userInitial + '\n' +
           lmSignature + '\n' +
           lmInitial + '\n' +
           oneUpSignature + '\n' +
           oneUpInitial + "}";
  }
}