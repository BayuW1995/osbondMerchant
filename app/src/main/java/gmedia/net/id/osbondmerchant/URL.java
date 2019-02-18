package gmedia.net.id.osbondmerchant;

public class URL {
    //real baseURL
//        public static String BaseUrl = "http://osbond.gmedia.bz/";

    //baseURL ala2
    public static String BaseUrl = "http://gmedia.bz/osbondv2/";
    //localhost mas Victor
//    public static String BaseUrl = "http://192.168.20.4/osbond/";
    public static String Login = BaseUrl + "merchant/authentication";
    public static String MasterPaket = BaseUrl + "merchant/ms_paket";
    public static String MasterCabang = BaseUrl + "merchant/ms_cabang";
    public static String HistoryPenjualan = BaseUrl + "merchant/penjualan";
    public static String TopUpVoucher = BaseUrl + "merchant/topup";
    public static String BayarVoucher = BaseUrl + "merchant/bayar";
    public static String EmailTopUp = BaseUrl + "merchant/email_topup";
    public static String HistoryKunjungan = BaseUrl + "merchant/kunjungan";
    public static String totalPenjualan = BaseUrl + "kupon/total_jual";
    public static String upVersions = BaseUrl + "app/ver_merchant";
    public static String CheckPin = BaseUrl + "merchant/check_pin";
}
