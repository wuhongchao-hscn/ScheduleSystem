package com.hitachi.schedule.controller.handler.gsac;

import com.hitachi.schedule.controller.actionform.GSACS020Form;
import com.hitachi.schedule.controller.checker.GSACS020Checker;
import com.hitachi.schedule.controller.common.CsvExportUtil;
import com.hitachi.schedule.controller.common.GCConstGlobals;
import com.hitachi.schedule.controller.common.SessionUtil;
import com.hitachi.schedule.controller.component.CommonUtil;
import com.hitachi.schedule.controller.exception.ErrorDownload;
import com.hitachi.schedule.controller.param.UserInfoList;
import com.hitachi.schedule.service.param.UserFindParam;
import com.hitachi.schedule.service.param.UserFindResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class GSACS020Handler {
    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private GSACS020Checker checker;

    private static final String RTN_STR_NG = "redirect:/GSACS020Display";
    private static final String RTN_STR_OK = "redirect:/GSACS020Display";


    @GetMapping("/GSACS020PageJump")
    public String GSACS020PageJump(
            HttpServletRequest request,
            @Nullable String param,
            @Nullable String index) {
        log.info("ページ遷移ボタンを押下しました。");
        int pageNo = 0;
        UserFindParam ufp = SessionUtil.getGsacSearchParam(request);

        if (!StringUtils.isEmpty(index)) {
            pageNo = Integer.parseInt(index);
        } else {
            switch (param) {
                case "first":
                    pageNo = 1;
                    break;
                case "pre":
                    pageNo = commonUtil.getPageNo(ufp.getStartNo()) - 1;
                    break;
                case "pro":
                    pageNo = commonUtil.getPageNo(ufp.getStartNo()) + 1;
                    break;
                case "last":
                    pageNo = ufp.getAllPage();
                    break;
                default:
            }
        }

        Integer startNo = (pageNo - 1) * GCConstGlobals.GSAA_PROP_GSACT020_DISPLAY_SIZE + 1;
        Integer endNo = startNo + GCConstGlobals.GSAA_PROP_GSACT020_DISPLAY_SIZE - 1;
        ufp.setStartNo(startNo);
        ufp.setEndNo(endNo);

        return RTN_STR_OK;
    }

    @RequestMapping("/GSACS020Delete")
    public String GSACS020Delete(
            String userId,
            @Valid GSACS020Form form,
            BindingResult br,
            RedirectAttributes redirectModel,
            HttpServletRequest request) {
        log.info("削除ボタンを押下しました。");

        if (checker.formCheck(redirectModel, form, br)) {
            doDeleteErrorRtnPre(request, form);
            return RTN_STR_NG;
        }

        String deleteUserId = form.getDeleteUserId();

        if (!StringUtils.isEmpty(userId)) {
            deleteUserId = userId;
        }

        List<String> deleteUserIdList = commonUtil.getListFromString(deleteUserId);
        String loginUserId = SessionUtil.getUserId(request);
        if (checker.gmCheck(redirectModel, form, deleteUserIdList, loginUserId)) {
            doDeleteErrorRtnPre(request, form);
            return RTN_STR_NG;
        }
        doDeleteErrorRtnPre(request, form, deleteUserId);
        return "redirect:/GSACS030Display";
    }

    private void doDeleteErrorRtnPre(HttpServletRequest request, GSACS020Form form, String... deleteUserId) {
        UserFindParam ufp = SessionUtil.getGsacSearchParam(request);
        ufp.setErrItemId(form.getErrItemId());
        ufp.setDeleteUserIdForm(form.getDeleteUserId());
        if (deleteUserId.length > 0) {
            ufp.setDeleteUserId(deleteUserId[0]);
        } else {
            ufp.setDeleteUserId(form.getDeleteUserId());
        }
        SessionUtil.setGsacSearchParam(request, ufp);
    }

    @RequestMapping("/GSACS020Detail")
    public String GSACS020Detail(
            HttpServletRequest request,
            String userId) {
        log.info("利用者リンクを押下しました。");

        SessionUtil.saveSessionValue(
                request,
                GCConstGlobals.GSAA_PROP_GSACT020_USER_ID,
                userId
        );
        return "redirect:/GSACS030Display";
    }

    @PostMapping
    @RequestMapping("/GSACS020Download")
    public void GSACS020Download(
            HttpServletRequest request,
            HttpServletResponse response) {
        log.info("CSV出力ボタンを押下しました。");
        UserFindResult ufr = SessionUtil.getGsacSearchResult(request);
        List<UserInfoList> userList = ufr.getUserList();

        List<List<String>> csvData = new ArrayList<>();
        for (UserInfoList uil : userList) {
            csvData.add(uil.toList());
        }

        try {
            CsvExportUtil.setProperties(GCConstGlobals.GSAA_PROP_GSACT010_CSV_NAME, response);
            CsvExportUtil.doExport(csvData, response.getOutputStream());
        } catch (Exception e) {
            throw new ErrorDownload();
        }
    }

    @PostMapping
    @RequestMapping("/GSACS020Back")
    public String GSACS020Back(HttpServletRequest request) {
        log.info("戻るボタンを押下しました。");
        SessionUtil.removeSessionValue(request,
                new String[]{
                        GCConstGlobals.GSAA_PROP_GSACT010_KNSK_KEKA,
                        GCConstGlobals.GSAA_PROP_GSACT020_USER_ID,
                        GCConstGlobals.GSAA_PROP_GSACT030_USER_KEKA,
                        GCConstGlobals.GSAA_PROP_GSAAT040_EX_KEY
                });
        return "redirect:/GSACS010Display";
    }
}
