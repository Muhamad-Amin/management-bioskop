package Models;

public class Pengunjung {

    private int id;
    private String nama;
    private String movieId;
    private String judulFilm;
    private int jumlahTiket;
    private double totalHarga;

    public Pengunjung(String nama, String movieId, String judulFilm, int jumlahTiket, double totalHarga) {
        this.nama = nama;
        this.movieId = movieId;
        this.judulFilm = judulFilm;
        this.jumlahTiket = jumlahTiket;
        this.totalHarga = totalHarga;
    }

    // Getter dan Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getJudulFilm() {
        return judulFilm;
    }

    public void setJudulFilm(String judulFilm) {
        this.judulFilm = judulFilm;
    }

    public int getJumlahTiket() {
        return jumlahTiket;
    }

    public void setJumlahTiket(int jumlahTiket) {
        this.jumlahTiket = jumlahTiket;
    }

    public double getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(double totalHarga) {
        this.totalHarga = totalHarga;
    }
}
