package logic;

import com.google.common.collect.Lists;
import dnl.utils.text.table.TextTable;
import domain.SalaryInfo;
import domain.TeacherOutput;
import file.FileReader;
import logic.SalariesLogic;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import parse.CsvParser;
import parse.CsvResult;
import util.FilePathProvider;
import parse.SalaryInfoParser;

import javax.swing.table.TableModel;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SalariesLogicTest {

  private Map<Integer, List<SalaryInfo>> groupedSalaryInfo;
  private SalariesLogic salariesLogic;

  @Before
  public void setUp() throws Exception {

    FileReader fileReader = new FileReader();
    CsvParser csvParser = new CsvParser();
    CsvResult salariesData = csvParser.parse(fileReader.read(FilePathProvider.salariesFilePath, "UTF-8"));
    SalaryInfoParser salaryInfoParser = new SalaryInfoParser();
    groupedSalaryInfo = Lists.newArrayList(salariesData.data).
            stream().
            map(salaryInfoParser::parse).
            collect(Collectors.groupingBy(
                    SalaryInfo::getTeacherId,
                    Collectors.toList()));
    salariesLogic = new SalariesLogic(salariesData.data);
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
  private void checkOutputDimensions(Map<Integer, TextTable> teacherOutputs) {

    teacherOutputs.forEach((teacherId, table) -> {

      TableModel tableModel = table.getTableModel();

      int columnCount = tableModel.getColumnCount();
      int rowCount = tableModel.getRowCount();

      Assert.assertEquals(SalariesLogic.OUTPUT_COLUMNS.length, columnCount);
      Assert.assertEquals(groupedSalaryInfo.get(teacherId).size(), rowCount);
    });
  }
}