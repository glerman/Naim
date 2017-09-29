import javax.mail.MessagingException;
import java.io.IOException;

/**
 * Created by glerman on 24/9/17.
 */
public class HelloWorld {

  public static void main(String[] args) throws IOException, MessagingException {

    Sender.sendMail(
            "ppm21@yandex.ru",
            "gal.lerman1@gmail.com",
            "hi babe",
            "kill me :)");
  }
}
