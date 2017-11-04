package domain;

import parse.TeacherParser;
import report.ReportAggregator;

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
    parser.parse(teacherRow).ifPresent(teacher -> {
      Teacher prevTeacher = registry.put(teacher.getName(), teacher);
      if (prevTeacher != null) {
        ReportAggregator.instance.userInputError("Teacher '" + teacher.getName() + "' appears more than once in the teachers input");
      }
    });
  }

  public void registerTeachers(final Object[][] teacherRows) {

    for (Object[] teacherRow : teacherRows) {
      register(teacherRow);
    }
  }

  public Teacher getTeacher(final String teacherName) {
    return registry.get(teacherName);
  }

  public void registerMessages(MessageRegistry messageRegistry) {

    messageRegistry.findDummyMessages(this);
    if (registry.isEmpty()) {
      throw new IllegalStateException("Teacher registry should be set up");
    }
    registry.values().forEach(teacher -> {
      Message message = messageRegistry.getMessage(teacher.getName());
      if (message != null) { //A teacher may not have a personal message, it's ok
        teacher.setMessage(message.getMessage());
      }
    });
  }
}
