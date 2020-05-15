package com.hitachi.schedule.controller.common;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

@Slf4j
public class ImgGetUtil {

    public static void setProperties(HttpServletResponse response) {
        String utf = GCConstGlobals.GS_PROP_STORE_UTF8;
        response.setContentType("image/jpeg");
        response.setCharacterEncoding(utf);
    }

    public static void doExport(byte[] imgData, OutputStream os) throws Exception {
        InputStream in = new ByteArrayInputStream(imgData);
        int len = 0;
        byte[] buf = new byte[1024];
        while ((len = in.read(buf, 0, 1024)) != -1) {
            os.write(buf, 0, len);
        }
        os.flush();
        os.close();
    }

}
