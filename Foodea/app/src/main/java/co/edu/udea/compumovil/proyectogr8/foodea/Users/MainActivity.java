package co.edu.udea.compumovil.proyectogr8.foodea.Users;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import co.edu.udea.compumovil.proyectogr8.foodea.Database.DBAdapter;
import co.edu.udea.compumovil.proyectogr8.foodea.HomeActivity;
import co.edu.udea.compumovil.proyectogr8.foodea.Model.User;
import co.edu.udea.compumovil.proyectogr8.foodea.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DBAdapter dbHandler;
    private boolean isEmpty;
    private EditText user,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Capturamos los views
        user =(EditText)findViewById(R.id.et_user_login);
        password=(EditText)findViewById(R.id.et_password_login);

        //Create the database handler
        dbHandler = new DBAdapter(this);

        //Open the connection
        dbHandler.openConnection();
        DBAdapter dbAdapter = new DBAdapter(getApplicationContext());
        dbAdapter.openConnection();
        User isOn = dbAdapter.getCurrentUser();

        if(isOn!=null) {
            Intent events = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(events);
        }





        //Launch the HomeActivity
        //Intent home = new Intent(MainActivity.this, HomeActivity.class);
        //startActivity(home);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (validateLogin()) {
                    //Se guarda el login en la base de datos
                    DBAdapter dbAdapter = new DBAdapter(this);
                    dbAdapter.openConnection();
                    dbAdapter.setCurrentLogin(user.getText().toString());
                    dbAdapter.closeConnection();
                    //Login correcto - Se lanza la otra actividad

                    Intent home = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(home);
                }

                //Se limpian los campos de texto
                user.setText("");
                password.setText("");
                    break;
                    case R.id.btn_register:
                        Intent register = new Intent(MainActivity.this, RegisterActivity.class);
                        startActivity(register);
                        break;

        }
    }

    private boolean validateLogin() {

        //Validamos el logeo
        boolean userEmpty, passwordEmpty,wrongData;
        userEmpty = user.getText().toString().equals("");
        passwordEmpty = password.getText().toString().equals("");

        //Validar que el campo usuario no este vacio
        if(userEmpty){
            Toast.makeText(getApplicationContext(), "Introduce tu nombre de usuario", Toast.LENGTH_SHORT).show();
            return false;
        }

        //Validar que el campo contraseña no este vacio
        if(passwordEmpty){
            Toast.makeText(getApplicationContext(),"Introduce tu contraseña",Toast.LENGTH_SHORT).show();
            return false;
        }

        //Validar que los datos ingresados esten correctos
        DBAdapter dbAdapter = new DBAdapter(this);
        dbAdapter.openConnection();
        wrongData=dbAdapter.getUser(getApplicationContext(),user.getText().toString(),password.getText().toString());
        dbAdapter.closeConnection();
        return wrongData;
    }


}
