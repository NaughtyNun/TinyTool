/*
   @author  ©Copyright Anthonie Botha, All Rights Reserved
   @date    June 2022
   @name    tinytool.models.ServerRecord

   @notes   basic record for FTP functionality
   @changes
*/

package tinytool.models;

import java.util.ArrayList;
import java.util.List;

public class ServerRecord {
  private List<String> ftpName = new ArrayList<>();
  private List<String> ftpIp = new ArrayList<>();
  private List<Integer> ftpPort = new ArrayList<>();
  private List<String> dtpDns = new ArrayList<>();

  public List<String> getFtpName() { return ftpName; }
  public List<String> getFtpIp() { return ftpIp; }
  public List<Integer> getFtpPort() { return ftpPort; }
  public List<String> getDtpDns() { return dtpDns; }

  public void setFtpName(List<String> ftpName) { this.ftpName = ftpName; }
  public void setFtpIp(List<String> ftpIp) { this.ftpIp = ftpIp; }
  public void setFtpPort(List<Integer> ftpPort) { this.ftpPort = ftpPort; }
  public void setDtpDns(List<String> dtpDns) { this.dtpDns = dtpDns; }
}