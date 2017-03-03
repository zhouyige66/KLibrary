package cn.kk20.lib.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.kk20.lib.R;

public class TipDialog extends Dialog {
    private TextView tv_title, tv_message;
    private Button btn_cancle, btn_confirm;

    private OnConfirmBtnClicked listener;

    public TipDialog(Context context) {
        this(context, R.style.CustomDialog);
        init(context);
    }

    public TipDialog(Context context, int theme) {
        super(context, theme);

        init(context);
    }

    public void init(Context c) {
        View v = LayoutInflater.from(c).inflate(R.layout.layout_dialog_default, null);
        tv_title = (TextView) v.findViewById(R.id.id_tv_title);
        tv_message = (TextView) v.findViewById(R.id.id_tv_content);
        btn_cancle = (Button) v.findViewById(R.id.btn_cancle);
        btn_confirm = (Button) v.findViewById(R.id.btn_confirm);
        tv_title.setVisibility(View.GONE);
        tv_message.setText("正在处理，请稍后...");

        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TipDialog.this.dismiss();
                if (listener != null) {
                    listener.onCancleBtnClicked();
                }
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TipDialog.this.dismiss();
                if (listener != null) {
                    listener.onConfirmBtnCliecked();
                }
            }
        });

        setContentView(v);
        setCanceledOnTouchOutside(false);
    }

    public void setTitleVisiable() {
        tv_title.setVisibility(View.VISIBLE);
    }

    public void setTitle(String title) {
        tv_title.setText(title);
    }

    public void setMessage(String msg) {
        tv_message.setText(msg);
    }

    public void setCancleBtnText(String text) {
        btn_cancle.setText(text);
    }

    public void setConfirmBtnText(String text) {
        btn_confirm.setText(text);
    }

    public void setListener(OnConfirmBtnClicked listener) {
        this.listener = listener;
    }

    public interface OnConfirmBtnClicked {
        void onConfirmBtnCliecked();

        void onCancleBtnClicked();
    }

}
