package za.co.absa.pangea.ops.service.step;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/step")
@RestController
public class StepController {

    @RequestMapping(produces= MediaType.APPLICATION_JSON_VALUE)
    public List<StepDTO> stepTypes() {
        List<StepDTO> stepDTOs = new ArrayList<>(0);
        stepDTOs.add(new StepDTO("ISS", "Issuance"));
        stepDTOs.add(new StepDTO("AMD", "Amendment"));
        stepDTOs.add(new StepDTO("PAY", " Payment"));
        return stepDTOs;
    }
}
