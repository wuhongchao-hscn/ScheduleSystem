package com.hitachi.schedule.controller.actionform;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class GSAXS010Form extends BaseForm {

    @NotBlank(message = "{GSAXS010.strUserId1}")
    @Pattern(regexp = "^[0-9a-zA-Z]+$", message = "{GSAXS010.strUserId2}")
    @Size(max = 8, message = "{GSAXS010.strUserId3}")
    private String strUserId;

    @NotBlank(message = "{GSAXS010.strUserPassword1}")
    @Size(min = 8, max = 16, message = "{GSAXS010.strUserPassword2}")
    private String strUserPassword;
}
