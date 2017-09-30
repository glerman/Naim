package domain;

import parse.TeacherParser;

import java.util.HashMap;
import java.util.Map;

public class TeacherRegistry {

  private final TeacherParser parser;
  private final Map<Integer, Teacher> registry;

  public TeacherRegistry() {
    parser = new TeacherParser();
    registry = new HashMap<>();
  }

  private void register(final Object[] teacherRow) {
    Teacher teacher = parser.parse(teacherRow);
    registry.put(teacher.getId(), teacher);
  }

  public void registerAll(final Object[][] teacherRows) {

    for (Object[] teacherRow : teacherRows) {
      register(teacherRow);
    }
  }

  public Teacher getTeacher(final int id) {
    return registry.get(id);
  }

}
