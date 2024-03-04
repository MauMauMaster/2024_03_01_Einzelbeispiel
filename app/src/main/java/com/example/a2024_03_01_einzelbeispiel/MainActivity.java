package com.example.a2024_03_01_einzelbeispiel;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Arrays;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private EditText editTextMatriculationNumber;
    private TextView textViewResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        editTextMatriculationNumber = findViewById(R.id.editTextMatriculationNumber);
        textViewResponse = findViewById(R.id.textViewResponse);
        Button buttonSend = findViewById(R.id.buttonSend);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMatriculationNumber();
            }
        });

        Button buttonCalculate = findViewById(R.id.buttonCalculate);
        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performModuloCalculation();
            }
        });
    }

    private void performModuloCalculation() {
        String matriculationNumberStr = editTextMatriculationNumber.getText().toString();
        if (!matriculationNumberStr.isEmpty() && matriculationNumberStr.length() >= 8 && matriculationNumberStr.length() <= 12) {

            String calculationResult = checkForCommonDivisors(matriculationNumberStr);
            textViewResponse.setText(calculationResult.isEmpty() ? "Keine gemeinsamen Teiler gefunden" : calculationResult);
        } else {
            textViewResponse.setText("Bitte geben Sie eine gültige Matrikelnummer mit einer Länge zwischen 8 und 12 Zeichen ein.");
        }
    }




    private void sendMatriculationNumber() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String matriculationNumber = editTextMatriculationNumber.getText().toString();
                    Socket socket = new Socket("se2-submission.aau.at", 20080);
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    out.println(matriculationNumber);
                    final String response = in.readLine();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewResponse.setText(response);
                        }
                    });

                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


//Hilfsmethode
    private int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    // Aufgabenmethoden

    private String checkForCommonDivisors(String number) {
        StringBuilder commonDivisors = new StringBuilder();
        for (int i = 0; i < number.length(); i++) {
            for (int j = i + 1; j < number.length(); j++) {
                if (gcd(Character.getNumericValue(number.charAt(i)), Character.getNumericValue(number.charAt(j))) > 1) {
                    commonDivisors.append(String.format("(%d, %d) ", i, j));
                }
            }
        }
        return commonDivisors.toString();
    }


}






