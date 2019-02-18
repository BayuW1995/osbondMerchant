package gmedia.net.id.osbondmerchant;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import cz.intik.overflowindicator.OverflowPagerIndicator;
import cz.intik.overflowindicator.SimpleSnapHelper;
import gmedia.net.id.osbondmerchant.HistoryKunjungan.HistoryKunjungan;
import gmedia.net.id.osbondmerchant.HistoryPenjualan.History_Penjualan;
import gmedia.net.id.osbondmerchant.MenuPenjualanVoucher.RecyclerViewAdapterPenjualanVoucher;
import gmedia.net.id.osbondmerchant.MenuPenjualanVoucher.SetGetMasterPaketBaruPenjualanVoucher;
import gmedia.net.id.osbondmerchant.MenuPenjualanViaEmail.RecyclerViewAdapterViaEmail;
import gmedia.net.id.osbondmerchant.MenuPenjualanViaEmail.SetGetMasterPaketBaruViaEmail;

public class MainActivity extends RuntimePermissionsActivity {
    private LinearLayout menuPenjualanVoucher, menuPemakaianVoucher, menuPenjualanViaEmail, menuHistoryPenjualan, menuHistoryKunjungan, menuDOKU;
    private List<SetGetMasterPaket> masterPaket;
    private List<SetGetMasterCabang> masterCabang;
    private ArrayAdapter<SetGetMasterPaket> adapterPaket;
    private ArrayAdapter<SetGetMasterCabang> adapterCabang;
    private Spinner dropdownPilihKupon, dropdownPilihJam;
    private Proses proses;
    private Dialog dialogPenjualan, dialogPemakaian;
    public static IntentResult resultScanBarcode;
    private String IDCabang = "0", IDPaket = "0";
    private Boolean WSPenjualan = false, WSPemakaian = false, doubleBackToExitPressedOnce = false;
    private EditText inputan;
    private SessionManager session;
    private static final int REQUEST_PERMISSIONS = 20;
    private String invoiceNumber;
    private JSONObject respongetTokenSDK;
    String jsonRespon;
    private TelephonyManager telephonyManager;
    private String imei;
    private EditText isianJualEmail, isianJumlahEmail, jumlahKuponDoku, inputanPIN;
    private String version, latestVersion, link;
    private TextView totalPenjualan;
    public static boolean kosong = true;
    private LinearLayoutManager layoutManagerPenjualan, layoutManagerViaEmail;
    private RecyclerView recyclerViewPenjualan, recyclerViewViaEmail;
    private ArrayList<SetGetMasterPaketBaruPenjualanVoucher> menuPaketPenjualan;
    private ArrayList<SetGetMasterPaketBaruViaEmail> menuPaketViaEmail;
    private RecyclerViewAdapterPenjualanVoucher adapterRVPenjualan;
    private RecyclerViewAdapterViaEmail adapterRvViaEmail;
    public static LinearLayout layoutDPCabang;
    public static SetGetMasterPaketBaruPenjualanVoucher selectedPaketPenjualanVoucher;
    public static SetGetMasterPaketBaruViaEmail selectedPaketViaEmail;
    private boolean updateRequired;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initAction();
    }

    private void initUI() {
        session = new SessionManager(MainActivity.this);
        proses = new Proses(MainActivity.this);
        menuPenjualanVoucher = (LinearLayout) findViewById(R.id.menuPenjualanVoucher);
        menuPemakaianVoucher = (LinearLayout) findViewById(R.id.menuPemakaianVoucher);
        menuPenjualanViaEmail = (LinearLayout) findViewById(R.id.menuPenjualanViaEmail);
        menuHistoryPenjualan = (LinearLayout) findViewById(R.id.menuHistoryPenjualan);
        menuHistoryKunjungan = (LinearLayout) findViewById(R.id.menuHistoryKunjungan);
        totalPenjualan = (TextView) findViewById(R.id.totalPenjualanEKupon);
    }

    private void initAction() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.action_bar);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            SettingPromo.openCamera.setEnabled(true);
            super.requestAppPermissions(new
                            String[]{Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.READ_PHONE_STATE},
                    R.string.runtime_permissions_txt, REQUEST_PERMISSIONS);
        }
//        Toast.makeText(getApplicationContext(),imei,Toast.LENGTH_LONG).show();
        menuPenjualanVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WSPenjualan = true;
                WSPemakaian = false;
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.popup_menu_penjualan_voucher);
//                dropdownPilihKupon = (Spinner) dialog.findViewById(R.id.dropdownJenisKuponEmail);
                dropdownPilihJam = (Spinner) dialog.findViewById(R.id.dropdownCabangPenjualan);
                RelativeLayout btnSave = (RelativeLayout) dialog.findViewById(R.id.btnSavePenjualan);
//                RelativeLayout btnCancel = (RelativeLayout) dialog.findViewById(R.id.btnCancel);
                layoutDPCabang = (LinearLayout) dialog.findViewById(R.id.layoutDropdownCabangPenjualan);
