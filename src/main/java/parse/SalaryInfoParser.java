package parse;

import domain.SalaryInfo;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class SalaryInfoParser {

  public static DateTimeFormatter inputDtf = DateTimeFormat.forPattern("dd/MM/yyyy");
  public static DateTimeFormatter ouputDtf = DateTimeFormat.forPattern("dd/MM");

  public SalaryInfo parse(final Object[] salaryRow) {

    int teacherId = Integer.parseInt(((String) salaryRow[0]).trim());
    String teacherName = ((String) salaryRow[1]).trim();
    String className = ((String) salaryRow[4]).trim();
    String room = ((String) salaryRow[5]).trim();
    String date = ((String) salaryRow[7]).trim();
    String storedDate = ouputDtf.print(inputDtf.parseDateTime(date));
    int attendees = Integer.parseInt(((String) salaryRow[9]).trim());
    Double ratePerAttendee = Double.parseDouble(((String) salaryRow[11]).trim());

    return new SalaryInfo(teacherId, teacherName, className, room, storedDate, attendees, ratePerAttendee, salaryRow);
  }
}
