package com.hitachi.schedule.controller.actionform;

import com.hitachi.schedule.controller.param.SelectInfo;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class GSACS040Form extends BaseForm {

    private String strUserId;

    private String strShkinId;

    private String strShkinSmi;

    private String strImageRadio;

    private List<SelectInfo> imgList;

    private MultipartFile imageFile;

    @NotBlank(message = "{GSACS040.strUserPassword1}")
    @Size(min = 8, max = 16, message = "{GSACS040.strUserPassword2}")
    private String strUserPassword;

    @NotBlank(message = "{GSACS040.strUserPasswordReInput1}")
    @Size(min = 8, max = 16, message = "{GSACS040.strUserPasswordReInput2}")
    private String strUserPasswordReInput;

    private List<String> rlIdList;

    private List<SelectInfo> rlList;

}
