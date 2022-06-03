/*
  @author  ©Copyright Anthonie Botha, All Rights Reserved
  @date    March 2022
  @name    tinytool.models.TmsRecord

  @notes   TMS Record class, will be used to read XML file sent from the TMS system
  @changes -
*/

package tinytool.models;

import java.util.ArrayList;
import java.util.List;

public class TmsRecord {
  private String customerName;                                  // Header Component
  private String customerNr;
  private String personnelName;
  private String personnelNr;
  private String weekEnding;
  public List<String> nonCostType = new ArrayList<>();          // Non-Billable Component
  public List<String> nonCostNr = new ArrayList<>();
  public List<String> nonCostItem = new ArrayList<>();
  public List<String> nonTaskLevel = new ArrayList<>();
  public List<String> nonTaskType = new ArrayList<>();
  public List<String> nonTaskComponent = new ArrayList<>();
  public List<String> nonComment = new ArrayList<>();
  public List<Double> nonSunday = new ArrayList<>();
  public List<Double> nonMonday = new ArrayList<>();
  public List<Double> nonTuesday = new ArrayList<>();
  public List<Double> nonWednesday = new ArrayList<>();
  public List<Double> nonThursday = new ArrayList<>();
  public List<Double> nonFriday = new ArrayList<>();
  public List<Double> nonSaturday = new ArrayList<>();
  public List<Double> nonLineTotal = new ArrayList<>();
  public List<String> billCostType = new ArrayList<>();         // Billable Component
  public List<String> billCostNr = new ArrayList<>();
  public List<String> billCostItem = new ArrayList<>();
  public List<String> billTaskLevel = new ArrayList<>();
  public List<String> billTaskType = new ArrayList<>();
  public List<String> billTaskComponent = new ArrayList<>();
  public List<String> billComment = new ArrayList<>();
  public List<Double> billSunday = new ArrayList<>();
  public List<Double> billMonday = new ArrayList<>();
  public List<Double> billTuesday = new ArrayList<>();
  public List<Double> billWednesday = new ArrayList<>();
  public List<Double> billThursday = new ArrayList<>();
  public List<Double> billFriday = new ArrayList<>();
  public List<Double> billSaturday = new ArrayList<>();
  public List<Double> billLineTotal = new ArrayList<>();
  public List<String> travelCostType = new ArrayList<>();       // Travel Component
  public List<String> travelCostNr = new ArrayList<>();
  public List<String> travelCostItem = new ArrayList<>();
  public List<String> travelTaskLevel = new ArrayList<>();
  public List<String> travelTaskComponent = new ArrayList<>();
  public List<String> travelTaskType = new ArrayList<>();
  public List<String> travelComment = new ArrayList<>();
  public List<Double> travelSunday = new ArrayList<>();
  public List<Double> travelMonday = new ArrayList<>();
  public List<Double> travelTuesday = new ArrayList<>();
  public List<Double> travelWednesday = new ArrayList<>();
  public List<Double> travelThursday = new ArrayList<>();
  public List<Double> travelFriday = new ArrayList<>();
  public List<Double> travelSaturday = new ArrayList<>();
  public List<Double> travelLineTotal = new ArrayList<>();
  private String signatureDate;                                 // Footer Component
  private String employee;
  private String manager;
  private String oneup;
  private String customer;
  private String printDate;                                     // Audit Component
  private String printTime;
  private String printBy;
  private String printVersion;

