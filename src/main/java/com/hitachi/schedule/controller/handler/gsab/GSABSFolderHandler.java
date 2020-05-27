package com.hitachi.schedule.controller.handler.gsab;

import com.hitachi.schedule.controller.common.SessionUtil;
import com.hitachi.schedule.jpa.entity.Folder;
import com.hitachi.schedule.service.GSABSScheduleF;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


@Controller
@Slf4j
public class GSABSFolderHandler {
    @Autowired
    private GSABSScheduleF gsabsService;

    @GetMapping("/GSABSFolder")
    @ResponseBody
    public void GSABSFolder(
            HttpServletRequest request,
            String title,
            String content,
            boolean level) {
        String loginUserId = SessionUtil.getUserId(request);

        Folder folder = new Folder();
        folder.setTitle(title);
        folder.setContent(content);
        folder.setLevel(level);
        folder.setUpdateId(loginUserId);

        gsabsService.insertFolder(folder);
    }


}
