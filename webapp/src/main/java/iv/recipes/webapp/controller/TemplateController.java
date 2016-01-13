package iv.recipes.webapp.controller;

import iv.recipes.webapp.view.FreemarkerExceptionHandler;

/**
 * @author Ioanna Vletsou
 *         Copyright (c) 2016 GMX GmbH, Muenchen. All rights reserved.
 */
public class TemplateController {

  private FreemarkerExceptionHandler bla;
  private boolean enabled;

  public TemplateController(FreemarkerExceptionHandler bla) {
    this.bla = bla;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
}
