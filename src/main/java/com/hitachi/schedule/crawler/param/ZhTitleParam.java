package com.hitachi.schedule.crawler.param;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ZhTitleParam {

    private Long titleId;

    private String titleName;

    private String titleContent;

    private List<ZhArticleParam> articleParams = new ArrayList<>();
}
