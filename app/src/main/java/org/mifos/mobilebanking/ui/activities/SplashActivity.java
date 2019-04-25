package org.mifos.mobilebanking.ui.activities;

/*
 * Created by saksham on 01/June/2018
 */

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mifos.mobile.passcode.utils.PasscodePreferencesHelper;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.ui.activities.base.BaseActivity;
import org.mifos.mobilebanking.utils.Constants;

public class SplashActivity extends BaseActivity {

    PasscodePreferencesHelper passcodePreferencesHelper;
    Intent intent;
    TextView mTosTextView;
    CheckBox mTosCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //  getActivityComponent().inject(this);
        setContentView(R.layout.activity_splash);

        WebView browser = (WebView) findViewById(R.id.webview);
        browser.loadUrl("https://techsavanna.net:8181/uipb2ccallback/terms/index.php");

        mTosTextView = (TextView) findViewById(R.id.tosTextView);
        mTosTextView.setText(Html.fromHtml(getString(R.string.TOSInfo)));
        mTosTextView.setMovementMethod(LinkMovementMethod.getInstance());

        mTosCheckBox = (CheckBox) findViewById(R.id.tosCheckBox);

        mTosCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passcodePreferencesHelper = new PasscodePreferencesHelper(SplashActivity.this);
                if (!passcodePreferencesHelper.getPassCode().isEmpty()) {
                    intent = new Intent(SplashActivity.this, PassCodeActivity.class);
                    intent.putExtra(Constants.INTIAL_LOGIN, true);
                } else {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                finish();

            }
        });


    }

}
