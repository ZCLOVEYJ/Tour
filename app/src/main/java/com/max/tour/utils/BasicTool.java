package com.max.tour.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.DeviceUtils;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;


/**
 * @author liubp
 */
public class BasicTool {

    /**
     * 检测字符串是否为空，
     *
     * @param str
     * @return 是空 返回 <code>false</code> 否则返回 <code>true</code>
     */
    public static boolean isNotEmpty(CharSequence str) {
        if (null == str)
            return false;
        if ("".equals(str)) {
            return false;
        }
        if ((str.toString()).trim().equalsIgnoreCase("null"))
            return false;
        return (str.toString()).trim().length() > 0;
    }

    public static boolean isEmpty(CharSequence str) {
        return !isNotEmpty(str);
    }


    /**
     * 将单个list转成json字符串
     *
     * @param list
     * @return
     */
    public static String listToJsonString(List<Map<String, Object>> list)
            throws Exception {
        String jsonL = "";
        StringBuffer temp = new StringBuffer();
        temp.append("[");
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> m = list.get(i);
            if (i == list.size() - 1) {
                temp.append(mapToJsonObj(m));
            } else {
                temp.append(mapToJsonObj(m) + ",");
            }
        }
        if (temp.length() > 1) {
            temp = new StringBuffer(temp.substring(0, temp.length()));
        }
        temp.append("]");
        jsonL = temp.toString();
        return jsonL;
    }


    /**
     * 将map数据解析出来，并拼接成json字符串
     *
     * @param map
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static JSONObject mapToJsonObj(Map map) throws Exception {
        JSONObject json = null;
        StringBuffer temp = new StringBuffer();
        if (!map.isEmpty()) {
            temp.append("{");
            // 遍历map
            Set set = map.entrySet();
            Iterator i = set.iterator();
            while (i.hasNext()) {
                Map.Entry entry = (Map.Entry) i.next();
                String key = (String) entry.getKey();

                Object value = entry.getValue();

                temp.append("\"" + key + "\":");

                if (null == value || "".equals(value)) {
                    temp.append("\"\"" + ", ");
                } else if (value instanceof Map<?, ?>) {
                    temp.append(mapToJsonObj((Map<String, Object>) value) + ",");
                } else if (value instanceof List<?>) {
                    temp.append(listToJsonString((List<Map<String, Object>>) value)
                            + ",");
                } else if (value instanceof String) {
                    temp.append("\"" + String.valueOf(value) + "\",");
                } else {
                    temp.append(String.valueOf(value) + ",");
                }

            }
            if (temp.length() > 1) {
                String mString = temp.toString();
                mString = mString.trim();

                temp = new StringBuffer(mString.substring(0,
                        mString.length() - 1));
            }

            temp.append("}");

            json = new JSONObject(temp.toString());
        }
        return json;
    }


    public static Drawable getDrawable(String urlpath) {
        Drawable d = null;
        try {
            URL url = new URL(urlpath);
            URLConnection conn = url.openConnection();
            conn.connect();
            InputStream in;
            in = conn.getInputStream();
            d = Drawable.createFromStream(in, "abc.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return d;
    }


    /**
     * 获取字符串，过滤空、NULL
     *
     * @param object
     * @return
     */
    public static String valueOf(Object object) {
        String str = String.valueOf(object);
        if (isNotEmpty(str)) {
            return str;
        } else {
            return "";
        }
    }


    /**
     * @param first
     * @param content 给content这个内容设置颜色color,且字体大小设置为spValue
     * @param end
     * @param color
     * @return
     */
    public static SpannableString setTextPartSizeColor(Context context, String first, String content, String end, int color, float spValue) {
        SpannableString spannableSale = new SpannableString(first + content + end);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        spannableSale.setSpan(colorSpan, first.length(), first.length() + content.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableSale.setSpan(new AbsoluteSizeSpan(ConvertUtils.sp2px(spValue)), first.length(), first.length() + content.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableSale;
    }

    public static SpannableString setTextPartSizeColorBold(Context context, String first, String content, String end, int color, float spValue) {
        SpannableString spannableSale = new SpannableString(first + content + end);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        spannableSale.setSpan(colorSpan, first.length(), first.length() + content.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableSale.setSpan(boldSpan, first.length(), first.length() + content.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableSale.setSpan(new AbsoluteSizeSpan(ConvertUtils.sp2px(spValue)), first.length(), first.length() + content.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableSale;
    }

    public static SpannableString setTextPartSizeColor(Context context, String content1, String content2, String content3, String content4, int color, float spValue) {
        SpannableString spannableSale = new SpannableString(content1 + content2 + content3 + content4);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(color);
        spannableSale.setSpan(colorSpan, 0, content1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableSale.setSpan(colorSpan2, content1.length() + content2.length(), content1.length() + content2.length() + content3.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableSale;
    }


    /**
     * 修改TextView 的属性 drawableRight
     *
     * @param mContext 上下文
     * @param textView 要修改的控件
     * @param Id       要修改成的资源文件名
     */
    public static void setChangeTextRightDrawalbe(Context mContext, TextView textView, @DrawableRes int Id) {
        if (Id == 0) {
            textView.setCompoundDrawables(null, null, null, null);
        } else {
            Drawable rightDrawable = mContext.getResources().getDrawable(Id);
            rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
            textView.setCompoundDrawables(null, null, rightDrawable, null);

        }
    }


    /**
     * 强行弹出软键盘
     */
    public static void showInput(final EditText mEditText) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() { //让软键盘延时弹出，以更好的加载Activity

            public void run() {
                InputMethodManager inputManager =
                        (InputMethodManager) mEditText.getContext().
                                getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.showSoftInput(mEditText, 0);
            }

        }, 200);
    }


    /**
     * 网络图片Url 转 Bitmap
     *
     * @param url
     * @return
     */
    public static Bitmap getBitmap(String url) {
        Bitmap bm = null;
        try {
            URL iconUrl = new URL(url);
            URLConnection conn = iconUrl.openConnection();
            HttpURLConnection http = (HttpURLConnection) conn;

            int length = http.getContentLength();

            conn.connect();
            // 获得图像的字符流
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is, length);

            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();// 关闭流
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }


    //scanRecords的格式转换
    static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }


    /**
     * 获取手机唯一串号IMEI
     *
     * @param context
     * @return imei
     */
    @SuppressLint("MissingPermission")
    public static String getIMEI(Context context) {
        return DeviceUtils.getUniqueDeviceId();

    }

    /**
     * 获取手机唯一标识
     *
     * @return
     */
    public static String getIMEI() {
        return DeviceUtils.getUniqueDeviceId();

    }


    /**
     * 给一段文字设置点击事件
     *
     * @param content
     * @param start
     * @param end
     * @param color
     * @return
     */
    public static SpannableString getClickableSpan(String content, int start, int end, int color, final View.OnClickListener onClickListener) {
        SpannableString spanableInfo = new SpannableString(content);
        spanableInfo.setSpan(
                new ClickableColorSpan(color) {
                    @Override
                    public void onClick(View widget) {
                        onClickListener.onClick(widget);
                    }
                }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE

        );
        return spanableInfo;
    }


    /**
     * 质量压缩方法(32k)
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage32(Bitmap image) {
        if (image == null) {
            Log.d("BasicTool", "image========null");
            return null;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;

        while (baos.toByteArray().length / 1024 > 32) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }


    //数据不能为null
    public static String notNull(String content) {
        if (content == null) {
            return "";
        }
        return content;
    }

    /**
     * 动态设置MaginRight
     */
    public static void setMaginLR(TextView textView, int left, int right) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) textView.getLayoutParams();
        lp.setMargins(left, 0, right, 0);
        textView.setLayoutParams(lp);
    }

    public static void setMaginLR(View textView, int left, int top,int right,int bottom) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) textView.getLayoutParams();
        lp.setMargins(left, top, right, bottom);
        textView.setLayoutParams(lp);
    }

    public static void setMaginLR(ImageView textView, int left, int top, int right, int bottom) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) textView.getLayoutParams();
        lp.setMargins(left, top, right, bottom);
        textView.setLayoutParams(lp);
    }

    public static void setMaginLR(View view, int left, int right) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
        lp.setMargins(left, 0, right, 0);
        view.setLayoutParams(lp);
    }



}