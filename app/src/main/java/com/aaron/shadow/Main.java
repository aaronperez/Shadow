package com.aaron.shadow;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class Main extends ActionBarActivity {

    private final int ACTIVIDAD_GAME = 1;
    public static int score = 0;
    private TextView mejor, ultima;
    public static SharedPreferences prefs;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mp=MediaPlayer.create(this, R.raw.inicio);
        mp.start();
        prefs =getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        mejor = (TextView)findViewById(R.id.tvMejorP);
        int best=prefs.getInt("score", 0);
        mejor.setText(""+best);
        ultima = (TextView)findViewById(R.id.tvUltimaP);
        ultima.setText(""+score);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mp.start();
        ultima.setText(""+score);
        mejor.setText(""+prefs.getInt("score", 0));
    }

    public void start(View v){
        VistaJuego.score = 0;
        VistaJuego.mejor = prefs.getInt("score", 0);
        Intent i = new Intent(this, Game.class);
        mp.stop();
        startActivity(i);
    }

}
