package cn.kk20.lib.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cn.kk20.lib.R;

public class LoadingDialog extends Dialog {
    private TextView  mTextView;

    public LoadingDialog(Context context) {
        this(context, R.style.CustomDialog);
        init(context);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);

        init(context);
    }

    public void init(Context c) {
        View view = LayoutInflater.from(c).inflate(R.layout.layout_dialog_loading, null);
        mTextView = (TextView) view.findViewById(R.id.tv_msg);
        setContentView(view);
        setCanceledOnTouchOutside(false);
    }

    public void setMessage(String msg) {
        mTextView.setText(msg);
    }

}
