package com.hitachi.schedule.controller.actionform;

import com.hitachi.schedule.controller.param.ArticleDetialInfo;
import lombok.Data;

import java.util.List;

@Data
public class GSABS010Form extends BaseForm {
    private List<ArticleDetialInfo> articleList;
}
