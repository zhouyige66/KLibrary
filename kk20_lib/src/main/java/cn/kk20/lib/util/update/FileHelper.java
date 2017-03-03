package cn.kk20.lib.util.update;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;

public class FileHelper {
    private Context context;

    public FileHelper(Context context) {
        this.context = context;
    }

    /**
     * 打开文件
     *
     * @param filePath
     */
    public void openFile(String filePath) {
        try {
            Intent intent = new Intent();
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);

            File file = new File(filePath);
            // 获取文件file的MIME类型
            String type = getMIMEType(file);
            // 设置intent的data和Type属性。
            intent.setDataAndType(Uri.fromFile(file), type);

            // 跳转
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context,
                    "请安装可打开后缀名“" + getFileType(filePath) + "”的应用！",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void openFile(String filePath, String fileMIMEType) {
        if (fileMIMEType.equals("application/octet-stream")) {
            openFile(filePath);
            return;
        }
        try {
            Intent intent = new Intent();
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);

            // 设置intent的data和Type属性。
            intent.setDataAndType(Uri.fromFile(new File(filePath)),
                    fileMIMEType);

            // 跳转
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context,
                    "请安装可打开后缀名“" + getFileType(filePath) + "”的应用！",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void openFile(File file) {
        try {
            Intent intent = new Intent();

            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);

            // 获取文件file的MIME类型
            String type = getMIMEType(file);
            // 设置intent的data和Type属性。
            intent.setDataAndType(Uri.fromFile(file), type);

            // 跳转
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context,
                    "请安装可打开后缀名“" + getFileType(file.getName()) + "”的应用！",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void openFile(File file, String fileMIMEType) {
        if (fileMIMEType.equals("application/octet-stream")) {
            openFile(file);
            return;
        }
        try {
            Intent intent = new Intent();

            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);

            // 设置intent的data和Type属性。
            intent.setDataAndType(Uri.fromFile(file), fileMIMEType);

            // 跳转
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context,
                    "请安装可打开后缀名“" + getFileType(file.getName()) + "”的应用！",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void update(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    private String getFileType(String fName) {
        String type = "无后缀";
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
        String end = fName.substring(dotIndex + 1, fName.length())
                .toLowerCase();
        if (end.equals(""))
            return type;
        return end;
    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param file
     */
    @SuppressLint("DefaultLocale")
    public String getMIMEType(File file) {
        String type = "*/*";
        String fName = file.getName();

        // 获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }

		/* 获取文件的后缀名 */
        String end = fName.substring(dotIndex + 1, fName.length())
                .toLowerCase();
        if (end == "")
            return type;
        // 在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MimeUtility.MIME_TYPE_BY_EXTENSION_MAP.length; i++) {
            if (end.equals(MimeUtility.MIME_TYPE_BY_EXTENSION_MAP[i][0]))
                type = MimeUtility.MIME_TYPE_BY_EXTENSION_MAP[i][1];
        }
        return type;
    }

    /**
     * 从assets文件夹中读取文件
     *
     * @param fileName
     * @return
     */
    public String getFromAssets(String fileName) {
        String result = "";
        try {
            InputStream in = context.getResources().getAssets().open(fileName);
            int lenght = in.available();
            byte[] buffer = new byte[lenght];
            in.read(buffer);

            result = new String(buffer, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
