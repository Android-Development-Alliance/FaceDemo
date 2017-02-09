package com.yudan.face;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tv1 = null, tv2 = null, tv3 = null;


    private String message1 = "大家好我是新来的<emoji1f602>才怪呢<emoji1f605>";
    private String message2 = "我是鱼蛋[微笑], [左哼哼][右哼哼]我很邪恶";
    private String message3 = "[右哼哼][右哼哼]鱼蛋[左哼哼][左哼哼], 请多指教<emoji1f633><emoji1f601><emoji1f604>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tv1 = (TextView) findViewById(R.id.main_text1);
        tv2 = (TextView) findViewById(R.id.main_text2);
        tv3 = (TextView) findViewById(R.id.main_text3);

        playFace();
    }

    private void playFace() {
        tv1.setTypeface(FaceMaster.getInstace(this).getTypeface(FaceMaster.BLACK_EMOJI_FONT));
        tv3.setTypeface(FaceMaster.getInstace(this).getTypeface());

        tv1.setText(FaceMaster.getInstace(this).matchFaceToMessage(message1));
        tv2.setText(FaceMaster.getInstace(this).matchFaceToMessage(message2));
        tv3.setText(FaceMaster.getInstace(this).matchFaceToMessage(message3));
    }
}
