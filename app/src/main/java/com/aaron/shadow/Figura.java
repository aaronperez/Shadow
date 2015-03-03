package com.aaron.shadow;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by 2dam on 02/02/2015.
 */
public class Figura{

    private Bitmap bmp;
    private int frameActual=0;
    private int ancho, alto;
    private int ejeX, ejeY;
    private int direccionX, direccionY;
    private static  int anchoMax=0,altoMax=0;
    private static final int COLUMNAS = 6;
    private static final int FILAS = 2;
    private boolean muerto=false;


    public Figura(VistaJuego vista,Bitmap bmp) {
        this.bmp = bmp;
        this.ancho=bmp.getWidth();
        this.alto=bmp.getHeight();
        this.alto=bmp.getHeight()/FILAS;
        this.ancho=bmp.getWidth()/COLUMNAS;
    }



    public void setRun(){
        direccionX=+1;
    }

    public void setJump(){
        direccionX=-1;
    }

    public void setPosicion(int y, int x){
        ejeX=x;
        ejeY=y;
    }


    public int getX(){
        return ejeX;
    }

    public int getY(){
        return ejeY;
    }

    private void movimiento(){
        frameActual = ++frameActual % COLUMNAS;
    }

    public void dibujar(Canvas canvas) {
        int origenx = frameActual * ancho;
        int origeny = 0;
        Log.v("muerto",""+muerto);
        if(muerto){
            Log.v("muerto","del todo");
            origenx = ancho;
            origeny = alto;
        }else {
            movimiento();
            if (direccionX < 0) {
                origenx = 0;
                origeny = alto;
            } else
                origeny = 0;
        }
        Rect origen = new Rect(origenx, origeny, origenx + ancho, origeny + alto);
        Rect destino = new Rect(ejeX, ejeY, ejeX + ancho, ejeY + alto);
        canvas.drawBitmap(bmp, origen, destino, null);
    }

    public boolean colisiona(Coin c){
        int pos=this.getX() + this.ancho;
        if(pos > c.getX()+50 && this.getX() < c.getX()-50){
            return true;
        }
        return false;
    }

    public boolean tocado(RollingStone r){
        int pos=this.getX() + this.ancho;
        if(pos-5 > r.getX() && this.getX() < r.getX()-10){
            return true;
        }
        return false;
    }

    public void setMuerto(){
        muerto=true;
    }

}
