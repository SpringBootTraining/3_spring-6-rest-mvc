package guru.springframework.spring6restmvc.services;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseModel {

    private Boolean success;
    private String message;
}
