package com.crhealth.customview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    private Button btnMain ;
    private Button btnMain3 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        btnMain = ( Button )findViewById( R.id.btn_main ) ;
        btnMain3 = ( Button )findViewById( R.id.btn_main3 ) ;

        btnMain.setOnClickListener( this ) ;
        btnMain3.setOnClickListener( this ) ;
    }

    @Override
    public void onClick(View v) {
        switch( v.getId() ){
            case R.id.btn_main :{
                Intent intent = new Intent( this , MainActivity.class ) ;
                startActivity( intent ) ;
                break ;
            }
            case R.id.btn_main3 :{
                Intent intent = new Intent( this , Main3Activity.class ) ;
                startActivity( intent ) ;
                break ;
            }
        }
    }
}
