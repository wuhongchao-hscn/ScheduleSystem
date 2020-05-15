package com.hitachi.schedule.controller.actionform;

import com.hitachi.schedule.controller.param.SelectInfo;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
public class GSACS050Form extends BaseForm {

    @NotBlank(message = "{GSACS050.strShkinSmi1}")
    @Size(max = 30, message = "{GSACS050.strShkinSmi2}")
    private String strShkinSmi;

    @NotBlank(message = "{GSACS050.strSsk1}")
    private String strSsk;

    private List<SelectInfo> listSsk;

    private MultipartFile imageFile;

    @NotBlank(message = "{GSACS050.strUserPassword1}")
    @Size(min = 8, max = 16, message = "{GSACS050.strUserPassword2}")
    private String strUserPassword;

    @NotBlank(message = "{GSACS050.strUserPasswordReInput1}")
    @Size(min = 8, max = 16, message = "{GSACS050.strUserPasswordReInput2}")
    private String strUserPasswordReInput;

    private List<String> rlIdList = new ArrayList<>();

    private List<SelectInfo> rlList;

}
