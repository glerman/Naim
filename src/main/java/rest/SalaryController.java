package rest;

import java.util.concurrent.atomic.AtomicLong;

import logic.AppLogic;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import report.ReportAggregator;

@RestController
public class SalaryController {

  private static final String template = "Hello, %s!";
  private final AtomicLong counter = new AtomicLong();

  @RequestMapping("/greeting")
  public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
    return new Greeting(counter.incrementAndGet(),
            String.format(template, name));
  }

  //todo: add error controller
  @RequestMapping("/salary")
  public SalaryResponse salary(@RequestParam(value = "salariesFilePath") String salariesFilePath,
                     @RequestParam(value = "teacherFilePath") String teacherFilePath,
                     @RequestParam(value = "messagesFilePath") String messagesFilePath,
                     @RequestParam(value = "charset", defaultValue = "UTF-8") String charset,
                     @RequestParam(value = "sendMails") boolean sendMails,
                     @RequestParam(value = "sendFromNaim") boolean sendFromNaim,
                     @RequestParam(value = "teachersToIterate") AppLogic.TeachersToIterate teachersToIterate,
                     @RequestParam(value = "receiptTo") String receiptTo,
                     @RequestParam(value = "debug", defaultValue = "false") boolean debug) {

    ReportAggregator.instance.init();
    String salariesPreview = null;
    try {
      AppLogic appLogic = new AppLogic();
      salariesPreview = appLogic.start(salariesFilePath, teacherFilePath, messagesFilePath, charset, sendMails, sendFromNaim, teachersToIterate, receiptTo);
    } catch (Throwable t) {
      ReportAggregator.instance.unexpectedError(t);
      System.out.println(ReportAggregator.instance.report(true));
    }
    return new SalaryResponse(salariesPreview, ReportAggregator.instance.report(debug));
  }
}
