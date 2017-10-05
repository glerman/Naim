package report.pojo;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import java.util.List;

@AutoValue
public abstract class DomainObjectParsingProblem {

  public abstract List<Object> domainObjectRow();
  public abstract Exception exception();

  public static DomainObjectParsingProblem create(Object[] domainObjectRow, Exception exception) {
    return new AutoValue_DomainObjectParsingProblem(ImmutableList.copyOf(domainObjectRow), exception);
  }

}
