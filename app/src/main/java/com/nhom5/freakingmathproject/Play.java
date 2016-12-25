package com.nhom5.freakingmathproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.logging.Handler;
import java.util.logging.LogRecord;


public class Play extends AppCompatActivity implements Runnable{
    ProgressBar timeLine;
    Animation slidedown;// Làm mờ
    ImageButton trueBtn;
    ImageButton falseBtn, menu, replay;
    Thread t;
    public static String fileName = "saveScore";
    SharedPreferences data;


    private boolean isPlaying = true;
    private int totalTime = 3000;
    private int percent = 100;
    ImageView board;
    TextView txtPoint, txtNum1, txtNum2, txtResult, equals, plush, score, highScore;// điểm,số thứ 1, số thứ 2,kq,ghi diem,diemcao
    private int point = 0, num1 = 0, num2 = 0, result = 0, tmpResult = 0, high;


    private void swanNumber() {
        Random random = new Random();
        int t = 0;
        int op = 0;
        num1 = random.nextInt(10) + 1;
        num2 = random.nextInt(10) + 1;
        txtNum1.setText("" + num1);
        txtNum2.setText("" + num2);
        t = random.nextInt(5) + 1;
        op = random.nextInt(2) + 1;
        if (op==1){
            plush.setText("+");
        }else{
            plush.setText("-");
        }
        if (t % 2 == 0) {
            if (op == 1)
            {
                txtResult.setText("" + (num1 + num2));
            }
            else
            {
                txtResult.setText("" + (num1 - num2));
            }
        } else {
            tmpResult = random.nextInt(19) + 1;
            txtResult.setText("" + tmpResult);
        }
    }

    private boolean checkUserAns(boolean ans) {
        if (ans) {
            if (num1 + num2 == Integer.parseInt(txtResult.getText().toString()) || num1 - num2 == Integer.parseInt(txtResult.getText().toString())) {
                return true;
            } else
                return false;
        } else {
            if (num1 + num2 != Integer.parseInt(txtResult.getText().toString())) {
                return true;
            } else
                return false;
        }
    }

    private void hideButton() {
        trueBtn.setVisibility(View.INVISIBLE);
        falseBtn.setVisibility(View.INVISIBLE);
    }

    private void gameOver() { //sự kiện sau khi chọn sai
        isPlaying = false;
        hideButton();
        menu.setVisibility(View.VISIBLE);
        replay.setVisibility(View.VISIBLE);
        board.setVisibility(View.VISIBLE);
        score.setText(txtPoint.getText());

        score.setVisibility(View.VISIBLE);
        if (high < point) {
            high = point;
            data.edit().putInt("HighScore", point).commit();
        }
        highScore.setText("" + high);

        highScore.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        high = 0;
        isPlaying = true;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        board = (ImageView) findViewById(R.id.imageViewBoard);
        board.setVisibility(View.INVISIBLE);
        slidedown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down_animation);
        txtNum1 = (TextView) findViewById(R.id.num1);
        txtNum2 = (TextView) findViewById(R.id.num2);
        menu = (ImageButton) findViewById(R.id.menuBtn);
        replay = (ImageButton) findViewById(R.id.repayBtn);
        score = (TextView) findViewById(R.id.textViewScore);
        replay.setVisibility(View.INVISIBLE);
        menu.setVisibility(View.INVISIBLE);
        highScore = (TextView) findViewById(R.id.textViewHighscore);
        score.setVisibility(View.INVISIBLE);
        highScore.setVisibility(View.INVISIBLE);
        txtResult = (TextView) findViewById(R.id.txtResult);
        trueBtn = (ImageButton) findViewById(R.id.truebtn);
        falseBtn = (ImageButton) findViewById(R.id.falsebtn);
        timeLine = (ProgressBar) findViewById(R.id.TimeLine);
        plush = (TextView) findViewById(R.id.plush);
        timeLine.setProgressTintList(ColorStateList.valueOf(Color.WHITE));

        swanNumber();

        data = getSharedPreferences(fileName, MODE_PRIVATE);
        high = data.getInt("HighScore", 0);

        timeLine.setProgress(100);
        txtPoint = (TextView) findViewById(R.id.textPoint);

        t = new Thread(this);




        trueBtn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                if (!t.isAlive()) { // tồn tại phương thức Thread này
                    t.start();      //timeLine chạy

                    if (checkUserAns(true)) { //nếu kết quả bằng hoặc không bằng phép tính->Đúng
                        swanNumber();         // tiếp tục random phép tính
                        totalTime = 3000;     // Khởi động lại thanh timeLine
                        percent = 100;
                        point++;

                        txtPoint.setText("" + point); //+1 điểm mỗi khi trả lời đúng
                    } else {
                        gameOver(); // Nếu trả lời sai -> gọi funcion gameOver .. gọi menu tương tác thông báo điểm đạt đc và điểm cao nhất
                    }
                } else {
                    if (checkUserAns(true)) {//nếu kết quả khôngg bằng hoặc  bằng phép tính->Đúng
                        swanNumber();
                        totalTime = 3000;
                        percent = 100;
                        point++;

                        txtPoint.setText("" + point);
                    } else {
                        gameOver();
                    }
                }
            }
        });
        falseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!t.isAlive()) {
                    t.start();
                    if (checkUserAns(false)) {
                        swanNumber();
                        totalTime = 3000;
                        percent = 100;
                        point++;

                        txtPoint.setText("" + point);
                    } else {
                        gameOver();
                    }
                } else {
                    if (checkUserAns(false)) {
                        swanNumber();
                        totalTime = 3000;
                        percent = 100;
                        point++;
                        txtPoint.setText("" + point);
                    } else {
                        gameOver();
                    }
                }
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainMenu = new Intent(Play.this, MainActivity.class);
                startActivity(mainMenu);
                finish();
            }
        });

        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resetGame = new Intent(Play.this, Play.class);
                startActivity(resetGame);
                finish();
            }
        });

    }

    @Override
    public void run() {
        if ( totalTime == 0) {
            isPlaying = false;
            gameOver();
        }


       while (isPlaying) {
            try {

                totalTime -= 30;
                percent -= 1;
                timeLine.setProgress(percent);
                Thread.sleep(30);
            } catch (Exception e) {


            }
        }

    }

}
