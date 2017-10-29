package logic;

import com.google.common.collect.*;
import domain.SalaryInfo;
import domain.TeacherOutput;
import domain.TeacherRegistry;
import parse.SalaryInfoParser;

import java.util.*;
import java.util.stream.Collectors;

public class SalariesLogic {

  private final TeacherRegistry teacherRegistry;
  private final Multimap<String, SalaryInfo> salariesRegistry;

  public SalariesLogic(final Object[][] salariesData, TeacherRegistry teacherRegistry) {

    SalaryInfoParser parser = new SalaryInfoParser();
    List<SalaryInfo> salariesInfo = Lists.newArrayList(salariesData).
            stream().
            map(parser::parse).
            filter(Optional::isPresent).
            map(Optional::get).
            collect(Collectors.toList());
    salariesRegistry = Multimaps.index(salariesInfo, SalaryInfo::getTeacherName);
    this.teacherRegistry = teacherRegistry;
  }

  /**
   *
   * @return Map from teacher name to an output object
   */
  public Map<String, TeacherOutput> createTeacherOutputs() {

    Map<String, TeacherOutput> allTeacherOutputs = new HashMap<>();
    Set<String> teacherNames = salariesRegistry.keySet();
    for (String teacherName : teacherNames) {
      List<SalaryInfo> allSalariesSingleTeacher = Lists.newArrayList(salariesRegistry.get(teacherName));

      ImmutableMap<String, Collection<SalaryInfo>> classNameToSalariesInfoSingleTeacher = Multimaps.index(allSalariesSingleTeacher, SalaryInfo::getClassName).asMap();
      int totalPaymentSingleTeacher = allSalariesSingleTeacher.
              stream().
              mapToInt(SalaryInfo::getPayment).
              sum();
      String teacherMessage = teacherRegistry.getTeacher(teacherName).getMessage();
      TeacherOutput singleTeacherOutput = new TeacherOutput(classNameToSalariesInfoSingleTeacher, totalPaymentSingleTeacher, teacherMessage);
      allTeacherOutputs.put(teacherName, singleTeacherOutput);
    }
    return allTeacherOutputs;
  }
}
