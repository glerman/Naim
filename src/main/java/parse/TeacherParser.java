package parse;

import domain.Teacher;
import report.ReportAggregator;

import java.util.Optional;

public class TeacherParser {

  public Optional<Teacher> parse(Object[] teacherRow) {
    try {
      String name = ((String) teacherRow[0]).trim();
      String email = ((String) teacherRow[1]).trim();
      return Optional.of(new Teacher(name, email));

    } catch (Exception e) {
      ReportAggregator.instance.teacherParsingError(teacherRow, e);
      return Optional.empty();
    }
  }
}
