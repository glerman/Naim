package domain;

import dnl.utils.text.table.TextTable;

import java.util.List;

public class TeacherOutput {

  public final List<TextTable> tablePerClass;
  public final int totalPayment;

  public TeacherOutput(List<TextTable> tablePerClass, int totalPayment) {
    this.tablePerClass = tablePerClass;
    this.totalPayment = totalPayment;
  }
}
