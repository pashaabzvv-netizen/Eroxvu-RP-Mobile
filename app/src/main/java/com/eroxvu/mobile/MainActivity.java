
package com.eroxvu.mobile;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.content.ClipboardManager;
import android.content.ClipData;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.*;

public class MainActivity extends Activity {

    private final String IP = "188.127.241.74:2663";
    private EditText nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs =
            getSharedPreferences("eroxvu", MODE_PRIVATE);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(28, 30, 28, 30);
        layout.setBackgroundColor(Color.rgb(13, 17, 28));

        TextView title = new TextView(this);
        title.setText("EROXVU RP");
        title.setTextSize(32);
        title.setTextColor(Color.WHITE);
        title.setGravity(Gravity.CENTER);
        layout.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText("MOBILE LAUNCHER");
        subtitle.setTextColor(Color.CYAN);
        subtitle.setGravity(Gravity.CENTER);
        layout.addView(subtitle);

        nickname = new EditText(this);
        nickname.setSingleLine(true);
        nickname.setHint("Введите ваш ник");
        nickname.setTextColor(Color.WHITE);
        nickname.setHintTextColor(Color.LTGRAY);
        nickname.setText(prefs.getString("nickname", ""));
        layout.addView(nickname);

        Button copy = new Button(this);
        copy.setText("СКОПИРОВАТЬ IP");
        layout.addView(copy);

        copy.setOnClickListener(v -> {
            ClipboardManager clipboard =
                (ClipboardManager) getSystemService(
                    Context.CLIPBOARD_SERVICE);
            clipboard.setPrimaryClip(
                ClipData.newPlainText("Server IP", IP));
            Toast.makeText(this, "IP скопирован!",
                Toast.LENGTH_SHORT).show();
        });

        Button play = new Button(this);
        play.setText("ИГРАТЬ");
        layout.addView(play);

        play.setOnClickListener(v -> {
            prefs.edit().putString(
                "nickname",
                nickname.getText().toString()
            ).apply();

            Toast.makeText(this,
                "Для игры нужен совместимый SA-MP клиент.",
                Toast.LENGTH_LONG).show();
        });

        TextView server = new TextView(this);
        server.setText("Сервер: " + IP);
        server.setTextColor(Color.WHITE);
        server.setGravity(Gravity.CENTER);
        server.setPadding(0, 25, 0, 0);
        layout.addView(server);

        setContentView(layout);
    }
}
