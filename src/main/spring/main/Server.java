package spring.main;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Server {

  private static final String template = "Hello!";

  @RequestMapping("/greeting")
  public String greeting() {
    return "Hej!";
  }

}