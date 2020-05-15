package com.hitachi.schedule.controller.configuration;

import com.hitachi.schedule.controller.param.NavInfo;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Configuration
@PropertySource(value = "classpath:public/gamenInfo.properties", encoding = "UTF-8")
public class GamenInfoConfig implements EnvironmentAware {
    private static Environment env;

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }

    public static String getScreenNameById(String id) {
        return env.getProperty(id);
    }

    public static List<NavInfo> getNavInfoListById(String id) {
        List<NavInfo> navInfoList = new ArrayList<>();
        getNavInfoListById(id, navInfoList);
        if (navInfoList.isEmpty()) {
            return null;
        }
        return navInfoList;
    }

    private static void getNavInfoListById(String id, List<NavInfo> navInfoList) {
        String parentGamen = env.getProperty(id.concat(".parent"));
        if (!StringUtils.isEmpty(parentGamen)) {
            NavInfo navInfo = new NavInfo("/".concat(parentGamen).concat("Display"), parentGamen);
            navInfoList.add(navInfo);
            getNavInfoListById(parentGamen, navInfoList);
        }
        return;
    }

}