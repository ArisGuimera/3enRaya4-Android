package com.example.dam.controldesde0;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TresEnRaya terTablero;
    private TextView txtCasilla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        terTablero = (TresEnRaya)findViewById(R.id.tablero);
        terTablero.setActivity(this);

        txtCasilla = (TextView)findViewById(R.id.txtCasilla);

        Button miBoton = (Button) findViewById(R.id.button2);









        terTablero.setOnCasillaSeleccionadaListener(new TresEnRaya.OnCasillaSeleccionadaListener() {
            @Override
            public void onCasillaSeleccionada(int fila, int columna) {
                txtCasilla.setText("Última casilla seleccionada: " + fila + "." + columna);
            }
        });
    }

    public void reiniciar(View v){

        terTablero.reiniciar();


    }

    //....
}
