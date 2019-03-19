package cn.kk20.lib.util;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 文件工具类
 */
public class UtilFile {

    /**
     * 检验SDCard状态
     */
    public static boolean checkSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SDCard的目录路径功能
     */
    public static String getSDCardPath() {
        String path = checkSDCard() ? Environment.getExternalStorageDirectory().getAbsolutePath()
                : Environment.getDataDirectory().getAbsolutePath();
        if (path.endsWith(File.separator)) {
            return path;
        }
        return path + File.separator;
    }

    /**
     * 获取临时文件路径
     *
     * @param context
     * @param fileName
     * @return 应用目录temp文件夹下文件
     */
    public static String createTempFile(Context context, String fileName) {
        String rootPath = getSDCardPath();
        String tempPath = checkSDCard() ? rootPath + context.getPackageName() + File.separator
                : rootPath;
        tempPath += "temp";
        if (fileName.startsWith(File.separator)) {
            tempPath += fileName;
        } else {
            tempPath += File.separator + fileName;
        }

        File tempFile = new File(tempPath);
        File dir = tempFile.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }

        if (!tempFile.exists()) {
            try {
                tempFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return tempFile.getAbsolutePath();
    }

    /**
     * 按名称创建目录
     *
     * @param filePath
     * @return 文件夹完整路径
     */
    public static String mkdir2SDCard(@NonNull String filePath) {
        String rootPath = getSDCardPath();
        String dirPath;
        if (filePath.startsWith(File.separator)) {
            dirPath = rootPath + filePath.substring(1);
        } else {
            dirPath = rootPath + filePath;
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getAbsolutePath();
    }

    /**
     * 保存文件数据到SD卡
     *
     * @param filePath
     * @param fileName
     * @param content
     * @throws Exception
     */
    public static void save2SDCard(String filePath, String fileName, String content)
            throws Exception {
        // 从API中获取SDCard的路径，解决各种Android系统的兼容性问题
        String path = mkdir2SDCard(filePath);
        // 创建文件
        File file = new File(path, fileName);
        // 新建一个文件的输出流
        FileOutputStream outStream = new FileOutputStream(file);
        outStream.write(content.getBytes());
        // 关掉这个流
        outStream.close();
    }

    /**
     * 保存数据到指定文件
     *
     * @param context
     * @param fileName 文件名（全路径）
     * @param content
     * @throws Exception
     */
    public static void save(Context context, String fileName, String content) throws Exception {
        // 利用javaIO实现文件的保存
        FileOutputStream outStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        // 向文件中写入数据，将字符串转换为字节
        outStream.write(content.getBytes());
        outStream.close();
    }

    /**
     * 解压文件到指定目录
     *
     * @param zipPath
     * @param descDir
     */
    public static boolean unZipFile(String zipPath, String descDir) throws IOException {
        File zipFile = new File(zipPath);
        if (!zipFile.exists()) {
            System.out.println("压缩文件不存在");
            return false;
        }

        File pathFile = new File(descDir);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        ZipFile zip = new ZipFile(zipFile);
        for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = zip.getInputStream(entry);
            String outPath = (descDir + zipEntryName).replaceAll("\\*", "/");
            // 判断路径是否存在,不存在则创建文件路径
            File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
            if (!file.exists()) {
                file.mkdirs();
            }
            // 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
            if (new File(outPath).isDirectory()) {
                continue;
            }
            //输出文件路径信息
            System.out.println(outPath);

            OutputStream out = new FileOutputStream(outPath);
            byte[] buf1 = new byte[1024];
            int len;
            while ((len = in.read(buf1)) > 0) {
                out.write(buf1, 0, len);
            }
            in.close();
            out.close();
        }
        System.out.println("解压完毕");
        return true;
    }
}
