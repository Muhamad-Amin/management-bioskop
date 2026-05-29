package Models;

import java.util.ArrayList;
import java.util.List;

public class PengunjungModel {

    // Penyimpanan data sementara di memori
    private static List<Pengunjung> daftarPengunjung = new ArrayList<>();
    private static int counterId = 1;

    public void addPengunjung(Pengunjung p) {
        p.setId(counterId++);
        daftarPengunjung.add(p);
    }

    public List<Pengunjung> getAllPengunjung() {
        return daftarPengunjung;
    }

    public void updatePengunjung(int id, Pengunjung dataBaru) {
        for (int i = 0; i < daftarPengunjung.size(); i++) {
            if (daftarPengunjung.get(i).getId() == id) {
                dataBaru.setId(id);
                daftarPengunjung.set(i, dataBaru);
                break;
            }
        }
    }

    public void deletePengunjung(int id) {
        daftarPengunjung.removeIf(p -> p.getId() == id);
    }
}
