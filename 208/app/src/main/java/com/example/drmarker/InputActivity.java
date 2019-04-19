package com.example.drmarker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InputActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edit_height;
    private EditText edit_weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        getInfo();
    }

    private void getInfo(){
        //Get the height and weight
        edit_height=findViewById(R.id.edit_height);
        edit_weight=findViewById(R.id.edit_weight);
        //Initialize the button
        Button button_send = findViewById(R.id.button_send);
        button_send.setOnClickListener(this);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.button_send:
                //Initialize an intent to the receive class
                Intent intent=new Intent(InputActivity.this, NewActivity.class);
                //Get the input from text view
                String height=edit_height.getText().toString().trim();
                String weight=edit_weight.getText().toString().trim();
                //put the info into the intent and send it
                intent.putExtra("height",height);
                intent.putExtra("weight",weight);
                startActivity(intent);
        }
    }

}