//                inputan = (EditText) dialog.findViewById(R.id.inputanEmail);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCanceledOnTouchOutside(false);
                layoutManagerPenjualan = new LinearLayoutManager(MainActivity.this, LinearLayout.HORIZONTAL, false);
                recyclerViewPenjualan = (RecyclerView) dialog.findViewById(R.id.rvViewPenjualan);
                recyclerViewPenjualan.setLayoutManager(layoutManagerPenjualan);
                final OverflowPagerIndicator overflowPagerIndicator = dialog.findViewById(R.id.indicatorPopupMenuPenjualan);
                jumlahKuponDoku = (EditText) dialog.findViewById(R.id.inputanJumlahKuponDokuPenjualan);
                jumlahKuponDoku.setText("");
                ApiVolley requestPaket = new ApiVolley(MainActivity.this, new JSONObject(), "GET", URL.MasterPaket, "", "", 0, new ApiVolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        menuPaketPenjualan = new ArrayList<>();
                        try {
                            JSONObject object = new JSONObject(result);
                            String status = object.getJSONObject("metadata").getString("status");
                            if (status.equals("200")) {
                                JSONArray response = object.getJSONArray("response");
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject isi = response.getJSONObject(i);
                                    menuPaketPenjualan.add(new SetGetMasterPaketBaruPenjualanVoucher(
                                            isi.getString("id"),
                                            isi.getString("image"),
                                            isi.getString("label"),
                                            isi.getString("keterangan"),
                                            isi.getString("harga")
                                    ));
                                }
                                recyclerViewPenjualan.setAdapter(null);
                                adapterRVPenjualan = new RecyclerViewAdapterPenjualanVoucher(MainActivity.this, menuPaketPenjualan);
                                recyclerViewPenjualan.setAdapter(adapterRVPenjualan);
                                overflowPagerIndicator.attachToRecyclerView(recyclerViewPenjualan);
                                new SimpleSnapHelper(overflowPagerIndicator).attachToRecyclerView(recyclerViewPenjualan);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String result) {
                        Toast.makeText(getApplicationContext(), "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
                    }
                });
                ApiVolley requestCabang = new ApiVolley(MainActivity.this, new JSONObject(), "GET", URL.MasterCabang, "", "", 0, new ApiVolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        masterCabang = new ArrayList<>();
                        try {
                            JSONObject object = new JSONObject(result);
                            String status = object.getJSONObject("metadata").getString("status");
                            if (status.equals("200")) {
                                JSONArray response = object.getJSONArray("response");
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject isiMasterCabang = response.getJSONObject(i);
                                    masterCabang.add(new SetGetMasterCabang(
                                            isiMasterCabang.getString("id"),
                                            isiMasterCabang.getString("cabang")
                                    ));
                                }
//                                android.R.layout.simple_spinner_item
                                adapterCabang = new ArrayAdapter<SetGetMasterCabang>(MainActivity.this, R.layout.layout_simple_list, masterCabang);
                                dropdownPilihJam.setAdapter(adapterCabang);
//                                dropdownPilihKupon.setSelection(0, true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String result) {
                        Toast.makeText(getApplicationContext(), "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
                    }
                });
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (kosong) {
                            Toast.makeText(getApplicationContext(), "Silahkan Pilih Paket", Toast.LENGTH_LONG).show();
                            return;
                        } else if (jumlahKuponDoku.getText().toString().equals("")) {
                            jumlahKuponDoku.setError("Mohon Di Isi");
                            jumlahKuponDoku.requestFocus();
                            return;
                        }
//                        proses.ShowDialog();
                        /*SetGetMasterCabang setGetMasterCabang = (SetGetMasterCabang) dropdownPilihJam.getSelectedItem();
                        posisiCabang = setGetMasterCabang.getId();*/
                        dialog.dismiss();
                        popupVerifikasiPenjualan();
//                        proses.DismissDialog();
                    }
                });
                dialog.show();
            }
        });
        menuPemakaianVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WSPemakaian = true;
                WSPenjualan = false;
                openScanBarcode();
            }
        });
        menuPenjualanViaEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.popup_menu_via_email);
//                dropdownPilihKupon = (Spinner) dialog.findViewById(R.id.dropdownJenisKuponEmail);
                dropdownPilihJam = (Spinner) dialog.findViewById(R.id.dropdownCabangViaEmail);
                RelativeLayout btnSave = (RelativeLayout) dialog.findViewById(R.id.btnSaveViaEmail);
//                RelativeLayout btnCancel = (RelativeLayout) dialog.findViewById(R.id.btnCancel);
                layoutDPCabang = (LinearLayout) dialog.findViewById(R.id.layoutDropdownCabangViaEmail);
