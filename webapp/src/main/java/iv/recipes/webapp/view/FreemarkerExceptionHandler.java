package iv.recipes.webapp.view;

import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import java.io.PrintWriter;
import java.io.Writer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ioanna Vletsou
 */
public class FreemarkerExceptionHandler implements TemplateExceptionHandler {

  private static final Logger LOG = LoggerFactory.getLogger(FreemarkerExceptionHandler.class);

  @Override
  public void handleTemplateException(TemplateException templateException, Environment environment,
                                      Writer writer) throws TemplateException {
    LOG.warn("FTL Stack:\n {}", templateException.getFTLInstructionStack());
    PrintWriter printWriter = (writer instanceof PrintWriter)
        ? (PrintWriter) writer
        : new PrintWriter(writer);
    printWriter.println("<!-- An internal Rendering Error occured -->");
    throw templateException;
  }

}
