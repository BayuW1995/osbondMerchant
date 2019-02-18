package gmedia.net.id.osbondmerchant.HistoryKunjungan;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import gmedia.net.id.osbondmerchant.R;


public class ListAdapterHistoryKunjungan extends ArrayAdapter {
    private Context context;
    private List<SetGetLVHistoryKunjungan> listKunjungan;

    public ListAdapterHistoryKunjungan(Context context, List<SetGetLVHistoryKunjungan> listKunjungan) {
        super(context,R.layout.listview_history_kunjungan,listKunjungan);
        this.context = context;
        this.listKunjungan = listKunjungan;
    }

    private static class ViewHolder {
        private TextView tanggal, nama, jenis_paket;
    }
    public void addMoreData(List<SetGetLVHistoryKunjungan> moreData){

        listKunjungan.addAll(moreData);
        notifyDataSetChanged();
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
        ViewHolder holder = new ViewHolder();
        int tipeViewList = getItemViewType(position);
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
//            LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            /*LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);*/
            convertView = inflater.inflate(R.layout.listview_history_kunjungan, null);
            holder.tanggal = (TextView) convertView.findViewById(R.id.tanggal_history_kunjungan);
            holder.nama = (TextView) convertView.findViewById(R.id.nama_history_kunjungan);
            holder.jenis_paket = (TextView) convertView.findViewById(R.id.jenis_paket_history_kunjungan);
//            holder.total_belanja = convertView.findViewById(R.id.total_belanja_history_penjualan);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final SetGetLVHistoryKunjungan absen = listKunjungan.get(position);
        holder.tanggal.setText(absen.getTanggal());
        holder.nama.setText(absen.getNama());
        holder.jenis_paket.setText(absen.getJenis_paket());
        return convertView;
    }
}