  // Setters
  public void setCustomerName(String customerName) { this.customerName = customerName; }
  public void setCustomerNr(String customerNr) { this.customerNr = customerNr; }
  public void setPersonnelName(String personnelName) { this.personnelName = personnelName; }
  public void setPersonnelNr(String personnelNr) { this.personnelNr = personnelNr; }
  public void setWeekEnding(String weekEnding) { this.weekEnding = weekEnding; }
  // Non-Billable Components
  public void setNonCostType(int pos, String costType) { this.nonCostType.add(pos, costType); }
  public void setNonCostNr(int pos, String costNr) { this.nonCostNr.add(pos, costNr); }
  public void setNonCostItem(int pos, String costItem) { this.nonCostItem.add(pos, costItem); }
  public void setNonTaskLevel(int pos, String taskLevel) { this.nonTaskLevel.add(pos, taskLevel); }
  public void setNonTaskType(int pos, String taskType) { this.nonTaskType.add(pos, taskType); }
  public void setNonTaskComponent(int pos, String taskComponent) { this.nonTaskComponent.add(pos, taskComponent); }
  public void setNonComment(int pos, String comment) { this.nonComment.add(pos, comment); }
  public void setNonSunday(int pos, double sunday) { this.nonSunday.add(pos, sunday); }
  public void setNonMonday(int pos, double monday) { this.nonMonday.add(pos, monday); }
  public void setNonTuesday(int pos, double tuesday) { this.nonTuesday.add(pos, tuesday); }
  public void setNonWednesday(int pos, double wednesday) { this.nonWednesday.add(pos, wednesday); }
  public void setNonThursday(int pos, double thursday) { this.nonThursday.add(pos, thursday); }
  public void setNonFriday(int pos, double friday) { this.nonFriday.add(pos, friday); }
  public void setNonSaturday(int pos, double saturday) { this.nonSaturday.add(pos, saturday); }
  public void setNonLineTotal(int pos, double lineTotal) { this.nonLineTotal.add(pos, lineTotal); }
  // Billable Components
  public void setBillCostType(int pos, String costType) { this.billCostType.add(pos, costType); }
  public void setBillCostNr(int pos, String costNr) { this.billCostNr.add(pos, costNr); }
  public void setBillCostItem(int pos, String costItem) { this.billCostItem.add(pos, costItem); }
  public void setBillTaskLevel(int pos, String taskLevel) { this.billTaskLevel.add(pos, taskLevel); }
  public void setBillTaskType(int pos, String taskType) { this.billTaskType.add(pos, taskType); }
  public void setBillTaskComponent(int pos, String taskComponent) { this.billTaskComponent.add(pos, taskComponent); }
  public void setBillComment(int pos, String comment) { this.billComment.add(pos, comment); }
  public void setBillSunday(int pos, double sunday) { this.billSunday.add(pos, sunday); }
  public void setBillMonday(int pos, double monday) { this.billMonday.add(pos, monday); }
  public void setBillTuesday(int pos, double tuesday) { this.billTuesday.add(pos, tuesday); }
  public void setBillWednesday(int pos, double wednesday) { this.billWednesday.add(pos, wednesday); }
  public void setBillThursday(int pos, double thursday) { this.billThursday.add(pos, thursday); }
  public void setBillFriday(int pos, double friday) { this.billFriday.add(pos, friday); }
  public void setBillSaturday(int pos, double saturday) { this.billSaturday.add(pos, saturday); }
  public void setBillLineTotal(int pos, double lineTotal) { this.billLineTotal.add(pos, lineTotal); }
  // Travel Components
  public void setTravelCostType(int pos, String costType) { this.travelCostType.add(pos, costType); }
  public void setTravelCostNr(int pos, String costNr) { this.travelCostNr.add(pos, costNr); }
  public void setTravelCostItem(int pos, String costItem) { this.travelCostItem.add(pos, costItem); }
  public void setTravelTaskLevel(int pos, String taskLevel) { this.travelTaskLevel.add(pos, taskLevel); }
  public void setTravelTaskType(int pos, String taskType) { this.travelTaskType.add(pos, taskType); }
  public void setTravelTaskComponent(int pos, String taskComponent) { this.travelTaskComponent.add(pos, taskComponent); }
  public void setTravelComment(int pos, String comment) { this.travelComment.add(pos, comment); }
  public void setTravelSunday(int pos, double sunday) { this.travelSunday.add(pos, sunday); }
  public void setTravelMonday(int pos, double monday) { this.travelMonday.add(pos, monday); }
  public void setTravelTuesday(int pos, double tuesday) { this.travelTuesday.add(pos, tuesday); }
  public void setTravelWednesday(int pos, double wednesday) { this.travelWednesday.add(pos, wednesday); }
  public void setTravelThursday(int pos, double thursday) { this.travelThursday.add(pos, thursday); }
  public void setTravelFriday(int pos, double friday) { this.travelFriday.add(pos, friday); }
  public void setTravelSaturday(int pos, double saturday) { this.travelSaturday.add(pos, saturday); }
  public void setTravelLineTotal(int pos, double lineTotal) { this.travelLineTotal.add(pos, lineTotal); }
  // Rest of the components
  public void setSignatureDate(String signatureDate) { this.signatureDate = signatureDate; }
  public void setEmployee(String employee) { this.employee = employee; }
  public void setManager(String manager) { this.manager = manager; }
  public void setOneup(String oneup) { this.oneup = oneup; }
  public void setCustomer(String customer) { this.customer = customer; }
  public void setPrintDate(String printDate) { this.printDate = printDate; }
  public void setPrintTime(String printTime) { this.printTime = printTime; }
  public void setPrintBy(String printBy) { this.printBy = printBy; }
  public void setPrintVersion(String printVersion) { this.printVersion = printVersion; }