//                inputan = (EditText) dialog.findViewById(R.id.inputanEmail);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCanceledOnTouchOutside(false);
                layoutManagerViaEmail = new LinearLayoutManager(MainActivity.this, LinearLayout.HORIZONTAL, false);
                recyclerViewViaEmail = (RecyclerView) dialog.findViewById(R.id.rvViewViaEmail);
                recyclerViewViaEmail.setLayoutManager(layoutManagerViaEmail);
                final OverflowPagerIndicator overflowPagerIndicator = dialog.findViewById(R.id.indicatorPopupMenuPenjualanViaEmail);
                isianJualEmail = (EditText) dialog.findViewById(R.id.inputanViaEmail);
                isianJumlahEmail = (EditText) dialog.findViewById(R.id.inputanJumlahKuponViaEmail);
                ApiVolley requestPaket = new ApiVolley(MainActivity.this, new JSONObject(), "GET", URL.MasterPaket, "", "", 0, new ApiVolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        menuPaketViaEmail = new ArrayList<>();
                        try {
                            JSONObject object = new JSONObject(result);
                            String status = object.getJSONObject("metadata").getString("status");
                            if (status.equals("200")) {
                                JSONArray response = object.getJSONArray("response");
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject isi = response.getJSONObject(i);
                                    menuPaketViaEmail.add(new SetGetMasterPaketBaruViaEmail(
                                            isi.getString("id"),
                                            isi.getString("image"),
                                            isi.getString("label"),
                                            isi.getString("keterangan"),
                                            isi.getString("harga")
                                    ));
                                }
                                recyclerViewViaEmail.setAdapter(null);
                                adapterRvViaEmail = new RecyclerViewAdapterViaEmail(MainActivity.this, menuPaketViaEmail);
                                recyclerViewViaEmail.setAdapter(adapterRvViaEmail);
                                overflowPagerIndicator.attachToRecyclerView(recyclerViewViaEmail);
                                new SimpleSnapHelper(overflowPagerIndicator).attachToRecyclerView(recyclerViewViaEmail);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String result) {
                        Toast.makeText(getApplicationContext(), "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
                    }
                });
                ApiVolley requestCabang = new ApiVolley(MainActivity.this, new JSONObject(), "GET", URL.MasterCabang, "", "", 0, new ApiVolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        masterCabang = new ArrayList<>();
                        try {
                            JSONObject object = new JSONObject(result);
                            String status = object.getJSONObject("metadata").getString("status");
                            if (status.equals("200")) {
                                JSONArray response = object.getJSONArray("response");
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject isiMasterCabang = response.getJSONObject(i);
                                    masterCabang.add(new SetGetMasterCabang(
                                            isiMasterCabang.getString("id"),
                                            isiMasterCabang.getString("cabang")
                                    ));
                                }
//                                android.R.layout.simple_spinner_item
                                adapterCabang = new ArrayAdapter<SetGetMasterCabang>(MainActivity.this, R.layout.layout_simple_list, masterCabang);
                                dropdownPilihJam.setAdapter(adapterCabang);
//                                dropdownPilihKupon.setSelection(0, true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String result) {
                        Toast.makeText(getApplicationContext(), "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
                    }
                });
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (kosong) {
                            Toast.makeText(getApplicationContext(), "Silahkan Pilih Paket", Toast.LENGTH_LONG).show();
                            return;
                        } else if (isianJualEmail.getText().toString().equals("")) {
                            isianJualEmail.setError("Mohon Di Isi");
                            isianJualEmail.requestFocus();
                            return;
                        } else if (isianJumlahEmail.getText().toString().equals("")) {
                            isianJumlahEmail.setError("Mohon Di Isi");
                            isianJumlahEmail.requestFocus();
                            return;
                        }
//                        proses.ShowDialog();
                        /*SetGetMasterCabang setGetMasterCabang = (SetGetMasterCabang) dropdownPilihJam.getSelectedItem();
                        posisiCabang = setGetMasterCabang.getId();*/
                        dialog.dismiss();
                        popupVerifikasiPenjualanViaEmail();
//                        popupVerifikasiDoku();

