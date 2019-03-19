package cn.kk20.lib.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cn.kk20.lib.R;

/**
 * @Description 通用版加载对话框
 * @Author kk20
 * @Date 2017/5/15
 * @Version V1.0.0
 */
public class CommonLoadingDialog extends Dialog {
    private TextView mTextView;

    public CommonLoadingDialog(Context context) {
        this(context, R.style.StyleCustomDialog);
        init(context);
    }

    public CommonLoadingDialog(Context context, int theme) {
        super(context, theme);

        init(context);
    }

    public void init(Context c) {
        View dialogView = LayoutInflater.from(c).inflate(R.layout.layout_dialog_loading, null);
        mTextView =  dialogView.findViewById(R.id.tv_msg);
        setContentView(dialogView);
        setCanceledOnTouchOutside(false);
    }

    public void setMessage(String msg) {
        mTextView.setText(msg);
    }

}
