package com.starkindustries.chatgptai.activities;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.airbnb.lottie.LottieAnimationView;
import com.starkindustries.chatgptai.R;
import com.starkindustries.chatgptai.chatmodel.Message;
import com.starkindustries.chatgptai.chatmodel.MessageAdapter;

public class ChatScreen extends AppCompatActivity {

    LottieAnimationView gen, loads;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_item);

        gen = findViewById(R.id.generate);
        loads = findViewById(R.id.loading);


        gen.animate().translationX(1000).setDuration(9000).setStartDelay(2000);
        loads.animate().translationX(1000).setDuration(9000).setStartDelay(2000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(), MessageAdapter.MyViewHolder.class);
                startActivity(i);
            }
        }, 0);

    }
}