package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.net.URL;

public class MenuScreen extends JPanel {

    private static final Color ROJO_BA = new Color(180, 20, 20);
    private static final Color ROJO_OSCURO = new Color(140, 12, 12);
    private static final Color GRIS_CLARO = new Color(248, 248, 248);
    private static final Color GRIS_CAJA = new Color(240, 240, 240);
    private static final Color GRIS_BORDE = new Color(225, 225, 225);
    private static final Color GRIS_TEXTO = new Color(110, 110, 110);
    private static final Color BLANCO = Color.WHITE;

    private JLabel tabActivaLabel;
    private JPanel tabActivaLinea;
    private String tabActivaTexto;

    public MenuScreen() {
        setLayout(new BorderLayout());
        setBackground(BLANCO);
        add(crearHeader(), BorderLayout.NORTH);

    }

    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(ROJO_BA);
        header.setPreferredSize(new Dimension(0, 100));
        header.setBorder(new EmptyBorder(0, 20, 0, 20));

        JPanel panelLogo = new JPanel();
        panelLogo.setOpaque(false);
        panelLogo.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JLabel lblLogo = crearLogoHeader();
        panelLogo.add(lblLogo);
        header.add(panelLogo, BorderLayout.WEST);

        JPanel panelTabs = new JPanel(new FlowLayout(FlowLayout.CENTER, 35, 0));
        panelTabs.setOpaque(false);

        String[] tabs = {"Cuentas", "Operaciones", "Estado de Cuenta", "Intereses", "Reportes"};
        for (int i = 0; i < tabs.length; i++) {
            panelTabs.add(crearTabHeader(tabs[i], i == 0));
        }
        header.add(panelTabs, BorderLayout.CENTER);

        JPanel panelDerecha = new JPanel();
        panelDerecha.setOpaque(false);
        panelDerecha.setLayout(new BoxLayout(panelDerecha, BoxLayout.X_AXIS));

        JLabel lblFecha = new JLabel("📅 " + java.time.LocalDate.now().toString());
        lblFecha.setForeground(BLANCO);
        lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelDerecha.add(lblFecha);
        panelDerecha.add(Box.createHorizontalStrut(20));

        JLabel lblReloj = new JLabel("🕐");
        lblReloj.setForeground(BLANCO);
        lblReloj.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        panelDerecha.add(lblReloj);
        panelDerecha.add(Box.createHorizontalStrut(12));

        JLabel lblAvatar = crearAvatar();
        panelDerecha.add(lblAvatar);

        header.add(panelDerecha, BorderLayout.EAST);

        return header;
    }

    private JLabel crearLogoHeader() {
        Image img = cargarImagen("/ASSETS/LOGO.png");
        if (img != null) {
            Image escalada = img.getScaledInstance(150, 50, Image.SCALE_SMOOTH);
            return new JLabel(new ImageIcon(escalada));
        } else {
            JLabel lbl = new JLabel("Banco Atlántida");
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
            lbl.setForeground(BLANCO);
            return lbl;
        }
    }

    private JPanel crearTabHeader(String texto, boolean activa) {
        JPanel panelTab = new JPanel();
        panelTab.setOpaque(false);
        panelTab.setLayout(new BoxLayout(panelTab, BoxLayout.Y_AXIS));
        panelTab.setBorder(new EmptyBorder(8, 6, 4, 6));

        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", activa ? Font.BOLD : Font.PLAIN, 14));
        lbl.setForeground(BLANCO);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelTab.add(lbl);

        panelTab.add(Box.createVerticalStrut(6));

        JPanel linea = new JPanel();
        linea.setMaximumSize(new Dimension(texto.length() * 8 + 10, 3));
        linea.setPreferredSize(new Dimension(texto.length() * 8 + 10, 3));
        linea.setBackground(activa ? BLANCO : ROJO_BA);
        linea.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelTab.add(linea);

        panelTab.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        panelTab.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                seleccionarTab(texto, lbl, linea);
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (tabActivaLabel != lbl) {
                    panelTab.setOpaque(true);
                    panelTab.setBackground(ROJO_OSCURO);
                    panelTab.repaint();
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                panelTab.setOpaque(false);
                panelTab.repaint();
            }
        });

        if (activa) {
            tabActivaLabel = lbl;
            tabActivaLinea = linea;
            tabActivaTexto = texto;
        }

        return panelTab;
    }

    private void seleccionarTab(String texto, JLabel lblClickeado, JPanel lineaClickeada) {
        if (tabActivaLabel != null) {
            tabActivaLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        }
        if (tabActivaLinea != null) {
            tabActivaLinea.setBackground(ROJO_BA);
        }

        // Activar la nueva
        lblClickeado.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lineaClickeada.setBackground(BLANCO);

        tabActivaLabel = lblClickeado;
        tabActivaLinea = lineaClickeada;
        tabActivaTexto = texto;

        System.out.println("Pestaña seleccionada: " + texto);
    }

    private Image cargarImagen(String ruta) {
        try {
            URL url = getClass().getResource(ruta);
            if (url != null) {
                return new ImageIcon(url).getImage();
            }
        } catch (Exception e) {
            System.err.println("No se pudo cargar imagen: " + ruta);
        }
        return null;
    }

    private JLabel crearAvatar() {
        JLabel avatar = new JLabel("👤");
        avatar.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        avatar.setForeground(BLANCO);
        avatar.setOpaque(true);
        avatar.setBackground(ROJO_OSCURO);
        avatar.setHorizontalAlignment(SwingConstants.CENTER);
        avatar.setPreferredSize(new Dimension(36, 36));
        avatar.setBorder(new LineBorder(BLANCO, 2, true));
        return avatar;
    }

}
