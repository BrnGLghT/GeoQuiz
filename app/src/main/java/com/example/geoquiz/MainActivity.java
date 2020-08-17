package com.example.geoquiz;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends SingleFragmentActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private TextView mQuestionTextView;
    private TextView mCountTextView;
    private Button mResetButton;
    private TextView mAnswerButton;
    private int mCount = 0;
    private int mCurrentIndex = 0;
    private Button mCheatButton;
    private boolean mIsCheater;
    public static final int REQUEST_CODE_CHEAT = 0;
    private static long back_pressed;
    private FrameLayout mBackgroundImage;
    private View mGreyBg;

    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
            new Question(R.string.question_result, true),
    };

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        checkLastQuestion();
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        animationShowAnswerButton();
        if (userPressedTrue == answerIsTrue) {
            mAnswerButton.setVisibility(View.VISIBLE);
            incrementCount();
            mAnswerButton.setText(getString(R.string.correct_answer));
            mAnswerButton.setTextColor(getResources().getColor(android.R.color.white));
            mAnswerButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
//            mAnswerButton.setBackground(getDrawable(R.drawable.correct_bg));
        } else {
            mAnswerButton.setVisibility(View.VISIBLE);
            mAnswerButton.setTextColor(getResources().getColor(android.R.color.white));
            mAnswerButton.setText(getString(R.string.incorrect_answer));
//            mAnswerButton.setBackground(getDrawable(R.drawable.incorrect_bg));
            mAnswerButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
        }
    }

    private void incrementCount() {
        if (mCount < (mQuestionBank.length - 1))
        mCount++;
        else mCount = 1;
        mCountTextView.setText(String.valueOf(mCount));
    }

    private void goToNextWhilePressedButton() {
        mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
        updateQuestion();
    }

    @SuppressLint("SetTextI18n")
    private void checkLastQuestion() {
        if (mCurrentIndex == (mQuestionBank.length - 1)) {
            mCountTextView.setText((mCount * 100) / (mQuestionBank.length - 1) + " %");
            mCountTextView.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            hideAllButtons();
            mResetButton.setVisibility(View.VISIBLE);
            mGreyBg.setVisibility(View.VISIBLE);
            mQuestionTextView.setVisibility(View.INVISIBLE);
            int percentage = (mCount * 100) / (mQuestionBank.length - 1);
            if (percentage == 100) {
//                mQuestionTextView.setText(getString(R.string.perfect_result));
                mBackgroundImage.setBackground(getDrawable(R.drawable.perfect_score_bg));
            } else if (percentage < 100 && percentage >= 40) {
//                mQuestionTextView.setText(getString(R.string.normal_result));
                mBackgroundImage.setBackground(getDrawable(R.drawable.middle_score_bg));
            } else if (percentage < 40) {
//                mQuestionTextView.setText(getString(R.string.bad_result));
                mBackgroundImage.setBackground(getDrawable(R.drawable.bad_score_bg));
            }
        }
    }

    private void cancelAllStates() {
        mCount = 0;
        mCurrentIndex = 0;
        mCountTextView.setText(String.valueOf(mCount));
        updateQuestion();
        mTrueButton.setVisibility(View.VISIBLE);
        mFalseButton.setVisibility(View.VISIBLE);
        mResetButton.setVisibility(View.GONE);
        mQuestionTextView.setVisibility(View.VISIBLE);
        mBackgroundImage.setBackgroundColor(getResources().getColor(android.R.color.white));
        mCountTextView.setTextColor(getResources().getColor(android.R.color.black));
        mGreyBg.setVisibility(View.GONE);
    }

    private void hideAllButtons() {
        mTrueButton.setVisibility(View.GONE);
        mFalseButton.setVisibility(View.GONE);
        mCheatButton.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis())
            super.onBackPressed();
        else
            Toast.makeText(getBaseContext(), "Press once again to exit!",
                    Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        mAnswerButton.getTop();

        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() { //TODO finish this pls
            @Override
            public void onClick(View view) {

            }
        });
//        mFalseButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                checkAnswer(false);
//            }
//        });

        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAllStates();
            }
        });

        mAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationHideAnswerButton();
            }
        });
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(MainActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });

        updateQuestion();
    }

    @Override
    public Fragment createFragment() {
        return new CheatFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    private void animationShowAnswerButton() {
        float butStart = mAnswerButton.getTop() - mAnswerButton.getHeight();
        float butEnd = mAnswerButton.getTop();

        ObjectAnimator buttonAnimator = ObjectAnimator.ofFloat(mAnswerButton, "y", butStart, butEnd)
                .setDuration(200);
        buttonAnimator.start();
        mAnswerButton.setClickable(true);
        mTrueButton.setClickable(false);
        mFalseButton.setClickable(false);
        mResetButton.setClickable(false);
    }
    private void animationHideAnswerButton() {
        mAnswerButton.setClickable(false);
        float butStart = mAnswerButton.getTop();
        float butEnd = mAnswerButton.getBottom();

        ObjectAnimator buttonAnimator = ObjectAnimator.ofFloat(mAnswerButton, "y", butStart, butEnd)
                .setDuration(200);
        buttonAnimator.start();
        goToNextWhilePressedButton();
        mTrueButton.setClickable(true);
        mFalseButton.setClickable(true);
        mResetButton.setClickable(true);
    }



    private void initViews() {
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mTrueButton = (Button) findViewById(R.id.true_button);
        mFalseButton = (Button) findViewById(R.id.false_button);
        mCountTextView = (TextView) findViewById(R.id.count_text);
        mResetButton = (Button) findViewById(R.id.reset_button);
        mAnswerButton = (TextView) findViewById(R.id.answer_button);
        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mBackgroundImage = findViewById(R.id.main_bg);
        mGreyBg = findViewById(R.id.grey_bg);
    }
}

