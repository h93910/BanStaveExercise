package com.ban.staveexercise;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ban.staveexercise.base.ComBase;
import com.ban.staveexercise.base.MySampleDate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends MyBaseActivity {
    private int correct;
    private int incorrect;
    private int combo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState, R.layout.activity_main);
        doNextExercise();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private RelativeLayout comboLayout;//连击
    private ImageView panelImageView;// 面板
    private ImageView resultImageView;// 是否正确
    private ImageView noteImageView;// 音符
    private ImageView alnImageView;// 上线
    private ImageView blnImageView;// 下线
    private TextView correctNumberTextView;// 正确数
    private TextView inCorrectNumberTextView;// 错误数
    private TextView speedTextView;// 速度
    private TextView incorrectRatioTextView;// 错误率


    private ArrayList<ComboViewHolder> comboList;

    @Override
    public void initView() {
        comboList = new ArrayList<ComboViewHolder>();
        MySampleDate.getInstance(this, "set");
        correct = MySampleDate.getIntValue("corrcct");
        incorrect = MySampleDate.getIntValue("incorrect");
        startCount = correct;
        startIncorrectCount = incorrect;

        panelImageView = (ImageView) findViewById(R.id.panel);
        resultImageView = (ImageView) findViewById(R.id.result);
        noteImageView = (ImageView) findViewById(R.id.n);
        alnImageView = (ImageView) findViewById(R.id.aln);
        blnImageView = (ImageView) findViewById(R.id.bln);
        correctNumberTextView = (TextView) findViewById(R.id.correct);
        inCorrectNumberTextView = (TextView) findViewById(R.id.incorrect);
        speedTextView = (TextView) findViewById(R.id.speed);
        incorrectRatioTextView = (TextView) findViewById(R.id.incorrect_ratio);

        comboLayout = (RelativeLayout) findViewById(R.id.combo);
        correctNumberTextView.setText(correct + "");
        inCorrectNumberTextView.setText(incorrect + "");

        alnImageView.setVisibility(View.INVISIBLE);
        blnImageView.setVisibility(View.INVISIBLE);
    }

    private List<String> btnResultSet;
    private List<String> resultSet;
    private List<Integer> keyCodeSet;
    private List<String> keyResultSet;

    @Override
    public void initButton() {
        final List<Integer> btnIDSet = new ArrayList<Integer>();
        btnIDSet.add(R.id.btn_c);
        btnIDSet.add(R.id.btn_cs);
        btnIDSet.add(R.id.btn_d);
        btnIDSet.add(R.id.btn_ds);
        btnIDSet.add(R.id.btn_e);
        btnIDSet.add(R.id.btn_f);
        btnIDSet.add(R.id.btn_fs);
        btnIDSet.add(R.id.btn_g);
        btnIDSet.add(R.id.btn_gs);
        btnIDSet.add(R.id.btn_a);
        btnIDSet.add(R.id.btn_as);
        btnIDSet.add(R.id.btn_b);

        btnResultSet = new ArrayList<String>();
        btnResultSet.add("c");
        btnResultSet.add("c#");
        btnResultSet.add("d");
        btnResultSet.add("d#");
        btnResultSet.add("e");
        btnResultSet.add("f");
        btnResultSet.add("f#");
        btnResultSet.add("g");
        btnResultSet.add("g#");
        btnResultSet.add("a");
        btnResultSet.add("a#");
        btnResultSet.add("b");

        resultSet = new ArrayList<String>();
        resultSet.add(btnResultSet.get(4));
        resultSet.add(btnResultSet.get(5));
        resultSet.add(btnResultSet.get(7));
        resultSet.add(btnResultSet.get(9));
        resultSet.add(btnResultSet.get(11));
        resultSet.add(btnResultSet.get(0));
        resultSet.add(btnResultSet.get(2));

        keyCodeSet = new ArrayList<Integer>();
        keyCodeSet.add(KeyEvent.KEYCODE_1);
        keyCodeSet.add(KeyEvent.KEYCODE_2);
        keyCodeSet.add(KeyEvent.KEYCODE_3);
        keyCodeSet.add(KeyEvent.KEYCODE_4);
        keyCodeSet.add(KeyEvent.KEYCODE_5);
        keyCodeSet.add(KeyEvent.KEYCODE_6);
        keyCodeSet.add(KeyEvent.KEYCODE_7);

        keyResultSet = new ArrayList<String>();
        keyResultSet.add(btnResultSet.get(0));
        keyResultSet.add(btnResultSet.get(2));
        keyResultSet.add(btnResultSet.get(4));
        keyResultSet.add(btnResultSet.get(5));
        keyResultSet.add(btnResultSet.get(7));
        keyResultSet.add(btnResultSet.get(9));
        keyResultSet.add(btnResultSet.get(11));

        OnClickListener clickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < btnIDSet.size(); i++) {
                    if (v.getId() == btnIDSet.get(i)) {
                        doAnswer(btnResultSet.get(i));
                        break;
                    }
                }
            }
        };

        for (int i = 0; i < btnIDSet.size(); i++) {
            findViewById(btnIDSet.get(i)).setOnClickListener(clickListener);
        }
    }

    @Override
    public void initListView() {

    }

    /**
     * 获取连击
     * @return
     */
    private ComboViewHolder getComboView() {
        for (ComboViewHolder holder : comboList) {
            if (holder.finishAnimation) {
                return holder;
            }
        }
        ComboViewHolder comboViewHolder = new ComboViewHolder();
        View view = getLayoutInflater().inflate(R.layout.item_combo, comboLayout, false);
        comboViewHolder.comboTextLayout = (LinearLayout) view.findViewById(R.id.combo_text);
        comboViewHolder.comboImageView = (ImageView) view.findViewById(R.id.combo_result);
        comboViewHolder.comboTextView = (TextView) view.findViewById(R.id.combo_number);
        comboLayout.addView(view);
        comboList.add(comboViewHolder);
        comboViewHolder.view = view;

        return comboViewHolder;
    }

    private void doAnswer(String string) {
        String result = resultSet.get(random % 7);

        ComboViewHolder comboViewHolder = getComboView();
        if (result.equals(string)) {
            correct++;
            combo++;
            resultImageView.setImageResource(R.drawable.tick);

            comboViewHolder.comboTextView.setText(combo + "");
            comboViewHolder.comboImageView.setImageResource(R.drawable.true_p);
            comboViewHolder.comboTextLayout.setVisibility(View.VISIBLE);
        } else {
            incorrect++;
            combo = 0;
            resultImageView.setImageResource(R.drawable.cross);

            comboViewHolder.comboImageView.setImageResource(R.drawable.false_p);
            comboViewHolder.comboTextLayout.setVisibility(View.INVISIBLE);
        }

        correctNumberTextView.setText(correct + "");
        inCorrectNumberTextView.setText(incorrect + "");

        showCombo(comboViewHolder);
        doNextExercise();
        updateInfo();
    }

    private long startTime;
    private int startCount;
    private int startIncorrectCount;
    private boolean first = true;
    private DecimalFormat decimalFormat;

    /**
     * 更新速度、更新错误率
     */
    private void updateInfo() {
        if (first) {
            decimalFormat = new DecimalFormat(".00");
            startTime = System.currentTimeMillis();
            first = false;
        }
        long a = System.currentTimeMillis() - startTime;
        if (a == 0) {
            return;
        }
        float speed = (correct - startCount) / (a / 1000f) * 60f;
        speedTextView.setText(decimalFormat.format(speed));

        float incorrectRatio = ((float) (incorrect - startIncorrectCount)) / (correct - startCount + incorrect - startIncorrectCount) * 100;
        incorrectRatioTextView.setText(decimalFormat.format(incorrectRatio) + "%");
    }


    /**
     * 生成下一个音符
     */
    private int random = 0;

    private void doNextExercise() {
        random = (int) (Math.random() * 23);
        float position = -45f / 7 * (random - 5);// 　.3　与　1的距离为5

        noteMove(position);

        alnImageView.setVisibility(View.INVISIBLE);
        blnImageView.setVisibility(View.INVISIBLE);

        if (random < 2) {
            alnImageView.setImageResource(R.drawable.tribln);
            alnImageView.setVisibility(View.VISIBLE);
        } else if (random >= 2 && random < 4) {
            alnImageView.setImageResource(R.drawable.dubbln);
            alnImageView.setVisibility(View.VISIBLE);
        } else if (random >= 4 && random < 6) {
            alnImageView.setImageResource(R.drawable.sglbln);
            alnImageView.setVisibility(View.VISIBLE);
        } else if (random >= 21 && random < 23) {
            blnImageView.setImageResource(R.drawable.trialn);
            blnImageView.setVisibility(View.VISIBLE);
        } else if (random >= 19 && random < 21) {
            blnImageView.setImageResource(R.drawable.dubaln);
            blnImageView.setVisibility(View.VISIBLE);
        } else if (random >= 17 && random < 19) {
            blnImageView.setImageResource(R.drawable.sglaln);
            blnImageView.setVisibility(View.VISIBLE);
        }
    }

    private float lastPosition = 0;

    /**
     * 音符移动
     *
     * @param position
     */
    private void noteMove(float position) {
        noteImageView.setVisibility(View.GONE);

        TranslateAnimation animation = new TranslateAnimation(0, 0,
                ComBase.dp2pix(lastPosition), ComBase.dp2pix(position));
        animation.setFillEnabled(true);
        animation.setFillAfter(true);

        noteImageView.startAnimation(animation);

        lastPosition = position;
        noteImageView.setVisibility(View.VISIBLE);
    }

    /**
     * 连击移动
     *
     * @param comboViewHolder
     */
    private void showCombo(final ComboViewHolder comboViewHolder) {
        comboViewHolder.view.setVisibility(View.GONE);
        comboViewHolder.finishAnimation = false;

        TranslateAnimation animation = new TranslateAnimation(0, 0, ComBase.dp2pix(lastPosition), ComBase.dp2pix(lastPosition));
        animation.setDuration(0);

        AnimationSet animation2 = (AnimationSet) AnimationUtils.loadAnimation(this, R.anim.combo);
        animation2.setFillEnabled(true);
        animation2.setFillAfter(true);
        animation2.addAnimation(animation);
        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                comboViewHolder.finishAnimation = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        comboViewHolder.view.clearAnimation();
        comboViewHolder.view.setAnimation(animation2);
        comboViewHolder.view.setVisibility(View.VISIBLE);
    }


    /**
     * 播放音乐
     */
    private void playMusic() {

    }

    @Override
    protected void onDestroy() {
        MySampleDate.saveInfo("corrcct", correct);
        MySampleDate.saveInfo("incorrect", incorrect);

        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("Ban", keyCode + "");
        for (int i = 0; i < keyCodeSet.size(); i++) {
            if (keyCodeSet.get(i) == keyCode) {
                doAnswer(keyResultSet.get(i));
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    class ComboViewHolder {
        boolean finishAnimation = true;
        View view;
        ImageView comboImageView;// 连击是否正确的图片
        TextView comboTextView;// 连击数
        LinearLayout comboTextLayout;//连击字
    }
}
