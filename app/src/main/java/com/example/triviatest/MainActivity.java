package com.example.triviatest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.triviatest.data.AnswerListAsyncResponse;
import com.example.triviatest.data.QuestionBank;
import com.example.triviatest.model.Question;
import com.example.triviatest.model.Score;
import com.example.triviatest.util.Prefs;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mCard;
    private TextView mCounter;
    private TextView mHighScore;
    private TextView mScoreTextView;
    private ImageButton mPrev;
    private ImageButton mNext;
    private Button mTrue;
    private Button mFalse;
    private int currentQuestionIndex = 0;
    private int currentScore = 0;
    private Score score;
    private List<Question> questionList;
    private Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        score = new Score();
        prefs = new Prefs(MainActivity.this);

        mCard = (TextView)findViewById(R.id.question_text);
        mCounter = (TextView)findViewById(R.id.counter);
        mHighScore = (TextView)findViewById(R.id.high_score);
        mScoreTextView = (TextView)findViewById(R.id.current_score);
        mNext = (ImageButton)findViewById(R.id.next_button);
        mPrev = (ImageButton)findViewById(R.id.prev_button);
        mTrue = (Button)findViewById(R.id.true_button);
        mFalse = (Button)findViewById(R.id.false_button);

        mNext.setOnClickListener(this);
        mPrev.setOnClickListener(this);
        mFalse.setOnClickListener(this);
        mTrue.setOnClickListener(this);

        //load the values from the shared preference
        mScoreTextView.setText(score.toString());
        mHighScore.setText("Highest Score: " + prefs.getHighScore());
        currentQuestionIndex = prefs.getstate();

        questionList = new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
                mCard.setText(questionArrayList.get(currentQuestionIndex).getAnswer());
                mCounter.setText(currentQuestionIndex +"/" + questionArrayList.size());
            }
        });

    }

    @Override
    protected void onPause() {
        prefs.saveHighestScore(score.getScore());
        prefs.saveState(currentQuestionIndex);
        super.onPause();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.false_button:
                checkAnswer(false);
                updateQuestion();
                break;

            case R.id.true_button:
                checkAnswer(true);
                updateQuestion();
                break;

            case R.id.next_button:
                goNext();
                break;

            case R.id.prev_button:
                if(currentQuestionIndex > 0){
                    currentQuestionIndex = (currentQuestionIndex - 1) % questionList.size();
                    updateQuestion();
                }
                break;

            default:
                break;
        }
    }

    private void updateQuestion(){
        mCard.setText(questionList.get(currentQuestionIndex).getAnswer());
        mCounter.setText(currentQuestionIndex + "/" + questionList.size());
    }

    private void goNext(){
        currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
        updateQuestion();
    }

    private void checkAnswer(boolean userAnswer) {
        boolean isAnswerTrue = questionList.get(currentQuestionIndex).isAnswerTrue();
        int toastMessageId;

        if(userAnswer == isAnswerTrue) {
            fadeView();
            addPoints();
            toastMessageId = R.string.correct_answer;
        } else {
            shakeAnimation();
            subtractPoints();
            toastMessageId = R.string.wrong_answer;
        }
        mScoreTextView.setText(score.toString());
        Toast.makeText(MainActivity.this, toastMessageId, Toast.LENGTH_SHORT).show();
    }

    private void addPoints(){
        currentScore += 10;
        score.setScore(currentScore);
    }

    private void subtractPoints(){
        currentScore -= 10;
        if(currentScore > 0){
            score.setScore(currentScore);
        } else {
            currentScore = 0;
            score.setScore(currentScore);
        }
    }

    private void shakeAnimation(){
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_animation);
        final CardView cardView = (CardView)findViewById(R.id.cardView);
        cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                //move to the next question if true
                goNext();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void fadeView(){
        final CardView cardView = (CardView)findViewById(R.id.cardView);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                //move to the next ques if true
                goNext();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
