package co.edu.udea.compumovil.proyectogr8.foodea;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import co.edu.udea.compumovil.proyectogr8.foodea.Database.DBAdapter;

public class RegisterActivity extends AppCompatActivity {

    public final static String KEY_USER = "Username";
    public final static String KEY_PASS = "Password";

    //Variables globales
    private EditText username, password, email, passwordConfirmation;
    private TextView passwordMessage;
    private Button btnSave;
    private DBAdapter dbConexion;
    private boolean enabledButton;  //Variable para controlar que todos los campos sean llenados


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Registro");
        setContentView(R.layout.register);
        //Capturar los views como objetos
        username = (EditText) findViewById(R.id.et_user_register);
        password=(EditText) findViewById(R.id.et_password_register);
        email=(EditText) findViewById(R.id.et_email_register);
        passwordConfirmation= (EditText) findViewById(R.id.et_password_confirmation);
        btnSave = (Button) findViewById(R.id.btn_save);
        passwordMessage = (TextView) findViewById(R.id.tv_password_message);

        //Se añaden los listeners
        passwordConfirmation.setOnEditorActionListener(editorActionListener);
        passwordConfirmation.addTextChangedListener(textWatcher);

        //Se crea la conexión a la base de datos
        dbConexion = new DBAdapter(this);
        dbConexion.openConnection();
    }

    public void onClick(View v) {

        switch (v.getId()) {
            //Insertar datos en la base de datos
            case R.id.btn_save:
                String name = username.getText().toString();
                String password=this.password.getText().toString();
                String email = this.email.getText().toString();
                dbConexion.insertUser(name, password, email);
                dbConexion.closeConnection();
                finish();
                break;

            //Validar que no queden campos de texto vacios
            case R.id.et_user_register:
            case R.id.et_password_register:
            case R.id.et_email_register:
            case R.id.et_password_confirmation:
                checkSaveButton();
                break;

            default:
                break;
        }
    }

    //Metodo para validar que se hayan llenado los  campos
    private void checkSaveButton() {
        enabledButton = username.getText().toString().equals("") ||
                email.getText().toString().equals("") ||
                passwordConfirmation.getText().toString().equals("") ||
                password.getText().toString().equals("") ||
                !passwordMessage.getText().toString().equals("");
        btnSave.setEnabled(!enabledButton);
    }
    //TextWatcher para verificar que ambas claves ingresadas coinciden
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(password.getText().toString().equals(s.toString())){
                passwordMessage.setText("");
            }else{
                passwordMessage.setText("Las contraseñas no coinciden");
                btnSave.setEnabled(false);
            }


        }
    };

    //Validar que cuando se termine de llenar el ultimo EditText, se hayan llenado los demás
    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if(actionId== EditorInfo.IME_ACTION_DONE) {
                checkSaveButton();
                return false;
            }
            return true;
        }
    };
    @Override
    public void finish() {

        //Preparar los datos a retornar a la actividad principal
        Intent loginData = new Intent();
        loginData.putExtra(KEY_USER,username.getText().toString());
        loginData.putExtra(KEY_PASS, password.getText().toString());

        //Finalizar la actividad y retornar los datos
        setResult(RESULT_OK,loginData);
        super.finish();
    }
}

