package domain;

import com.google.common.collect.*;
import dnl.utils.text.table.TextTable;
import parse.SalaryInfoParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SalariesLogic {

  static final String[] OUTPUT_COLUMNS = {"כיתה", "אולם", "תאריך", "משתתפים", "תעריף"};

  private Multimap<String, SalaryInfo> salariesRegistry;

  public SalariesLogic(final Object[][] salariesData) {

    SalaryInfoParser parser = new SalaryInfoParser();
    List<SalaryInfo> salariesInfo = Lists.newArrayList(salariesData).stream().map(parser::parse).collect(Collectors.toList());
    salariesRegistry = Multimaps.index(salariesInfo, SalaryInfo::getTeacherName);
  }

  public Map<String, TeacherOutput> createTeacherOutputs() {

    Map<String, TeacherOutput> allTeacherOutputs = new HashMap<>();
    Set<String> teacherNames = salariesRegistry.keySet();
    for (String teacherName : teacherNames) {

      List<TextTable> salaryOutputsSingleTeacher = Lists.newArrayList();
      List<SalaryInfo> allSalariesSingleTeacher = Lists.newArrayList(salariesRegistry.get(teacherName));
      int totalPaymentSingleTeacher = (int) allSalariesSingleTeacher.
              stream().
              mapToDouble(SalaryInfo::getRatePerAttendee).
              sum();
      ImmutableListMultimap<String, SalaryInfo> teacherSalariesByClass = Multimaps.index(allSalariesSingleTeacher, SalaryInfo::getClassName);

      for (String className : teacherSalariesByClass.keySet()) {
        TextTable textTable = getTextTable(teacherSalariesByClass.get(className));
        salaryOutputsSingleTeacher.add(textTable);
      }
      TeacherOutput singleTeacherOutput = new TeacherOutput(salaryOutputsSingleTeacher, totalPaymentSingleTeacher);
      allTeacherOutputs.put(teacherName, singleTeacherOutput);
    }
    return allTeacherOutputs;
  }

  private TextTable getTextTable(List<SalaryInfo> teacherSalaries) {
    Object[][] salariesOutputData = new Object[teacherSalaries.size()][OUTPUT_COLUMNS.length];
    for (int i = 0; i < teacherSalaries.size(); i++) {
      SalaryInfo salaryInfo = teacherSalaries.get(i);
      salariesOutputData[i][0] = salaryInfo.getClassName();
      salariesOutputData[i][1] = salaryInfo.getRoom();
      salariesOutputData[i][2] = salaryInfo.getDate();
      salariesOutputData[i][3] = salaryInfo.getAttendees();
      salariesOutputData[i][4] = (int) salaryInfo.getRatePerAttendee();
    }
    return new TextTable(OUTPUT_COLUMNS, salariesOutputData);
  }
}
