package co.edu.udea.compumovil.proyectogr8.foodea;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v){
        switch (v.getId()) {
            case R.id.btn_login:
                Intent home = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(home);
                break;
            case R.id.btn_register:
                Intent register = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(register);
                break;
        }
    }

}
