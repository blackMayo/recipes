package iv.recipes.webapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Ioanna Vletsou
 *         Copyright (c) 2016 GMX GmbH, Muenchen. All rights reserved.
 */
@Controller
@RequestMapping
public class TemplateController {

  private static final Logger LOG = LoggerFactory.getLogger(TemplateController.class);

  @RequestMapping(value = "/welcome", method = RequestMethod.GET)
  public ModelAndView welcome() {
    LOG.info("HELLO!");
    return new ModelAndView("hello");
  }

}
