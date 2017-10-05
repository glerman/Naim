package domain;

import parse.TeacherParser;

import java.util.HashMap;
import java.util.Map;

public class TeacherRegistry {

  private final TeacherParser parser;
  private final Map<String, Teacher> registry;

  public TeacherRegistry() {
    parser = new TeacherParser();
    registry = new HashMap<>();
  }

  private void register(final Object[] teacherRow) {
    parser.parse(teacherRow).ifPresent(teacher -> registry.put(teacher.getName(), teacher));
  }

  public void registerAll(final Object[][] teacherRows) {

    for (Object[] teacherRow : teacherRows) {
      register(teacherRow);
    }
  }

  public Teacher getTeacher(final String teacherName) {
    return registry.get(teacherName);
  }

}
