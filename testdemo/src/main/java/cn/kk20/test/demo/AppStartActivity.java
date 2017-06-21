package cn.kk20.test.demo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import cn.kk20.lib.permission.PermissionsActivity;
import cn.kk20.lib.permission.PermissionsChecker;
import cn.kk20.test.demo.base.IBaseApplication;

public class AppStartActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 0; // 请求码
    //所需的全部权限
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private PermissionsChecker mPermissionsChecker; // 权限检测器

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_app_start);

        // 缺少权限时, 进入权限配置页面
        mPermissionsChecker = new PermissionsChecker(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean result = mPermissionsChecker.lacksPermissions(PERMISSIONS);
                if (result) {
                    startPermissionsActivity();
                } else {
                    jump2MainActivity();
                }
            }
        }, 3000);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == 0) {//所有权限都被授予
            ((IBaseApplication) getApplication()).init();
            jump2MainActivity();
        } else {
            finish();
        }
    }

    private void startPermissionsActivity() {
        Intent intent = new Intent(this, PermissionsActivity.class);
        intent.putExtra(PermissionsActivity.EXTRA_PERMISSIONS, PERMISSIONS);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void jump2MainActivity(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
