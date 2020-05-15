package com.hitachi.schedule.controller.handler;

import com.hitachi.schedule.controller.common.ImgGetUtil;
import com.hitachi.schedule.controller.common.SessionUtil;
import com.hitachi.schedule.controller.component.MessageReadUtil;
import com.hitachi.schedule.controller.exception.ErrorDownload;
import com.hitachi.schedule.controller.param.UserDetialInfoList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


@Controller
@Slf4j
public class CommonHandler {
    @Autowired
    private MessageReadUtil messageUtil;

    @Value("${web.default-image.path}")
    private String webDefaultImagePath;

    @RequestMapping("/commonLogoutSucess")
    public String commonLogoutSucess(
            HttpServletRequest request,
            RedirectAttributes redirectModel) {
        String loginMsg = messageUtil.getMessage("GSAXM009I");
        redirectModel.addFlashAttribute("warning", loginMsg);

        SessionUtil.clearSessionValue(request);
        return "redirect:/GSAXS010Display";
    }

    @RequestMapping("/commonReloginlink")
    public String commonReloginlink() {
        log.info("再ログインボタンを押下しました。");
        return "redirect:/GSAXS010Display";
    }


    @GetMapping("/commonUserDetail")
    public String commonUserDetail(HttpServletRequest request) {
        log.info("ユーザ情報リンクを押下しました。");

        String loginUserId = SessionUtil.getUserId(request);
        return "forward:/GSACS050Detail?userId=" + loginUserId;
    }

    @GetMapping("/commonGetImg")
    public void commonGetImg(
            HttpServletRequest request,
            String user_id,
            HttpServletResponse response) throws IOException {
        log.info("IMG取得ボタンを押下しました。");


        byte[] imgData = null;
        String loginUserId = SessionUtil.getUserId(request);
        if (loginUserId.equals(user_id)) {
            imgData = SessionUtil.getUserImg(request);
        } else {
            List<UserDetialInfoList> udil = SessionUtil.getGsacUserDetialList(request);
            for (UserDetialInfoList obj : udil) {
                if (user_id.equals(obj.getUserId())) {
                    imgData = obj.getImageSrc();
                    break;
                }
            }
        }
        if (null == imgData) {
            ClassPathResource classPathResource = new ClassPathResource(webDefaultImagePath);
            File f = classPathResource.getFile();
            imgData = Files.readAllBytes(Paths.get(f.getAbsolutePath()));
        }
        try {
            ImgGetUtil.setProperties(response);
            ImgGetUtil.doExport(imgData, response.getOutputStream());
        } catch (Exception e) {
            throw new ErrorDownload();
        }
    }
}
