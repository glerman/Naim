package parse;

import domain.Message;
import report.ReportAggregator;

import java.util.Optional;

public class MessageParser {

  public Optional<Message> parse(Object[] messageRow) {
    try {
      String teacherName = ((String) messageRow[0]).trim();
      String message = ((String) messageRow[1]).trim();
      return Optional.of(new Message(teacherName, removeWrappingQuotes(message)));

    } catch (Exception e) {
      ReportAggregator.instance.teacherParsingError(messageRow, e);
      return Optional.empty();
    }
  }

  private String removeWrappingQuotes(String message) {
    char[] chars = message.toCharArray();
    if (chars[0] == '\"') {
      chars[0] = ' ';
    }
    if (chars[chars.length - 1] == '\"') {
      chars[chars.length - 1] = ' ';
    }
    return new String(chars).trim();
  }
}