  // Getters
  public String getCustomerName() { return customerName; }
  public String getCustomerNr() { return customerNr; }
  public String getPersonnelName() { return personnelName; }
  public String getPersonnelNr() { return personnelNr; }
  public String getWeekEnding() { return weekEnding; }
  // Non-Billable Components
  public String getNonCostType(int pos) { return nonCostType.get(pos); }
  public String getNonCostNr(int pos) { return nonCostNr.get(pos); }
  public String getNonCostItem(int pos) { return nonCostItem.get(pos); }
  public String getNonTaskLevel(int pos) { return nonTaskLevel.get(pos); }
  public String getNonTaskType(int pos) { return nonTaskType.get(pos); }
  public String getNonTaskComponent(int pos) { return nonTaskComponent.get(pos); }
  public String getNonComment(int pos) { return nonComment.get(pos); }
  public double getNonSunday(int pos) { return nonSunday.get(pos); }
  public double getNonMonday(int pos) { return nonMonday.get(pos); }
  public double getNonTuesday(int pos) { return nonTuesday.get(pos); }
  public double getNonWednesday(int pos) { return nonWednesday.get(pos); }
  public double getNonThursday(int pos) { return nonThursday.get(pos); }
  public double getNonFriday(int pos) { return nonFriday.get(pos); }
  public double getNonSaturday(int pos) { return nonSaturday.get(pos); }
  public double getNonLineTotal(int pos) { return nonLineTotal.get(pos); }
  // Billable Components
  public String getBillCostType(int pos) { return billCostType.get(pos); }
  public String getBillCostNr(int pos) { return billCostNr.get(pos); }
  public String getBillCostItem(int pos) { return billCostItem.get(pos); }
  public String getBillTaskLevel(int pos) { return billTaskLevel.get(pos); }
  public String getBillTaskType(int pos) { return billTaskType.get(pos); }
  public String getBillTaskComponent(int pos) { return billTaskComponent.get(pos); }
  public String getBillComment(int pos) { return billComment.get(pos); }
  public double getBillSunday(int pos) { return billSunday.get(pos); }
  public double getBillMonday(int pos) { return billMonday.get(pos); }
  public double getBillTuesday(int pos) { return billTuesday.get(pos); }
  public double getBillWednesday(int pos) { return billWednesday.get(pos); }
  public double getBillThursday(int pos) { return billThursday.get(pos); }
  public double getBillFriday(int pos) { return billFriday.get(pos); }
  public double getBillSaturday(int pos) { return billSaturday.get(pos); }
  public double getBillLineTotal(int pos) { return billLineTotal.get(pos); }
  // Travel Components
  public String getTravelCostType(int pos) { return travelCostType.get(pos); }
  public String getTravelCostNr(int pos) { return travelCostNr.get(pos); }
  public String getTravelCostItem(int pos) { return travelCostItem.get(pos); }
  public String getTravelTaskLevel(int pos) { return travelTaskLevel.get(pos); }
  public String getTravelTaskType(int pos) { return travelTaskType.get(pos); }
  public String getTravelTaskComponent(int pos) { return travelTaskComponent.get(pos); }
  public String getTravelComment(int pos) { return travelComment.get(pos); }
  public double getTravelSunday(int pos) { return travelSunday.get(pos); }
  public double getTravelMonday(int pos) { return travelMonday.get(pos); }
  public double getTravelTuesday(int pos) { return travelTuesday.get(pos); }
  public double getTravelWednesday(int pos) { return travelWednesday.get(pos); }
  public double getTravelThursday(int pos) { return travelThursday.get(pos); }
  public double getTravelFriday(int pos) { return travelFriday.get(pos); }
  public double getTravelSaturday(int pos) { return travelSaturday.get(pos); }
  public double getTravelLineTotal(int pos) { return travelLineTotal.get(pos); }
  // Rest of the components
  public String getSignatureDate() { return signatureDate; }
  public String getEmployee() { return employee; }
  public String getManager() { return manager; }
  public String getOneup() { return oneup; }
  public String getCustomer() { return customer; }
  public String getPrintDate() { return printDate; }
  public String getPrintTime() { return printTime; }
  public String getPrintBy() { return printBy; }
  public String getPrintVersion() { return printVersion; }
}