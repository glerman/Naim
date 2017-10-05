package domain;

import java.util.Collection;
import java.util.Map;

public class TeacherOutput {

  public final Map<String, Collection<SalaryInfo>> classNameToSalariesInfo;
  public final int totalPayment;

  public TeacherOutput(Map<String, Collection<SalaryInfo>> classNameToSalariesInfo, int totalPayment) {
    this.classNameToSalariesInfo = classNameToSalariesInfo;
    this.totalPayment = totalPayment;
  }
}
