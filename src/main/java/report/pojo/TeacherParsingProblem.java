package report.pojo;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import java.util.List;

@AutoValue
public abstract class TeacherParsingProblem {

  public abstract List<Object> teacherRow();
  public abstract Exception exception();

  public static TeacherParsingProblem create(Object[] teacherRow, Exception exception) {
    return new AutoValue_TeacherParsingProblem(ImmutableList.copyOf(teacherRow), exception);
  }

}
