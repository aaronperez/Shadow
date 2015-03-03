package com.aaron.shadow;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;

/**
 * Created by 2dam on 02/02/2015.
 */
public class HebraJuego extends Thread {

    private VistaJuego vista;
    private boolean funcionando = false;
    private static final long FPS = 20;


    public HebraJuego(VistaJuego vj) {
        this.vista = vj;
    }

    public void setFuncionando(boolean f) {
        funcionando = f;
    }

    @Override
    public void run() {
        long inicio;
        long ticksPS = 1000 / FPS;
        long tiempoEspera;
        while (funcionando) {
            Canvas canvas = null;
            inicio = System.currentTimeMillis();
            try {
                canvas = vista.getHolder().lockCanvas();
                synchronized (vista.getHolder()) {
                    vista.draw(canvas);
                }
            }catch (NullPointerException e){
                funcionando = false;
            }finally {
                if (canvas != null) {
                    vista.getHolder().unlockCanvasAndPost(canvas);
                }
            }
            tiempoEspera = ticksPS - (System.currentTimeMillis() - inicio);
            try {
                if (tiempoEspera > 0) {
                    sleep(tiempoEspera);
                }
                else {
                    sleep(10);
                }
            } catch (InterruptedException e) {}
        }
    }

}