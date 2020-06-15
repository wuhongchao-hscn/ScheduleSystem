package com.hitachi.schedule.config.common;

import com.hitachi.schedule.config.exception.ErrorTimeOut;
import com.hitachi.schedule.controller.param.UserDetialInfoList;
import com.hitachi.schedule.service.param.UserFindParam;
import com.hitachi.schedule.service.param.UserFindResult;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public class SessionUtil {
    public static Object getSessionValue(HttpServletRequest request, String key) {
        HttpSession session = request.getSession();
        return session.getAttribute(key);
    }

    public static void saveSessionValue(HttpServletRequest request, String key, Object value) {
        HttpSession session = request.getSession();
        session.setAttribute(key, value);
    }

    public static void clearSessionValue(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
    }

    public static String getSessionValueString(HttpServletRequest request, String key) {
        Object obj = getSessionValue(request, key);
        return StringUtils.isEmpty(obj) ? "" : obj.toString();
    }

    public static int getSessionValueInt(HttpServletRequest request, String key) {
        Object obj = getSessionValue(request, key);
        return StringUtils.isEmpty(obj) ? -1 : Integer.parseInt(obj.toString());
    }

    public static void removeSessionValue(HttpServletRequest request, List<String> keyList) {
        HttpSession session = request.getSession();
        for (String obj : keyList) {
            session.removeAttribute(obj);
        }
    }

    public static String getUserId(HttpServletRequest request) {
        Map<String, Object> gs_info = getUserDetial(request);
        return (String) gs_info.get("userId");
    }

    public static UserFindParam getGsacSearchParam(HttpServletRequest request) {
        Object obj = getSessionValue(request, GXConst.GSAA_PROP_GSACT010_KNSK_JUKN);
        return (UserFindParam) obj;
    }

    public static void setGsacSearchParam(HttpServletRequest request, UserFindParam ufp) {
        saveSessionValue(request, GXConst.GSAA_PROP_GSACT010_KNSK_JUKN, ufp);
    }

    public static UserFindResult getGsacSearchResult(HttpServletRequest request) {
        Object obj = getSessionValue(request, GXConst.GSAA_PROP_GSACT010_KNSK_KEKA);
        return StringUtils.isEmpty(obj) ? null : (UserFindResult) obj;
    }

    public static void setGsacSearchResult(HttpServletRequest request, UserFindResult ufr) {
        saveSessionValue(request, GXConst.GSAA_PROP_GSACT010_KNSK_KEKA, ufr);
    }

    @SuppressWarnings("unchecked")
    public static List<UserDetialInfoList> getGsacUserDetialList(HttpServletRequest request) {
        Object obj = getSessionValue(request, GXConst.GSAA_PROP_GSACT030_USER_KEKA);
        return (List<UserDetialInfoList>) obj;
    }

    public static void setGsacUserDetialList(HttpServletRequest request, List<UserDetialInfoList> udil) {
        saveSessionValue(request, GXConst.GSAA_PROP_GSACT030_USER_KEKA, udil);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> getUserDetial(HttpServletRequest request) {
        Object obj = getSessionValue(request, GXConst.GS_PROP_USER_INFO);
        if (StringUtils.isEmpty(obj)) {
            throw new ErrorTimeOut();
        }
        return (Map<String, Object>) obj;
    }

    public static void saveUserDetial(
            HttpServletRequest request,
            Map<String, Object> gs_info) {
        saveSessionValue(request, GXConst.GS_PROP_USER_INFO, gs_info);
    }

    public static boolean isUserDetialExist(HttpServletRequest request) {
        return null != getSessionValue(request, GXConst.GS_PROP_USER_INFO);
    }

    public static byte[] getUserImg(HttpServletRequest request) {
        Map<String, Object> gs_info = getUserDetial(request);
        return (byte[]) gs_info.get("userImg");
    }
}
