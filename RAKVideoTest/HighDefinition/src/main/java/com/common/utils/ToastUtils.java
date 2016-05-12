package com.common.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jean.rakvideotest.R;


/**
 * Created by t00284576 on 2015/10/27.
 */
public class ToastUtils extends Toast {
    public ToastUtils(Context context) {
        super(context);

    }

    @Override
    public void show() {
        super.show();

    }

    /**
     * 获取控件实例,发送成功等调用
     *
     * @param context
     * @param text
     *            提示消息
     * @return
     */
    public static ToastUtils makeText(Context context, CharSequence text) {
        ToastUtils result = new ToastUtils(context);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = inflate.inflate(R.layout.new_data_toast, null);
        v.setMinimumWidth(dm.widthPixels - 80);// 设置控件最小宽度为手机屏幕宽度

        TextView tv = (TextView) v.findViewById(R.id.new_data_toast_message);
        tv.setText(text);

        result.setView(v);
        result.setDuration(Toast.LENGTH_LONG);
        result.setGravity(Gravity.BOTTOM, 0, (int) (dm.density * 75));
        // result.setGravity(Gravity.TOP, 0, (int)(dm.heightPixels-250));
        return result;
    }

    /**
     * 用途 显示提示
     *
     * @param context
     * @param sourceId
     *            文本的资源Id
     * @return
     */
    public static ToastUtils makeText(Context context, int sourceId) {
        return makeselfText(context, context.getResources().getString(sourceId), null);
    }

    public static void showText(Context context, int sourceId) {
        makeselfText(context, context.getResources().getString(sourceId), null).show();
    }

    public static void showSuccessText(Context context, int sourceId) {
        makeselfText(context, context.getResources().getString(sourceId), R.drawable.toast_success).show();
    }

    public static void showFailText(Context context, int sourceId) {
        makefailText(context, context.getResources().getString(sourceId)).show();
    }

    public static void showFailText(Context context, String str, int length) {
        makefailText(context, str, length).show();
    }

    /**
     * 失败页面调用的
     *
     * @param context
     * @param text
     *            提示消息
     * @return
     */
    public static ToastUtils makefailText(Context context, CharSequence text) {
        return makefailText(context, text, 600);
    }

    public static ToastUtils makefailText(Context context, CharSequence text, int length) {
        ToastUtils result = new ToastUtils(context);

        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();

        View v = inflate.inflate(R.layout.new_data_toast, null);
        v.setMinimumWidth(dm.widthPixels - 80);// 设置控件最小宽度为手机屏幕宽度

        TextView tv = (TextView) v.findViewById(R.id.new_data_toast_message);
        tv.setText(text);

        ImageView iconImageView = (ImageView) v.findViewById(R.id.imageView1);
        iconImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.toastfailed));

        result.setView(v);
        result.setDuration(length);
        result.setGravity(Gravity.BOTTOM, 0, (int) (dm.density * 75));
        // result.setGravity(Gravity.TOP, 0, (int)(dm.heightPixels-250));
        return result;
    }

    /**
     * 自定义图标的调用
     *
     * @param context
     * @param text
     *            文本内容
     * @param id
     *            图片ID;eg: R.drawable.toastfailed
     * @return
     */
    public static ToastUtils makeselfText(Context context, CharSequence text, Integer id) {
        ToastUtils result = new ToastUtils(context);

        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();

        View v = inflate.inflate(R.layout.new_data_toast, null);
        v.setMinimumWidth(dm.widthPixels - 80);// 设置控件最小宽度为手机屏幕宽度

        TextView tv = (TextView) v.findViewById(R.id.new_data_toast_message);
        tv.setText(text);

        ImageView iconImageView = (ImageView) v.findViewById(R.id.imageView1);
        iconImageView.setVisibility(View.GONE);
        if (!GeneralUtils.isEmpty(id)) {
            iconImageView.setImageDrawable(context.getResources().getDrawable(id));
            iconImageView.setVisibility(View.VISIBLE);
        }
        result.setView(v);
        result.setDuration(Toast.LENGTH_LONG);
        result.setGravity(Gravity.BOTTOM, 0, (int) (dm.density * 75));
        // result.setGravity(Gravity.TOP, 0, (int)(dm.heightPixels-250));
        return result;
    }

    /**
     * 用途：只显示内容，并将内容显示在top，不显示图标 zwx220990
     *
     * @param context
     * @param text
     *            文本内容
     * @return
     */
    public static ToastUtils makeCenterText(Context context, CharSequence text) {
        ToastUtils result = new ToastUtils(context);

        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();

        View v = inflate.inflate(R.layout.new_data_toast, null);
        v.setMinimumWidth(dm.widthPixels - 80);// 设置控件最小宽度为手机屏幕宽度

        v.findViewById(R.id.imageView1).setVisibility(View.GONE);
        TextView tv = (TextView) v.findViewById(R.id.new_data_toast_message);
        tv.setText(text);
        result.setView(v);
        result.setDuration(600);
        result.setGravity(Gravity.TOP, 0, (int) (dm.density * 75));
        return result;
    }
}
