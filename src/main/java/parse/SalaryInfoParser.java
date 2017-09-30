package parse;

import domain.SalaryInfo;

public class SalaryInfoParser {

  public SalaryInfo parse(final Object[] salaryRow) {

    int teacherId = Integer.parseInt(((String) salaryRow[0]).trim());
    String className = (String) salaryRow[4];
    String room = (String) salaryRow[5];
    String date = (String) salaryRow[7];
    int attendees = Integer.parseInt(((String) salaryRow[9]).trim());
    Double ratePerAttendee = Double.parseDouble(((String) salaryRow[11]).trim());

    return new SalaryInfo(teacherId, className, room, date, attendees, ratePerAttendee, salaryRow);
  }
}
