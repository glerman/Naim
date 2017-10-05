package report;

import com.google.auto.value.AutoValue;
import domain.Teacher;

@AutoValue
public abstract class SendMailProblem {

  abstract Teacher teacher();
  abstract Exception exception();
  abstract String mailSubject();
  abstract String mailBody();

  public static SendMailProblem create(Teacher teacher, Exception exception, String mailSubject, String mailBody) {
    return new AutoValue_SendMailProblem(teacher, exception, mailSubject, mailBody);
  }

}
