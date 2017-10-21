package view;

import com.google.common.collect.Lists;
import domain.SalaryInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SalaryInfoToOutputRow implements Function<SalaryInfo, List<Object>> {

  @Override
  public List<Object> apply(SalaryInfo i) {
    List<Object> outputRow = Lists.newArrayList(i.getClassName(), i.getRoom(),
            i.getDate(), i.getAttendees(), i.getPayment());
    return Lists.reverse(outputRow); //Reverse output because Hebrew is fun...
  }
}
