package logic;

import com.google.common.collect.Lists;
import domain.SalaryInfo;
import domain.TeacherOutput;
import domain.TeacherRegistry;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import parse.CsvResult;
import parse.SalaryInfoParser;
import util.InputReaderHelper;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class SalariesLogicTest {

  private Map<Integer, List<SalaryInfo>> groupedSalaryInfo;
  private SalariesLogic salariesLogic;

  @Before
  public void setUp() throws Exception {

    CsvResult salariesData = InputReaderHelper.readCsv(InputReaderHelper.salariesFilePath);
    CsvResult teacherData = InputReaderHelper.readCsv(InputReaderHelper.teachersFilePath);
    TeacherRegistry teacherRegistry = new TeacherRegistry();
    teacherRegistry.registerTeachers(teacherData.data)
    ;
    SalaryInfoParser salaryInfoParser = new SalaryInfoParser();
    groupedSalaryInfo = Lists.newArrayList(salariesData.data).
            stream().
            map(salaryInfoParser::parse).
            map(Optional::get).
            collect(Collectors.groupingBy(
                    SalaryInfo::getTeacherId,
                    Collectors.toList()));
    salariesLogic = new SalariesLogic(salariesData.data, teacherRegistry);
  }

  @Test
  public void test() throws Exception {

    Map<String, TeacherOutput> teacherOutputs = salariesLogic.createTeacherOutputs();

    Assert.assertEquals(groupedSalaryInfo.size(), teacherOutputs.size());
//    checkOutputDimensions(teacherOutputs);
//    checkOutputData(teacherOutputs);
  }

//  private void checkOutputData(Map<Integer, TextTable> teacherOutputs) {
//
//    teacherOutputs.forEach((teacherId, table) -> {
//
//      TableModel tableModel = table.getTableModel();
//      List<SalaryInfo> teacherSalaryInfos = groupedSalaryInfo.get(teacherId);
//
//      for ()
//    });
//  }
//
//  private void checkOutputDimensions(Map<Integer, TextTable> teacherOutputs) {
//
//    teacherOutputs.forEach((teacherId, table) -> {
//
//      TableModel tableModel = table.getTableModel();
//
//      int columnCount = tableModel.getColumnCount();
//      int rowCount = tableModel.getRowCount();
//
//      Assert.assertEquals(SalariesLogic.OUTPUT_COLUMNS.length, columnCount);
//      Assert.assertEquals(groupedSalaryInfo.get(teacherId).size(), rowCount);
//    });
//  }
}
