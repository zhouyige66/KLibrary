package cn.kk20.lib.util;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import java.io.File;
import java.util.UUID;

import cn.kk20.lib.BuildConfig;


/**
 * @Description
 * @Author kk20
 * @Date 2017/11/8
 * @Version V1.0.0
 */
public class UtilIntent {

    public static File createTempFile(Context context, String dirPath, String fileName) {
        String path = (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                ? Environment.getExternalStorageDirectory().getAbsolutePath()
                : Environment.getDataDirectory().getAbsolutePath()) + "/" + context.getPackageName();
        if (TextUtils.isEmpty(dirPath)) {
            path += "/temp";
        } else {
            if (dirPath.startsWith("/")) {
                path += dirPath;
            } else {
                path += "/" + dirPath;
            }
        }
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, fileName);
        return file;
    }

    private static Uri createUri(Context context, File file) {
        Uri uri = FileProvider.getUriForFile(context,
                BuildConfig.APPLICATION_ID + ".FileProvider", file);
        return uri;
    }

    public static Intent createCameraIntent(Context context) {
        String fileName = UUID.randomUUID().toString() + ".jpg";
        return createCameraIntent(context, fileName);
    }

    public static Intent createCameraIntent(Context context, String fileName) {
        return createCameraIntent(context, null, fileName);
    }

    public static Intent createCameraIntent(Context context, String dirPath, String fileName) {
        File file = createTempFile(context, dirPath, fileName);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri fileUri = null;
        // 判断是否是7.0
        if (Build.VERSION.SDK_INT >= 24) {
            // 适配android7.0 ，不能直接访问原路径，需要对intent 授权
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileUri = createUri(context, file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        intent.putExtra("filePath", file.getAbsolutePath());
        return intent;
    }

    public static Intent createCropIntent(Context context, Uri originUri, String originFilePath) {
        Uri uri = getImageContentUri(context, new File(originFilePath));
        //设置裁剪后图片
        int endIndex = originFilePath.lastIndexOf(".");
        String fileType = originFilePath.substring(endIndex);
        String cropFilePath = originFilePath.substring(0, endIndex) + "_crop" + fileType;
        File cropFile = new File(cropFilePath);
        // 裁剪之后，保存在裁剪文件中，关键
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cropFile));// 返回的uri开头为file://
        intent.putExtra("filePath", cropFile.getAbsolutePath());
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", false);
        intent.putExtra("crop", "true");
        intent.putExtra("scale", false);//是否保留比例
//        // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        // outputX outputY 是裁剪图片宽高
//        intent.putExtra("outputX", 150);
//        intent.putExtra("outputY", 150);
        return intent;
    }

    public static Intent createChooseImgIntent() {
        // 调用图库
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        // 调用图片选择器
//        Intent intent = new Intent(Intent.ACTION_PICK, null);
//        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        return intent;
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public static Intent handleImageIntent(Context context, Intent data) {
        Uri uri = data.getData();
        String filePath = null;
        if (Build.VERSION.SDK_INT >= 19) {// 4.4及以上系统使用这个方法处理图片
            filePath = handleImageOnKitKat(context, uri);
        } else {
            filePath = handleImageBeforeKitKat(context, uri);
        }
        Intent intent = new Intent();
        intent.putExtra("fileUri", uri);
        intent.putExtra("filePath", filePath);
        return intent;
    }

    private static String handleImageBeforeKitKat(Context context, Uri uri) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = context.getContentResolver().query(uri, null, null,
                null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 4.4及以上获取图片绝对路径的方法
     *
     * @param context
     * @param uri
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String handleImageOnKitKat(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {// ExternalStorageProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                } else {
                    String id = split[1]; // 解析出数字格式的id
                    String selection = MediaStore.Images.Media._ID + "=" + id;
                    // 通过Uri和selection来获取真实的图片路径
                    String filePath = null;
                    Cursor cursor = context.getContentResolver().query(uri, null,
                            selection, null, null);
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        }
                        cursor.close();
                    }
                    return filePath;
                }
            } else if (isDownloadsDocument(uri)) {// DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {// MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {// MediaStore (and general)
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) { // File
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

}
