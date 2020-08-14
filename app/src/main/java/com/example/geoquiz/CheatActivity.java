package com.example.geoquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE =
            "com.example.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN =
            "com.example.geoquiz.answer_shown";

    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    private boolean mAnswerIsTrue;

    private TextView mAnswerTextView;
    private Button mShowAnswerButton;
    private ImageButton mCheatImage;
    private Button mBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        initViews();
        mShowAnswerButton.setClickable(true);
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationShowCheatButton();
            }
        });
        mCheatImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationHideCheatButton();
                mShowAnswerButton.setClickable(false);
                mShowAnswerButton.setVisibility(View.INVISIBLE);
                if (mAnswerIsTrue) {
                    mAnswerTextView.setText(R.string.true_button);
                } else {
                    mAnswerTextView.setText(R.string.false_button);
                }
                setAnswerShownResult(true);
                mBackButton.setVisibility(View.VISIBLE);
            }
        });
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }

    private void animationShowCheatButton() {
        float butStart = mCheatImage.getTop() - mCheatImage.getHeight();
        float butEnd = mCheatImage.getTop();

        ObjectAnimator buttonAnimator = ObjectAnimator.ofFloat(mCheatImage, "y", butStart, butEnd)
                .setDuration(200);
        buttonAnimator.start();
        mCheatImage.setVisibility(View.VISIBLE);
        mShowAnswerButton.setClickable(false);
    }
    private void animationHideCheatButton() {
        float butStart = mCheatImage.getTop();
        float butEnd = mCheatImage.getBottom();

        ObjectAnimator buttonAnimator = ObjectAnimator.ofFloat(mCheatImage, "y", butStart, butEnd)
                .setDuration(200);
        buttonAnimator.start();
    }

    private void initViews(){

        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);
        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);
        mCheatImage = (ImageButton) findViewById(R.id.cheat_image);
        mBackButton = (Button) findViewById(R.id.back_button);
    }
}


