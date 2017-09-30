package domain;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import dnl.utils.text.table.TextTable;
import parse.SalaryInfoParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SalariesLogic {

  static final String[] OUTPUT_COLUMNS = {"className", "room", "date", "attendees", "ratePerAttendee"};

  private Multimap<Integer, SalaryInfo> salariesRegistry;

  public SalariesLogic(final Object[][] salariesData) {

    SalaryInfoParser parser = new SalaryInfoParser();
    List<SalaryInfo> salariesInfo = Lists.newArrayList(salariesData).stream().map(parser::parse).collect(Collectors.toList());
    salariesRegistry = Multimaps.index(salariesInfo, SalaryInfo::getTeacherId);
  }

  public Map<Integer, TextTable> createTeacherOutputs() {

    Map<Integer, TextTable> teacherOutput = new HashMap<>();
    Set<Integer> teacherIds = salariesRegistry.keySet();
    for (int teacherId : teacherIds) {

      List<SalaryInfo> teacherSalaries = Lists.newArrayList(salariesRegistry.get(teacherId));
      Object[][] salariesOutputData = new Object[teacherSalaries.size()][OUTPUT_COLUMNS.length];
      for (int i = 0; i < teacherSalaries.size(); i++) {
        salariesOutputData[i] = teacherSalaries.get(i).getSalaryRow();
      }
      TextTable textTable = new TextTable(OUTPUT_COLUMNS, salariesOutputData);
      teacherOutput.put(teacherId, textTable);
    }
    return teacherOutput;
  }
}
