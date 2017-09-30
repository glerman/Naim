package parse;

import domain.Teacher;

public class TeacherParser {

  public Teacher parse(Object[] teacherRow) {

    int id = Integer.parseInt((String) teacherRow[0]);
    String name = (String) teacherRow[1];
    String email = (String) teacherRow[2];

    return new Teacher(id, name, email);
  }
}
