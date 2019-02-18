package gmedia.net.id.osbondmerchant.HistoryKunjungan;

public class SetGetLVHistoryKunjungan {
    private String tanggal, nama, jenis_paket;

    public SetGetLVHistoryKunjungan(String tanggal, String nama, String jenis_paket) {
        this.tanggal = tanggal;
        this.nama = nama;
        this.jenis_paket = jenis_paket;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }


    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJenis_paket() {
        return jenis_paket;
    }

    public void setJenis_paket(String jenis_paket) {
        this.jenis_paket = jenis_paket;
    }
}
