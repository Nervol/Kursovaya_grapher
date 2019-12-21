package com.example.kursovaya_grapher;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<View> allEds; //Создаем список View которые будут создаваться
    private int counter = 0; //счетчик для кнопки addButton

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button addButton = (Button) findViewById(R.id.buttonAdd);
        Button answButton = (Button) findViewById(R.id.butt_spravka);
        allEds = new ArrayList<View>(); //инициализируем arraylist View-ов
        final LinearLayout linear = (LinearLayout) findViewById(R.id.linear);//подкрепляем linear, который под кнопкой addButton в activity_main.xml
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //listener на кнопку добавления
                if (counter != 3) { //ограничение по функ.требованию
                    final View view = getLayoutInflater().inflate(R.layout.custom_edittext_layout, null); //подцепляем разметку
                    //задаем кнопку удаления и обработчик по нажатию
                    Button deleteField = (Button) view.findViewById(R.id.buttonDel);
                    deleteField.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                ((LinearLayout) view.getParent()).removeView(view);
                                allEds.remove(view);
                                --counter;
                            } catch (IndexOutOfBoundsException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                    ;
                    allEds.add(view); //добавляем все что создаем
                    linear.addView(view);//добавляем елементы в linearlayout
                    counter++;
                } else
                    Toast.makeText(getApplicationContext(), "Превышен лимит создания функций!", Toast.LENGTH_LONG).show();
            }
        });
        Button btnStart = (Button) findViewById(R.id.buttonStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter == 0) {
                    Toast.makeText(getApplicationContext(), "Невозможно создать графики. Вы не ввели функции!", Toast.LENGTH_LONG).show();
                } else {
                    boolean flag = true;
                    //проверка на пустоту поля
                    for (int i = 0; i < allEds.size(); i++) {
                        if (((EditText) allEds.get(i).findViewById(R.id.editText)).getText().toString().equals("")) {
                            flag = false;
                            Toast.makeText(getApplicationContext(), "Одно из полей пустое! Сократите количество полей или введите в пустое поле функцию.",
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                        //запускаем чтение всех элементов и передачу на второе активити
                        if (flag == true) {
                            String[] formulas = new String[allEds.size()];
                            for (int i = 0; i < allEds.size(); i++) {
                                formulas[i] = ((EditText) allEds.get(i).findViewById(R.id.editText)).getText().toString().toLowerCase();
                            }
                            Bundle bundle = new Bundle();
                            bundle.putStringArray("functions", formulas);
                            Intent intent = new Intent("android.intent.action.GRAPH");
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }
                }
        });
        answButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //вывод справки
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Поддерживаемые функции:")
                        .setMessage(getString(R.string.help_text) +"\n"+
                                getString(R.string.abs) +"\n"+
                                getString(R.string.acos) +"\n"+
                                getString(R.string.asin) +"\n"+
                                getString(R.string.atan) +"\n"+
                                getString(R.string.cbrt) +"\n"+
                                getString(R.string.ceil) +"\n"+
                                getString(R.string.cos) +"\n"+
                                getString(R.string.cosh) +"\n"+
                                getString(R.string.exp) +"\n"+
                                getString(R.string.floor) +"\n"+
                                getString(R.string.log) +"\n"+
                                getString(R.string.log10) +"\n"+
                                getString(R.string.log2) +"\n"+
                                getString(R.string.sin) +"\n"+
                                getString(R.string.sinh) +"\n"+
                                getString(R.string.sqrt) +"\n"+
                                getString(R.string.tan) +"\n"+
                                getString(R.string.tanh) +"\n"+
                                getString(R.string.signum))
                        .setCancelable(false)
                        .setNegativeButton("ОК, продолжить работу",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
}
