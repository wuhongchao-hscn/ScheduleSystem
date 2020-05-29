package com.hitachi.schedule.config.common;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
public class CsvExportUtil {

    private static final String CSV_COLUMN_SEPARATOR = ",";
    private static final String CSV_ROW_SEPARATOR = System.lineSeparator();

    public static void setProperties(String fileName, HttpServletResponse response) throws UnsupportedEncodingException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fn = fileName + sdf.format(new Date()) + ".csv";
        String utf = GCConstGlobals.GS_PROP_STORE_UTF8;
        response.setContentType("application/ms-txt.numberformat:@");
        response.setCharacterEncoding(utf);
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "max-age=30");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fn, utf));
    }

    public static void doExport(List<Map<String, Object>> dataList, String titles, String keys, OutputStream os) throws Exception {
        StringBuffer buf = new StringBuffer();
        String utf = GCConstGlobals.GS_PROP_STORE_UTF8;

        String[] titleArr = titles.split(",");
        String[] keyArr = keys.split(",");

        for (String title : titleArr) {
            buf.append(title).append(CSV_COLUMN_SEPARATOR);
        }
        buf.append(CSV_ROW_SEPARATOR);

        if (null != dataList && !dataList.isEmpty()) {
            for (Map<String, Object> data : dataList) {
                for (String key : keyArr) {
                    buf.append(data.get(key)).append(CSV_COLUMN_SEPARATOR);
                }
                buf.append(CSV_ROW_SEPARATOR);
            }
        }

        os.write(buf.toString().getBytes(utf));
        os.flush();
    }

    public static void doExport(List<List<String>> dataList, OutputStream os) throws Exception {
        String utf = GCConstGlobals.GS_PROP_STORE_UTF8;
        StringBuffer buf = new StringBuffer();
        if (null != dataList && !dataList.isEmpty()) {
            for (List<String> data : dataList) {
                buf.append(String.join(GCConstGlobals.GS_PROP_LIST_STRING_SPLIT_FUGO, data));
                buf.append(CSV_ROW_SEPARATOR);
            }
        }
        os.write(buf.toString().getBytes(utf));
        os.flush();
    }
}
