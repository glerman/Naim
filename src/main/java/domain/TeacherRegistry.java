package domain;

import java.util.HashMap;
import java.util.Map;

public class TeacherRegistry {

  private final Map<Integer, Teacher> registry;

  public TeacherRegistry() {
    registry = new HashMap<>();
  }

  public State register(final Object[] teacherRow) {
    Teacher teacher = parseTeacher(teacherRow);
    Teacher prevValue = registry.putIfAbsent(teacher.getId(), teacher);

    if (prevValue == null) {
      return State.OK;
    } else {
      return State.DUPLICATE;
    }
  }

  private Teacher parseTeacher(Object[] teacherRow) {
    return new Teacher(Integer.parseInt((String) teacherRow[0]), (String) teacherRow[1], (String) teacherRow[2]);
  }

  public void registerAll(final Object[][] teacherRows) {
    for (Object[] teacherRow : teacherRows) {
      register(teacherRow);
    }
  }

  public Teacher getTeacher(final int id) {
    return registry.get(id);
  }

  public enum State {DUPLICATE, OK}
}
