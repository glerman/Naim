import dnl.utils.text.table.TextTable;
import domain.SalariesLogic;
import domain.Teacher;
import domain.TeacherRegistry;
import mail.Sender;
import parse.CsvParser;
import parse.CsvResult;
import file.FileReader;

import javax.mail.MessagingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by glerman on 24/9/17.
 */
public class App {

  public static void main(String[] args) throws IOException, MessagingException {

    String salariesFilePath = args[0];
    String salariesCharset = args[1];
    String teacherFilePath = args[2];
    String teacherCharset = args[3];


    FileReader fileReader = new FileReader();
    CsvParser csvParser = new CsvParser();

    CsvResult parsedSalaries = csvParser.parse(fileReader.read(salariesFilePath, salariesCharset));
    CsvResult parsedTeachers = csvParser.parse(fileReader.read(teacherFilePath, teacherCharset));

    TeacherRegistry teacherRegistry = new TeacherRegistry();
    teacherRegistry.registerAll(parsedTeachers.data);

    SalariesLogic salariesLogic = new SalariesLogic(parsedSalaries.data);
    Map<Integer, TextTable> teacherOutputs = salariesLogic.createTeacherOutputs();

    teacherOutputs.forEach((teacherId, outputTable) -> {

      Teacher teacher = teacherRegistry.getTeacher(teacherId);
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      outputTable.printTable(new PrintStream(outputStream), 0);
      try {
        String outputTableStr = outputStream.toString(salariesCharset);
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(outputTableStr);
      } catch (UnsupportedEncodingException e) {
        throw new RuntimeException("Problem with encoding: " + salariesCharset, e);
      }
    });

//    Sender.sendMail(
//            "ppm21@yandex.ru",
//            "gal.lerman1@gmail.com",
//            "Naim",
//            "kill me :)");
  }
}
