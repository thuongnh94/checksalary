package haui.vn.checksalary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class CheckSalaryActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth firebaseAuth;
    private TextView tvUser, tvName, tvChucVu, tvLCB, tvPhuCap, tvTong;
    private TextView tvNhaO, tvDiLai, tvTN, tvBHYT, tvBHXH, tvBHTN;
    private Button btnLogout;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_salary);
        getSupportActionBar().hide();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));

        }
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //Ánh xạ các views
        tvUser = (TextView) findViewById(R.id.tvUser);
        tvName = (TextView) findViewById(R.id.tvName);
        tvChucVu = (TextView) findViewById(R.id.tvChucVu);
        tvLCB = (TextView) findViewById(R.id.tvLCB);
        tvPhuCap = (TextView) findViewById(R.id.tvPhuCap);
        tvNhaO = (TextView) findViewById(R.id.tvNhaO);
        tvDiLai = (TextView) findViewById(R.id.tvDiLai);
        tvTN = (TextView) findViewById(R.id.tvTN);
        tvBHYT = (TextView) findViewById(R.id.tvBHYT);
        tvBHXH = (TextView) findViewById(R.id.tvBHXH);
        tvBHTN = (TextView) findViewById(R.id.tvBHTN);
        tvTong = (TextView) findViewById(R.id.tvTong);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        String email = user.getEmail();
        String key = email.replace('.', '_');
        tvUser.setText("Xin chào " + email);
        btnLogout.setOnClickListener(this);
        databaseReference.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                NumberFormat vnd = new DecimalFormat("#,###");
                Staff staff = new Staff();
                long tong = staff.luongcb + staff.phu_cap + staff.trach_nhiem + staff.nha_o + staff.di_lai - staff.bhyt - staff.bhxh - staff.bhtn;
                tvName.setText("Họ tên: " + staff.name);
                tvChucVu.setText("Chức vụ: " + staff.chuc_vu);
                tvLCB.setText("Lương cơ bản: "  + vnd.format(staff.luongcb) + " VNĐ");
                tvPhuCap.setText("Phụ cấp: " + vnd.format(staff.phu_cap) + " VNĐ");
                tvNhaO.setText("Phụ cấp chỗ ở: " + vnd.format(staff.nha_o) + " VNĐ");
                tvDiLai.setText("Phụ cấp đi lại: " + vnd.format(staff.di_lai) + " VNĐ");
                tvTN.setText("Phụ cấp trách nhiệm: " + vnd.format(staff.trach_nhiem) + " VNĐ");
                tvBHYT.setText("Bảo hiểm y tế: " + vnd.format(staff.bhyt) + " VNĐ");
                tvBHXH.setText("Bảo hiểm xã hội: " + vnd.format(staff.bhxh) + " VNĐ");
                tvBHTN.setText("Bảo hiểm thất nghiệp: " + vnd.format(staff.bhtn) + " VNĐ");
                tvTong.setText("Tổng: " + vnd.format(tong) + " VNĐ");
                if (dataSnapshot == null || dataSnapshot.getValue() == null) {
                    Toast.makeText(getApplicationContext(), "Dữ liệu không có sẵn", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    staff = dataSnapshot.getValue(Staff.class);
                    tong = staff.luongcb + staff.phu_cap + staff.trach_nhiem + staff.nha_o + staff.di_lai - staff.bhyt - staff.bhxh - staff.bhtn;
                    tvName.setText("Họ tên: " + staff.name);
                    tvChucVu.setText("Chức vụ: " + staff.chuc_vu);
                    tvLCB.setText("Lương cơ bản: "  + vnd.format(staff.luongcb) + " VNĐ");
                    tvPhuCap.setText("Phụ cấp: " + vnd.format(staff.phu_cap) + " VNĐ");
                    tvNhaO.setText("Phụ cấp chỗ ở: " + vnd.format(staff.nha_o) + " VNĐ");
                    tvDiLai.setText("Phụ cấp đi lại: " + vnd.format(staff.di_lai) + " VNĐ");
                    tvTN.setText("Phụ cấp trách nhiệm: " + vnd.format(staff.trach_nhiem) + " VNĐ");
                    tvBHYT.setText("Bảo hiểm y tế: " + vnd.format(staff.bhyt) + " VNĐ");
                    tvBHXH.setText("Bảo hiểm xã hội: " + vnd.format(staff.bhxh) + " VNĐ");
                    tvBHTN.setText("Bảo hiểm thất nghiệp: " + vnd.format(staff.bhtn) + " VNĐ");
                    tvTong.setText("Tổng: " + vnd.format(tong) + " VNĐ");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CheckSalaryActivity.this, "...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == btnLogout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
