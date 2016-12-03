package budgetAppV2;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@EnableAutoConfiguration
public class greeting {

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello World!";
    }

    //public static void main(String[] args) throws Exception {
    //    SpringApplication.run(greeting.class, args);
    //}
}
