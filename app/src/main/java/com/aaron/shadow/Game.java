package com.aaron.shadow;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class Game extends ActionBarActivity {

    private VistaJuego vjuego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vjuego = new VistaJuego(this);
        setContentView(vjuego);
    }
}
