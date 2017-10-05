import com.google.common.collect.Lists;
import logic.SalariesLogic;
import domain.Teacher;
import domain.TeacherOutput;
import domain.TeacherRegistry;
import file.FileReader;
import mail.Sender;
import parse.CsvParser;
import parse.CsvResult;
import view.TeacherOutputFormatter;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by glerman on 24/9/17.
 */
public class App {

  public static void main(String[] args) throws IOException, MessagingException {

    String salariesFilePath = args[0];
    String teacherFilePath = args[1];
    String charset = args[2];
    boolean sendMails = Boolean.valueOf(args[3]);
    boolean sendFromNaim = Boolean.valueOf(args[4]);

    Sender sender = new Sender(sendFromNaim);

    FileReader fileReader = new FileReader();
    CsvParser csvParser = new CsvParser();

    CsvResult parsedSalaries = csvParser.parse(fileReader.read(salariesFilePath, charset));
    CsvResult parsedTeachers = csvParser.parse(fileReader.read(teacherFilePath, charset));

    TeacherRegistry teacherRegistry = new TeacherRegistry();
    teacherRegistry.registerAll(parsedTeachers.data);

    SalariesLogic salariesLogic = new SalariesLogic(parsedSalaries.data);
    Map<String, TeacherOutput> teacherOutputs = salariesLogic.createTeacherOutputs();

    List<String> problemTeachers = Lists.newArrayList();
    TeacherOutputFormatter formatter = new TeacherOutputFormatter();

    teacherOutputs.forEach((teacherName, teacherOutput) -> {

      Teacher teacher = teacherRegistry.getTeacher(teacherName);
      if (teacher == null) {
        problemTeachers.add(teacherName);
        return;
      }
      StringBuilder sb = formatter.formatTeacherOutput(charset, teacherOutput);

      String subjectLine = formatter.formatSubjectLine(teacherName);
      if (sendMails) {
        try {
          sender.sendMail(
                  teacher.getEmail(),
                  subjectLine,
                  sb.toString());
          Thread.sleep(500);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
      sb.append("\n").append(subjectLine).append("\n").append(teacher.getEmail());
      System.out.println(sb);
    });

    System.out.printf("\n\n");
    System.out.println("Total teacher salaries: " + teacherOutputs.size());
    System.out.println("Out of which, didn't find emails for: " + problemTeachers.size());
    System.out.println(problemTeachers);

  }


//  private static List<TextTable> createSingleTeacherOutput(List<SalaryInfo> allSalariesSingleTeacher) {
//    List<TextTable> salaryOutputsSingleTeacher = Lists.newArrayList();
//    ImmutableListMultimap<String, SalaryInfo> teacherSalariesByClassName = Multimaps.index(allSalariesSingleTeacher, SalaryInfo::getClassName);
//
//    for (String className : teacherSalariesByClassName.keySet()) {
//      TextTable textTable = toTextTable(teacherSalariesByClassName.get(className));
//      salaryOutputsSingleTeacher.add(textTable);
//    }
//    return salaryOutputsSingleTeacher;
//  }
//

}
