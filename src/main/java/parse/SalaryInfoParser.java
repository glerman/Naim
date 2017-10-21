package parse;

import domain.SalaryInfo;
import report.ReportAggregator;

import java.util.Optional;


public class SalaryInfoParser {

  public Optional<SalaryInfo> parse(final Object[] salaryRow) {
    try {
      int teacherId = Integer.parseInt(((String) salaryRow[0]).trim());
      String teacherName = ((String) salaryRow[1]).trim();
      String className = ((String) salaryRow[4]).trim();
      String room = ((String) salaryRow[5]).trim();
      String date = ((String) salaryRow[7]).trim();
      int attendees = Integer.parseInt(((String) salaryRow[10]).trim());
      Integer ratePerAttendee = Integer.parseInt(((String) salaryRow[11]).trim());

      SalaryInfo salaryInfo = new SalaryInfo(teacherId, teacherName, className, room, date, attendees, ratePerAttendee, salaryRow);
      return Optional.of(salaryInfo);
    } catch (Exception e) {
      ReportAggregator.instance.salaryParsingError(salaryRow, e);
      return Optional.empty();
    }
  }
}
