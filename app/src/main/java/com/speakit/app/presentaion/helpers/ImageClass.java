package com.speakit.app.presentaion.helpers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public abstract class ImageClass {
    static int FileNumber = 0;
    static Uri outputFileUri;
    private static Bitmap resizedImage(Activity activity, Uri uri, String Path)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(Path, options);
        if(options.outHeight==0)
        {
            try {
                InputStream in = activity.getContentResolver().openInputStream(
                        uri);
                BitmapFactory.decodeStream(in, null, options);
            } catch (FileNotFoundException e) {
                // do something
            }
        }
        float maxWidth = 500;
        float maxHeight = 500;
        float actualWidth = options.outWidth;
        float actualHeight = options.outHeight;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                //adjust width according to maxHeight
                imgRatio = maxHeight / actualHeight;
                actualWidth = imgRatio * actualWidth;
                actualHeight = maxHeight;
            } else if (imgRatio > maxRatio) {
                //adjust height according to maxWidth
                imgRatio = maxWidth / actualWidth;
                actualHeight = imgRatio * actualHeight;
                actualWidth = maxWidth;
            } else {
                actualHeight = maxHeight;
                actualWidth = maxWidth;
            }
        }
        try {
            return Picasso.with(activity).load(uri).resize((int) actualWidth, (int) actualHeight)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void pickFromGallery(Activity activity)
    {
        if (externalStoragePermission(activity)) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
        }
        else
        {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},123);
        }

    }
    private static boolean externalStoragePermission(Activity activity)
    {
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            return false;

        } else {
            return true;
        }
    }
    private static boolean cameraPermission(Activity activity)
    {
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            return false;

        } else {
            return true;
        }
    }
    private static String getRelaPath(Intent data, Activity activity)
    {
        String realPath;
        if (Build.VERSION.SDK_INT < 11)
            realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(activity, data.getData());
        else
        if (Build.VERSION.SDK_INT < 19)
            realPath = RealPathUtil.getRealPathFromURI_API11to18(activity, data.getData());
        else
            realPath = RealPathUtil.getRealPathFromURI_API19(activity, data.getData());
        return realPath;
    }
    public static void captureImage(Activity activity)
    {
        if(handleCameraPermissions(activity)) {
            Random r = new Random();
            FileNumber = r.nextInt(999999 - 1) + 1;
            File file = new File(Environment.getExternalStorageDirectory(), "DCIM/" + FileNumber + "citybookers.jpg");
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                outputFileUri = Uri.fromFile(file);
            } else {
                outputFileUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".citybookers", file);
            }
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            activity.startActivityForResult(intent, 2);
        }
    }
    private static Bitmap resizedGalleryImage(Intent data, Activity activity)
    {   String realPath;
        try {
            realPath = getRelaPath(data,activity);
        }catch (Exception e)
        {
            realPath = GetFilePathFromDevice.getPath(activity,data.getData());
        }
        Uri uri =  Uri.fromFile(new File(realPath));
        return resizedImage(activity,uri,realPath);
    }

    private static Bitmap resizedCameraImage(Activity activity)
    {
        return resizedImage(activity,outputFileUri,outputFileUri.getPath());
    }
    public static class CreateImageAsync extends AsyncTask<String,Void,Bitmap>
    {
        Context context;
        public CreateImageAsync(Context context){
            this.context = context;
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                return Picasso.with(context).load(params[0]).resize(500,500)
                        .get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {

/*
                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

                RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.custom_notification);
                remoteViews.setImageViewBitmap(R.id.notif_imageView,bitmap);
                remoteViews.setTextViewText(R.id.notif_title,"title");
                remoteViews.setTextViewText(R.id.notif_subtitle,"subtitle");

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                builder.setSmallIcon(R.drawable.ic_launcher)
                        .setAutoCancel(false)
                        .setCustomBigContentView(remoteViews)
                        .setSound(defaultSoundUri) ;

                notificationManager.notify(0,builder.build());
*/
            }
        }
    }
    private static class CameraAsync extends AsyncTask<Activity,Void,Bitmap>
    {
        @Override
        protected Bitmap doInBackground(Activity... params) {
            return resizedCameraImage(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            EventBus.getDefault().post(new ImageModel(bitmap));
        }
    }
    private static class GalleryAsync extends AsyncTask<GalleryAsyncParams,Void,Bitmap>
    {
        @Override
        protected Bitmap doInBackground(GalleryAsyncParams... params) {
            return resizedGalleryImage(params[0].data,params[0].activity);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            EventBus.getDefault().post(new ImageModel(bitmap));
        }
    }
    private static class GalleryAsyncParams
    {
        Activity activity;
        Intent data;

        public GalleryAsyncParams(Activity activity, Intent data) {
            this.activity = activity;
            this.data = data;
        }
    }
    public static void receiveGalleryImage(Activity activity, Intent data)
    {
        GalleryAsync galleryAsync = new GalleryAsync();
        galleryAsync.execute(new GalleryAsyncParams(activity,data));
    }
    public static void receiveCameraImage(Activity activity)
    {
        CameraAsync cameraAsync = new CameraAsync();
        cameraAsync.execute(activity);
    }
    private static boolean handleCameraPermissions(Activity activity)
    {
        ArrayList<String> strings = new ArrayList<>();
        boolean flag = true;
        if(!cameraPermission(activity))
        {
            strings.add(Manifest.permission.CAMERA);
            flag = false;
        }
        if(!externalStoragePermission(activity))
        {
            strings.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            flag = false;
        }
        if(flag)
        {
            return true;
        }else
        {
            String permissions[] = new String[strings.size()];
            for(int i = 0;i<strings.size();i++)
            {
                permissions[i] = strings.get(i);
            }
            ActivityCompat.requestPermissions(activity,
                    permissions,
                    123);
            return false;
        }
    }
    private static abstract class RealPathUtil
    {

        public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
        public static void showDialog(final String msg, final Context context,
                                      final String permission) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
            alertBuilder.setCancelable(true);
            alertBuilder.setTitle("Permission necessary");
            alertBuilder.setMessage(msg + " permission is necessary");
            alertBuilder.setPositiveButton(android.R.string.yes,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context,
                                    new String[] { permission },
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
            AlertDialog alert = alertBuilder.create();
            alert.show();
        }
        public static boolean checkPermissionREAD_EXTERNAL_STORAGE(final Context context) {
            int currentAPIVersion = Build.VERSION.SDK_INT;
            if (currentAPIVersion >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            (Activity) context,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        showDialog("External storage", context,
                                Manifest.permission.READ_EXTERNAL_STORAGE);

                    } else {
                        ActivityCompat
                                .requestPermissions(
                                        (Activity) context,
                                        new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                    return false;
                } else {
                    return true;
                }

            } else {
                return true;
            }
        }

        @SuppressLint("NewApi")
        public static String getRealPathFromURI_API19(Context context, Uri uri){
            if (checkPermissionREAD_EXTERNAL_STORAGE(context)) {

                String filePath = "";
                String wholeID = DocumentsContract.getDocumentId(uri);

                // Split at colon, use second item in the array
                String id = wholeID.split(":")[1];

                String[] column = {MediaStore.Images.Media.DATA};

                // where id is equal to
                String sel = MediaStore.Images.Media._ID + "=?";

                Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{id}, null);

                int columnIndex = cursor.getColumnIndex(column[0]);

                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(columnIndex);
                }
                cursor.close();
                return filePath;
            }
            return null ;
        }


        @SuppressLint("NewApi")
        public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
            String[] proj = { MediaStore.Images.Media.DATA };
            String result = null;

            CursorLoader cursorLoader = new CursorLoader(
                    context,
                    contentUri, proj, null, null, null);
            Cursor cursor = cursorLoader.loadInBackground();

            if(cursor != null){
                int column_index =
                        cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                result = cursor.getString(column_index);
            }
            return result;
        }

        public static String getRealPathFromURI_BelowAPI11(Context context, Uri contentUri){
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index
                    = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }

    }
    public static class ImageModel {
        Bitmap bitmap;

        public ImageModel(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }
    }
    private static class GetFilePathFromDevice {

        public static String getPath(final Context context, final Uri uri) {
            final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                }
                else if (isDownloadsDocument(uri)) {
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                    return getDataColumn(context, contentUri, null, null);
                }
                else if (isMediaDocument(uri)) {
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
            }
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                if (isGooglePhotosUri(uri))
                    return uri.getLastPathSegment();
                return getDataColumn(context, uri, null, null);
            }
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
            return null;
        }

        public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
            Cursor cursor = null;
            final String column = "_data";
            final String[] projection = {column};
            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
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

        public static boolean isExternalStorageDocument(Uri uri) {
            return "com.android.externalstorage.documents".equals(uri.getAuthority());
        }

        public static boolean isDownloadsDocument(Uri uri) {
            return "com.android.providers.downloads.documents".equals(uri.getAuthority());
        }

        public static boolean isMediaDocument(Uri uri) {
            return "com.android.providers.media.documents".equals(uri.getAuthority());
        }

        public static boolean isGooglePhotosUri(Uri uri) {
            return "com.google.android.apps.photos.content".equals(uri.getAuthority());
        }
    }
}