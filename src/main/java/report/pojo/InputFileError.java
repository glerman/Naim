package report.pojo;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class InputFileError {

  public abstract String message();
  public abstract Exception error();

  public static InputFileError create(String message, Exception error) {
    return new AutoValue_InputFileError(message, error);
  }


}
