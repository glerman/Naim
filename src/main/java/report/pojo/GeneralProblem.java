package report.pojo;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class GeneralProblem {

  public abstract String message();
  public abstract Exception error();

  public static GeneralProblem create(String message, Exception error) {
    return new AutoValue_GeneralProblem(message, error);
  }

}
