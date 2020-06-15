package com.hitachi.schedule.config.component;

import com.hitachi.schedule.config.common.GXConst;
import com.hitachi.schedule.config.exception.ErrorInfoGM;
import com.hitachi.schedule.controller.actionform.*;
import com.hitachi.schedule.controller.param.HmInfo;
import com.hitachi.schedule.controller.param.YmdInfo;
import com.hitachi.schedule.service.GSAAScheduleF;
import com.hitachi.schedule.service.GSACScheduleF;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class CommonUtil {
    @Autowired
    private GSAAScheduleF gsaaService;
    @Autowired
    private GSACScheduleF gsacService;
    @Autowired
    private MessageReadUtil messageUtil;

    public void doInitGSAAS010(GSAAS010Form form) {
        form.setListKigst(gsaaService.getKigstList());
    }

    public void doInitGSACS010(GSACS010Form form) {
        form.setListKnskShbt(gsacService.getCodeInfoByBnri("CD000002"));
    }

    public void doInitGSACS040(GSACS040Form form) {
        form.setImgList(gsacService.getCodeInfoByBnri("CD000003"));
        form.setRlList(gsacService.getRlInfo());
    }

    public void doInitGSACS050(GSACS050Form form) {
        form.setRlList(gsacService.getRlInfo());
    }

    public void doRtnPre(Model model, Object obj, boolean msgGetFlg) {
        doRtnPre(model, null, obj, msgGetFlg);
    }

    public void doRtnPre(Model model, BaseForm form, Object obj) {
        doRtnPre(model, form, obj, true);
    }

    public void doRtnPre(Model model, BaseForm form, Object obj, boolean msgGetFlg) {
        String errMsg = null;
        if (msgGetFlg && obj instanceof String) {
            errMsg = messageUtil.getMessage((String) obj);
        } else if (obj instanceof List) {
            errMsg = getStringFromList((List) obj);
        } else {
            errMsg = (String) obj.toString();
        }

        if (model instanceof RedirectAttributes) {
            RedirectAttributes redirectModel = (RedirectAttributes) model;
            redirectModel.addFlashAttribute("error", errMsg);
            redirectModel.addFlashAttribute("form", form);
        } else {
            model.addAttribute("error", errMsg);
            model.addAttribute("form", form);
        }
        log.info("項目チェックには間違いがある、戻ります。");
    }

    public void doRtnPre(Map<String, Object> result, String errField, String errMsg, boolean msgGetFlg) {
        doRtnPre(result, Arrays.asList(errField), Arrays.asList(msgGetFlg ? messageUtil.getMessage(errMsg) : errMsg));
    }

    public void doRtnPre(Map<String, Object> result, List<String> errField, List<String> errMsg) {
        log.info("項目チェックには間違いがある、戻ります。");
        Map<String, Object> rtn_map = new HashMap<>();
        rtn_map.put("errItemId", errField);
        rtn_map.put("errMsg", errMsg);
        result.put("error", rtn_map);
    }

    public void checkDateExist(YmdInfo yyk_ymd, ErrorInfoGM e_in) {
        try {
            LocalDate.of(
                    Integer.parseInt(yyk_ymd.getYyyy()),
                    Integer.parseInt(yyk_ymd.getMm()),
                    Integer.parseInt(yyk_ymd.getDd())
            );
        } catch (Exception e) {
            throw e_in;
        }
    }

    public void checkStartEndHmOk(HmInfo start_hm, HmInfo end_hm, ErrorInfoGM e_in) {
        int start_hm_int = Integer.parseInt(start_hm.toString());
        int end_hm_int = Integer.parseInt(end_hm.toString());
        if (start_hm_int >= end_hm_int) {
            throw e_in;
        }
    }

    public String ymdConvert(String yyyyMMdd) {
        return new StringBuilder()
                .append(yyyyMMdd.substring(0, 4))
                .append("年")
                .append(yyyyMMdd.substring(4, 6))
                .append("月")
                .append(yyyyMMdd.substring(6))
                .append("日")
                .toString();
    }

    public String hmConver(String hhmm) {
        return new StringBuilder()
                .append(hhmm.substring(0, 2))
                .append("時")
                .append(hhmm.substring(2))
                .append("分")
                .toString();
    }

    public int getPageNo(Integer obj) {
        int rtn = obj / GXConst.GSAA_PROP_GSACT020_DISPLAY_SIZE;
        if (obj % GXConst.GSAA_PROP_GSACT020_DISPLAY_SIZE > 0) {
            rtn++;
        }
        return rtn;
    }

    public List<String> getListFromString(String str) {
        List<String> rtn_obj = new ArrayList<>();
        if (!StringUtils.isEmpty(str)) {
            String[] array = str.split(GXConst.GS_PROP_LIST_STRING_SPLIT_FUGO);
            Collections.addAll(rtn_obj, array);
        }
        return rtn_obj;
    }

    public String getStringFromList(List<String> li) {
        return String.join(GXConst.GS_PROP_LIST_STRING_SPLIT_FUGO, li);
    }

    public String getStringFromListBySpace(List<String> li) {
        return String.join(GXConst.GS_PROP_LIST_STRING_SPLIT_FUGO_SPACE, li);
    }

    public Set<String> getSetFromList(List<String> strList) {
        List<String> rtn_obj = new ArrayList<>();
        for (String str : strList) {
            if (!StringUtils.isEmpty(str)) {
                String[] array = str.split(GXConst.GS_PROP_LIST_STRING_SPLIT_FUGO);
                Collections.addAll(rtn_obj, array);
            }
        }
        return new HashSet<>(rtn_obj);
    }

    public Set<GrantedAuthority> getAuthoritySet(Set<String> userRoles) {
        List<GrantedAuthority> rtn_obj = new ArrayList<>();
        for (String role : userRoles) {
            SimpleGrantedAuthority obj = new SimpleGrantedAuthority(role);
            rtn_obj.add(obj);
        }
        return new HashSet<>(rtn_obj);
    }


    public boolean checkFileSize(Long len, int size, String unit) {
        double fileSize = 0;
        if ("B".equals(unit.toUpperCase())) {
            fileSize = (double) len;
        } else if ("K".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1024;
        } else if ("M".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1048576;
        } else if ("G".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1073741824;
        }
        if (fileSize > size) {
            return true;
        }
        return false;
    }

    public String getCollectionName(String param) {
        switch (param) {
            case GXConst.GSAB_MONGODB_PARAM_USER:
                return GXConst.GSAB_MONGODB_COLLECTION_NAME_USER;
            case GXConst.GSAB_MONGODB_PARAM_ARTICLE:
                return GXConst.GSAB_MONGODB_COLLECTION_NAME_ARTICLE;
            default:
                return GXConst.GSAB_MONGODB_COLLECTION_NAME_DEFAULT;
        }
    }
}
