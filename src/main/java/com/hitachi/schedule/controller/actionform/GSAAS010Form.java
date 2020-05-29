package com.hitachi.schedule.controller.actionform;

import com.hitachi.schedule.config.constraints.NoSpace;
import com.hitachi.schedule.controller.param.SelectInfo;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.List;

@Data
public class GSAAS010Form extends BaseForm {

    @NotBlank(message = "{GSAAS010.strScheduleYykYmdY1}")
    @Pattern(regexp = "[0-9]{4}", message = "{GSAAS010.strScheduleYykYmdY2}")
    private String strScheduleYykYmdY;

    @NotBlank(message = "{GSAAS010.strScheduleYykYmdM1}")
    @Min(value = 1, message = "{GSAAS010.strScheduleYykYmdM2}")
    @Max(value = 12, message = "{GSAAS010.strScheduleYykYmdM2}")
    private String strScheduleYykYmdM;

    @NotBlank(message = "{GSAAS010.strScheduleYykYmdD1}")
    @Min(value = 1, message = "{GSAAS010.strScheduleYykYmdD2}")
    @Max(value = 31, message = "{GSAAS010.strScheduleYykYmdD2}")
    private String strScheduleYykYmdD;

    @NotBlank(message = "{GSAAS010.strScheduleStartHmH1}")
    @Min(value = 0, message = "{GSAAS010.strScheduleStartHmH2}")
    @Max(value = 23, message = "{GSAAS010.strScheduleStartHmH2}")
    private String strScheduleStartHmH;

    @NotBlank(message = "{GSAAS010.strScheduleStartHmM1}")
    @Min(value = 0, message = "{GSAAS010.strScheduleStartHmM2}")
    @Max(value = 59, message = "{GSAAS010.strScheduleStartHmM2}")
    private String strScheduleStartHmM;

    @NotBlank(message = "{GSAAS010.strScheduleEndHmH1}")
    @Min(value = 0, message = "{GSAAS010.strScheduleEndHmH2}")
    @Max(value = 23, message = "{GSAAS010.strScheduleEndHmH2}")
    private String strScheduleEndHmH;

    @NotBlank(message = "{GSAAS010.strScheduleEndHmM1}")
    @Min(value = 0, message = "{GSAAS010.strScheduleEndHmM2}")
    @Max(value = 59, message = "{GSAAS010.strScheduleEndHmM2}")
    private String strScheduleEndHmM;

    @NotBlank(message = "{GSAAS010.strScheduleYukn1}")
    @NoSpace(message = "{GSAAS010.strScheduleYukn2}")
    @Size(max = 64, message = "{GSAAS010.strScheduleYukn3}")
    private String strScheduleYukn;

    @NotBlank(message = "{GSAAS010.strKigstName1}")
    private String strKigstName;

    private List<SelectInfo> listKigst;

    @Size(max = 400, message = "{GSAAS010.strScheduleBku1}")
    private String strScheduleBku;

    private boolean insertFlg = true;
}
