import org.junit.Test;
import util.InputReaderHelper;

public class AppTest {

  @Test
  public void testSystemWeak() throws Exception {
    String sendMails = "false";
    String sendFromNaim = "false";
    String[] args = {
            InputReaderHelper.salariesFilePath,
            InputReaderHelper.teachersFilePath,
            "UTF-8",
            sendMails,
            sendFromNaim
    };
    App.main(args);
  }
}