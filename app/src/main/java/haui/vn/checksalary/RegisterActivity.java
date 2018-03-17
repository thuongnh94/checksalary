package haui.vn.checksalary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnRegister;
    private EditText edtEmail, edtPassword;
    private TextView tvLogin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(), CheckSalaryActivity.class));
        }
        btnRegister = (Button) findViewById(R.id.btnRegister);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        tvLogin = (TextView) findViewById(R.id.tvLogin);
        progressDialog = new ProgressDialog(this);
        btnRegister.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
    }
    private void registerUser(){
        String email = edtEmail.getText().toString().trim();
        String pass = edtPassword.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Vui lòng nhập Email!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this, "Vui lòng nhập mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Đang đăng ký...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), CheckSalaryActivity.class));
                        }
                        else
                            Toast.makeText(RegisterActivity.this, "Không thành công, xin hãy thử lại!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }
    @Override
    public void onClick(View view) {
        if (view == btnRegister){
            registerUser();
        }
        if (view == tvLogin){
            startActivity(new Intent(this,LoginActivity.class));
        }
    }
}
