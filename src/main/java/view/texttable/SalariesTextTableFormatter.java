package view.texttable;

import dnl.utils.text.table.TextTable;
import domain.SalaryInfo;

import java.util.Collection;

public class SalariesTextTableFormatter {

  public TextTable toTextTable(Collection<SalaryInfo> teacherSalaries, String[] columnNames) {
    Object[][] salariesOutputData = new Object[teacherSalaries.size()][columnNames.length];
    int i = 0;
    for (SalaryInfo salaryInfo : teacherSalaries) {
      salariesOutputData[i][0] = salaryInfo.getClassName();
      salariesOutputData[i][1] = salaryInfo.getRoom();
      salariesOutputData[i][2] = salaryInfo.getDate();
      salariesOutputData[i][3] = salaryInfo.getAttendees();
      salariesOutputData[i][4] = salaryInfo.getPayment();
      i++;
    }
    return new TextTable(columnNames, salariesOutputData);
  }
}
