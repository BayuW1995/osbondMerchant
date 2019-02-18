package gmedia.net.id.osbondmerchant;

public class SetGetMasterPaket {
    private String id, keterangan;

    public SetGetMasterPaket(String id, String keterangan) {
        this.id = id;
        this.keterangan = keterangan;
    }
    @Override
    public String toString() {
        return keterangan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}
