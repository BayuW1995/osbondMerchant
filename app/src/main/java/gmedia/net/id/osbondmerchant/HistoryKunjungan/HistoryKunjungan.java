package gmedia.net.id.osbondmerchant.HistoryKunjungan;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import gmedia.net.id.osbondmerchant.ApiVolley;
import gmedia.net.id.osbondmerchant.Proses;
import gmedia.net.id.osbondmerchant.R;
import gmedia.net.id.osbondmerchant.URL;

public class HistoryKunjungan extends AppCompatActivity {
    private ListView listView;
    private ArrayList<SetGetLVHistoryKunjungan> listKunjungan;
    private ArrayList<SetGetLVHistoryKunjungan> moreListKunjungan;
    private ListAdapterHistoryKunjungan adapter;
    private boolean isLoading = false;
    private int startIndex = 0;
    private final int count = 10;
    private View footerList;
    private Proses proses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_kunjungan);
        initUI();
        initAction();


    }

    private void initUI() {
        proses = new Proses(HistoryKunjungan.this);
        listView = (ListView) findViewById(R.id.lvHistoryKunjungan);
    }

    private void initAction() {
        LayoutInflater li = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        footerList = li.inflate(R.layout.footer_list, null);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("History Kunjungan");
        actionBar.setDisplayHomeAsUpEnabled(true);
        prepareDataHistoryKunjungan();
    }

    @Override
    protected void onResume() {
        super.onResume();
        prepareDataHistoryKunjungan();
    }

    private void prepareDataHistoryKunjungan() {
        isLoading = true;
        proses.ShowDialog();
        JSONObject jBody = new JSONObject();
        try {
            jBody.put("start", String.valueOf(startIndex));
            jBody.put("count", String.valueOf(count));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiVolley request = new ApiVolley(HistoryKunjungan.this, jBody, "POST", URL.HistoryKunjungan, "", "", 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                isLoading = false;
                proses.DismissDialog();
                listKunjungan = new ArrayList<>();
                try {
                    JSONObject object = new JSONObject(result);
                    String status = object.getJSONObject("metadata").getString("status");
                    if (status.equals("404")) {
                        Toast.makeText(getApplicationContext(), "Tidak Ada Data", Toast.LENGTH_LONG).show();
                    } else if (status.equals("200")) {
                        JSONArray response = object.getJSONArray("response");
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject isi = response.getJSONObject(i);
                            listKunjungan.add(new SetGetLVHistoryKunjungan(
                                    isi.getString("tanggal"),
                                    isi.getString("nama"),
                                    isi.getString("paket")
                            ));
                        }
                        listView.setAdapter(null);
                        adapter = new ListAdapterHistoryKunjungan(HistoryKunjungan.this, listKunjungan);
                        listView.setAdapter(adapter);
                        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(AbsListView view, int i) {
                                /*int threshold = 1;
                                int countMerchant = listView.getCount();

                                if (i == SCROLL_STATE_IDLE) {
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
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String result) {
                isLoading = false;
                proses.DismissDialog();
                Toast.makeText(getApplicationContext(), "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getMoreData() {
        isLoading = true;
        moreListKunjungan = new ArrayList<>();
        JSONObject jBody = new JSONObject();
        try {
            jBody.put("start", String.valueOf(startIndex));
            jBody.put("count", String.valueOf(count));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiVolley request = new ApiVolley(HistoryKunjungan.this, jBody, "POST", URL.HistoryKunjungan, "", "", 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                proses.DismissDialog();
                moreListKunjungan = new ArrayList<>();
                listView.removeFooterView(footerList);
                try {
                    JSONObject object = new JSONObject(result);
                    String status = object.getJSONObject("metadata").getString("status");
                    String message = object.getJSONObject("metadata").getString("message");
                    if (status.equals("200")) {
                        JSONArray response = object.getJSONArray("response");
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject isi = response.getJSONObject(i);
                            moreListKunjungan.add(new SetGetLVHistoryKunjungan(
                                    isi.getString("tanggal"),
                                    isi.getString("nama"),
                                    isi.getString("paket")
                            ));
                        }
                        isLoading = false;
                        listView.removeFooterView(footerList);
                        if (adapter != null) adapter.addMoreData(moreListKunjungan);
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
