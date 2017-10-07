package view;

import domain.SalaryInfo;

import java.util.function.Function;

public class SalaryInfoToOutputRow implements Function<SalaryInfo, Object[]> {

  @Override
  public Object[] apply(SalaryInfo salaryInfo) {
    Object[] salariesOutputRow = new Object[5];
    salariesOutputRow[0] = salaryInfo.getClassName();
    salariesOutputRow[1] = salaryInfo.getRoom();
    salariesOutputRow[2] = salaryInfo.getDate();
    salariesOutputRow[3] = salaryInfo.getAttendees();
    salariesOutputRow[4] = salaryInfo.getPayment();
    return salariesOutputRow;
  }
}
