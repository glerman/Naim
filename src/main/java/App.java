import com.google.api.client.repackaged.com.google.common.base.Objects;
import com.google.common.collect.Lists;
import dnl.utils.text.table.TextTable;
import domain.SalariesLogic;
import domain.Teacher;
import domain.TeacherOutput;
import domain.TeacherRegistry;
import file.FileReader;
import mail.Sender;
import parse.CsvParser;
import parse.CsvResult;

import javax.mail.MessagingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * Created by glerman on 24/9/17.
 */
public class App {

  private static String subjectSuffix = "פירוט תשלום עבור חודש - 09/2017";

  private static String message = "הי,\n" +
          "\n" +
          "להלן פירוט תשלומים עבור חודש 09/2017 \n" +
          "נא לרשום על הקבלה / חשבונית עבור \"נורמל מחקר\" על הסכום הנ״ל \n" +
          "(בתוספת מע\"מ במידה וצריך).\n" +
          "\n";

  private static String galEmail = "gal.lerman1@gmail.com";
  private static String naimEmail = "info@naim.org.il";
  private static String naimSecret = "client_secret_naim.json";
  private static String galSecret = "client_secret_gal.json";

  private static boolean sendFromNaim = true;

  public static void main(String[] args) throws IOException, MessagingException {

    String salariesFilePath = args[0];
    String teacherFilePath = args[1];
    String charset = args[2];

    Sender sender = new Sender(sendFromNaim ? naimSecret : galSecret);

    FileReader fileReader = new FileReader();
    CsvParser csvParser = new CsvParser();

    CsvResult parsedSalaries = csvParser.parse(fileReader.read(salariesFilePath, charset));
    CsvResult parsedTeachers = csvParser.parse(fileReader.read(teacherFilePath, charset));

    TeacherRegistry teacherRegistry = new TeacherRegistry();
    teacherRegistry.registerAll(parsedTeachers.data);

    SalariesLogic salariesLogic = new SalariesLogic(parsedSalaries.data);
    Map<String, TeacherOutput> teacherOutputs = salariesLogic.createTeacherOutputs();

    List<String> problemTeachers = Lists.newArrayList();

    teacherOutputs.forEach((teacherName, teacherOutput) -> {

      StringBuilder sb = new StringBuilder();
      Teacher teacher = teacherRegistry.getTeacher(teacherName);
      if (teacher == null) {
        problemTeachers.add(teacherName);
        return;
      }
      String subjectLine = String.format("%s %s", teacher.getName(), subjectSuffix);

      sb.append("\n").append(message).append("\n").append("דו\"ח שיעורים\n");

      for (TextTable outputTable : teacherOutput.tablePerClass) {
        handleSingleOutputTable(charset, sb, outputTable);
      }
      sb.append("סה\"כ בגין שיעורים: ").append(teacherOutput.totalPayment).append(" ש״ח").append("\n");
      sb.append("\n").append("תודה").append("\n\n");
      sb.append("לנה").append("\n");
      sb.append("סטודיו נעים - יוגה, מחול, פילאטיס").append("\n");
      sb.append("דרך שלמה (סלמה) 46, תל אביב, מיקוד: 66073").append("\n\n");
      sb.append("naim.org.il\n" +
              "facebook.com/stnaim\n" +
              "facebook.com/gymnaim");

      try {
        sender.sendMail(
                "gal.lerman1@gmail.com",
                sendFromNaim ? naimEmail : galEmail,
                subjectLine,
                sb.toString());
        Thread.sleep(500);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      System.out.println(sb);//.append("\n").append(subjectLine).append("\n").append(teacher.getName()).append("\n").append(teacher.getEmail()));
      System.exit(1);
    });

    System.out.printf("\n\n");
    System.out.println("Total teacher salaries: " + teacherOutputs.size());
    System.out.println("Out of which, didn't find emails for: " + problemTeachers.size());
    System.out.println(problemTeachers);

  }

  private static void handleSingleOutputTable(String charset, StringBuilder sb, TextTable outputTable) {
    //      Teacher teacher = teacherRegistry.getTeacher(teacherId);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    outputTable.printTable(new PrintStream(outputStream), 0);
    try {
      String outputTableStr = outputStream.toString(charset);
//        String teacherEmail = teacher.getEmail();

      sb.append(outputTableStr).append("\n");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }
}
