package parse;

import domain.Teacher;

public class TeacherParser {

  public Teacher parse(Object[] teacherRow) {

    String name = ((String) teacherRow[0]).trim();
    String email = ((String) teacherRow[1]).trim();

    return new Teacher(name, email);
  }
}
