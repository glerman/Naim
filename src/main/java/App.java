import domain.TeacherRegistry;
import mail.Sender;
import parse.CsvParser;
import parse.CsvResult;
import file.FileReader;

import javax.mail.MessagingException;
import java.io.IOException;

/**
 * Created by glerman on 24/9/17.
 */
public class App {

  public static void main(String[] args) throws IOException, MessagingException {

    String salariesFilePath = args[1];
    String teacherFilePath = args[2];

    FileReader fileReader = new FileReader();
    CsvParser csvParser = new CsvParser();

    CsvResult parsedSalaries = csvParser.parse(fileReader.read(salariesFilePath));
    CsvResult parsedTeachers = csvParser.parse(fileReader.read(salariesFilePath));

    TeacherRegistry teacherRegistry = new TeacherRegistry();
    teacherRegistry.registerAll(parsedTeachers.data);



    Sender.sendMail(
            "ppm21@yandex.ru",
            "gal.lerman1@gmail.com",
            "Naim",
            "kill me :)");
  }
}
