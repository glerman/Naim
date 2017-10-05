package report.pojo;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import java.util.List;

@AutoValue
public abstract class SalaryParsingProblem {

  public abstract List<Object> salaryRow();
  public abstract Exception exception();

  public static SalaryParsingProblem create(Object[] salaryRow, Exception exception) {
    return new AutoValue_SalaryParsingProblem(ImmutableList.copyOf(salaryRow), exception);
  }
}
