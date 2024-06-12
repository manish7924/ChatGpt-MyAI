package com.starkindustries.chatgptai;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.Manifest;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.starkindustries.chatgptai.activities.About;
import com.starkindustries.chatgptai.activities.ChatHistory;
import com.starkindustries.chatgptai.chatmodel.API;
import com.starkindustries.chatgptai.chatmodel.Message;
import com.starkindustries.chatgptai.chatmodel.MessageAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    RecyclerView recyclerView;
    EditText message_text_text;
    // Send button
    ImageView send_btn;
    ImageButton scrollbtn, speaker;
    // Media player
    private MediaPlayer sendplayer, receiveplayer;

    List<Message> messageList = new ArrayList<>();
    MessageAdapter messageAdapter;

    private SharedPreferences sharedPreferences;

    private final String[] themes = {"System Theme", "Dark Theme", "Light Theme"};

    static boolean isSystem = false;
    static boolean isNight = false;
    static boolean isLight = false;

    //Voice Func
    private final int ReqCode = 120;
    private TextToSpeech textToSpeech;

    // Vibrate
    private Vibrator myVib;


    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Mediaplayer
        sendplayer = MediaPlayer.create(this, R.raw.sent);
        receiveplayer = MediaPlayer.create(this, R.raw.recieve);


        // Vibrate
        myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);

        // Initialize the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences("ThemePrefs", Context.MODE_PRIVATE);

        // Voice func
        message_text_text = findViewById(R.id.message_text_text);

        //Initialize send button an recycler view
        send_btn = findViewById(R.id.send_btn);
        recyclerView = findViewById(R.id.recyclerView);
        scrollbtn = findViewById(R.id.scrollbtn);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);



        Dexter.withContext(this)
                .withPermission(Manifest.permission.RECORD_AUDIO)

                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
//                        Toast.makeText(MainActivity.this, "For using voice search Re-launch the app and grant the permission", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();

        try {
            applyTheme(getSelectedTheme());
        }catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

//        send_btn.setVisibility(View.INVISIBLE);

        scrollbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollToBottom();
//                scrollbtn.setVisibility(View.INVISIBLE);
            }
        });


        message_text_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    send_btn.setVisibility(View.VISIBLE);
                }
                else {
                    send_btn.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        send_btn.setOnClickListener(view -> {
            //Vibrte
            myVib.vibrate(40);
            sendplayer.start();
            String question = message_text_text.getText().toString().trim();
            if (question.equals("")){
                Toast.makeText(this, "Please Enter Your Question", Toast.LENGTH_SHORT).show();
            }
            else if (question.equals(".")){
                Toast.makeText(this, "Please Enter Your Question", Toast.LENGTH_SHORT).show();
            }
            else {
                addToChat(question, Message.SEND_BY_ME);
                message_text_text.setText("");
                callAPI(question);
            }
        });


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ReqCode) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                message_text_text.setText(result.get(0));
            }
        }
    }

