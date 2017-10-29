package domain;

import parse.CsvResult;
import parse.MessageParser;
import report.ReportAggregator;

import java.util.HashMap;
import java.util.Map;
//todo: add tests related to messages
public class MessageRegistry {

  private final MessageParser parser;
  private final Map<String, Message> registry; //teacherName -> Message

  public MessageRegistry(CsvResult parsedMessages) {
    parser = new MessageParser();
    registry = new HashMap<>();
    registerAll(parsedMessages);
  }

  private void register(final Object[] messageRow) {
    parser.parse(messageRow).ifPresent(message -> {
      String teacher = message.getTeacherName();
      Message prevMessage = registry.put(teacher, message);
      if (prevMessage != null) {
        ReportAggregator.instance.userInputError("Teacher " + teacher + " has more than one message");
      }
    });
  }

  private void registerAll(final CsvResult parsedMessages) {

    for (Object[] messageRow : parsedMessages.data) {
      register(messageRow);
    }
  }

  public void findDummyMessages(TeacherRegistry teacherRegistry) {

    if (teacherRegistry == null) {
      throw new NullPointerException("Teacher registry cannot be null");
    }
    registry.values().stream().
            map(Message::getTeacherName).
            forEach(teacherName -> {
              Teacher teacher = teacherRegistry.getTeacher(teacherName);
              if (teacher == null) {
                ReportAggregator.instance.userInputError("Teacher " + teacherName + " has a message but no email");
              }
            });
  }

  public Message getMessage(final String teacherName) {
    return registry.get(teacherName);
  }

}
