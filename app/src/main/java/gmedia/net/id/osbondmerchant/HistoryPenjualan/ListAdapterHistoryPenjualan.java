package gmedia.net.id.osbondmerchant.HistoryPenjualan;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;

import gmedia.net.id.osbondmerchant.R;

public class ListAdapterHistoryPenjualan extends ArrayAdapter {
    private Context context;
    private List<SetGetLVHistoryPenjualan> listPenjualan;

    public ListAdapterHistoryPenjualan(Context context, List<SetGetLVHistoryPenjualan> listPenjualan) {
        super(context, R.layout.listview_history_penjualan, listPenjualan);
        this.context = context;
        this.listPenjualan = listPenjualan;
    }

    public void addMoreData(List<SetGetLVHistoryPenjualan> moreData) {

        listPenjualan.addAll(moreData);
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        private TextView tanggal, nama_user, nama_paket, jumlah_paket, total_harga_per_kupon;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {

        int hasil = 0;
        if (position % 2 == 1) {
            hasil = 0;
        } else {
            hasil = 1;
        }
        return hasil;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ListAdapterHistoryPenjualan.ViewHolder holder = new ListAdapterHistoryPenjualan.ViewHolder();
        int tipeViewList = getItemViewType(position);
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
//            LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            /*LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);*/
            convertView = inflater.inflate(R.layout.listview_history_penjualan, null);
            holder.tanggal = (TextView) convertView.findViewById(R.id.tanggal_history_penjualan);
            holder.nama_user = (TextView) convertView.findViewById(R.id.nama_history_penjualan);
            holder.nama_paket = (TextView) convertView.findViewById(R.id.jenis_paket_history_penjualan);
            holder.jumlah_paket = (TextView) convertView.findViewById(R.id.jumlah_paket_history_penjualan);
            holder.total_harga_per_kupon = (TextView) convertView.findViewById(R.id.total_harga_per_kupon);
//            holder.total_belanja = convertView.findViewById(R.id.total_belanja_history_penjualan);
            convertView.setTag(holder);
        } else {
            holder = (ListAdapterHistoryPenjualan.ViewHolder) convertView.getTag();
        }
        final SetGetLVHistoryPenjualan absen = listPenjualan.get(position);
        holder.tanggal.setText(absen.getTanggal());
        holder.nama_user.setText(absen.getNama_user());
        holder.nama_paket.setText(absen.getNama_paket());
        holder.jumlah_paket.setText(absen.getJumlah_paket());
        holder.total_harga_per_kupon.setText(ChangeToRupiahFormat(String.valueOf(absen.getTotal_belanja())));
//        holder.total_belanja.setText(absen.getTotal_belanja());
        return convertView;
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
}

