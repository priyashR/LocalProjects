package budgetAppV2;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@EnableAutoConfiguration
public class greeting {

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }
}
