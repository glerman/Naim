package view;

import com.google.common.collect.Lists;
import domain.SalaryInfo;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SalaryInfoToOutputRow implements Function<SalaryInfo, List<Object>> {

  @Override
  public List<Object> apply(SalaryInfo i) {
    List<Object> outputRow = Lists.newArrayList(i.getClassName(), i.getRoom(),
            i.getDisplayDate(), i.getAttendees(), i.getPayment());
    return Lists.reverse(outputRow); //Reverse output because Hebrew is fun...
  }
}
