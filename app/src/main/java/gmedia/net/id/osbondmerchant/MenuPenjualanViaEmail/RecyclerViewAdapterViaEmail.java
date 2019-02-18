package gmedia.net.id.osbondmerchant.MenuPenjualanViaEmail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;

import gmedia.net.id.osbondmerchant.MainActivity;
import gmedia.net.id.osbondmerchant.R;

public class RecyclerViewAdapterViaEmail extends RecyclerView.Adapter<RecyclerViewAdapterViaEmail.ViewHolder> {
    private Context context;
    private ArrayList<SetGetMasterPaketBaruViaEmail> rvData;
    private int lastPosition = -1;

    public RecyclerViewAdapterViaEmail(Context context, ArrayList<SetGetMasterPaketBaruViaEmail> rvData) {
        this.context = context;
        this.rvData = rvData;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView gambar;
        private TextView paket, keterangan, harga;
        private LinearLayout group;
        private RadioButton pilihan;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            gambar = (ImageView) itemView.findViewById(R.id.icon);
            paket = (TextView) itemView.findViewById(R.id.txtPaket);
            keterangan = (TextView) itemView.findViewById(R.id.txtKeterangan);
            harga = (TextView) itemView.findViewById(R.id.txtHarga);
            group = (LinearLayout) itemView.findViewById(R.id.layoutPilihPaket);
            group.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastPosition = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
            pilihan = (RadioButton) itemView.findViewById(R.id.pilihan);
            /*pilihan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastPosition = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });*/
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_rv_penjualan, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapterViaEmail.ViewHolder holder, final int position) {
        Picasso.with(context).load(rvData.get(position).getIcon()).into(holder.gambar);
        holder.paket.setText(rvData.get(position).getPaket());
        holder.keterangan.setText(rvData.get(position).getKeterangan());
        holder.harga.setText(ChangeToRupiahFormat(rvData.get(position).getHarga()));
        /*holder.group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                holder.pilihan.setChecked(true);
            }
        });*/
        if (lastPosition == -1) {
            MainActivity.kosong = true;
        }
        holder.group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastPosition = position;
                if (lastPosition == 0) {
                    MainActivity.layoutDPCabang.setVisibility(View.GONE);
                    MainActivity.kosong = false;
                } else {
                    MainActivity.layoutDPCabang.setVisibility(View.VISIBLE);
                    MainActivity.kosong = false;
                }
                MainActivity.selectedPaketViaEmail = rvData.get(position);
                notifyDataSetChanged();
            }
        });
        holder.pilihan.setChecked(lastPosition == position);

    }

    @Override
    public int getItemCount() {
        return rvData.size();
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
