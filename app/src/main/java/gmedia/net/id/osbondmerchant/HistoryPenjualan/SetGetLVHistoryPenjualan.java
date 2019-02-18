package gmedia.net.id.osbondmerchant.HistoryPenjualan;

public class SetGetLVHistoryPenjualan {
    private String tanggal, nama_user, nama_paket, jumlah_paket;
    private int total_belanja;

    public SetGetLVHistoryPenjualan(String tanggal, String nama_user, String nama_paket, String jumlah_paket, int total_belanja) {
        this.tanggal = tanggal;
        this.nama_user = nama_user;
        this.nama_paket = nama_paket;
        this.jumlah_paket = jumlah_paket;
        this.total_belanja = total_belanja;
    }


    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getNama_user() {
        return nama_user;
    }

    public void setNama_user(String nama_user) {
        this.nama_user = nama_user;
    }

    public String getNama_paket() {
        return nama_paket;
    }

    public void setNama_paket(String nama_paket) {
        this.nama_paket = nama_paket;
    }

    public String getJumlah_paket() {
        return jumlah_paket;
    }

    public void setJumlah_paket(String jumlah_paket) {
        this.jumlah_paket = jumlah_paket;
    }

    public int getTotal_belanja() {
        return total_belanja;
    }

    public void setTotal_belanja(int total_belanja) {
        this.total_belanja = total_belanja;
    }


}
