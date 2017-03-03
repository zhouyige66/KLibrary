package cn.kk20.lib.util;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Propertiees工具类
 */
public class IPropertieesUtils {
    static final String CONFIG_FILE = "config";	//文件名
    private Context mContext;

    public IPropertieesUtils(Context context){
        this.mContext = context;
    }

    public Properties read() throws IOException {
        createFile();
        Properties config = new Properties();
        FileInputStream fis = mContext.openFileInput(CONFIG_FILE);
        config.load(fis);
        fis.close();
        return config;
    }

    public void write(Properties config) throws IOException{
        createFile();
        FileOutputStream fos = mContext.openFileOutput(CONFIG_FILE, Context.MODE_PRIVATE);
        config.store(fos, null);
        fos.close();
    }

    void createFile() throws IOException{
        File file = new File(mContext.getFilesDir(),CONFIG_FILE);
        if (!file.exists()) {
            file.createNewFile();
        }
    }
}