//    sroll func
    private void scrollToBottom() {
        if (messageList.size() == 0){
//            recyclerView.scrollToPosition(messageList.size()-1);
            scrollbtn.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "scrolling down", Toast.LENGTH_SHORT).show();
        }
        else if(messageList.size() > 0){
            recyclerView.scrollToPosition(messageList.size()-1);
            scrollbtn.setVisibility(View.INVISIBLE);
        }
        else {
            recyclerView.scrollToPosition(messageList.size());
            scrollbtn.setVisibility(View.VISIBLE);
        }
        scrollbtn.setVisibility(View.VISIBLE);
    }

    //voice Func
    public void tapToDictate(View view) {
        myVib.vibrate(50);
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "I'm Listening...");

        try {
            startActivityForResult(intent, ReqCode);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "Didn't hear that. Try again or \nCheck your Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.more_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.ac_new_chat) {
            // Implement your desired functionality here
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            // finish(); if you have to clean the older chat

            Toast.makeText(this, "New Chat", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (id == R.id.ac_history) {

            Intent history = new Intent(this, ChatHistory.class);
            startActivity(history);
            Toast.makeText(this, "Examples", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (id == R.id.new_chat) {
            // Implement your desired functionality here
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(this, "New Chat", Toast.LENGTH_SHORT).show();
            return true;

        }
        else if (id == R.id.ac_theme) {
            showThemeSelectionDialog();
            return true;
        }

        else if (id == R.id.ac_about) {
            Intent intent = new Intent(this, About.class);
            startActivity(intent);
//            Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
            return true;

        }
        else if (id == R.id.ac_whats_new) {
            new AlertDialog.Builder(this)
                    .setTitle("Whats New!")
                    .setIcon(R.drawable.chat_icon)
                    .setMessage(R.string.whats_new)
                    .setPositiveButton("Ok", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .setNeutralButton("View on Github", (dialog, which) -> {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/manish7924/ChatGpt-MyAI"));
                        startActivity(intent);
                    })
                    .show();
            return true;

        }
        else if (id == R.id.exit_btn) {
            new AlertDialog.Builder(this)
                    .setTitle("Exit App")
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Exit the app
                        if (textToSpeech != null) {
                            textToSpeech.stop();
                            textToSpeech.shutdown();
                        }
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        }
        return false;
    }

    @SuppressLint("NotifyDataSetChanged")
    void addToChat(String message, String sendBy) {
        runOnUiThread(() -> {
            try {
                messageList.add(new Message(message, sendBy));
                messageAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(this, "Check your Internet Connection or try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void addResponse(String response) {
        messageList.remove(messageList.size() - 1);
        addToChat(response, Message.SEND_BY_BOT);
        receiveplayer.start();
        speakResponse(response);
    }


    void callAPI(String question) {
        // okhttp
        messageList.add(new Message("Generating...", Message.SEND_BY_BOT));

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model", "text-davinci-003");
            jsonBody.put("prompt", question);
            jsonBody.put("max_tokens", 4000);
            jsonBody.put("temperature", 0);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        RequestBody requestBody = RequestBody.create(jsonBody.toString(), JSON);
        Request request = new Request.Builder()
                .url(API.API_URL)
                .header("Authorization", "Bearer " + API.API)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("Check your Internet connection ⚠️ because failed to load response due to " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    JSONObject jsonObject;
                    try {
                        assert response.body() != null;
                        jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        String result = jsonArray.getJSONObject(0).getString("text");
                        addResponse(result.trim());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    assert response.body() != null;
                    addResponse("Check your Internet connection ⚠️ because failed to load response due to " + response.body());
                }
            }
        });
    }

    private void speakResponse(String response) {
        textToSpeech.speak(response, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        if (sendplayer != null){
            sendplayer.stop();
            sendplayer.release();
        }
        if (receiveplayer != null){
            receiveplayer.stop();
            receiveplayer.release();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.getDefault());

            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TextToSpeech", "Language not supported");
            }
        } else {
            Log.e("TextToSpeech", "Initialization failed");
        }
    }



//    Theme

    private void applyTheme(String theme) {
        switch (theme) {
            case "System Theme":
                try {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    isSystem = true;
                    isNight = false;
                    isLight = false;
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                }
                break;
            case "Dark Theme":
                try {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    isNight = true;
                    isLight = false;
                    isSystem = false;
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                }
                break;
            case "Light Theme":
                try {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    isLight = true;
                    isNight = false;
                    isSystem = false;
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void showThemeSelectionDialog() {

        final Spinner spinner = new Spinner(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, themes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        new AlertDialog.Builder(this)
                .setTitle("Theme")
                .setIcon(R.drawable.theme)
                .setMessage("Choose your default theme for app")
                .setView(spinner)
                .setPositiveButton("Ok", (dialog, which) -> {
                    String selectedTheme = themes[spinner.getSelectedItemPosition()];
                    try {
                        saveSelectedTheme(selectedTheme); // Save the selected theme

                    } catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(this, e.toString() , Toast.LENGTH_SHORT).show();
                    }
                    try {
                        applyTheme(selectedTheme); // Apply the selected theme
                        Toast.makeText(this, selectedTheme + " Applied Successfully. ", Toast.LENGTH_SHORT).show();
                    } catch (Exception e1){
                        e1.printStackTrace();
                        Toast.makeText(this, e1.toString() , Toast.LENGTH_SHORT).show();
                    }


                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Exit the app
                    dialog.dismiss();
                })
                .show();

    }



    private String getSelectedTheme() {
        // Retrieve the selected theme from SharedPreferences
        return sharedPreferences.getString("theme", "System Theme");
    }

    private void saveSelectedTheme(String theme) {
        // Save the selected theme to SharedPreferences
        sharedPreferences.edit().putString("theme", theme).apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        textToSpeech = new TextToSpeech(this, this);

    }

}
