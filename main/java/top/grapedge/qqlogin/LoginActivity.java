package top.grapedge.qqlogin;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    public static final String ACTION = "top.grapedge.qqlogin.intent.action.LOGIN";
    private final String ACCOUNT = "203202537";
    private final String PASSWORD = "123456";
    private EditText accountEdit;
    private EditText passwdEdit;
    private Button loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setupEditText();
        setupBackground();
        changeImageWithTimeInterval();
        setupButton();

        scrollToButton(loginButton);

    }

    private void setupEditText() {
        accountEdit = findViewById(R.id.activity_login_account_edit);
        passwdEdit = findViewById(R.id.activity_login_password_edit);
    }

    private int scrollTo = 0;
    private void scrollToButton(final Button button) {
        final LinearLayout loginView = findViewById(R.id.login_view);
        loginView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                // 获得可见区域的高度
                loginView.getWindowVisibleDisplayFrame(rect);
                // 父布局的高度减去可见区域的底部高度就是不可见区域的高度
                int invisibleHeight = loginView.getRootView().getHeight() - rect.bottom;
                // 如果不可见高度大于150说明键盘已经打开
                if (invisibleHeight > 150) {
                    // 下面的部分和 ScrollView 版是相似原理
                    int[] location = new int[2];
                    button.getLocationInWindow(location);

                    scrollTo += location[1] + button.getHeight() - rect.bottom;

                } else scrollTo = 0;

                loginView.scrollTo(0, scrollTo);
            }
        });
    }


    private void setupButton() {
        loginButton = findViewById(R.id.activity_login_login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = accountEdit.getText().toString();
                String passwd = passwdEdit.getText().toString();
                if (account.equals(ACCOUNT) && passwd.equals(PASSWORD)) {
                    // 进入聊天界面
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private int[] backgroundIds = new int[3];

    // 初始化图片资源
    private void setupBackground() {
        for (int i = 0; i < backgroundIds.length; i++) {
            // 获取图片的id
            int resId = getResources().getIdentifier("login_background" + i, "mipmap", this.getPackageName());
            backgroundIds[i] = resId;
        }
    }

    // 动态改变图片
    private void changeImageWithTimeInterval() {
        final ImageView imageView = findViewById(R.id.activity_login_random_image);
        final int delayMillis = 10000;
        final Random random = new Random();
        new Runnable() {

            @Override
            public void run() {
                int index = random.nextInt() % backgroundIds.length;
                index = index < 0 ? index + backgroundIds.length : index;
                imageView.setImageResource(backgroundIds[index]);
                imageView.postDelayed(this, delayMillis);
            }
        }.run();
    }

    // 实现点击空白处隐藏软键盘
    private void touchBlank(View v, MotionEvent ev) {
        if (v != null && v instanceof EditText) {
            Rect rect = new Rect();
            v.getHitRect(rect);
            // 判断点击点是否在编辑框区域外
            if (!rect.contains((int)ev.getX(), (int)ev.getY())) {
                // 隐藏软键盘
                ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            touchBlank (getCurrentFocus(), ev);
        }
        return super.dispatchTouchEvent(ev);
    }


}
