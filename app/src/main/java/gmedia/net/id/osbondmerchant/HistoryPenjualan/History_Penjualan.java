package gmedia.net.id.osbondmerchant.HistoryPenjualan;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import gmedia.net.id.osbondmerchant.ConvertDate;
import gmedia.net.id.osbondmerchant.R;

public class History_Penjualan extends AppCompatActivity {
    private Button btnProses;
    private RelativeLayout btnAwal, btnAkhir;
    private TextView txtAwal, txtAkhir;
    public static String awal, akhir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_penjualan);
        initUI();
        initAction();
    }

    private void initUI() {
        btnProses = (Button) findViewById(R.id.btnProsesListHistoryPenjualan);
        btnAwal = (RelativeLayout) findViewById(R.id.btnAwal);
        btnAkhir = (RelativeLayout) findViewById(R.id.btnAkhir);
        txtAwal = (TextView) findViewById(R.id.txtTanggalMulai);
        txtAkhir = (TextView) findViewById(R.id.txtTanggalSelesai);
    }

    private void initAction() {
        btnAwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final java.util.Calendar customDate = java.util.Calendar.getInstance();
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        customDate.set(java.util.Calendar.YEAR, year);
                        customDate.set(java.util.Calendar.MONTH, month);
                        customDate.set(java.util.Calendar.DATE, dayOfMonth);
                        SimpleDateFormat sdFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                        txtAwal.setText(sdFormat.format(customDate.getTime()));
                        txtAwal.setAlpha(1);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        awal = sdf.format(customDate.getTime());

                    }
                };
                new DatePickerDialog(History_Penjualan.this, date, customDate.get(java.util.Calendar.YEAR), customDate.get(java.util.Calendar.MONTH), customDate.get(java.util.Calendar.DATE)).show();
            }
        });
        btnAkhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final java.util.Calendar customDate = java.util.Calendar.getInstance();
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        customDate.set(java.util.Calendar.YEAR, year);
                        customDate.set(java.util.Calendar.MONTH, month);
                        customDate.set(java.util.Calendar.DATE, dayOfMonth);
                        SimpleDateFormat sdFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                        txtAkhir.setText(sdFormat.format(customDate.getTime()));
                        txtAkhir.setAlpha(1);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        akhir = sdf.format(customDate.getTime());
                    }
                };
                new DatePickerDialog(History_Penjualan.this, date, customDate.get(java.util.Calendar.YEAR), customDate.get(java.util.Calendar.MONTH), customDate.get(java.util.Calendar.DATE)).show();
            }
        });
        btnProses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtAwal.getText().toString().equals("Tanggal Mulai") && txtAkhir.getText().toString().equals("Tanggal Selesai")) {
                    Toast.makeText(History_Penjualan.this, "Silahkan pilih tanggal mulai dan tanggal selesai terlebih dahulu", Toast.LENGTH_LONG).show();
                    return;
                } else if (txtAwal.getText().toString().equals("Tanggal Mulai")) {
                    Toast.makeText(History_Penjualan.this, "Silahkan pilih tanggal mulai", Toast.LENGTH_LONG).show();
                    return;
                } else if (txtAkhir.getText().toString().equals("Tanggal Selesai")) {
                    Toast.makeText(History_Penjualan.this, "Silahkan pilih tanggal selesai", Toast.LENGTH_LONG).show();
                    return;
                } else {
//                    awal = ConvertDate.parseDateToyyyyMMdd(txtAwal.getText().toString());
//                    akhir = ConvertDate.parseDateToyyyyMMdd(txtAkhir.getText().toString());
                    Intent intent = new Intent(History_Penjualan.this, ListHistoryPenjualan.class);
                    intent.putExtra("tglAwal", awal);
                    intent.putExtra("tglAkhir", akhir);
                    startActivity(intent);
                }
            }
        });
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("History Penjualan");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        txtAwal.setText("Tanggal Mulai");
        txtAwal.setAlpha(0.5f);
        txtAkhir.setText("Tanggal Selesai");
        txtAkhir.setAlpha(0.5f);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            /*case R.id.settings:
                onBackPressed();
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);*/
        return super.onCreateOptionsMenu(menu);
    }
}
