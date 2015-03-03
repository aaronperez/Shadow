package com.aaron.shadow;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by Aaron on 28/02/2015.
 */
public class Coin {
    private Bitmap bmp;
    private int frameActual=0;
    private int ancho, alto;
    private int ejeX, ejeY;
    private int direccionX, direccionY;
    private static  int anchoMax=0,altoMax=0;
    private static final int COLUMNAS = 10;
    private static final int FILAS = 1;


    public Coin(VistaJuego vista,Bitmap bmp) {
        this.bmp = bmp;
        this.ancho=bmp.getWidth();
        this.alto=bmp.getHeight();
        this.alto=bmp.getHeight()/FILAS;
        this.ancho=bmp.getWidth()/COLUMNAS;
    }

    public void setMovimiento(){
        direccionX=+1;
    }

    public void setPosicion(int y, int x){
        ejeX=x;
        ejeY=y;
    }

    private void movimiento(){
        frameActual = ++frameActual % COLUMNAS;
    }

    public int getX(){
        return ejeX;
    }

    public void dibujar(Canvas canvas) {
        movimiento();
        int origenx = frameActual * ancho;
        int origeny = 0;
        if(direccionX<0)
            origeny=alto;
        else
            origeny=0;
        Rect origen = new Rect(origenx, origeny, origenx + ancho, origeny +
                alto);
        Rect destino = new Rect(ejeX, ejeY, ejeX + ancho, ejeY + alto);
        canvas.drawBitmap(bmp, origen, destino, null);
    }

    public void eliminar(){
        direccionX=0;
        direccionY=0;
        setPosicion(-40,-40);
    }

    @Override
    public boolean equals(Object o) {
        Coin c=(Coin)o;
        if((this.ejeX>c.ejeX&&this.ejeX<c.ejeX+ancho)&&(this.ejeY>c.ejeY&&this.ejeY<c.ejeY+alto)){
            return true;
        }return false;
    }
}
