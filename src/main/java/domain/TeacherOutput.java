package domain;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class TeacherOutput {

  public final Map<String, Collection<SalaryInfo>> classNameToSalariesInfo;
  public final int totalPayment;
  public final Optional<String> teacherMessage;

  public TeacherOutput(Map<String, Collection<SalaryInfo>> classNameToSalariesInfo, int totalPayment, Optional<String> teacherMessage) {
    this.classNameToSalariesInfo = classNameToSalariesInfo;
    this.totalPayment = totalPayment;
    this.teacherMessage = teacherMessage;
  }
}
