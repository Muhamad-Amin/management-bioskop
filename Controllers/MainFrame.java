package Controllers;

import Models.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MainFrame extends JFrame {

    private JPanel panelKiri, panelSearch;
    private JTextField txtNama, txtMovieId, txtJumlahTiket, txtCariFilm;
    private JTable tabelUtama;
    private DefaultTableModel tableModel;
    private MovieController movieController = new MovieController();
    private PengunjungModel pengunjungModel = new PengunjungModel();
    private int selectedUserId = -1; 

    public MainFrame() {
        setTitle("Admin Bioskop - Mataram");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10)); 

        // PANEL KIRI (FORM) - HIDDEN BY DEFAULT
        panelKiri = new JPanel(new GridLayout(12, 1, 5, 5));
        panelKiri.setBorder(BorderFactory.createTitledBorder("Form Pengunjung"));
        panelKiri.setPreferredSize(new Dimension(250, 700));
        panelKiri.setVisible(false);

        txtNama = new JTextField();
        txtMovieId = new JTextField();
        txtJumlahTiket = new JTextField();
        JButton btnSubmit = new JButton("Submit Data");

        panelKiri.add(new JLabel("Nama Pengunjung:"));
        panelKiri.add(txtNama);
        panelKiri.add(new JLabel("Movie ID:"));
        panelKiri.add(txtMovieId);
        panelKiri.add(new JLabel("Jumlah Tiket:"));
        panelKiri.add(txtJumlahTiket);
        panelKiri.add(new JLabel(""));
        panelKiri.add(btnSubmit);
        add(panelKiri, BorderLayout.WEST);

        // PANEL KANAN (NAV & DATA)
        JPanel panelData = new JPanel(new BorderLayout());
        JPanel panelNav = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnNavListFilm = new JButton("List Film");
        JButton btnNavListUser = new JButton("List Pengunjung");
        JButton btnNavAdd = new JButton("Add Pengunjung");
        panelNav.add(btnNavListFilm);
        panelNav.add(btnNavListUser);
        panelNav.add(btnNavAdd);

        panelSearch = new JPanel(new BorderLayout());
        txtCariFilm = new JTextField();
        JButton btnCari = new JButton("Cari Film");
        panelSearch.add(new JLabel(" Judul: "), BorderLayout.WEST);
        panelSearch.add(txtCariFilm, BorderLayout.CENTER);
        panelSearch.add(btnCari, BorderLayout.EAST);

        JPanel panelAtas = new JPanel(new GridLayout(2, 1));
        panelAtas.add(panelNav);
        panelAtas.add(panelSearch);
        panelData.add(panelAtas, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();
        tabelUtama = new JTable(tableModel);
        panelData.add(new JScrollPane(tabelUtama), BorderLayout.CENTER);
        add(panelData, BorderLayout.CENTER);

        // LOGIKA TOMBOL
        btnNavAdd.addActionListener(e -> {
            selectedUserId = -1;
            clearForm();
            panelKiri.setVisible(true);
            revalidate();
        });

        btnNavListFilm.addActionListener(e -> {
            panelSearch.setVisible(true);
            tableModel.setDataVector(null, new String[]{"ID Film", "Judul", "Tahun"});
        });

        btnNavListUser.addActionListener(e -> {
            panelSearch.setVisible(false);
            loadUserTable();
        });

        btnCari.addActionListener(e -> {
            String query = txtCariFilm.getText().trim();

            // Validasi jika input kosong sebelum menembak API
            if (query.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Silakan masukkan judul film terlebih dahulu!");
                return;
            }

            String hasil = movieController.searchMovie(query);

            // Kosongkan tabel setiap kali pencarian baru dimulai agar tidak membingungkan
            tableModel.setRowCount(0);

            if (hasil.contains("Title")) {
                // Parsing manual jika data ditemukan
                String judul = hasil.split("\"Title\":\"")[1].split("\"")[0];
                String id = hasil.split("\"imdbID\":\"")[1].split("\"")[0];
                String tahun = hasil.split("\"Year\":\"")[1].split("\"")[0];

                tableModel.addRow(new Object[]{id, judul, tahun});
                JOptionPane.showMessageDialog(this, "Film ditemukan!");
            } else {
                // Jika hasil API tidak mengandung "Title" (Data tidak ditemukan/Error)
                JOptionPane.showMessageDialog(this, "Film dengan judul '" + query + "' tidak ditemukan.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnSubmit.addActionListener(e -> {
            try {
                String nama = txtNama.getText();
                String mid = txtMovieId.getText();
                int tiket = Integer.parseInt(txtJumlahTiket.getText());
                Pengunjung p = new Pengunjung(nama, mid, "Film Info", tiket, tiket * 50000);

                if (selectedUserId == -1) {
                    pengunjungModel.addPengunjung(p);
                } else {
                    pengunjungModel.updatePengunjung(selectedUserId, p);
                }

                loadUserTable();
                panelKiri.setVisible(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Input Error!");
            }
        });

        // Klik Tabel untuk Update atau Delete
        tabelUtama.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // Pastikan kita sedang melihat list pengunjung (bukan list film)
                if (tableModel.getColumnName(0).equals("ID")) {
                    int row = tabelUtama.getSelectedRow();
                    selectedUserId = (int) tableModel.getValueAt(row, 0);

                    // Menampilkan pilihan aksi
                    Object[] options = {"Update", "Delete", "Batal"};
                    int n = JOptionPane.showOptionDialog(null,
                            "Pilih aksi untuk Pengunjung: " + tableModel.getValueAt(row, 1),
                            "Aksi Data",
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null, options, options[2]);

                    if (n == JOptionPane.YES_OPTION) {
                        // AKSI UPDATE: Isi form di kiri dengan data yang dipilih
                        txtNama.setText(tableModel.getValueAt(row, 1).toString());
                        txtMovieId.setText(tableModel.getValueAt(row, 2).toString());
                        txtJumlahTiket.setText(tableModel.getValueAt(row, 3).toString());
                        panelKiri.setVisible(true);
                        revalidate();
                    } else if (n == JOptionPane.NO_OPTION) {
                        // AKSI DELETE: Konfirmasi lalu hapus
                        int konfirmasi = JOptionPane.showConfirmDialog(null,
                                "Apakah anda yakin ingin menghapus data ini?",
                                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);

                        if (konfirmasi == JOptionPane.YES_OPTION) {
                            pengunjungModel.deletePengunjung(selectedUserId);
                            loadUserTable(); // Refresh tabel
                            JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus!");
                        }
                    }
                }
            }
        });
    }

    
}
