package haui.vn.checksalary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class AddInfoActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private EditText edtEmail, edtName, edtLCB, edtPhuCap;
    private Spinner spinner_chucvu;
    private TextView tvNhaO, tvDiLai, tvTN, tvBHYT, tvBHXH, tvBHTN;
    private FirebaseAuth firebaseAuth;
    private Button btnLogout, btnSave;
    private DatabaseReference databaseReference;
    private ArrayAdapter<String> arrayAdapter;
    private String chucvu = "", lcb = "", pc = "";
    private long luongcb = 0, phucap = 0, nha_o = 0, di_lai = 0, trach_nhiem = 0;
    private NumberFormat vnd = new DecimalFormat("#,###");
    String[] positions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_info);
        getSupportActionBar().hide();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));

        }
        Toast.makeText(this, "Xin chào " + user.getEmail(), Toast.LENGTH_LONG).show();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //Ánh xạ các views
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtName = (EditText) findViewById(R.id.edtName);
        spinner_chucvu = (Spinner) findViewById(R.id.spinner_chucvu);
        edtLCB = (EditText) findViewById(R.id.edtLCB);
        edtPhuCap = (EditText) findViewById(R.id.edtPhuCap);
        tvNhaO = (TextView) findViewById(R.id.tvNhaO);
        tvDiLai = (TextView) findViewById(R.id.tvDiLai);
        tvTN = (TextView) findViewById(R.id.tvTN);
        tvBHYT = (TextView) findViewById(R.id.tvBHYT);
        tvBHXH = (TextView) findViewById(R.id.tvBHXH);
        tvBHTN = (TextView) findViewById(R.id.tvBHTN);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnSave = (Button) findViewById(R.id.btnSave);
        positions = getResources().getStringArray(R.array.position_array);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, positions);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_chucvu.setAdapter(arrayAdapter);
        spinner_chucvu.setOnItemSelectedListener(this);
        edtLCB.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                lcb = edtLCB.getText().toString();
                if (TextUtils.isEmpty(lcb)) {
                    return;
                }
                luongcb = Long.parseLong(lcb);
                tvBHYT.setText(vnd.format(luongcb / 100) + " VNĐ");
                tvBHXH.setText(vnd.format(luongcb * 8 / 100) + " VNĐ");
                tvBHTN.setText(vnd.format(luongcb / 100) + " VNĐ");
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        btnLogout.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    private void saveInfo() {
        String email = edtEmail.getText().toString().trim();
        String name = edtName.getText().toString().trim();
        pc = edtPhuCap.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Vui lòng nhập Email!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Vui lòng nhập tên!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(lcb)) {
            Toast.makeText(this, "Vui lòng nhập lương cơ bản!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pc)) {
            Toast.makeText(this, "Vui lòng nhập phụ cấp!", Toast.LENGTH_SHORT).show();
            return;
        }
        phucap = Long.parseLong(pc);
        String key = email.replace('.', '_');
        Staff staff = new Staff(email, name, chucvu, luongcb, phucap, nha_o, di_lai, trach_nhiem);
        databaseReference.child(key).setValue(staff);
        Toast.makeText(this, "Đã lưu!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        if (view == btnSave) {
            saveInfo();
        }
        if (view == btnLogout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        chucvu = positions[i];
        if (chucvu.equals("Giám đốc")) { //Giám đốc được hưởng phụ cấp trách nhiệm
            trach_nhiem = 2000000;
            nha_o = di_lai = 0;
        } else if (chucvu != null) { //Nhân viên được hưởng phụ cấp đi lại và nhà ở
            trach_nhiem = 0;
            nha_o = di_lai = 500000;
        }
        tvTN.setText(vnd.format(trach_nhiem) + " VNĐ");
        tvDiLai.setText(vnd.format(di_lai) + " VNĐ");
        tvNhaO.setText(vnd.format(nha_o) + " VNĐ");
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
