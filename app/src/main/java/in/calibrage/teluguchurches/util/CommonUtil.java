package in.calibrage.teluguchurches.util;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import in.calibrage.teluguchurches.R;

/*
 * This is Common class to use same method in multiple places
 * these all are common methods created by developer
 * we need to import this class to use the data wherever needed
 */


public class CommonUtil {
    private static Date convertedDate;
    private static String outputDateStr, fileUrl, fileName, nameOFfile;
    private static boolean alertType = false, viewPdfVar;
    private static OnCartChangedListener onCartChangedListener;
    private static Context mContext;
    private static AlertDialog dialog;
    public static int fileSize;
    private static NotificationManager manager;
    public static MyCounter timer;
    public static boolean isShowNotifiction = false;

    /*
     *   formate the date as required
     * */
    public static String dateFormatUser(String inputDateStr) {
        if (inputDateStr == null || inputDateStr.equals(""))
            return "";
        DateFormat inputFormat = new SimpleDateFormat(inputDateStr.contains("-") ? "yyyy-MM-dd" : "MM/dd/yyyy");
        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd ");
        String strCurrentDate = inputDateStr;
        try {
            if (strCurrentDate != null) {
                convertedDate = inputFormat.parse(strCurrentDate);
                outputDateStr = outputFormat.format(convertedDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputDateStr;

    }

    /*
     *   formate the date as required
     * */
    public static String dateFormat(String inputDateStr) {
        if (inputDateStr == null || inputDateStr.equals(""))
            return "";
        DateFormat inputFormat = new SimpleDateFormat(inputDateStr.contains("-") ? "yyyy-MM-dd" : "MM/dd/yyyy");
        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd ");
        String strCurrentDate = inputDateStr;
        try {
            if (strCurrentDate != null) {
                convertedDate = inputFormat.parse(strCurrentDate);
                outputDateStr = outputFormat.format(convertedDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputDateStr;

    }

    /*
     *  formate the date as required
     * */
    public static String formatDateTimeUi(String fromStr) {
        String date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date newDate = format.parse(fromStr);
            format = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
            date = format.format(newDate);
            return date;
        } catch (Exception e) {
            return date;
        }
    }

    /*
     *   formate the time as required
     * */
    public static String formatTime(String fromStr) {
        String date = null;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        try {
            Date newDate = format.parse(fromStr);
            format = new SimpleDateFormat("hh:mm a");
            date = format.format(newDate);
            return date;
        } catch (Exception e) {
            return date;
        }
    }
    public static boolean ISLANG = true;
    /*
     *  check user permissions
     * */
    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    /*
     *  method to add multiple languages
     * */
    public static void updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.locale = locale;
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

    /*
     *  method to show alert dialog in pop-up
     * */
    public static boolean showAlertDialog(Context context, String title, String message, boolean isCancilable, int icon) {
        alertType = false;
        dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertType = true;
                        onCartChangedListener.setCartClickListener("ok");
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface forgotPasswordDialog, int which) {
                        // do nothing
                    }
                })
                .setCancelable(isCancilable)
                .setIcon(icon)
                .show();

        return alertType;
    }

    /*
     *  method to show alert dialog in pop-up with ok button
     * */
    public static boolean showAlertDialogOk(Context context, String title, String message, boolean isCancilable, int icon) {
        alertType = false;
        dialog = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertType = true;
                        onCartChangedListener.setCartClickListener("ok");
                    }
                })

                .setCancelable(isCancilable)
                .setIcon(icon)
                .show();

        return alertType;
    }

    /*
     * to check the phone is connected to Internet or Not
     * */
    public static boolean isNetworkAvailable(Context ctx) {

        try {
            ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            @SuppressLint("MissingPermission") NetworkInfo networkInfo = cm.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                return true;
            } else {
                Toast.makeText(ctx, "internet not available", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /*
     * interface between adapter and Class
     * */
    public interface OnCartChangedListener {
        void setCartClickListener(String status);

    }

    /*
     * interface between adapter and Class
     * */
    public static void setOnCartChangedListener(OnCartChangedListener mOnCartChangedListener) {
        onCartChangedListener = mOnCartChangedListener;

    }

    /*
     * downlod files
     * */
    public static void download(Context context, String url, String name, boolean viewVar) {
        viewPdfVar = viewVar;
        mContext = context;
        fileUrl = url;
        nameOFfile = name;
        fileName = name.replace(" ", "");
        new DownloadFile().execute(url, fileName);
    }

    /**
     * dowmload files
     */
    private static class DownloadFile extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... strings) {
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "Download");
            folder.mkdir();
            File pdfFile = new File(folder, fileName);

            try {

                if (!pdfFile.exists())
                    pdfFile.createNewFile();
                else {
                    pdfFile.createNewFile();
                    if (viewPdfVar)
                        isShowNotifiction = true;
                    view();
                    if (pdfFile.exists()) {
                        showServiceToast("Download completed..Please check downloads folder in your mobile");
                        //removeProgressNotification(mContext, 1);
                    }


                    return null;
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile, fileSize, mContext, nameOFfile);

            if (viewPdfVar)
                view();
            else {
                showServiceToast("Download completed..Please check downloads folder in your mobile");
                // removeProgressNotification(mContext, 1);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public static void removeProgressNotification(Context context, int id) {
        manager.cancel(id);
    }

    public static void showServiceToast(final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
            }
        });

    }

    /**
     * Download pdf and view the pdf
     */
    public static void view() {
        Uri path;
        File pdfFile = new File(Environment.getExternalStorageDirectory().getPath() + "/Download/" + fileName);  // -> filename = maven.pdf
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
            path = Uri.fromFile(pdfFile);
        else
            path = FileProvider.getUriForFile(mContext, "com.yiscustomerprovider.provider", pdfFile);

        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            pdfIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            mContext.startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            showServiceToast("No Application available to view PDF");

        }
    }

    /*
     * showing progressbar
     * */
    public static void showProgressNotification(Context context, int notificationId, String title, String message) {
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

        mBuilder.setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setTicker("")
                .setOngoing(true)
                .setProgress(100, FileDownloader.percentageValue, false);
        manager.notify(notificationId, mBuilder.build());
    }

    /*
     * customised toast
     * */
    public static void customToast(String title, Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_login, null);
        TextView dialogMessage = (TextView) view.findViewById(R.id.dialogMessage);
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        // toast.setGravity(Gravity.CENTER_VERTICAL, 10, 10);
        toast.setView(view);
        dialogMessage.setText(title);
        toast.show();
    }
}
