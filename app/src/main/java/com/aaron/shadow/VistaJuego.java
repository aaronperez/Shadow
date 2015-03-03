package com.aaron.shadow;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by 2dam on 02/02/2015.
 */
public class VistaJuego  extends SurfaceView implements SurfaceHolder.Callback  {

    /*******************************************************************************************/
    /*     constructor                                                                         */
    /*******************************************************************************************/

    private Bitmap bmp, back1,back2,ground,coin, stone, templobmp;
    private int alto, ancho, z=0;
    private HebraJuego hebraJuego;
    private ArrayList<Coin> coins=new ArrayList<>();
    private ArrayList<RollingStone> stones=new ArrayList<>();
    private Temple templo;
    private Figura prota;
    public boolean salto=false, muerto=false;
    MediaPlayer mp1,jump,takecoin,level,ups;
    private int inicio, coinX=0, stoneX=0;
    public static int score=0, mejor=0;
    private int color=Color.BLACK;
    //Shared preferences
    private SharedPreferences.Editor editor;


    public VistaJuego(Context contexto){
        super(contexto);
        getHolder().addCallback(this);
        //Figura principal
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ninja);
        prota = new Figura(this,bmp);
        //Fondo
        back1 = BitmapFactory.decodeResource(getResources(), R.drawable.back);
        back1 = back1.copy(Bitmap.Config.RGB_565, true);
        back2 = back1.copy(Bitmap.Config.RGB_565, true);
        ground = BitmapFactory.decodeResource(getResources(), R.drawable.ground);
        //Sounds
        mp1=MediaPlayer.create(contexto, R.raw.game);
        jump=MediaPlayer.create(contexto, R.raw.jump);
        takecoin=MediaPlayer.create(contexto, R.raw.cointake);
        level=MediaPlayer.create(contexto, R.raw.level);
        ups=MediaPlayer.create(contexto, R.raw.ups);
        mp1.start();
        mp1.setLooping(true);
        //Monedas
        coin = BitmapFactory.decodeResource(getResources(), R.drawable.coin);
        for(int i=0; i<20; i++ ){
            coins.add(new Coin(this,coin));
        }
        //Piedras
        stone = BitmapFactory.decodeResource(getResources(), R.drawable.roca);
        for(int i=0; i<60; i++ ){
            stones.add(new RollingStone(this,stone));
        }
        //Templo
        templobmp = BitmapFactory.decodeResource(getResources(), R.drawable.temple);
        templo = new Temple(this,templobmp);
        //
        hebraJuego=new HebraJuego(this);
    }

    /*******************************************************************************************/
    /*     Métodos de la clase Surfaces View                                                   */
    /*******************************************************************************************/

    private long ultimoClick = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (System.currentTimeMillis() - ultimoClick > 800 && !muerto) {
            ultimoClick = System.currentTimeMillis();
            //salto
            salto=true;
            jump.start();
        }
        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(color);
        //background bucle
        z=z-5;
        if(z==-back1.getWidth()) {
            z = 0;
        }
        canvas.drawBitmap(back1, z, 0, null);
        canvas.drawBitmap(back2, z+back1.getWidth(), 0, null);
        //Suelo
        canvas.drawBitmap(ground, 0, alto-125, null);
        //score
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setAntiAlias(true);
        paint.setFakeBoldText(true);
        paint.setTextSize(60);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(getResources().getString(R.string.puntuacion)+": "+score, 90, 60, paint);
        //Monedas
        for(int c=0; c<coins.size();c++) {
            coins.get(c).setPosicion(inicio - 300, coins.get(c).getX() - 10);
            if(coins.get(c).getX()<ancho) {
                coins.get(c).dibujar(canvas);
                if(coins.get(c).getX()<-80){
                    coins.remove(c);
                }
            }
        }
        //Acciones
        acciones();
        //Dibujar prota
        prota.dibujar(canvas);
        //Piedras
        for(int c=0;c < stones.size();c++) {
            stones.get(c).setPosicion(inicio + 140, stones.get(c).getX() - 40);
            if(stones.get(c).getX()<ancho+80) {
                stones.get(c).dibujar(canvas);
                if(stones.get(c).getX()<-80){
                    stones.remove(c);
                }
            }
        }
        if(coins.isEmpty() && stones.isEmpty()){
            templo.setPosicion(0, templo.getX() - 10);
            templo.dibujar(canvas);
        }
        if(muerto){
            hebraJuego.setFuncionando(false);
            String s=getResources().getString(R.string.gameover)+" "+getResources().getString(R.string.puntuacion)+":";
            canvas.drawText(s+" "+score, 250, inicio-50, gameOver());
            fin();
        }
    }

    private void acciones(){
        if(salto){
            if(prota.getY()<=inicio && prota.getY()>inicio-320){
                prota.setPosicion(prota.getY()-40, prota.getX());
            }else{
                salto=false;
            }
        }else{
            if(prota.getY()<inicio){
                prota.setPosicion(prota.getY()+40, prota.getX());
            }
        }
        //Monedas
        if(prota.getY() <= inicio-310){
            for(int y=0;y<coins.size();y++) {
                if (prota.colisiona(coins.get(y))) {
                    coins.get(y).eliminar();
                    score = score + 50;
                    takecoin.start();
                }
            }
        }
        //Piedras
        if(prota.getY() > inicio-50){
            for(int y=0;y<stones.size();y++) {
                if (prota.tocado(stones.get(y))) {
                    ups.start();
                    prota.setMuerto();
                    muerto = true;
                    mp1.stop();
                }
            }
        }
        //Piedras
        if(prota.getX() >= templo.getX()+30){
            level.start();
            prota.setPosicion(inicio,ancho+300);
            muerto = true;
            mp1.stop();

        }
        //Dibujar salto
        if(prota.getY()==inicio){
            prota.setRun();
        }else{
            prota.setJump();
        }
    }

    private void actualizarProta(){
        prota.setRun();
        prota.setPosicion(inicio, ancho / 3);
    }

    private Paint gameOver(){
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setAntiAlias(true);
        paint.setFakeBoldText(true);
        paint.setTextSize(100);
        paint.setTextAlign(Paint.Align.LEFT);
        return paint;
    }

    private void fin(){
        Main.score=score;
        if(score>mejor) {
            editor = Main.prefs.edit();
            editor.putInt("score", score);
            editor.commit();
        }
        HebraJuego.interrupted();
    }


    /*******************************************************************************************/
    /*     interfaz callback                                                                   */
    /*******************************************************************************************/

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        hebraJuego.setFuncionando(true);
        mp1.start();
        ancho = getWidth();
        alto= getHeight();
        Random x=new Random();
        inicio = alto-350;
        actualizarProta();
        //Templo
        templo.setPosicion(50,800);
        //Monedas
        for(Coin c: coins) {
            c.setPosicion(inicio, ancho + coinX);
            coinX= coinX+(x.nextInt(20)*50);
            c.setMovimiento();
        }
        for(RollingStone r: stones) {
            r.setPosicion(inicio, ancho + stoneX);
            stoneX= 50+stoneX+(x.nextInt(50)*100);
            r.setMovimiento();
        }
        hebraJuego.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        alto = height;
        ancho = width;
        inicio = alto-350;
        //figura principal
        actualizarProta();
        mp1.start();
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean reintentar = true;
        hebraJuego.setFuncionando(false);
        while (reintentar) {
            try {
                hebraJuego.join();
                mp1.stop();
                reintentar = false;
            } catch (InterruptedException e) {
            }
        }
    }
}
