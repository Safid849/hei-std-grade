package school.hei.stdgrade.endpoint.web.controller;

import static org.reflections.Reflections.log;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebAuthController {

  @GetMapping("/web/login")
  public String loginPage() {
    log.info("=== Login page requested, returning view 'web/login' ===");
    return "web/login";
  }
}
