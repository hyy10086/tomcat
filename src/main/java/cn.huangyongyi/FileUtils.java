package cn.huangyongyi;

import java.io.*;

/**
 * @author huangyongyi.
 * @date 2018/4/14
 * @desc:读取静态资源
 */
public class FileUtils {

    public static String getFileByPath(String path) {
        StringBuffer sb = new StringBuffer();
        FileReader fileReader = null;
        BufferedReader bufferedReader=null;
        try {

            fileReader = new FileReader(path);
            bufferedReader = new BufferedReader(fileReader);
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }finally {
            try {
                fileReader.close();
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
