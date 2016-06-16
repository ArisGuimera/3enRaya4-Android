package com.example.dam.controldesde0;

/**
 * Created by 2dam on 11/11/2015.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;



import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Paint.Style;
import android.widget.TextView;
import android.widget.Toast;

public class TresEnRaya extends View {

    public static final int VACIA = 0;
    public static final int FICHA_O = 1;
    public static final int FICHA_X = 2;

    private int[][] tablero;
    private int fichaActiva;
    private int xColor;
    private int oColor;
    private OnCasillaSeleccionadaListener listener;
    private Activity activity;
    SoundPool s;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int ancho = calcularAncho(widthMeasureSpec);
        int alto = calcularAlto(heightMeasureSpec);

        if (ancho < alto)
            alto = ancho;
        else
            ancho = alto;

        setMeasuredDimension(ancho, alto);
    }

    private int calcularAlto(int limitesSpec) {
        int res = 100; //Alto por defecto

        int modo = MeasureSpec.getMode(limitesSpec);
        int limite = MeasureSpec.getSize(limitesSpec);

        if (modo == MeasureSpec.AT_MOST) {
            res = limite;
        } else if (modo == MeasureSpec.EXACTLY) {
            res = limite;
        }

        return res;
    }

    private int calcularAncho(int limitesSpec) {
        int res = 100; //Ancho por defecto

        int modo = MeasureSpec.getMode(limitesSpec);
        int limite = MeasureSpec.getSize(limitesSpec);

        if (modo == MeasureSpec.AT_MOST) {
            res = limite;
        } else if (modo == MeasureSpec.EXACTLY) {
            res = limite;
        }

        return res;
    }


    public TresEnRaya(Context context) {
        super(context);

        inicializacion();
    }

    public TresEnRaya(Context context, AttributeSet attrs, int defaultStyle) {
        super(context, attrs, defaultStyle);

        inicializacion();

        // Procesamos los atributos XML personalizados
        TypedArray a =
                getContext().obtainStyledAttributes(attrs,
                        R.styleable.TresEnRaya);

        oColor = a.getColor(
                R.styleable.TresEnRaya_ocolor, Color.BLUE);

        xColor = a.getColor(
                R.styleable.TresEnRaya_xcolor, Color.RED);

        a.recycle();
    }

    public TresEnRaya(Context context, AttributeSet attrs) {
        super(context, attrs);

        inicializacion();

        // Procesamos los atributos XML personalizados
        TypedArray a =
                getContext().obtainStyledAttributes(attrs,
                        R.styleable.TresEnRaya);

        oColor = a.getColor(
                R.styleable.TresEnRaya_ocolor, Color.BLUE);

        xColor = a.getColor(
                R.styleable.TresEnRaya_xcolor, Color.RED);

        a.recycle();
    }

    private void inicializacion() {
        tablero = new int[4][4];
        limpiar();

        fichaActiva = FICHA_X;
        xColor = Color.RED;
        oColor = Color.BLUE;
    }

    public void limpiar() {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                tablero[i][j] = VACIA;
    }


    public void setFichaActiva(int ficha) {
        fichaActiva = ficha;
    }

    public int getFichaActiva() {
        return fichaActiva;
    }

    public void alternarFichaActiva() {
        if (fichaActiva == FICHA_O)
            fichaActiva = FICHA_X;
        else
            fichaActiva = FICHA_O;
    }

    public void setXColor(int color) {
        xColor = color;
    }

    public int getXColor() {
        return xColor;
    }

    public void setOColor(int color) {
        oColor = color;
    }

    public int getOColor() {
        return oColor;
    }

    public void setCasilla(int fil, int col, int valor) {
        tablero[fil][col] = valor;
    }

    public int getCasilla(int fil, int col) {
        return tablero[fil][col];
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //Obtenemos las dimensiones del control
        int alto = getMeasuredHeight();
        int ancho = getMeasuredWidth();

        //Lineas
        Paint pBorde = new Paint();
        pBorde.setStyle(Paint.Style.STROKE);
        pBorde.setColor(Color.BLACK);
        pBorde.setStrokeWidth(8);

        canvas.drawLine(ancho / 4, 0, ancho / 4, alto, pBorde);
        canvas.drawLine(2 * ancho / 4, 0, 2 * ancho / 4, alto, pBorde);
        canvas.drawLine(3 * ancho / 4, 0, 3 * ancho / 4, alto, pBorde);

        canvas.drawLine(0, alto / 4, ancho, alto / 4, pBorde);
        canvas.drawLine(0, 2 * alto / 4, ancho, 2 * alto / 4, pBorde);
        canvas.drawLine(0, 3 * alto / 4, ancho, 3 * alto / 4, pBorde);


        //Marco
        canvas.drawRect(0, 0, ancho, alto, pBorde);

        //Marcas
        Paint pMarcaO = new Paint();
        pMarcaO.setStyle(Paint.Style.STROKE);
        pMarcaO.setStrokeWidth(8);
        pMarcaO.setColor(oColor);

        Paint pMarcaX = new Paint();
        pMarcaX.setStyle(Paint.Style.STROKE);
        pMarcaX.setStrokeWidth(8);
        pMarcaX.setColor(xColor);

        //Casillas Seleccionadas
        for (int fil = 0; fil < 4; fil++) {
            for (int col = 0; col < 4; col++) {

                if (tablero[fil][col] == FICHA_X) {
                    //Cruz
                    canvas.drawLine(
                            col * (ancho / 4) + (ancho / 4) * 0.1f,
                            fil * (alto / 4) + (alto / 4) * 0.1f,
                            col * (ancho / 4) + (ancho / 4) * 0.9f,
                            fil * (alto / 4) + (alto / 4) * 0.9f,
                            pMarcaX);

                    canvas.drawLine(
                            col * (ancho / 4) + (ancho / 4) * 0.1f,
                            fil * (alto / 4) + (alto / 4) * 0.9f,
                            col * (ancho / 4) + (ancho / 4) * 0.9f,
                            fil * (alto / 4) + (alto / 4) * 0.1f,
                            pMarcaX);
                } else if (tablero[fil][col] == FICHA_O) {
                    //Circulo
                    canvas.drawCircle(
                            col * (ancho / 4) + (ancho / 8),
                            fil * (alto / 4) + (alto / 8),
                            (ancho / 8) * 0.8f, pMarcaO);
                }
            }
        }
    }


    public interface OnCasillaSeleccionadaListener {
        void onCasillaSeleccionada(int fila, int columna);
    }

    public void setOnCasillaSeleccionadaListener(OnCasillaSeleccionadaListener l) {
        listener = l;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int fil = (int) (event.getY() / (getMeasuredHeight() / 4));
        int col = (int) (event.getX() / (getMeasuredWidth() / 4));


        if (tablero[fil][col] == VACIA) {
            tablero[fil][col] = fichaActiva;
            if (fichaActiva == FICHA_O) {
                fichaActiva = FICHA_X;
            } else {
                fichaActiva = FICHA_O;
            }
        } else {

            Toast toast1 =
                    Toast.makeText(activity.getApplicationContext(),
                            "Esa casilla está ocupada", Toast.LENGTH_SHORT);

            toast1.show();
        }


        compruebaGanador();


        this.invalidate();
        return super.onTouchEvent(event);
    }

    public void compruebaGanador() {
        diagonalSegunda();
        diagonalPrimera();
        diagonalTercera();
        diagonalCuarta();
        diagonalQuinta();
        diagonalSexta();
        calculaHorizontal(0, 3);
        calculaHorizontal(1, 4);
        calculaVertical(0,3);
        calculaVertical(1,4);
        empate();

    }

    public void diagonalPrimera() {


        if (tablero[0][0] == FICHA_X && tablero[1][1] == FICHA_X && tablero[2][2] == FICHA_X) {

            ganaX();

        } else if (tablero[0][0] == FICHA_O && tablero[1][1] == FICHA_O && tablero[2][2] == FICHA_O ) {

            ganaY();

        }

        if (tablero[3][3] == FICHA_X && tablero[1][1] == FICHA_X && tablero[2][2] == FICHA_X) {

            ganaX();

        } else if (tablero[3][3] == FICHA_O && tablero[1][1] == FICHA_O && tablero[2][2] == FICHA_O ) {

            ganaY();

        }

    }

    public void diagonalSegunda() {


        if (tablero[0][3] == FICHA_X && tablero[1][2] == FICHA_X && tablero[2][1] == FICHA_X) {

            ganaX();

        } else if (tablero[0][3] == FICHA_O && tablero[1][2] == FICHA_O && tablero[2][1] == FICHA_O) {

            ganaY();

        }

        if (tablero[3][0] == FICHA_X && tablero[1][2] == FICHA_X && tablero[2][1] == FICHA_X) {

            ganaX();

        } else if (tablero[3][0] == FICHA_O && tablero[1][2] == FICHA_O && tablero[2][1] == FICHA_O) {

            ganaY();

        }

    }

    public void diagonalTercera() {


        if (tablero[0][1] == FICHA_X && tablero[1][2] == FICHA_X && tablero[2][3] == FICHA_X) {

            ganaX();

        } else if (tablero[0][1] == FICHA_O && tablero[1][2] == FICHA_O && tablero[2][3] == FICHA_O) {

            ganaY();

        }

    }

    public void diagonalCuarta() {


        if (tablero[1][0] == FICHA_X && tablero[2][1] == FICHA_X && tablero[3][2] == FICHA_X) {

            ganaX();

        } else if (tablero[1][0] == FICHA_O && tablero[2][1] == FICHA_O && tablero[3][2] == FICHA_O) {

            ganaY();

        }

    }

    public void diagonalQuinta() {


        if (tablero[0][2] == FICHA_X && tablero[1][1] == FICHA_X && tablero[2][0] == FICHA_X) {

            ganaX();

        } else if (tablero[0][2] == FICHA_O && tablero[1][1] == FICHA_O && tablero[2][0] == FICHA_O) {

            ganaY();

        }

    }

    public void diagonalSexta() {


        if (tablero[1][3] == FICHA_X && tablero[2][2] == FICHA_X && tablero[3][1] == FICHA_X) {

            ganaX();

        } else if (tablero[1][3] == FICHA_O && tablero[2][2] == FICHA_O && tablero[3][1] == FICHA_O) {

            ganaY();

        }

    }

    public void calculaHorizontal( int dato, int fin) {
        int x;
        int o;
        for (int i = 0; i < 4; i++) {
            x = 0;
            o = 0;
            for (int j = dato; j < fin; j++) {
                if (tablero[i][j] == FICHA_X) {
                    x++;
                } else if (tablero[i][j] == FICHA_O) {
                    o++;
                }
            }
            if (x == 3) {
                ganaX();
            } else if (o == 3) {
                ganaY();
            }
        }

    }

    public void calculaVertical(int dato, int fin) {
        int x;
        int o;
        for (int i = dato; i < fin; i++) {
            x = 0;
            o = 0;
            for (int j = 0; j < 4; j++) {
                if (tablero[j][i] == FICHA_X) {
                    x++;
                } else if (tablero[j][i] == FICHA_O) {
                    o++;
                }
            }
            if (x == 3) {
                ganaX();
            } else if (o == 3) {
                ganaY();
            }
        }

    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }


    public void ganaX() {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this.getContext());

        builder.setMessage("¿Revancha?")
                .setTitle("Ganador X")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        limpiar();
                        invalidate();
                        Log.i("Dialogos", "Confirmacion Aceptada.");
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        System.exit(0);
                        Log.i("Dialogos", "Confirmacion Cancelada.");

                    }
                })
                .show();
        ;

    }

    public void ganaY() {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this.getContext());

        builder.setMessage("¿Revancha?")
                .setTitle("Ganador Y")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        limpiar();
                        invalidate();
                        Log.i("Dialogos", "Confirmacion Aceptada.");
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        System.exit(0);
                        Log.i("Dialogos", "Confirmacion Cancelada.");

                    }
                })
                .show();
        ;
    }

    public void reiniciar() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tablero[i][j] = VACIA;
            }
        }


        invalidate();
    }


    public void empate() {
        int contador = 0;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (tablero[i][j] == FICHA_O || tablero[i][j] == FICHA_X) {
                    contador++;
                }
            }
        }
        if(contador == 16){
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(this.getContext());

            builder.setMessage("¿Revancha?")
                    .setTitle("EMPATE")
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            limpiar();
                            invalidate();
                            Log.i("Dialogos", "Confirmacion Aceptada.");
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            System.exit(0);
                            Log.i("Dialogos", "Confirmacion Cancelada.");

                        }
                    })
                    .show();
            ;
        }
    }
}