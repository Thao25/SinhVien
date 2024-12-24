/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.DatabaseConnection;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Component;
import java.util.Date;
import java.text.SimpleDateFormat;  //
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author admin
 */
public class home extends javax.swing.JFrame {

    int x = 180;
    int y = 500;
    private String currentUser;
    private String currentPassword;
    private String imagePath;

    /**
     * Creates new form home
     */
    public home(String username, String password) {
        this.currentUser = username;
        this.currentPassword = "Password";
        initComponents();
        lblHanTra.setEditable(false);
        lblMaSachError.setText("");
        lblNgayMuonError.setText("");
        lblSoLuongError.setText("");
        lblMaSachError.setForeground(Color.red);
        lblNgayMuonError.setForeground(Color.red);
        lblSoLuongError.setForeground(Color.red);
        HoTenloi.setText("");
        NSloi.setText("");
        sdtloi.setText("");
        DiaChiloi.setText("");
        Emailloi.setText("");
        HoTenloi.setForeground(Color.red);
        NSloi.setForeground(Color.red);
        sdtloi.setForeground(Color.red);
        DiaChiloi.setForeground(Color.red);
        Emailloi.setForeground(Color.red);
        txtMaSV.setEditable(false);

        itemSach.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                DSSach();
            }
        });
        itemPM.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                DSPM();
            }
        });

        tableSach.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableSachMouseClicked(evt);  // Gọi phương thức khi người dùng nhấp vào một dòng
            }
        });

        lblMaSach.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String maSach = lblMaSach.getText().trim();

                if (!maSach.isEmpty()) {

                    hienThiAnhSach(maSach);
                }
            }
        });

        lblNgayMuon.addPropertyChangeListener("date", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                // Khi ngày mượn thay đổi, tính toán lại ngày hạn trả
                Date ngayMuon = lblNgayMuon.getDate();
                if (ngayMuon != null) {
                    // Cộng thêm 5 ngày vào ngày mượn
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(ngayMuon);
                    calendar.add(Calendar.DAY_OF_MONTH, 5); // Cộng thêm 5 ngày
                    Date hanTra = calendar.getTime(); // Lấy ngày hạn trả

                    // Hiển thị ngày hạn trả lên JLabel hoặc TextField
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    lblHanTra.setText(dateFormat.format(hanTra)); // Định dạng và hiển thị ngày
                }
            }
        });
    }

    private void DSSach() {
        try {
            tableSach.removeAll();
            String[] arr = {"Mã Sách ", "Tên Sách", "Tác giả", "Thể loại", "Số lượng", "NXB", "Ảnh"};
            DefaultTableModel model = new DefaultTableModel(arr, 0);
            Connection connection = DatabaseConnection.connect();
            String query = "SELECT *FROM Sach ";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vector vector = new Vector();
                vector.add(rs.getString("MaSach"));
                vector.add(rs.getString("TenSach"));
                vector.add(rs.getString("TacGia"));
                vector.add(rs.getString("TheLoai"));
                vector.add(rs.getInt("SoLuong"));
                vector.add(rs.getString("NhaXuatBan"));
                vector.add(rs.getString("Anh"));

                model.addRow(vector);
            }

            tableSach.setModel(model);

            tableSach.getColumnModel().getColumn(6).setCellRenderer(new TableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    if (value != null) {
                        // Lấy đường dẫn ảnh từ cột "Ảnh"
                        String imagePath = value.toString();
                        JLabel label = new JLabel();
                        try {
                            // Tạo đối tượng ImageIcon từ đường dẫn ảnh
                            ImageIcon icon = new ImageIcon(imagePath);
                            Image image = icon.getImage().getScaledInstance(50, 20, Image.SCALE_SMOOTH);
                            icon = new ImageIcon(image);

                            label.setIcon(icon);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        return label;
                    }
                    return new JLabel("No Image");
                }
            });

        } catch (SQLException ex) {
            Logger.getLogger(home.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void tableSachMouseClicked(java.awt.event.MouseEvent evt) {
        // Lấy dòng được chọn trong JTable
        int selectedRow = tableSach.getSelectedRow();

        // Kiểm tra nếu có dòng nào được chọn
        if (selectedRow != -1) {
            // Lấy dữ liệu từ dòng được chọn và hiển thị lên các trường thông tin
            String maSach = tableSach.getValueAt(selectedRow, 0).toString();  // Cột 0 là Mã Sách
            String tenSach = tableSach.getValueAt(selectedRow, 1).toString();  // Cột 1 là Tên Sách
            String tacGia = tableSach.getValueAt(selectedRow, 2).toString();  // Cột 2 là Tác Giả
            String theLoai = tableSach.getValueAt(selectedRow, 3).toString();  // Cột 3 là Thể Loại
            int soLuong = Integer.parseInt(tableSach.getValueAt(selectedRow, 4).toString()); // Cột 4 là Số Lượng
            String nxb = tableSach.getValueAt(selectedRow, 5).toString();  // Cột 5 là NXB
            String imagePath = tableSach.getValueAt(selectedRow, 6).toString();  // Cột 6 chứa đường dẫn ảnh
            // Hiển thị thông tin chi tiết lên các JTextField (hoặc JLabel)
            txtMaSach.setText(maSach);
            txtTenSach.setText(tenSach);
            txtTacGia.setText(tacGia);
            txtTheLoai.setText(theLoai);
            txtSoLuong.setText(String.valueOf(soLuong));
            txtNXB.setText(nxb);
            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage();
            Image resizedImage = img.getScaledInstance(110, 140, Image.SCALE_SMOOTH);
            AnhSach.setIcon(new ImageIcon(resizedImage));

        }
    }

    private void searchBooks(String searchQuery) {
        // Thực hiện tìm kiếm sách từ cơ sở dữ liệu
        try (Connection conn = DatabaseConnection.connect()) {
            String query = "SELECT * FROM Sach WHERE TenSach LIKE ? OR TacGia LIKE ? OR TheLoai LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, "%" + searchQuery + "%");
            stmt.setString(2, "%" + searchQuery + "%");
            stmt.setString(3, "%" + searchQuery + "%");

            ResultSet rs = stmt.executeQuery();
            if (!rs.isBeforeFirst()) {
                JOptionPane.showMessageDialog(null, "sách không tồn tại", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                DSSach();
            } else {
                tableSach.setModel(model(rs));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private DefaultTableModel model(ResultSet rs) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Mã Sách");
        model.addColumn("Tên Sách");
        model.addColumn("Tác Giả");
        model.addColumn("Thể Loại");
        model.addColumn("Số Lượng");
        model.addColumn("Nhà Xuất Bản");
        model.addColumn("Ảnh");

        try {
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("MaSach"));
                row.add(rs.getString("TenSach"));
                row.add(rs.getString("TacGia"));
                row.add(rs.getString("TheLoai"));
                row.add(rs.getInt("SoLuong"));
                row.add(rs.getString("NhaXuatBan"));
                row.add(rs.getString("Anh"));

                model.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return model;
    }

    private void DSPM() {
        try (Connection conn = DatabaseConnection.connect()) {
            String query = "SELECT * FROM PM WHERE MaSV = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, currentUser);
            ResultSet rs = stmt.executeQuery();
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Mã Phiếu Mượn");
            model.addColumn("Mã Sách");
            model.addColumn("Số Lượng");
            model.addColumn("Ngày Mượn");
            model.addColumn("Hạn Trả");
            model.addColumn("Ngày Trả");

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("MaPM"));
                row.add(rs.getString("MaSach"));
                row.add(rs.getInt("SoLuong"));
                row.add(rs.getDate("NgayMuon"));
                row.add(rs.getDate("HanTra"));
                row.add(rs.getDate("NgayTra"));

                model.addRow(row);
            }
            tablePM.setModel(model);

            tablePM.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // Lấy dòng được chọn
                    int row = tablePM.getSelectedRow();
                    String maSach = (String) tablePM.getValueAt(row, 1);

                    showPMDetails(maSach);
                }
            });

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void showPMDetails(String maSach) {
        try {
            lblMaSachError.setText("");
            lblNgayMuonError.setText("");
            lblSoLuongError.setText("");

            // Truy vấn thông tin sách từ mã sách
            Connection connection = DatabaseConnection.connect();
            String query = "SELECT * FROM Sach WHERE MaSach = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, maSach);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String anhSach = rs.getString("Anh");  // Đường dẫn ảnh của sách

                // Hiển thị ảnh sách lên JLabel (sử dụng ImageIcon)
                ImageIcon icon = new ImageIcon(anhSach);
                Image image = icon.getImage().getScaledInstance(110, 140, Image.SCALE_SMOOTH); // Điều chỉnh kích thước ảnh
                icon = new ImageIcon(image);
                lblAnhSach.setIcon(icon);  // Đặt icon cho JLabel
                Integer soLuong = (Integer) tablePM.getValueAt(tablePM.getSelectedRow(), 2);
                Date ngayMuon = (Date) tablePM.getValueAt(tablePM.getSelectedRow(), 3);  // Ngày mượn từ cột 3

                Date hanTra = (Date) tablePM.getValueAt(tablePM.getSelectedRow(), 4);
                // Hiển thị các thông tin phiếu mượn
                lblMaSach.setText(String.valueOf(tablePM.getValueAt(tablePM.getSelectedRow(), 1)));  // Mã sách
                lblSoLuong.setValue(soLuong);
                lblNgayMuon.setDate(ngayMuon);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                lblHanTra.setText(dateFormat.format(hanTra));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void hienThiAnhSach(String maSach) {
        try (Connection connection = DatabaseConnection.connect()) {

            String query = "SELECT Anh FROM Sach WHERE MaSach = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, maSach);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String anhSach = rs.getString("Anh");

                ImageIcon icon = new ImageIcon(anhSach);
                Image image = icon.getImage().getScaledInstance(110, 140, Image.SCALE_SMOOTH); // Resize ảnh
                icon = new ImageIcon(image);
                lblAnhSach.setIcon(icon);  // Set ảnh lên JLabel

            } else {
                // Nếu mã sách không tồn tại, làm trống ảnh
                lblAnhSach.setIcon(null);

            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi truy vấn cơ sở dữ liệu!");
        }
    }

    // Kiểm tra mã sách có tồn tại trong cơ sở dữ liệu không
    private boolean checkSachExists(String maSach) {
        try (Connection connection = DatabaseConnection.connect()) {
            String query = "SELECT COUNT(*) FROM Sach WHERE MaSach = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, maSach);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true; // Mã sách tồn tại
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false; // Mã sách không tồn tại
    }

    // Kiểm tra số lượng sách có đủ để mượn không
    private boolean checkSoLuongSach(String maSach, int soLuongMuon) {
        try (Connection connection = DatabaseConnection.connect()) {
            String query = "SELECT SoLuong FROM Sach WHERE MaSach = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, maSach);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int soLuongConLai = rs.getInt("SoLuong");
                return soLuongConLai >= soLuongMuon;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    // Cập nhật lại số lượng sách sau khi mượn
    private void updateSachQuantity(String maSach, int soLuongMuon) {
        try (Connection connection = DatabaseConnection.connect()) {
            String query = "UPDATE Sach SET SoLuong = SoLuong - ? WHERE MaSach = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, soLuongMuon);
            ps.setString(2, maSach);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Lưu phiếu mượn vào cơ sở dữ liệu
    private boolean savePhieuMuon(String maSach, int soLuong, Date ngayMuon, Date hanTra) {
        try (Connection connection = DatabaseConnection.connect()) {
            String query = "INSERT INTO PM (MaSV, MaSach, SoLuong, NgayMuon, HanTra) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);
            String maSV = currentUser;

            ps.setString(1, maSV);
            ps.setString(2, maSach);
            ps.setInt(3, soLuong);
            ps.setDate(4, new java.sql.Date(ngayMuon.getTime()));
            ps.setDate(5, new java.sql.Date(hanTra.getTime()));
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    private void taoPhieuMuon() {
        String maSach = lblMaSach.getText().trim();
        int soLuong = (Integer) lblSoLuong.getValue();
        Date ngayMuon = lblNgayMuon.getDate();

        LocalDate currentDate = LocalDate.now();
        LocalDate ngayMuonLocalDate = ngayMuon.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        Date hanTra = addDays(ngayMuon, 5);

        lblMaSachError.setText("");
        lblSoLuongError.setText("");
        lblNgayMuonError.setText("");

        boolean hasError = false;

        if (maSach.isEmpty()) {
            lblMaSachError.setText("Mã sách không được để trống.");
            hasError = true;
        } else if (!checkSachExists(maSach)) {
            lblMaSachError.setText("Sách không tồn tại.");
            hasError = true;
        }
        if (checkSachExists(maSach) && (!checkSoLuongSach(maSach, soLuong))) {
            lblSoLuongError.setText("Không đủ số lượng sách.");
            hasError = true;
        }
        if (ngayMuonLocalDate.isBefore(currentDate)) {
            lblNgayMuonError.setText("Ngày mượn không hợp lệ.");
            hasError = true;
        } else if (ngayMuonLocalDate.isEqual(currentDate)) {

        }
        if (hasError) {
            return;
        }
        boolean success = savePhieuMuon(maSach, soLuong, ngayMuon, hanTra);

        if (success) {
            updateSachQuantity(maSach, soLuong);
            DSPM();
            JOptionPane.showMessageDialog(null, "Tạo phiếu mượn thành công!");
        }
    }

    private boolean xoaPhieuMuon(String maPM) {
        try (Connection connection = DatabaseConnection.connect()) {

            String query = "DELETE FROM PM WHERE MaPM = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, maPM);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private void updateXoaSachQuantity(String maSach, int soLuongMuon) {
        try (Connection connection = DatabaseConnection.connect()) {
            // Cập nhật số lượng sách trong bảng Sach
            String updateQuery = "UPDATE Sach SET SoLuong = SoLuong + ? WHERE MaSach = ?";
            PreparedStatement ps = connection.prepareStatement(updateQuery);
            ps.setInt(1, soLuongMuon);  // Thêm lại số sách đã mượn
            ps.setString(2, maSach);    // Cập nhật theo mã sách

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Số lượng sách đã được cập nhật.");
            } else {
                System.out.println("Không tìm thấy sách để cập nhật.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật số lượng sách: " + ex.getMessage());
        }
    }

    private void updateSachQuantity(String maSachMoi, int soLuongMuonMoi, String maPM) {
        // Kết nối đến cơ sở dữ liệu
        try (Connection connection = DatabaseConnection.connect()) {
            // 1. Kiểm tra số lượng sách hiện tại trong kho
            String querySach = "SELECT SoLuong FROM Sach WHERE MaSach = ?";
            PreparedStatement psSach = connection.prepareStatement(querySach);
            psSach.setString(1, maSachMoi);
            ResultSet rsSach = psSach.executeQuery();

            if (rsSach.next()) {
                int soLuongSachTrongKho = rsSach.getInt("SoLuong");

                // 2. Cập nhật số lượng sách trong kho
                // Số lượng sách trong kho sẽ giảm khi sách được mượn
                int soLuongSachSauKhiMuon = soLuongSachTrongKho - soLuongMuonMoi;

                // Kiểm tra xem số lượng sách còn lại có đủ để mượn không
                if (soLuongSachSauKhiMuon < 0) {
                    return;
                }

                // Cập nhật lại số lượng sách trong kho
                String updateSach = "UPDATE Sach SET SoLuong = ? WHERE MaSach = ?";
                PreparedStatement psUpdateSach = connection.prepareStatement(updateSach);
                psUpdateSach.setInt(1, soLuongSachSauKhiMuon);
                psUpdateSach.setString(2, maSachMoi);
                psUpdateSach.executeUpdate();
            } else {
                JOptionPane.showMessageDialog(this, "Sách không tồn tại trong kho!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật số lượng sách: " + ex.getMessage());
        }
    }

    private boolean updatePhieuMuon() {
        int selectedRow = tablePM.getSelectedRow(); // Lấy dòng đã chọn trong JTable
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu mượn để sửa!");
            return false;
        }

        // Lấy maPM từ cột trong JTable
        String maPM = tablePM.getValueAt(selectedRow, 0).toString();  // Giả sử maPM là cột đầu tiên
        String maSachCu = tablePM.getValueAt(selectedRow, 1).toString(); // Mã sách cũ từ bảng
        String maSachMoi = lblMaSach.getText(); // Mã sách mới từ giao diện
        Date ngayMuon = lblNgayMuon.getDate();
        Date hanTra = addDays(ngayMuon, 5); // Thêm 5 ngày cho hạn trả
        int soLuongMuonMoi = (Integer) lblSoLuong.getValue(); // Số lượng sách mới mượn

        // Kiểm tra ngày mượn và hạn trả hợp lệ
        if (ngayMuon == null || hanTra == null) {
            lblNgayMuonError.setText("Ngày mượn và hạn trả không được bỏ trống.");
            return false;
        }

        if (maSachMoi.isEmpty()) {
            lblMaSachError.setText("Mã sách không được để trống.");
            return false;
        } else if (!checkSachExists(maSachMoi)) {
            lblMaSachError.setText("Sách không tồn tại.");
            return false;
        }

        // Thực hiện update trong cơ sở dữ liệu
        try (Connection connection = DatabaseConnection.connect()) {
            //kiểm tra xem PM đã trả chưa
            String queryCheckNgayTra = "SELECT NgayTra FROM PM WHERE MaPM = ?";
            PreparedStatement psCheckNgayTra = connection.prepareStatement(queryCheckNgayTra);
            psCheckNgayTra.setString(1, maPM);
            ResultSet rsCheckNgayTra = psCheckNgayTra.executeQuery();
            if (rsCheckNgayTra.next()) {
                Date ngayTraCu = rsCheckNgayTra.getDate("NgayTra");
                if (ngayTraCu != null) {
                    JOptionPane.showMessageDialog(this, "Không thể sửa phiếu mượn đã được trả.");
                    return false;
                }
            }

            // 1. Kiểm tra số lượng sách cũ trong kho (maSachCu)
            String querySachCu = "SELECT SoLuong FROM Sach WHERE MaSach = ?";
            PreparedStatement psSachCu = connection.prepareStatement(querySachCu);
            psSachCu.setString(1, maSachCu);
            ResultSet rsSachCu = psSachCu.executeQuery();
            if (!rsSachCu.next()) {
                JOptionPane.showMessageDialog(this, "Sách cũ không tồn tại trong kho!");
                return false;
            }
            int soLuongSachCu = rsSachCu.getInt("SoLuong");

            // 2. Kiểm tra số lượng sách mới trong kho (maSachMoi)
            String querySachMoi = "SELECT SoLuong FROM Sach WHERE MaSach = ?";
            PreparedStatement psSachMoi = connection.prepareStatement(querySachMoi);
            psSachMoi.setString(1, maSachMoi);
            ResultSet rsSachMoi = psSachMoi.executeQuery();
            if (!rsSachMoi.next()) {
                JOptionPane.showMessageDialog(this, "Sách mới không tồn tại trong kho!");
                return false;
            }
            int soLuongSachMoi = rsSachMoi.getInt("SoLuong");

            // 3. Kiểm tra số lượng sách đã mượn từ trước (số sách mượn cũ)
            String queryMuonCu = "SELECT SoLuong FROM PM WHERE MaPM = ?";
            PreparedStatement psMuonCu = connection.prepareStatement(queryMuonCu);
            psMuonCu.setString(1, maPM);
            ResultSet rsMuonCu = psMuonCu.executeQuery();
            int soLuongSachMuonCu = 0;
            if (rsMuonCu.next()) {
                soLuongSachMuonCu = rsMuonCu.getInt("SoLuong");
            }

            // 4. Tính lại số sách trong kho sau khi thay đổi số lượng mượn
            if (!maSachCu.equals(maSachMoi)) {
                // Trả lại sách cũ vào kho
                String updateSachCu = "UPDATE Sach SET SoLuong = SoLuong + ? WHERE MaSach = ?";
                PreparedStatement psUpdateSachCu = connection.prepareStatement(updateSachCu);
                psUpdateSachCu.setInt(1, soLuongSachMuonCu); // Trả lại số sách đã mượn
                psUpdateSachCu.setString(2, maSachCu);
                psUpdateSachCu.executeUpdate();

                // Kiểm tra xem sách mới có đủ số lượng để mượn không
                if (soLuongSachMoi < soLuongMuonMoi) {
                    JOptionPane.showMessageDialog(this, "Số lượng sách mới không đủ để mượn!");
                    return false;
                }

                // Cập nhật số sách mượn cho sách mới
                String updateSachMoi = "UPDATE Sach SET SoLuong = SoLuong - ? WHERE MaSach = ?";
                PreparedStatement psUpdateSachMoi = connection.prepareStatement(updateSachMoi);
                psUpdateSachMoi.setInt(1, soLuongMuonMoi);
                psUpdateSachMoi.setString(2, maSachMoi);
                psUpdateSachMoi.executeUpdate();
            } else {
                // Nếu cùng một sách, chỉ thay đổi số lượng mượn
                int soLuongSachHienTai = soLuongSachMoi + soLuongSachMuonCu;
                if (soLuongSachHienTai < soLuongMuonMoi) {
                    JOptionPane.showMessageDialog(this, "Số lượng sách trong kho không đủ để mượn.");
                    return false;
                }

                // Cập nhật số lượng sách trong kho (giảm số lượng sách đã mượn)
                String updateSach = "UPDATE Sach SET SoLuong = SoLuong + ? WHERE MaSach = ?";
                PreparedStatement psUpdateSach = connection.prepareStatement(updateSach);
                psUpdateSach.setInt(1, soLuongSachMuonCu - soLuongMuonMoi); // Trả lại sách nếu giảm số lượng
                psUpdateSach.setString(2, maSachCu);
                psUpdateSach.executeUpdate();
            }

            // 5. Cập nhật thông tin phiếu mượn trong bảng PM
            String queryUpdatePM = "UPDATE PM SET MaSach = ?, NgayMuon = ?, HanTra = ?, SoLuong = ? WHERE MaPM = ?";
            PreparedStatement psUpdatePM = connection.prepareStatement(queryUpdatePM);
            psUpdatePM.setString(1, maSachMoi);
            psUpdatePM.setDate(2, new java.sql.Date(ngayMuon.getTime()));
            psUpdatePM.setDate(3, new java.sql.Date(hanTra.getTime()));
            psUpdatePM.setInt(4, soLuongMuonMoi);
            psUpdatePM.setString(5, maPM);
            int rowsAffected = psUpdatePM.executeUpdate();

            if (rowsAffected > 0) {
                // Cập nhật lại danh sách phiếu mượn và số lượng sách trong kho
                updateSachQuantity(maSachMoi, soLuongMuonMoi, maPM);
                DSPM(); // Cập nhật lại danh sách phiếu mượn
                JOptionPane.showMessageDialog(this, "Sửa phiếu mượn thành công!");
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Không thể sửa phiếu mượn.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật phiếu mượn: " + ex.getMessage());
        }
        return false;
    }

    private void chooseImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn ảnh");

        // Chỉ hiển thị các tệp ảnh
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Ảnh", "jpg", "png", "jpeg", "bmp");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            // Đọc đường dẫn ảnh
            String imagePath = selectedFile.getAbsolutePath();

            // Hiển thị ảnh lên JLabel
            displayImage(imagePath, Avatar);

            // Lưu đường dẫn ảnh vào một biến (để sử dụng khi cập nhật thông tin sinh viên)
            this.imagePath = imagePath;  // Đảm bảo imagePath là biến toàn cục hoặc một trường dữ liệu
        }
    }

    private void displayImage(String imagePath, JLabel label) {
        try {
            // Tạo đối tượng File từ đường dẫn ảnh
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                // Đọc ảnh từ tệp
                Image img = ImageIO.read(imgFile);
                ImageIcon icon = new ImageIcon(img);

                // Hiển thị ảnh lên giao diện (trong JLabel)
                label.setIcon(icon);  // Cập nhật icon cho JLabel
            } else {
                JOptionPane.showMessageDialog(null, "Không tìm thấy tệp ảnh: " + imagePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi đọc ảnh: " + e.getMessage());
        }
    }

    private void hienThiAvatar(String maSV) {
        try (Connection connection = DatabaseConnection.connect()) {

            String query = "SELECT Anh FROM SV WHERE MaSV = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, maSV);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String anh = rs.getString("Anh");
                ImageIcon icon = new ImageIcon(anh);
                Image image = icon.getImage().getScaledInstance(130, 150, Image.SCALE_SMOOTH); // Resize ảnh
                icon = new ImageIcon(image);
                Avatar.setIcon(icon);
            } else {

                Avatar.setIcon(null);

            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi truy vấn cơ sở dữ liệu!");
        }
    }

    public void StudentInfo(String maSV) {
        try {
            // Kết nối với cơ sở dữ liệu
            Connection conn = DatabaseConnection.connect();
            // Truy vấn lấy thông tin sinh viên
            String query = "SELECT MaSV, HoTen, GioiTinh, NgaySinh, DiaChi, SDT, Email, Anh FROM SV WHERE MaSV = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, currentUser);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                txtMaSV.setText(rs.getString("MaSV"));
                txtHoTen.setText(rs.getString("HoTen"));

                String gioiTinh = rs.getString("GioiTinh");
                if ("Nam".equalsIgnoreCase(gioiTinh)) {
                    Nam.setSelected(true);
                } else if ("Nữ".equalsIgnoreCase(gioiTinh)) {
                    Nu.setSelected(true);
                }

                // Ngày sinh
                txtNgaySinh.setDate(rs.getDate("NgaySinh"));

                // Địa chỉ
                txtDiaChi.setText(rs.getString("DiaChi"));

                // Số điện thoại
                txtSdt.setText(rs.getString("SDT"));

                // Email
                txtEmail.setText(rs.getString("Email"));

                // Xử lý ảnh
                hienThiAvatar(currentUser);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải thông tin sinh viên: " + e.getMessage());
        }
    }
    // Kiểm tra trường không được trống

    private boolean isFieldEmpty(String field) {
        return field == null || field.trim().isEmpty();
    }

    // Kiểm tra số điện thoại hợp lệ (10 chữ số)
    private boolean isValidPhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile("^\\d{10}$"); // Kiểm tra số điện thoại có 10 chữ số
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    // Kiểm tra email hợp lệ
    private boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // Kiểm tra ngày sinh không nhỏ hơn ngày hiện tại
    private boolean isValidBirthDate(Date birthDate) {
        Date currentDate = new Date();
        return !birthDate.after(currentDate); // Kiểm tra nếu ngày sinh không lớn hơn ngày hiện tại
    }

    private boolean changePassword(String oldPassword, String newPassword) {
        try (Connection conn = DatabaseConnection.connect()) {
            String query = "SELECT * FROM TaiKhoan WHERE password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, oldPassword);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Cập nhật mật khẩu mới
                String updateQuery = "UPDATE TaiKhoan SET password = ? WHERE password = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                updateStmt.setString(1, newPassword);
                updateStmt.setString(2, oldPassword);
                updateStmt.executeUpdate();
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        main = new javax.swing.JPanel();
        home = new javax.swing.JPanel();
        TrangDau = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        phieuMuon = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        Luu = new javax.swing.JButton();
        lblMaSach = new javax.swing.JTextField();
        Tao = new javax.swing.JButton();
        Sua = new javax.swing.JButton();
        Xoa = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablePM = new javax.swing.JTable();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        lblNgayMuon = new com.toedter.calendar.JDateChooser();
        lblAnhSach = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblSoLuong = new javax.swing.JSpinner();
        lblHanTra = new javax.swing.JTextField();
        lblMaSachError = new javax.swing.JLabel();
        lblSoLuongError = new javax.swing.JLabel();
        lblNgayMuonError = new javax.swing.JLabel();
        sach = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        TKtext = new javax.swing.JTextField();
        TKicon = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableSach = new javax.swing.JTable();
        AnhSach = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        txtMaSach = new javax.swing.JTextField();
        txtTenSach = new javax.swing.JTextField();
        txtTheLoai = new javax.swing.JTextField();
        txtTacGia = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        txtNXB = new javax.swing.JTextField();
        loan = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtSoLuong = new javax.swing.JTextField();
        taiKhoan = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        Avatar = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        CapNhat = new javax.swing.JButton();
        txtMaSV = new javax.swing.JTextField();
        txtHoTen = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        txtSdt = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDiaChi = new javax.swing.JTextArea();
        Nam = new javax.swing.JRadioButton();
        Nu = new javax.swing.JRadioButton();
        txtNgaySinh = new com.toedter.calendar.JDateChooser();
        jSeparator3 = new javax.swing.JSeparator();
        HoTenloi = new javax.swing.JLabel();
        NSloi = new javax.swing.JLabel();
        sdtloi = new javax.swing.JLabel();
        DiaChiloi = new javax.swing.JLabel();
        Emailloi = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        chat = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        MK = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        btnĐoiMK = new javax.swing.JButton();
        txtOldPassword = new javax.swing.JPasswordField();
        txtNewPassword = new javax.swing.JTextField();
        txtConfirmPassword = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        menu = new javax.swing.JPanel();
        itemSach = new javax.swing.JLabel();
        itemPM = new javax.swing.JLabel();
        itemTK = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();
        itemChat = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        itemPW = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        main.setBackground(new java.awt.Color(0, 102, 0));
        main.setForeground(new java.awt.Color(255, 255, 255));

        home.setLayout(new java.awt.CardLayout());

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/thu vien.jpg"))); // NOI18N
        jLabel1.setMaximumSize(new java.awt.Dimension(700, 410));
        jLabel1.setMinimumSize(new java.awt.Dimension(700, 410));
        jLabel1.setPreferredSize(new java.awt.Dimension(700, 410));

        javax.swing.GroupLayout TrangDauLayout = new javax.swing.GroupLayout(TrangDau);
        TrangDau.setLayout(TrangDauLayout);
        TrangDauLayout.setHorizontalGroup(
            TrangDauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
        );
        TrangDauLayout.setVerticalGroup(
            TrangDauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE)
        );

        home.add(TrangDau, "card5");

        phieuMuon.setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 51, 255));
        jLabel7.setText("Phiếu mượn");

        jLabel12.setText("Mã sách");

        jLabel13.setText("Số lượng");

        Luu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Save as.png"))); // NOI18N
        Luu.setText("Lưu");
        Luu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LuuMouseClicked(evt);
            }
        });

        Tao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Create.png"))); // NOI18N
        Tao.setText("Tạo mới");
        Tao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TaoMouseClicked(evt);
            }
        });

        Sua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Edit.png"))); // NOI18N
        Sua.setText("Sửa");
        Sua.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SuaMouseClicked(evt);
            }
        });

        Xoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Delete.png"))); // NOI18N
        Xoa.setText("Xóa");
        Xoa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                XoaMouseClicked(evt);
            }
        });

        tablePM.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, null, null, null, new java.awt.Color(102, 102, 102)));
        tablePM.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã PM", "Mã Sách", "Số lượng", "Ngày mượn ", "Hạn trả "
            }
        ));
        tablePM.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                tablePMComponentShown(evt);
            }
        });
        jScrollPane1.setViewportView(tablePM);

        jSeparator4.setForeground(new java.awt.Color(255, 153, 51));

        jLabel3.setText("Ngày mượn");

        lblNgayMuon.setDateFormatString("dd/MM/yyyy");

        lblAnhSach.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel4.setText("Hạn trả");

        lblSoLuong.setModel(new javax.swing.SpinnerNumberModel(1, 1, 5, 1));

        lblMaSachError.setText("jLabel9");

        lblSoLuongError.setText("jLabel9");

        lblNgayMuonError.setText("jLabel9");

        javax.swing.GroupLayout phieuMuonLayout = new javax.swing.GroupLayout(phieuMuon);
        phieuMuon.setLayout(phieuMuonLayout);
        phieuMuonLayout.setHorizontalGroup(
            phieuMuonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(phieuMuonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator4)
                .addContainerGap())
            .addGroup(phieuMuonLayout.createSequentialGroup()
                .addGroup(phieuMuonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(phieuMuonLayout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addComponent(Tao)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Luu, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(94, 94, 94)
                        .addComponent(Sua, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(68, 68, 68)
                        .addComponent(Xoa, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(phieuMuonLayout.createSequentialGroup()
                        .addGroup(phieuMuonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, phieuMuonLayout.createSequentialGroup()
                                .addGap(92, 92, 92)
                                .addComponent(lblMaSachError)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(phieuMuonLayout.createSequentialGroup()
                                .addGap(67, 67, 67)
                                .addGroup(phieuMuonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(phieuMuonLayout.createSequentialGroup()
                                        .addComponent(jLabel12)
                                        .addGap(18, 18, 18)
                                        .addComponent(lblMaSach, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(phieuMuonLayout.createSequentialGroup()
                                        .addComponent(jLabel13)
                                        .addGap(18, 18, 18)
                                        .addComponent(lblSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblAnhSach, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(46, 46, 46)
                                .addGroup(phieuMuonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel3))
                                .addGap(30, 30, 30)))
                        .addGroup(phieuMuonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblHanTra, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNgayMuon, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNgayMuonError))))
                .addGap(154, 154, 154))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, phieuMuonLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 774, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(89, 89, 89))
            .addGroup(phieuMuonLayout.createSequentialGroup()
                .addGroup(phieuMuonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(phieuMuonLayout.createSequentialGroup()
                        .addGap(343, 343, 343)
                        .addComponent(jLabel7))
                    .addGroup(phieuMuonLayout.createSequentialGroup()
                        .addGap(102, 102, 102)
                        .addComponent(lblSoLuongError)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        phieuMuonLayout.setVerticalGroup(
            phieuMuonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(phieuMuonLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(phieuMuonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(phieuMuonLayout.createSequentialGroup()
                        .addGroup(phieuMuonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(phieuMuonLayout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addComponent(lblAnhSach, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(phieuMuonLayout.createSequentialGroup()
                                .addGap(51, 51, 51)
                                .addGroup(phieuMuonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel3)
                                    .addComponent(lblNgayMuon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(30, 30, 30)
                                .addComponent(lblNgayMuonError)
                                .addGap(32, 32, 32)
                                .addGroup(phieuMuonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(lblHanTra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(phieuMuonLayout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addGroup(phieuMuonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblMaSach, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addGap(26, 26, 26)
                        .addComponent(lblMaSachError)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                        .addGroup(phieuMuonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblSoLuong, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addComponent(lblSoLuongError)
                        .addGap(30, 30, 30)
                        .addGroup(phieuMuonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(phieuMuonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(Xoa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(Sua))
                            .addGroup(phieuMuonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(Tao)
                                .addComponent(Luu)))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        lblAnhSach.getAccessibleContext().setAccessibleDescription("");

        home.add(phieuMuon, "card3");

        sach.setBackground(new java.awt.Color(255, 255, 255));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 51, 255));
        jLabel8.setText("Thông tin sách");

        TKicon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Search.png"))); // NOI18N
        TKicon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TKiconMouseClicked(evt);
            }
        });

        tableSach.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tableSach.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã Sách", "Tên Sách", "Tác giả", "Thể loại", "số lượng", "NXB", "Ảnh"
            }
        ));
        tableSach.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                tableSachComponentShown(evt);
            }
        });
        jScrollPane3.setViewportView(tableSach);

        AnhSach.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel27.setText("Mã Sách");

        jLabel28.setText("Tên Sách");

        jLabel29.setText("Tác giả");

        jLabel30.setText("Thể loại");

        jLabel31.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(0, 51, 255));
        jLabel31.setText("Danh sách Sách");

        txtMaSach.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaSachActionPerformed(evt);
            }
        });

        txtTenSach.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTenSachActionPerformed(evt);
            }
        });

        jLabel32.setText("NXB");

        txtNXB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNXBActionPerformed(evt);
            }
        });

        loan.setText("Mượn");
        loan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                loanMouseClicked(evt);
            }
        });

        jLabel5.setText("Số lượng");

        javax.swing.GroupLayout sachLayout = new javax.swing.GroupLayout(sach);
        sach.setLayout(sachLayout);
        sachLayout.setHorizontalGroup(
            sachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sachLayout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addGroup(sachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27)
                    .addComponent(jLabel28)
                    .addComponent(jLabel32))
                .addGap(36, 36, 36)
                .addGroup(sachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(sachLayout.createSequentialGroup()
                        .addGroup(sachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNXB, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTenSach, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMaSach, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(46, 46, 46)
                        .addComponent(AnhSach, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(sachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel29)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel30))
                        .addGap(54, 54, 54)
                        .addGroup(sachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTacGia, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                            .addComponent(txtTheLoai)
                            .addComponent(txtSoLuong))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(sachLayout.createSequentialGroup()
                        .addComponent(loan)
                        .addGap(88, 88, 88)
                        .addComponent(jLabel31)
                        .addGap(55, 55, 55)
                        .addComponent(TKtext, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(TKicon)
                        .addContainerGap(78, Short.MAX_VALUE))))
            .addGroup(sachLayout.createSequentialGroup()
                .addGap(287, 287, 287)
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(sachLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3)
                .addContainerGap())
        );
        sachLayout.setVerticalGroup(
            sachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sachLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel8)
                .addGap(21, 21, 21)
                .addGroup(sachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(sachLayout.createSequentialGroup()
                        .addGroup(sachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel27)
                            .addComponent(txtMaSach, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34)
                        .addGroup(sachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel28)
                            .addComponent(txtTenSach, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35)
                        .addGroup(sachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel32)
                            .addComponent(txtNXB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(AnhSach, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(sachLayout.createSequentialGroup()
                        .addGroup(sachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTacGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel29))
                        .addGap(37, 37, 37)
                        .addGroup(sachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel30)
                            .addComponent(txtTheLoai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(sachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))))
                .addGap(18, 18, 18)
                .addGroup(sachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(sachLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel31))
                    .addComponent(loan)
                    .addGroup(sachLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(sachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(TKtext, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TKicon))))
                .addGap(10, 10, 10)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        home.add(sach, "card2");

        taiKhoan.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 51, 255));
        jLabel6.setText("Thông tin cá nhân");

        jLabel17.setText("Mã sinh viên");

        jLabel19.setText("Họ và tên");

        Avatar.setBackground(new java.awt.Color(204, 204, 255));
        Avatar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel21.setText("Ngày sinh");

        jLabel22.setText("Giới tính");

        jLabel23.setText("Địa chỉ");

        jLabel24.setText("SĐT");

        jLabel25.setText("Email");

        CapNhat.setText("Cập nhật");
        CapNhat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CapNhatMouseClicked(evt);
            }
        });

        txtMaSV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaSVActionPerformed(evt);
            }
        });

        txtDiaChi.setColumns(20);
        txtDiaChi.setRows(5);
        jScrollPane2.setViewportView(txtDiaChi);

        buttonGroup1.add(Nam);
        Nam.setText("Nam");

        buttonGroup1.add(Nu);
        Nu.setText("Nữ");

        txtNgaySinh.setDateFormatString("dd/MM/YYYY");

        jSeparator3.setBackground(new java.awt.Color(255, 153, 51));
        jSeparator3.setForeground(new java.awt.Color(255, 153, 0));

        HoTenloi.setText("jLabel10");

        NSloi.setText("jLabel11");

        sdtloi.setText("jLabel18");

        DiaChiloi.setText("jLabel20");

        Emailloi.setText("jLabel26");

        jButton1.setText("Chọn ảnh");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout taiKhoanLayout = new javax.swing.GroupLayout(taiKhoan);
        taiKhoan.setLayout(taiKhoanLayout);
        taiKhoanLayout.setHorizontalGroup(
            taiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(taiKhoanLayout.createSequentialGroup()
                .addGroup(taiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(taiKhoanLayout.createSequentialGroup()
                        .addGap(67, 67, 67)
                        .addGroup(taiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(taiKhoanLayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addGroup(taiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(taiKhoanLayout.createSequentialGroup()
                                        .addComponent(jLabel24)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtSdt, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(taiKhoanLayout.createSequentialGroup()
                                        .addComponent(jLabel21)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(taiKhoanLayout.createSequentialGroup()
                                        .addComponent(jLabel22)
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addGroup(taiKhoanLayout.createSequentialGroup()
                                .addGroup(taiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel17)
                                    .addComponent(jLabel19))
                                .addGap(29, 29, 29)
                                .addGroup(taiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtMaSV, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(taiKhoanLayout.createSequentialGroup()
                        .addGap(133, 133, 133)
                        .addGroup(taiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(taiKhoanLayout.createSequentialGroup()
                                .addGroup(taiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(sdtloi)
                                    .addGroup(taiKhoanLayout.createSequentialGroup()
                                        .addGap(29, 29, 29)
                                        .addComponent(Nam)
                                        .addGap(35, 35, 35)
                                        .addComponent(Nu)))
                                .addGap(90, 90, 90)
                                .addGroup(taiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel23)
                                    .addComponent(jLabel25)))
                            .addComponent(NSloi)
                            .addComponent(HoTenloi))))
                .addGroup(taiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(taiKhoanLayout.createSequentialGroup()
                        .addGroup(taiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(taiKhoanLayout.createSequentialGroup()
                                .addGap(46, 46, 46)
                                .addComponent(Avatar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(taiKhoanLayout.createSequentialGroup()
                                .addGap(74, 74, 74)
                                .addComponent(jButton1)))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, taiKhoanLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(taiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, taiKhoanLayout.createSequentialGroup()
                                .addGroup(taiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(99, 99, 99))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, taiKhoanLayout.createSequentialGroup()
                                .addComponent(DiaChiloi)
                                .addGap(265, 265, 265))))))
            .addGroup(taiKhoanLayout.createSequentialGroup()
                .addGroup(taiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(taiKhoanLayout.createSequentialGroup()
                        .addGap(307, 307, 307)
                        .addComponent(jLabel6))
                    .addGroup(taiKhoanLayout.createSequentialGroup()
                        .addGap(207, 207, 207)
                        .addComponent(CapNhat)
                        .addGap(212, 212, 212)
                        .addComponent(Emailloi)))
                .addGap(0, 261, Short.MAX_VALUE))
        );
        taiKhoanLayout.setVerticalGroup(
            taiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(taiKhoanLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(taiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(taiKhoanLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(Avatar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(taiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(taiKhoanLayout.createSequentialGroup()
                                .addGap(91, 91, 91)
                                .addComponent(jLabel23))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, taiKhoanLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(12, 12, 12)
                        .addComponent(DiaChiloi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Emailloi)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(taiKhoanLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(taiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(txtMaSV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(44, 44, 44)
                        .addGroup(taiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 21, Short.MAX_VALUE)
                        .addComponent(HoTenloi)
                        .addGap(18, 18, 18)
                        .addGroup(taiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel21)
                            .addComponent(txtNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                        .addComponent(NSloi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(taiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Nam)
                            .addComponent(Nu)
                            .addComponent(jLabel22))
                        .addGap(45, 45, 45)
                        .addGroup(taiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24)
                            .addComponent(txtSdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(taiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(sdtloi)
                            .addComponent(jLabel25))
                        .addGap(30, 30, 30)
                        .addComponent(CapNhat)
                        .addGap(56, 56, 56))))
        );

        home.add(taiKhoan, "card4");

        chat.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("Chat");

        javax.swing.GroupLayout chatLayout = new javax.swing.GroupLayout(chat);
        chat.setLayout(chatLayout);
        chatLayout.setHorizontalGroup(
            chatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chatLayout.createSequentialGroup()
                .addGap(344, 344, 344)
                .addComponent(jLabel2)
                .addContainerGap(417, Short.MAX_VALUE))
        );
        chatLayout.setVerticalGroup(
            chatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chatLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel2)
                .addContainerGap(470, Short.MAX_VALUE))
        );

        home.add(chat, "card6");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Đổi mật khẩu", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18), new java.awt.Color(0, 51, 255))); // NOI18N

        jLabel9.setText("Mật khẩu cũ");

        jLabel10.setText("Mật khẩu mới");

        jLabel11.setText("Xác nhận mật khẩu mới");

        btnĐoiMK.setText("Đổi mật khẩu");
        btnĐoiMK.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnĐoiMKMouseClicked(evt);
            }
        });

        txtOldPassword.setText("jPasswordField1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11))
                        .addGap(29, 29, 29)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNewPassword)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtOldPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txtConfirmPassword)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(140, 140, 140)
                        .addComponent(btnĐoiMK)))
                .addGap(90, 90, 90))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtOldPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtNewPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(48, 48, 48)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtConfirmPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                .addComponent(btnĐoiMK)
                .addGap(48, 48, 48))
        );

        javax.swing.GroupLayout MKLayout = new javax.swing.GroupLayout(MK);
        MK.setLayout(MKLayout);
        MKLayout.setHorizontalGroup(
            MKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MKLayout.createSequentialGroup()
                .addGap(219, 219, 219)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(145, Short.MAX_VALUE))
        );
        MKLayout.setVerticalGroup(
            MKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MKLayout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(73, Short.MAX_VALUE))
        );

        home.add(MK, "card7");

        jLabel14.setFont(new java.awt.Font("Segoe UI Black", 3, 24)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Thư Viện");

        menu.setBackground(new java.awt.Color(153, 153, 153));

        itemSach.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        itemSach.setText("Sách");
        itemSach.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                itemSachMouseClicked(evt);
            }
        });

        itemPM.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        itemPM.setText("Phiếu mượn");
        itemPM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                itemPMMouseClicked(evt);
            }
        });

        itemTK.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        itemTK.setText("Tài khoản");
        itemTK.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                itemTKMouseClicked(evt);
            }
        });

        btnLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Log out.png"))); // NOI18N
        btnLogout.setText("Đăng xuất");
        btnLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLogoutMouseClicked(evt);
            }
        });

        itemChat.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        itemChat.setText("Chat");
        itemChat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                itemChatMouseClicked(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/User_1.png"))); // NOI18N

        itemPW.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        itemPW.setText("Đổi mật khẩu");
        itemPW.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                itemPWMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout menuLayout = new javax.swing.GroupLayout(menu);
        menu.setLayout(menuLayout);
        menuLayout.setHorizontalGroup(
            menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuLayout.createSequentialGroup()
                .addGroup(menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(menuLayout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addGroup(menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(itemTK)
                            .addComponent(btnLogout)
                            .addComponent(itemPW)))
                    .addGroup(menuLayout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(itemPM)
                            .addGroup(menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel15)
                                .addComponent(itemSach))))
                    .addGroup(menuLayout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addComponent(itemChat)))
                .addContainerGap(48, Short.MAX_VALUE))
        );
        menuLayout.setVerticalGroup(
            menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel15)
                .addGap(52, 52, 52)
                .addComponent(itemSach)
                .addGap(32, 32, 32)
                .addComponent(itemPM)
                .addGap(40, 40, 40)
                .addComponent(itemTK)
                .addGap(26, 26, 26)
                .addComponent(itemPW)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(itemChat)
                .addGap(26, 26, 26)
                .addComponent(btnLogout)
                .addGap(40, 40, 40))
        );

        javax.swing.GroupLayout mainLayout = new javax.swing.GroupLayout(main);
        main.setLayout(mainLayout);
        mainLayout.setHorizontalGroup(
            mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainLayout.createSequentialGroup()
                .addGap(51, 854, Short.MAX_VALUE)
                .addComponent(jLabel14)
                .addGap(35, 35, 35))
            .addGroup(mainLayout.createSequentialGroup()
                .addComponent(menu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(home, javax.swing.GroupLayout.PREFERRED_SIZE, 800, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        mainLayout.setVerticalGroup(
            mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(home, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(menu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(main, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(main, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtMaSachActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaSachActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaSachActionPerformed

    private void txtTenSachActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTenSachActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTenSachActionPerformed

    private void txtNXBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNXBActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNXBActionPerformed

    private void txtMaSVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaSVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaSVActionPerformed

    private void tableSachComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_tableSachComponentShown
        DSSach();

    }//GEN-LAST:event_tableSachComponentShown

    private void TKiconMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TKiconMouseClicked

        searchBooks(TKtext.getText());
    }//GEN-LAST:event_TKiconMouseClicked

    private void tablePMComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_tablePMComponentShown
        DSPM();
    }//GEN-LAST:event_tablePMComponentShown

    private void itemChatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_itemChatMouseClicked
        chat.setVisible(true);
        phieuMuon.setVisible(false);
        sach.setVisible(false);
        TrangDau.setVisible(false);
        taiKhoan.setVisible(false);
    }//GEN-LAST:event_itemChatMouseClicked

    private void itemTKMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_itemTKMouseClicked
        TrangDau.setVisible(false);
        taiKhoan.setVisible(true);
        phieuMuon.setVisible(false);
        sach.setVisible(false);
        chat.setVisible(false);
        MK.setVisible(false);
        HoTenloi.setText("");
        NSloi.setText("");
        sdtloi.setText("");
        DiaChiloi.setText("");
        Emailloi.setText("");
        StudentInfo(currentUser);
    }//GEN-LAST:event_itemTKMouseClicked

    private void itemPMMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_itemPMMouseClicked
        TrangDau.setVisible(false);
        taiKhoan.setVisible(false);
        phieuMuon.setVisible(true);
        sach.setVisible(false);
        chat.setVisible(false);
        MK.setVisible(false);
        lblNgayMuon.setDate(null);
        lblHanTra.setText("");
        lblAnhSach.setIcon(null);
        lblSoLuong.setValue(1);
        lblMaSach.setText("");
        lblMaSachError.setText("");
        lblSoLuongError.setText("");
        lblNgayMuonError.setText("");
    }//GEN-LAST:event_itemPMMouseClicked

    private void itemSachMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_itemSachMouseClicked
        TrangDau.setVisible(false);
        taiKhoan.setVisible(false);
        phieuMuon.setVisible(false);
        sach.setVisible(true);
        chat.setVisible(false);
        MK.setVisible(false);
        txtMaSach.setText("");
        txtNXB.setText("");
        txtSoLuong.setText("");
        txtTacGia.setText("");
        txtTenSach.setText("");
        txtTheLoai.setText("");
        AnhSach.setIcon(null);
    }//GEN-LAST:event_itemSachMouseClicked

    private void TaoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TaoMouseClicked
        Date currentDate = new java.util.Date();
        Date hanTra = addDays(currentDate, 5);
        lblNgayMuon.setDate(currentDate);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        lblHanTra.setText(sdf.format(hanTra));
        lblAnhSach.setIcon(null);
        lblSoLuong.setValue(1);
        lblMaSach.setText("");
        lblMaSachError.setText("");
        lblSoLuongError.setText("");
        lblNgayMuonError.setText("");
    }//GEN-LAST:event_TaoMouseClicked

    private void LuuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LuuMouseClicked
        taoPhieuMuon();

    }//GEN-LAST:event_LuuMouseClicked

    private void XoaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_XoaMouseClicked
        int row = tablePM.getSelectedRow();
        Integer maPM = (Integer) tablePM.getValueAt(row, 0);
        String maPMString = String.valueOf(maPM);

        // Kiểm tra nếu mã phiếu mượn không rỗng
        if (maPMString != null && !maPMString.trim().isEmpty()) {
            String maSach = (String) tablePM.getValueAt(row, 1);  // Lấy mã sách từ bảng
            int soLuongMuon = (Integer) tablePM.getValueAt(row, 2);  // Lấy số lượng sách mượn

            boolean isDeleted = xoaPhieuMuon(maPMString);  // Gọi phương thức xóa

            // Hiển thị thông báo cho người dùng
            if (isDeleted) {
                updateXoaSachQuantity(maSach, soLuongMuon);
                JOptionPane.showMessageDialog(this, "Xóa phiếu mượn thành công!");
                DSPM();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa phiếu mượn thất bại!");

            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu mượn để xóa!");
        }
    }//GEN-LAST:event_XoaMouseClicked

    private void SuaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SuaMouseClicked
        updatePhieuMuon();
    }//GEN-LAST:event_SuaMouseClicked

    private void loanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loanMouseClicked
        TrangDau.setVisible(false);
        taiKhoan.setVisible(false);
        phieuMuon.setVisible(true);
        sach.setVisible(false);
        chat.setVisible(false);
        Date currentDate = new java.util.Date();
        Date hanTra = addDays(currentDate, 5);
        lblNgayMuon.setDate(currentDate);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        lblHanTra.setText(sdf.format(hanTra));
        lblAnhSach.setIcon(AnhSach.getIcon());
        lblSoLuong.setValue(1);
        lblMaSach.setText(txtMaSach.getText());
        lblMaSachError.setText("");
        lblSoLuongError.setText("");
        lblNgayMuonError.setText("");
        DSPM();
    }//GEN-LAST:event_loanMouseClicked

    private void btnLogoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutMouseClicked
        currentUser = null;
        currentPassword = null;
        login l = new login();
        l.setVisible(true);
        l.setLocationRelativeTo(null);
        this.dispose();

    }//GEN-LAST:event_btnLogoutMouseClicked

    private void itemPWMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_itemPWMouseClicked
        TrangDau.setVisible(false);
        taiKhoan.setVisible(false);
        phieuMuon.setVisible(false);
        sach.setVisible(false);
        chat.setVisible(false);
        MK.setVisible(true);
    }//GEN-LAST:event_itemPWMouseClicked

    private void CapNhatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CapNhatMouseClicked

        String maSV = txtMaSV.getText();
        String hoTen = txtHoTen.getText();
        String gioiTinh = Nam.isSelected() ? "Nam" : "Nữ";
        Date ngaySinh = txtNgaySinh.getDate();
        String diaChi = txtDiaChi.getText();
        String sdt = txtSdt.getText();
        String email = txtEmail.getText();

        boolean hasError = false;

        // Kiểm tra các trường không được trống
        if (isFieldEmpty(hoTen)) {
            HoTenloi.setText("Vui lòng nhập Họ và tên!");
            hasError = true;
        }
        if (isFieldEmpty(diaChi)) {
            DiaChiloi.setText("Vui lòng nhập địa chỉ!");
            hasError = true;
        }
        if (isFieldEmpty(sdt)) {
            sdtloi.setText("Vui lòng nhập Số điện thoại!");
            hasError = true;
        }
        if (isFieldEmpty(email)) {
            Emailloi.setText("Vui lòng nhập Email!");
            hasError = true;

        }

        // Kiểm tra ngày sinh không nhỏ hơn ngày hiện tại
        if (ngaySinh == null) {
            NSloi.setText("Vui lòng chọn  ngày sinh!");
            hasError = true;
        } else if (!isValidBirthDate(ngaySinh)) {
            NSloi.setText("Ngày sinh không hợp lệ!");
            hasError = true;
        }

        // Kiểm tra số điện thoại hợp lệ
        if (!isValidPhoneNumber(sdt)) {
            sdtloi.setText("Số điện thoại không hợp lệ!");
            hasError = true;
        }

        // Kiểm tra email hợp lệ
        if (!isValidEmail(email)) {
            Emailloi.setText("Email không hợp lệ!");
            hasError = true;
        }

        // Nếu có lỗi, dừng lại không thực hiện cập nhật
        if (hasError) {
            return;
        }
        // Dùng imagePath để lấy đường dẫn ảnh đã chọn
        String imagePathToSave = this.imagePath;
        try {
            Connection conn = DatabaseConnection.connect();

            // Câu lệnh SQL cập nhật thông tin sinh viên
            String query = "UPDATE SV SET HoTen = ?, GioiTinh = ?, NgaySinh = ?, DiaChi = ?, SDT = ?, Email = ?, Anh = ? WHERE MaSV = ?";
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, hoTen);
            ps.setString(2, gioiTinh);
            ps.setDate(3, new java.sql.Date(ngaySinh.getTime()));
            ps.setString(4, diaChi);
            ps.setString(5, sdt);
            ps.setString(6, email);
            ps.setString(7, imagePathToSave);
            ps.setString(8, maSV);

            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Cập nhật thông tin sinh viên thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy sinh viên với mã " + maSV);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật thông tin sinh viên: " + e.getMessage());
        }


    }//GEN-LAST:event_CapNhatMouseClicked

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        chooseImage();
    }//GEN-LAST:event_jButton1MouseClicked

    private void btnĐoiMKMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnĐoiMKMouseClicked

        String oldPassword = new String(txtOldPassword.getPassword());
        String newPassword = new String(txtNewPassword.getText());
        String confirmPassword = new String(txtConfirmPassword.getText());

        if (newPassword.equals(confirmPassword)) {
            if (changePassword(oldPassword, newPassword)) {
                JOptionPane.showMessageDialog(null, "Đổi mật khẩu thành công.");
                currentPassword = newPassword;
            } else {
                JOptionPane.showMessageDialog(null, "Mật khẩu cũ không chính xác.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Mật khẩu mới và xác nhận mật khẩu không khớp! Vui lòng nhập lại.");
        }

    }//GEN-LAST:event_btnĐoiMKMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                home h = new home("SV", "password");
                h.setVisible(true);
                h.setLocationRelativeTo(null);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AnhSach;
    private javax.swing.JLabel Avatar;
    private javax.swing.JButton CapNhat;
    private javax.swing.JLabel DiaChiloi;
    private javax.swing.JLabel Emailloi;
    private javax.swing.JLabel HoTenloi;
    private javax.swing.JButton Luu;
    private javax.swing.JPanel MK;
    private javax.swing.JLabel NSloi;
    private javax.swing.JRadioButton Nam;
    private javax.swing.JRadioButton Nu;
    private javax.swing.JButton Sua;
    private javax.swing.JLabel TKicon;
    private javax.swing.JTextField TKtext;
    private javax.swing.JButton Tao;
    private javax.swing.JPanel TrangDau;
    private javax.swing.JButton Xoa;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnĐoiMK;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel chat;
    private javax.swing.JPanel home;
    private javax.swing.JLabel itemChat;
    private javax.swing.JLabel itemPM;
    private javax.swing.JLabel itemPW;
    private javax.swing.JLabel itemSach;
    private javax.swing.JLabel itemTK;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JLabel lblAnhSach;
    private javax.swing.JTextField lblHanTra;
    private javax.swing.JTextField lblMaSach;
    private javax.swing.JLabel lblMaSachError;
    private com.toedter.calendar.JDateChooser lblNgayMuon;
    private javax.swing.JLabel lblNgayMuonError;
    private javax.swing.JSpinner lblSoLuong;
    private javax.swing.JLabel lblSoLuongError;
    private javax.swing.JButton loan;
    private javax.swing.JPanel main;
    private javax.swing.JPanel menu;
    private javax.swing.JPanel phieuMuon;
    private javax.swing.JPanel sach;
    private javax.swing.JLabel sdtloi;
    private javax.swing.JTable tablePM;
    private javax.swing.JTable tableSach;
    private javax.swing.JPanel taiKhoan;
    private javax.swing.JTextField txtConfirmPassword;
    private javax.swing.JTextArea txtDiaChi;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtHoTen;
    private javax.swing.JTextField txtMaSV;
    private javax.swing.JTextField txtMaSach;
    private javax.swing.JTextField txtNXB;
    private javax.swing.JTextField txtNewPassword;
    private com.toedter.calendar.JDateChooser txtNgaySinh;
    private javax.swing.JPasswordField txtOldPassword;
    private javax.swing.JTextField txtSdt;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JTextField txtTacGia;
    private javax.swing.JTextField txtTenSach;
    private javax.swing.JTextField txtTheLoai;
    // End of variables declaration//GEN-END:variables

}
