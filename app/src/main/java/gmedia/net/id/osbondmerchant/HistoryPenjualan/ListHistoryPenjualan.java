package gmedia.net.id.osbondmerchant.HistoryPenjualan;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import gmedia.net.id.osbondmerchant.ApiVolley;
import gmedia.net.id.osbondmerchant.Proses;
import gmedia.net.id.osbondmerchant.R;
import gmedia.net.id.osbondmerchant.URL;

public class ListHistoryPenjualan extends AppCompatActivity {
    private ListView listView;
    private ArrayList<SetGetLVHistoryPenjualan> listPenjualan;
    private ArrayList<SetGetLVHistoryPenjualan> listPenjualanBaru;
    private ListAdapterHistoryPenjualan adapter;
    private Proses proses;
    private boolean isLoading = false;
    private int startIndex = 0;
    private final int count = 10;
    private View footerList;
    private String awal = "", akhir = "";
    private TextView total_harga;
    private int isiArray = 0;
    private LinearLayout layoutTotalPenjualan;
    private Animation animShow, animHide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_history_penjualan);
        initUI();
        initAction();

    }

    private void initUI() {
        proses = new Proses(ListHistoryPenjualan.this);
        listView = (ListView) findViewById(R.id.lvHistoryPenjualan);
        total_harga = (TextView) findViewById(R.id.total_harga_penjualan_history_penjualan);
        layoutTotalPenjualan = (LinearLayout) findViewById(R.id.layoutTotalPenjualan);
        animShow = AnimationUtils.loadAnimation(ListHistoryPenjualan.this, R.anim.up_from_bottom);
        animHide = AnimationUtils.loadAnimation(ListHistoryPenjualan.this, R.anim.gone_to_down);
    }

    private void initAction() {
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        footerList = inflater.inflate(R.layout.footer_list, null);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            awal = bundle.getString("tglAwal");
            akhir = bundle.getString("tglAkhir");
        }
        prepareDataHistoryPenjualan();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("History Penjualan");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void prepareDataHistoryPenjualan() {
        isLoading = true;
        proses.ShowDialog();
        JSONObject jBody = new JSONObject();
        try {
            jBody.put("date_start", awal);
            jBody.put("date_end", akhir);
            jBody.put("start", String.valueOf(startIndex));
            jBody.put("count", String.valueOf(count));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiVolley request = new ApiVolley(ListHistoryPenjualan.this, jBody, "POST", URL.HistoryPenjualan, "", "", 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                isLoading = false;
                proses.DismissDialog();
                listPenjualan = new ArrayList<>();
                try {
                    JSONObject object = new JSONObject(result);
                    String status = object.getJSONObject("metadata").getString("status");
                    if (status.equals("404")) {
                        Toast.makeText(getApplicationContext(), "Tidak Ada Data", Toast.LENGTH_LONG).show();
                    } else if (status.equals("200")) {
                        JSONArray response = object.getJSONArray("response");
                        int[] jumlah = {};
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject isiHistoryPenjualan = response.getJSONObject(i);
                            listPenjualan.add(new SetGetLVHistoryPenjualan(
                                    isiHistoryPenjualan.getString("waktu"),
                                    isiHistoryPenjualan.getString("nama_user"),
                                    isiHistoryPenjualan.getString("nama_paket"),
                                    isiHistoryPenjualan.getString("jumlah"),
                                    isiArray = Integer.parseInt(isiHistoryPenjualan.getString("total"))
                            ));
//                            isiArray = new int[]{Integer.parseInt(isiHistoryPenjualan.getString("total"))};
                        }
                        listView.setAdapter(null);
                        adapter = new ListAdapterHistoryPenjualan(ListHistoryPenjualan.this, listPenjualan);
                        listView.setAdapter(adapter);
                        getTotalHarga(listPenjualan);

                        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                            int mLastFirstVisibleItem = 0;

                            @Override
                            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                                /*int threshold = 1;
                                int countMerchant = listView.getCount();

                                if (scrollState == SCROLL_STATE_IDLE) {
                                    if (listView.getLastVisiblePosition() >= countMerchant - threshold && !isLoading) {

                                        isLoading = true;
                                        listView.addFooterView(footerList);
                                        startIndex += count;
//                                        startIndex = 0;
                                        getMoreData();
                                        //Log.i(TAG, "onScroll: last ");
                                    }
                                }*/
                            }

                            @Override
                            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                                if (view.getLastVisiblePosition() == totalItemCount - 1 && listView.getCount() > (count - 1) && !isLoading) {
                                    isLoading = true;
                                    listView.addFooterView(footerList);
                                    startIndex += count;
//                                        startIndex = 0;
                                    getMoreData();
                                    //Log.i(TAG, "onScroll: last ");
                                }
                                if (view.getId() == listView.getId()) {
                                    final int currentFirstVisibleItem = listView.getFirstVisiblePosition();
                                    if (currentFirstVisibleItem > mLastFirstVisibleItem) {
//                                        layoutTotalPenjualan.animate().translationY(0).setDuration(600);
                                        layoutTotalPenjualan.startAnimation(animHide);
                                        layoutTotalPenjualan.setVisibility(View.GONE);
                                    } else {
                                        layoutTotalPenjualan.startAnimation(animShow);
                                        layoutTotalPenjualan.setVisibility(View.VISIBLE);
                                    }
                                    mLastFirstVisibleItem = currentFirstVisibleItem;
                                }
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
    }


    private void getMoreData() {
        isLoading = true;
        listPenjualanBaru = new ArrayList<>();
        JSONObject jBody = new JSONObject();
        try {
            jBody.put("start", String.valueOf(startIndex));
            jBody.put("count", String.valueOf(count));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiVolley request = new ApiVolley(ListHistoryPenjualan.this, jBody, "POST", URL.HistoryPenjualan, "", "", 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                listPenjualanBaru = new ArrayList<>();
                listView.removeFooterView(footerList);
                try {
                    JSONObject object = new JSONObject(result);
                    String status = object.getJSONObject("metadata").getString("status");
                    if (status.equals("200")) {
                        JSONArray response = object.getJSONArray("response");
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject isiHistoryPenjualan = response.getJSONObject(i);
                            listPenjualan.add(new SetGetLVHistoryPenjualan(
                                    isiHistoryPenjualan.getString("waktu"),
                                    isiHistoryPenjualan.getString("nama_user"),
                                    isiHistoryPenjualan.getString("nama_paket"),
                                    isiHistoryPenjualan.getString("jumlah"),
                                    isiArray = Integer.parseInt(isiHistoryPenjualan.getString("total"))
                            ));
                        }
                        isLoading = false;
                        listView.removeFooterView(footerList);
                        if (adapter != null) adapter.addMoreData(listPenjualanBaru);
                        getTotalHarga(listPenjualan);
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
    }

    private void getTotalHarga(ArrayList<SetGetLVHistoryPenjualan> list) {
        double total = 0.0;
        for (int i = 0; i < list.size(); i++) {
            total = total + list.get(i).getTotal_belanja();
        }
        total_harga.setText(ChangeToRupiahFormat(String.valueOf(total)));
        Log.d("jumlah harga", String.valueOf(total));
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

    @Override
    protected void onResume() {
        super.onResume();
        prepareDataHistoryPenjualan();
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
