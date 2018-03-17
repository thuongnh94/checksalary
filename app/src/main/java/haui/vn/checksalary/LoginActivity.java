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
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            finish();
            if (user.getEmail().equals("admin@haui.vn"))
                startActivity(new Intent(getApplicationContext(), AddInfoActivity.class));
            else
                startActivity(new Intent(getApplicationContext(), CheckSalaryActivity.class));
        }
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        progressDialog = new ProgressDialog(this);
        btnLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
    }

    private void userLogin() {
        final String email = edtEmail.getText().toString().trim();
        String pass = edtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Vui lòng nhập Email!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Vui lòng nhập mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Đang đăng nhập...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            finish();
                            if (email.equals("admin@haui.vn"))
                                startActivity(new Intent(getApplicationContext(), AddInfoActivity.class));
                            else
                                startActivity(new Intent(getApplicationContext(), CheckSalaryActivity.class));
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại! Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if (view == btnLogin) {
            userLogin();
        }
        if (view == tvRegister) {
            finish();
            startActivity(new Intent(this, RegisterActivity.class));
        }
    }
}
