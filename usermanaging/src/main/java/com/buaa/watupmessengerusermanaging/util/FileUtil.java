package com.buaa.watupmessengerusermanaging.util;

public class FileUtil {

    /**
     * 判断是否为图片
     * @param fileName
     * @return
     */
    public Boolean isImageFile(String fileName) {
        String[] img_type = new String[]{".jpg", ".jpeg", ".png", ".gif", ".bmp"};
        if (fileName == null) {
            return false;
        }
        fileName = fileName.toLowerCase();
        for (String type : img_type) {
            if (fileName.endsWith(type)) {
                return true;
            }
        }
        return false;
    }
    /**
     * 获取文件后缀名
     * @param fileName
     * @return
     */
    public String getFileType(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return "";
    }
}
