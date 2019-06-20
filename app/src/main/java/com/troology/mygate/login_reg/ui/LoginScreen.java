package com.troology.mygate.login_reg.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.troology.mygate.R;
import com.troology.mygate.utils.Loader;
import com.troology.mygate.utils.UtilsMethods;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener {

    LinearLayout llphone, llInfo;
    ImageView appLogo, ivFlag, ivBack;
    TextView tvMoving, tvPhoneNo, tvCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        setupWindowAnimations();

        llphone = findViewById(R.id.llphone);
        llInfo = findViewById(R.id.llInfo);
        appLogo = findViewById(R.id.appLogo);
        ivFlag = findViewById(R.id.ivFlag);
        ivBack = findViewById(R.id.ivback);
        tvMoving = findViewById(R.id.tvMoving);
        tvPhoneNo = findViewById(R.id.tvPhoneNo);
        tvCode = findViewById(R.id.tvCode);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        appLogo.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (0.65 * height)));
        ivBack.setImageAlpha(0);

        llInfo.setOnClickListener(this);
    }

    private void setupWindowAnimations() {

        ChangeBounds exitTransition = new ChangeBounds();
        exitTransition.setDuration(1000);
        exitTransition.addListener(exitListener);
        getWindow().setSharedElementExitTransition(exitTransition);

        ChangeBounds reenterTransition = new ChangeBounds();
        reenterTransition.setDuration(1000);
        reenterTransition.addListener(reenterListener);
        reenterTransition.setInterpolator(new DecelerateInterpolator(4));
        getWindow().setSharedElementReenterTransition(reenterTransition);

    }

    Transition.TransitionListener exitListener = new Transition.TransitionListener() {
        @Override
        public void onTransitionStart(Transition transition) {


        }

        @Override
        public void onTransitionEnd(Transition transition) {

        }

        @Override
        public void onTransitionCancel(Transition transition) {

        }

        @Override
        public void onTransitionPause(Transition transition) {

        }

        @Override
        public void onTransitionResume(Transition transition) {

        }
    };


    Transition.TransitionListener reenterListener = new Transition.TransitionListener() {
        @Override
        public void onTransitionStart(Transition transition) {

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(ObjectAnimator.ofFloat(tvMoving, "alpha", 0f, 1f));
            animatorSet.setDuration(800);
            animatorSet.start();
        }

        @Override
        public void onTransitionEnd(Transition transition) {


        }

        @Override
        public void onTransitionCancel(Transition transition) {

        }

        @Override
        public void onTransitionPause(Transition transition) {

        }

        @Override
        public void onTransitionResume(Transition transition) {

            tvMoving.setAlpha(1);
        }
    };


    @Override
    public void onClick(View v) {
        if (v == llInfo){
            Intent intent = new Intent(this, LoginWithPhone.class);

            Pair<View, String> p1 = Pair.create((View) ivBack, getString(R.string.transition_arrow));
            Pair<View, String> p2 = Pair.create((View) ivFlag, getString(R.string.transition_ivFlag));
            Pair<View, String> p3 = Pair.create((View) tvCode, getString(R.string.transition_tvCode));
            Pair<View, String> p4 = Pair.create((View) tvPhoneNo, getString(R.string.transition_tvPhoneNo));
            Pair<View, String> p5 = Pair.create((View) llphone, getString(R.string.transition_llPhone));

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1, p2, p3, p4, p5);
            startActivity(intent, options.toBundle());
        }
    }
}