//                        proses.DismissDialog();
                    }
                });
                dialog.show();
            }
        });
        menuHistoryPenjualan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, History_Penjualan.class);
                startActivity(intent);
            }
        });
        menuHistoryKunjungan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HistoryKunjungan.class);
                startActivity(intent);
            }
        });
    }

    private void popupVerifikasiPenjualan() {
        proses.ShowDialog();
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.popup_verifikasi_penjualan);
        ImageView icon = (ImageView) dialog.findViewById(R.id.iconVerifDoku);
        TextView paket = (TextView) dialog.findViewById(R.id.txtPaketVerifDoku);
        TextView keterangan = (TextView) dialog.findViewById(R.id.txtKeteranganVerifDoku);
        TextView harga = (TextView) dialog.findViewById(R.id.txtHargaVerifDoku);
        TextView jumlahKupon = (TextView) dialog.findViewById(R.id.txtJumlahKuponVerifPenjualan);
        final TextView subtotal = (TextView) dialog.findViewById(R.id.subTotalVerifDoku);
        RelativeLayout bayar = (RelativeLayout) dialog.findViewById(R.id.goToPembayaranDoku);
        if (selectedPaketPenjualanVoucher != null) {
            Picasso.with(MainActivity.this).load(selectedPaketPenjualanVoucher.getIcon()).into(icon);
            paket.setText(selectedPaketPenjualanVoucher.getPaket());
            keterangan.setText(selectedPaketPenjualanVoucher.getKeterangan());
            harga.setText(ChangeToRupiahFormat(selectedPaketPenjualanVoucher.getHarga()));
            jumlahKupon.setText(jumlahKuponDoku.getText().toString());
            double nomHarga = parseNullDouble(selectedPaketPenjualanVoucher.getHarga());
            double nomJumlahKupon = parseNullDouble(jumlahKupon.getText().toString());
            double nomSubTotal = nomHarga * nomJumlahKupon;
//            nomSubTotalInt = (int) nomSubTotal;
            subtotal.setText(ChangeToRupiahFormat(doubleToStringFull(nomSubTotal)));
//            namaPaket = paket.getText().toString();
//            jumlahPaket = jumlahKupon.getText().toString();
//            nomSubTotalInt = Integer.parseInt(selectedPaketPenjualanVoucher.getHarga());
        }
        bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                popUpPinPenjualan();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        proses.DismissDialog();
    }

    private void popUpPinPenjualan() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.pop_up_insert_pin);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        inputanPIN = (EditText) dialog.findViewById(R.id.inputanPIN);
        RelativeLayout btnSave = (RelativeLayout) dialog.findViewById(R.id.btnSavePin);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputanPIN.getText().toString().equals("")) {
                    inputanPIN.setError("Mohon di isi");
                    inputanPIN.requestFocus();
                    return;
                }
                proses.ShowDialog();
                final JSONObject jBody = new JSONObject();
                try {
                    jBody.put("pin", inputanPIN.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ApiVolley request = new ApiVolley(MainActivity.this, jBody, "POST", URL.CheckPin, "", "", 0, new ApiVolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        proses.DismissDialog();
                        try {
                            JSONObject object = new JSONObject(result);
                            String status = object.getJSONObject("metadata").getString("status");
                            String message = object.getJSONObject("metadata").getString("message");
                            if (status.equals("200")) {
                                dialog.dismiss();
                                openScanBarcode();
                            } else {
                                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String result) {
                        proses.DismissDialog();
                        Toast.makeText(MainActivity.this, "terjadi kesalahan", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        RelativeLayout btnCancel = (RelativeLayout) dialog.findViewById(R.id.btnCancelPin);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void popupVerifikasiPenjualanViaEmail() {
        proses.ShowDialog();
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.popup_verifikasi_penjualan_via_email);
        ImageView icon = (ImageView) dialog.findViewById(R.id.iconVerifDoku);
        TextView paket = (TextView) dialog.findViewById(R.id.txtPaketVerifDoku);
        TextView keterangan = (TextView) dialog.findViewById(R.id.txtKeteranganVerifDoku);
        TextView harga = (TextView) dialog.findViewById(R.id.txtHargaVerifDoku);
        TextView email = (TextView) dialog.findViewById(R.id.isianEmail);
        TextView jumlahKupon = (TextView) dialog.findViewById(R.id.txtJumlahKuponVerifViaEmail);
        final TextView subtotal = (TextView) dialog.findViewById(R.id.subTotalVerifDoku);
        RelativeLayout bayar = (RelativeLayout) dialog.findViewById(R.id.goToPembayaranDoku);
        if (selectedPaketViaEmail != null) {
            Picasso.with(MainActivity.this).load(selectedPaketViaEmail.getIcon()).into(icon);
            paket.setText(selectedPaketViaEmail.getPaket());
            keterangan.setText(selectedPaketViaEmail.getKeterangan());
            harga.setText(ChangeToRupiahFormat(selectedPaketViaEmail.getHarga()));
            email.setText(isianJualEmail.getText().toString());
            jumlahKupon.setText(isianJumlahEmail.getText().toString());
            double nomHarga = parseNullDouble(selectedPaketViaEmail.getHarga());
            double nomJumlahKupon = parseNullDouble(jumlahKupon.getText().toString());
            double nomSubTotal = nomHarga * nomJumlahKupon;
//            nomSubTotalInt = (int) nomSubTotal;
            subtotal.setText(ChangeToRupiahFormat(doubleToStringFull(nomSubTotal)));
//            namaPaket = paket.getText().toString();
//            jumlahPaket = jumlahKupon.getText().toString();
//            nomSubTotalInt = Integer.parseInt(selectedPaketPenjualanVoucher.getHarga());
        }
        bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                popUpPinPenjualanViaEmail();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        proses.DismissDialog();
    }

    private void popUpPinPenjualanViaEmail() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.pop_up_insert_pin);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        inputanPIN = (EditText) dialog.findViewById(R.id.inputanPIN);
        RelativeLayout btnSave = (RelativeLayout) dialog.findViewById(R.id.btnSavePin);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputanPIN.getText().toString().equals("")) {
                    inputanPIN.setError("Mohon di isi");
                    inputanPIN.requestFocus();
                    return;
                }
                proses.ShowDialog();
                final JSONObject jBody = new JSONObject();
                try {
                    jBody.put("pin", inputanPIN.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ApiVolley request = new ApiVolley(MainActivity.this, jBody, "POST", URL.CheckPin, "", "", 0, new ApiVolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        proses.DismissDialog();
                        try {
                            JSONObject object = new JSONObject(result);
                            String status = object.getJSONObject("metadata").getString("status");
                            String message = object.getJSONObject("metadata").getString("message");
                            if (status.equals("200")) {
                                dialog.dismiss();
                                prepareDataJualViaEmail();
                            } else {
                                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String result) {
                        proses.DismissDialog();
                        Toast.makeText(MainActivity.this, "terjadi kesalahan", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
        RelativeLayout btnCancel = (RelativeLayout) dialog.findViewById(R.id.btnCancelPin);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkVersion();
        totalPenjualan();
    }

    private void totalPenjualan() {
        ApiVolley request = new ApiVolley(MainActivity.this, new JSONObject(), "GET", URL.totalPenjualan, "", "", 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String status = object.getJSONObject("metadata").getString("status");
                    String total = object.getJSONObject("response").getString("total");
                    if (status.equals("200")) {
                        totalPenjualan.setText(total);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String result) {

            }
        });
    }

    private void checkVersion() {
        PackageInfo pInfo = null;
        version = "";

        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version = pInfo.versionName;
//        getSupportActionBar().setSubtitle(getResources().getString(R.string.app_name) + " v "+ version);
        latestVersion = "";
        link = "";
        ApiVolley request = new ApiVolley(MainActivity.this, new JSONObject(), "GET", URL.upVersions, "", "", 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                JSONObject responseAPI;
                try {
                    responseAPI = new JSONObject(result);
                    String status = responseAPI.getJSONObject("metadata").getString("status");
                    if (status.equals("200")) {
                        latestVersion = responseAPI.getJSONObject("response").getString("build_version");
                        link = responseAPI.getJSONObject("response").getString("link_update");
                        updateRequired = ((responseAPI.getJSONObject("response").getString("wajib")).equals("1")) ? true : false;
                        if (!version.trim().equals(latestVersion.trim()) && link.length() > 0) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            if (updateRequired) {
                                builder.setIcon(R.mipmap.ic_launcher)
                                        .setTitle("Update")
                                        .setMessage("Versi terbaru " + latestVersion + " telah tersedia, mohon download versi terbaru.")
                                        .setPositiveButton("Update Sekarang", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                                                startActivity(browserIntent);
                                            }
                                        })
                                        .setCancelable(false)
                                        .show();
                            } else {
                                builder.setIcon(R.mipmap.ic_launcher)
                                        .setTitle("Update")
                                        .setMessage("Versi terbaru " + latestVersion + " telah tersedia, mohon download versi terbaru.")
                                        .setPositiveButton("Update Sekarang", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                                                startActivity(browserIntent);
                                            }
                                        })
                                        .setNegativeButton("Update Nanti", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();
                            }

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String result) {
                Toast.makeText(getApplicationContext(), "Terjadi kesalahan saat memuat data", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    private void openScanBarcode() {

        IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
        //integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        resultScanBarcode = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (resultScanBarcode != null) {
            if (resultScanBarcode.getContents() == null) {
                Toast.makeText(getApplicationContext(), "Silahkan Scan Ulang", Toast.LENGTH_LONG).show();
            } else {
//                prepareDataPemakaianVoucher();
                if (WSPenjualan) {
                    WSPenjualan = false;
                    prepareDataJualVoucher();
                } else if (WSPemakaian) {
                    WSPemakaian = false;
                    prepareDataPemakaianVoucher();
                }
//                Toast.makeText(getApplicationContext(),resultScanBarcode.getContents(),Toast.LENGTH_LONG).show();
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void prepareDataJualViaEmail() {
        proses.ShowDialog();
        IDCabang = "0";
        IDPaket = "0";
        IDPaket = selectedPaketViaEmail.getId();
        if (IDPaket.equals("1")) {
            IDCabang = "0";
        } else {
            SetGetMasterCabang cabang = (SetGetMasterCabang) dropdownPilihJam.getSelectedItem();
            IDCabang = cabang.getId();
        }
        final JSONObject jBody = new JSONObject();
        try {
            jBody.put("pin", inputanPIN.getText().toString());
            jBody.put("email", isianJualEmail.getText().toString());
            jBody.put("cabang", IDCabang);
            jBody.put("paket", IDPaket);
            jBody.put("jumlah", isianJumlahEmail.getText().toString());
            Log.d("test", "test");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiVolley request = new ApiVolley(MainActivity.this, jBody, "POST", URL.EmailTopUp, "", "", 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                proses.DismissDialog();
                try {
                    JSONObject object = new JSONObject(result);
                    String status = object.getJSONObject("metadata").getString("status");
                    String message = object.getJSONObject("metadata").getString("message");
                    if (status.equals("200")) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String result) {
                proses.DismissDialog();
                Toast.makeText(getApplicationContext(), "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void prepareDataPemakaianVoucher() {
        proses.ShowDialog();
        /*IDPaket = "0";
        SetGetMasterCabang paket = (SetGetMasterCabang) dropdownPilihJam.getSelectedItem();
        IDPaket = paket.getId();*/
        final JSONObject jBody = new JSONObject();
        try {
            jBody.put("kupon", resultScanBarcode.getContents());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiVolley request = new ApiVolley(MainActivity.this, jBody, "POST", URL.BayarVoucher, "", "", 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                proses.DismissDialog();
                try {
                    JSONObject object = new JSONObject(result);
                    String status = object.getJSONObject("metadata").getString("status");
                    String message = object.getJSONObject("metadata").getString("message");
                    if (status.equals("200")) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String result) {
                proses.DismissDialog();
                Toast.makeText(getApplicationContext(), "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void prepareDataJualVoucher() {
        proses.ShowDialog();
        IDPaket = "0";
        IDCabang = "0";

//        SetGetMasterPaketBaruPenjualanVoucher paket = (SetGetMasterPaketBaruPenjualanVoucher) dropdownPilihKupon.getSelectedItem();
//        IDPaket = paket.getId();
        IDPaket = selectedPaketPenjualanVoucher.getId();
        if (IDPaket.equals("1")) {
            IDCabang = "0";
        } else {
            SetGetMasterCabang cabang = (SetGetMasterCabang) dropdownPilihJam.getSelectedItem();
            IDCabang = cabang.getId();
        }
        final JSONObject jBody = new JSONObject();
        try {
            jBody.put("pin", inputanPIN.getText().toString());
            jBody.put("uid", resultScanBarcode.getContents());
            jBody.put("paket", IDPaket);
            jBody.put("cabang", IDCabang);
            jBody.put("jumlah", jumlahKuponDoku.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiVolley request = new ApiVolley(MainActivity.this, jBody, "POST", URL.TopUpVoucher, "", "", 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                proses.DismissDialog();
                try {
                    JSONObject object = new JSONObject(result);
                    String status = object.getJSONObject("metadata").getString("status");
                    String message = object.getJSONObject("metadata").getString("message");
                    if (status.equals("200")) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String result) {
                proses.DismissDialog();
                Toast.makeText(getApplicationContext(), "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
            }
        });
    }

    public String ChangeToRupiahFormat(String number) {

        double value = parseNullDouble(number);

        NumberFormat format = NumberFormat.getCurrencyInstance();
        DecimalFormatSymbols symbols = ((DecimalFormat) format).getDecimalFormatSymbols();

        symbols.setCurrencySymbol("Rp ");
        ((DecimalFormat) format).setDecimalFormatSymbols(symbols);
        format.setMaximumFractionDigits(0);

        String hasil = String.valueOf(format.format(value));

        return hasil;
    }

    public Double parseNullDouble(String s) {
        double result = 0;
        if (s != null && s.length() > 0) {
            try {
                result = Double.parseDouble(s);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        return result;
    }

    public String doubleToStringFull(Double number) {
        return String.format("%s", number).replace(",", ".");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                session.logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Klik sekali lagi untuk keluar", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
/*ArrayAdapter<String> adapterJumlahJam = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, jam) {
                    @Override
                    public boolean isEnabled(int position) {
                        if (position == 0) {
                            // Disable the first item from Spinner
                            // First item will be use for hint
                            dropdownPilihJam.setAlpha(0.5f);
                            return false;
                        } else {
                            dropdownPilihJam.setAlpha(1);
                            return true;
                        }
                    }

                    @Override
                    public View getDropDownView(int position, View convertView,
                                                ViewGroup parent) {

                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        if (position == 0) {
                            // Set the hint text color gray
                            tv.setTextColor(Color.BLACK);
                        } else {
                            tv.setTextColor(Color.BLACK);
                        }
                        return view;
                    }
                };*/
                /*adapterJumlahJam.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dropdownPilihJam.setAdapter(adapterJumlahJam);*/
/*private void loadMasterCabang() {
                proses.ShowDialog();
                ApiVolley requestMasterCabang = new ApiVolley(MainActivity.this, new JSONObject(), "GET", URL.MasterCabang, "", "", 0, new ApiVolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        proses.DismissDialog();
                        masterCabang = new ArrayList<>();
                        masterCabang.add(new SetGetMasterPaket("", "Pilih Cabang:"));
                        try {
                            JSONObject object = new JSONObject(result);
                            String status = object.getJSONObject("metadata").getString("status");
                            if (status.equals("200")) {
                                JSONArray response = object.getJSONArray("response");
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject isiMasterCabang = response.getJSONObject(i);
                                    masterCabang.add(new SetGetMasterPaket(
                                            isiMasterCabang.getString("id").toString(),
                                            isiMasterCabang.getString("cabang").toString()
                                    ));
                                }
//                                android.R.layout.simple_spinner_item
                                adapterCabang = new ArrayAdapter<SetGetMasterPaket>(MainActivity.this, android.R.layout.simple_spinner_item, masterCabang) {
                                    @Override
                                    public boolean isEnabled(int position) {
                                        if (position == 0) {
                                            // Disable the first item from Spinner
                                            // First item will be use for hint
                                            dropdownPilihKupon.setAlpha(0.5f);
                                            return false;
                                        } else {
                                            dropdownPilihKupon.setAlpha(1);
                                            return true;
                                        }
                                    }

                                    @Override
                                    public View getDropDownView(int position, View convertView,
                                                                ViewGroup parent) {

                                        View view = super.getDropDownView(position, convertView, parent);
                                        TextView tv = (TextView) view;
                                        if (position == 0) {
                                            // Set the hint text color gray
                                            tv.setTextColor(Color.BLACK);
                                        } else {
                                            tv.setTextColor(Color.BLACK);
                                        }
                                        return view;
                                    }
                                };
//                        adapterCabang = ArrayAdapter.createFromResource(this,masterCabang,R.layout.item_simple_item);
                                adapterCabang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                dropdownPilihKupon.setAdapter(adapterCabang);
                                dropdownPilihKupon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        Toast.makeText(getApplicationContext(),adapterView.getItemAtPosition(i).toString(),Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String result) {
                        proses.DismissDialog();
                        Toast.makeText(getApplicationContext(), "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
                    }
                });
            }*/
// pop up pembelian lama
/*
final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.popup_pembelian_via_email);
                        dropdownPilihKupon=dialog.findViewById(R.id.dropdownJenisKuponEmail);
                        dropdownPilihJam=dialog.findViewById(R.id.dropdownJumlahJamEmail);
                        RelativeLayout btnSave=(RelativeLayout)dialog.findViewById(R.id.btnSave);
                        RelativeLayout btnCancel=(RelativeLayout)dialog.findViewById(R.id.btnCancel);
final LinearLayout layoutDPCabang=(LinearLayout)dialog.findViewById(R.id.layoutDPCabang);
        isianJualEmail=(EditText)dialog.findViewById(R.id.inputanEmail);
        isianJumlahEmail=(EditText)dialog.findViewById(R.id.inputanJam);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        ApiVolley requestMasterCabang=new ApiVolley(MainActivity.this,new JSONObject(),"GET",URL.MasterPaket,"","",0,new ApiVolley.VolleyCallback(){
@Override
public void onSuccess(String result){
        masterPaket=new ArrayList<>();
        try{
        JSONObject object=new JSONObject(result);
        String status=object.getJSONObject("metadata").getString("status");
        if(status.equals("200")){
        JSONArray response=object.getJSONArray("response");
        for(int i=0;i<response.length();i++){
        JSONObject isiMasterCabang=response.getJSONObject(i);
        masterPaket.add(new SetGetMasterPaket(
        isiMasterCabang.getString("id"),
        isiMasterCabang.getString("keterangan")
        ));
        }
//                                android.R.layout.simple_spinner_item
        adapterPaket=new ArrayAdapter<SetGetMasterPaket>(MainActivity.this,R.layout.layout_simple_list,masterPaket);
        dropdownPilihKupon.setAdapter(adapterPaket);
        dropdownPilihKupon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
@Override
public void onItemSelected(AdapterView<?> adapterView,View view,int i,long l){
        if(i==1){
        layoutDPCabang.setVisibility(View.VISIBLE);
        }else{
        layoutDPCabang.setVisibility(View.GONE);
        }
        }

@Override
public void onNothingSelected(AdapterView<?> adapterView){

        }
        });
//                                dropdownPilihKupon.setSelection(0, true);
        }
        }catch(JSONException e){
        e.printStackTrace();
        }
        }

@Override
public void onError(String result){
        Toast.makeText(getApplicationContext(),"Terjadi Kesalahan",Toast.LENGTH_LONG).show();
        }
        });
        ApiVolley requestMasterJam=new ApiVolley(MainActivity.this,new JSONObject(),"GET",URL.MasterCabang,"","",0,new ApiVolley.VolleyCallback(){
@Override
public void onSuccess(String result){
        masterCabang=new ArrayList<>();
//                        masterCabang.add(new SetGetMasterCabang("0","SEMUA CABANG"));
        try{
        JSONObject object=new JSONObject(result);
        String status=object.getJSONObject("metadata").getString("status");
        if(status.equals("200")){
        JSONArray response=object.getJSONArray("response");
        for(int i=0;i<response.length();i++){
        JSONObject isiMasterJam=response.getJSONObject(i);
        masterCabang.add(new SetGetMasterCabang(
        isiMasterJam.getString("id"),
        isiMasterJam.getString("cabang")
        ));
        }
        adapterCabang=new ArrayAdapter(MainActivity.this,R.layout.layout_simple_list,masterCabang);
        dropdownPilihJam.setAdapter(adapterCabang);
//                                dropdownPilihJam.setSelection(0, true);
        }
        }catch(JSONException e){
        e.printStackTrace();
        }
        }

@Override
public void onError(String result){
        Toast.makeText(getApplicationContext(),"Terjadi Kesalahan",Toast.LENGTH_LONG).show();
        }
        });
        btnCancel.setOnClickListener(new View.OnClickListener(){
@Override
public void onClick(View view){
        dialog.dismiss();
        }
        });
        btnSave.setOnClickListener(new View.OnClickListener(){
@Override
public void onClick(View view){
        if(isianJualEmail.getText().toString().isEmpty()){
        isianJualEmail.setError("Email harap di isi");
        isianJualEmail.requestFocus();
        return;
        }
        if(isianJumlahEmail.getText().toString().equals("")){
        isianJumlahEmail.setError("Mohon Di Isi");
        isianJumlahEmail.requestFocus();
        return;
        }
        prepareDataJualViaEmail();
        dialog.dismiss();
        }
        });
        dialog.show();*/
/*dialogPemakaian = new Dialog(MainActivity.this);
                dialogPemakaian.setContentView(R.layout.popup_pemakaian_voucher);
//                dropdownPilihJam = (Spinner) dialogPemakaian.findViewById(R.id.dropdownJumlahJamPemakaian);
                RelativeLayout btnSave = (RelativeLayout) dialogPemakaian.findViewById(R.id.btnSave);
                RelativeLayout btnCancel = (RelativeLayout) dialogPemakaian.findViewById(R.id.btnCancel);
                isianPakaiVoucher = (EditText) dialogPemakaian.findViewById(R.id.inputanJumlah);
                dialogPemakaian.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogPemakaian.setCanceledOnTouchOutside(false);
                ApiVolley request = new ApiVolley(MainActivity.this, new JSONObject(), "GET", URL.MasterCabang, "", "", 0, new ApiVolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        masterCabang = new ArrayList<>();
                        try {
                            JSONObject object = new JSONObject(result);
                            String status = object.getJSONObject("metadata").getString("status");
                            if (status.equals("200")) {
                                JSONArray response = object.getJSONArray("response");
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject isiMasterJam = response.getJSONObject(i);
                                    masterCabang.add(new SetGetMasterCabang(
                                            isiMasterJam.getString("id"),
                                            isiMasterJam.getString("cabang")
                                    ));
                                }
                                adapterCabang = new ArrayAdapter(MainActivity.this, R.layout.layout_simple_list, masterCabang);
                                dropdownPilihJam.setAdapter(adapterCabang);
                                dropdownPilihJam.setSelection(0, true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String result) {
                        Toast.makeText(getApplicationContext(), "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogPemakaian.dismiss();
                    }
                });
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        WSPemakaian = true;
                        WSPenjualan = false;
                        if (isianPakaiVoucher.getText().toString().equals("")) {
                            isianPakaiVoucher.setError("Mohon Di Isi");
                            isianPakaiVoucher.requestFocus();
                            return;
                        }
                        dialogPemakaian.dismiss();
                        openScanBarcode();
                    }
                });
                dialogPemakaian.show();*/

//                menuPaket = prepareDataMenuPaketDoku();

                /*ApiVolley requestPaket = new ApiVolley(MainActivity.this, new JSONObject(), "GET", URL.MasterPaket, "", "", 0, new ApiVolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        masterPaket = new ArrayList<>();
                        try {
                            JSONObject object = new JSONObject(result);
                            String status = object.getJSONObject("metadata").getString("status");
                            if (status.equals("200")) {
                                JSONArray response = object.getJSONArray("response");
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject isiMasterCabang = response.getJSONObject(i);
                                    masterPaket.add(new SetGetMasterPaket(
                                            isiMasterCabang.getString("flag"),
                                            isiMasterCabang.getString("keterangan")
                                    ));
                                }
//                                android.R.layout.simple_spinner_item
                                adapterPaket = new ArrayAdapter<SetGetMasterPaket>(MainActivity.this, R.layout.layout_simple_list, masterPaket);
                                dropdownPilihKupon.setAdapter(adapterPaket);
                                dropdownPilihKupon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                        Toast.makeText(getApplicationContext(),parent.getItemAtPosition(position).toString(),Toast.LENGTH_LONG).show();
                                        if (position == 1) {
                                            layoutDPCabang.setVisibility(View.VISIBLE);
                                        } else {
                                            layoutDPCabang.setVisibility(View.GONE);
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
//                                dropdownPilihKupon.setSelection(0, true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String result) {
                        Toast.makeText(getApplicationContext(), "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
                    }
                });*/

//                menuPaket = prepareDataMenuPaketDoku();

                /*ApiVolley requestPaket = new ApiVolley(MainActivity.this, new JSONObject(), "GET", URL.MasterPaket, "", "", 0, new ApiVolley.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        masterPaket = new ArrayList<>();
                        try {
                            JSONObject object = new JSONObject(result);
                            String status = object.getJSONObject("metadata").getString("status");
                            if (status.equals("200")) {
                                JSONArray response = object.getJSONArray("response");
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject isiMasterCabang = response.getJSONObject(i);
                                    masterPaket.add(new SetGetMasterPaket(
                                            isiMasterCabang.getString("flag"),
                                            isiMasterCabang.getString("keterangan")
                                    ));
                                }
//                                android.R.layout.simple_spinner_item
                                adapterPaket = new ArrayAdapter<SetGetMasterPaket>(MainActivity.this, R.layout.layout_simple_list, masterPaket);
                                dropdownPilihKupon.setAdapter(adapterPaket);
                                dropdownPilihKupon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                        Toast.makeText(getApplicationContext(),parent.getItemAtPosition(position).toString(),Toast.LENGTH_LONG).show();
                                        if (position == 1) {
                                            layoutDPCabang.setVisibility(View.VISIBLE);
                                        } else {
                                            layoutDPCabang.setVisibility(View.GONE);
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
//                                dropdownPilihKupon.setSelection(0, true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String result) {
                        Toast.makeText(getApplicationContext(), "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
                    }
                });*/