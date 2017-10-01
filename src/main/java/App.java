import dnl.utils.text.table.TextTable;
import domain.SalariesLogic;
import domain.Teacher;
import domain.TeacherRegistry;
import mail.Sender;
import parse.CsvParser;
import parse.CsvResult;
import file.FileReader;

import javax.mail.MessagingException;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.Map;

import static com.sun.org.apache.xml.internal.serialize.LineSeparator.Unix;

/**
 * Created by glerman on 24/9/17.
 */
public class App {

  public static void main(String[] args) throws IOException, MessagingException {

    String salariesFilePath = args[0];
    String teacherFilePath = args[1];
    String charset = args[2];


    FileReader fileReader = new FileReader();
    CsvParser csvParser = new CsvParser();

    CsvResult parsedSalaries = csvParser.parse(fileReader.read(salariesFilePath, charset));
    CsvResult parsedTeachers = csvParser.parse(fileReader.read(teacherFilePath, charset));

    TeacherRegistry teacherRegistry = new TeacherRegistry();
    teacherRegistry.registerAll(parsedTeachers.data);

    SalariesLogic salariesLogic = new SalariesLogic(parsedSalaries.data);
    Map<Integer, TextTable> teacherOutputs = salariesLogic.createTeacherOutputs();

    teacherOutputs.forEach((teacherId, outputTable) -> {

      Teacher teacher = teacherRegistry.getTeacher(teacherId);
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      outputTable.printTable(new PrintStream(outputStream), 0);
      try {
        String outputTableStr = outputStream.toString(charset);
//        String teacherEmail = teacher.getEmail();

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(outputTableStr);
//        Sender.sendMail(
//                "gal.lerman1@gmail.com",
//                "gal.lerman1@gmail.com",
//                "Naim",
//                outputTableStr);
      } catch (UnsupportedEncodingException e) {
        throw new RuntimeException(e);
      }
    });
  }
}
