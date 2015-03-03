package com.aaron.shadow;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by Aaron on 02/03/2015.
 */
public class Temple {
    private Bitmap bmp;
    private int ancho, alto;
    private int ejeX, ejeY;


    public Temple(VistaJuego vista,Bitmap bmp) {
        this.bmp = bmp;
        this.ancho=bmp.getWidth();
        this.alto=bmp.getHeight();
        this.alto=bmp.getHeight();
        this.ancho=bmp.getWidth();
    }


    public void setPosicion(int y, int x){
        ejeX=x;
        ejeY=y;
    }

    public int getX(){
        return ejeX;
    }

    public void dibujar(Canvas canvas) {
        int origenx = 0;
        int origeny = 0;
        Rect origen = new Rect(origenx, origeny, origenx + ancho, origeny +
                alto);
        Rect destino = new Rect(ejeX, ejeY, ejeX + ancho, ejeY + alto);
        canvas.drawBitmap(bmp, origen, destino, null);
    }

}