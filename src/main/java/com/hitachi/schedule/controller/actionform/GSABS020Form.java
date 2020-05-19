package com.hitachi.schedule.controller.actionform;

import com.hitachi.schedule.controller.param.ArticleDetialInfo;
import com.hitachi.schedule.controller.param.TitleInfo;
import lombok.Data;

import java.util.List;

@Data
public class GSABS020Form extends BaseForm {
    private TitleInfo titleInfo;

    private long articleAllSize;

    private List<ArticleDetialInfo> articleList;
}
