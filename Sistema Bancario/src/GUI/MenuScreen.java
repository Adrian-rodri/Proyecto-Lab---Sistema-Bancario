package GUI;

import sistema.bancario.CuentaBancaria;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
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
    private JPanel sidebar;
    private JPanel contenidoSidebar;
    private CuentaBancaria cuentaSeleccionada = null;
    private JPanel panelCentral;


    public MenuScreen() {
        setLayout(new BorderLayout());
        setBackground(BLANCO);
        sidebar = crearSidebar();
        sidebar.setVisible(false);
        panelCentral = crearPanelCentral();
        add(crearHeader(), BorderLayout.NORTH);
        add(sidebar, BorderLayout.WEST);
        add(panelCentral, BorderLayout.CENTER);
    }

    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(ROJO_BA);
        header.setPreferredSize(new Dimension(0, 110));
        header.setBorder(new EmptyBorder(10, 20, 0, 20));

        JPanel filaSuperior = new JPanel(new BorderLayout());
        filaSuperior.setOpaque(false);

        JPanel panelLogo = new JPanel();
        panelLogo.setOpaque(false);
        panelLogo.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JLabel lblLogo = crearLogoHeader();
        panelLogo.add(lblLogo);
        filaSuperior.add(panelLogo, BorderLayout.WEST);

        JPanel panelDerecha = new JPanel();
        panelDerecha.setOpaque(false);
        panelDerecha.setLayout(new BoxLayout(panelDerecha, BoxLayout.X_AXIS));

        JLabel lblFecha = new JLabel("" + java.time.LocalDate.now().toString());
        lblFecha.setForeground(BLANCO);
        lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelDerecha.add(lblFecha);
        panelDerecha.add(Box.createHorizontalStrut(20));

        JLabel lblReloj = new JLabel("");
        lblReloj.setForeground(BLANCO);
        lblReloj.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        panelDerecha.add(lblReloj);
        panelDerecha.add(Box.createHorizontalStrut(12));

        JLabel lblAvatar = crearAvatar();
        panelDerecha.add(lblAvatar);

        filaSuperior.add(panelDerecha, BorderLayout.EAST);

        header.add(filaSuperior, BorderLayout.NORTH);

        JPanel panelTabs = new JPanel(new FlowLayout(FlowLayout.CENTER, 35, 0));
        panelTabs.setOpaque(false);
        panelTabs.setBorder(new EmptyBorder(8, 0, 0, 0));

        String[] tabs = {"Cuentas", "Operaciones", "Estado de Cuenta", "Intereses", "Reportes"};
        for (int i = 0; i < tabs.length; i++) {
            panelTabs.add(crearTabHeader(tabs[i], i == 0));
        }
        header.add(panelTabs, BorderLayout.CENTER);

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

        lblClickeado.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lineaClickeada.setBackground(BLANCO);

        tabActivaLabel = lblClickeado;
        tabActivaLinea = lineaClickeada;
        tabActivaTexto = texto;

        sidebar.setVisible(true);
        switch (texto) {
            case "Cuentas" -> mostrarSidebarCuentas();
            case "Operaciones" -> mostrarSidebarOperaciones();
            case "Estado de Cuenta" -> mostrarSidebarEstadoCuenta();
            default -> {
                contenidoSidebar.removeAll();
                contenidoSidebar.revalidate();
                contenidoSidebar.repaint();
            }
        }
        revalidate();
        repaint();
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

    private JPanel crearSidebar() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(BLANCO);
        panel.setPreferredSize(new Dimension(280, 0));
        panel.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 0, 1, GRIS_BORDE),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JButton btnVolver = new JButton("←  Volver al inicio");
        btnVolver.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnVolver.setForeground(Color.DARK_GRAY);
        btnVolver.setBackground(GRIS_CLARO);
        btnVolver.setBorder(new CompoundBorder(
                new LineBorder(GRIS_BORDE, 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));
        btnVolver.setHorizontalAlignment(SwingConstants.LEFT);
        btnVolver.setFocusPainted(false);
        btnVolver.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVolver.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnVolver.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        btnVolver.addActionListener(e -> {
            sidebar.setVisible(false);
            revalidate();
            repaint();
        });

        JPanel norte = new JPanel();
        norte.setLayout(new BoxLayout(norte, BoxLayout.Y_AXIS));
        norte.setOpaque(false);
        norte.add(btnVolver);
        norte.add(Box.createVerticalStrut(15));

        contenidoSidebar = new JPanel();
        contenidoSidebar.setLayout(new BoxLayout(contenidoSidebar, BoxLayout.Y_AXIS));
        contenidoSidebar.setOpaque(false);

        JPanel botones = new JPanel(new GridLayout(1, 2, 8, 0));
        botones.setOpaque(false);
        botones.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        botones.add(crearBotonInferior("Actualizar"));
        botones.add(crearBotonInferior("Salir"));

        panel.add(norte, BorderLayout.NORTH);
        panel.add(contenidoSidebar, BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);

        return panel;
    }

    private void mostrarSidebarCuentas() {
        contenidoSidebar.removeAll();
        contenidoSidebar.add(crearCategoria("ABRIR CUENTA", new String[]{
                "Cuenta de Ahorros",
                "Cuenta Corriente",
                "Cuenta Plazo Fijo"
        }, new Runnable[]{
                () -> mostrarEnCentro(new AbrirCuentaPanel("ahorros")),
                () -> mostrarEnCentro(new AbrirCuentaPanel("corriente")),
                () -> mostrarEnCentro(new AbrirCuentaPanel("plazofijo"))
        }));
        contenidoSidebar.revalidate();
        contenidoSidebar.repaint();
    }
    private void mostrarSidebarOperaciones() {
        contenidoSidebar.removeAll();
        contenidoSidebar.add(crearCategoria("BUSCAR CUENTA", new String[]{
                "Buscar Cuenta"
        }, new Runnable[]{
                () -> mostrarEnCentro(new BuscarCuentaPanel(c -> {
                    cuentaSeleccionada = c;
                }))
        }));
        contenidoSidebar.add(Box.createVerticalStrut(18));
        contenidoSidebar.add(crearCategoria("OPERACIONES", new String[]{
                "Depósito",
                "Retiro",
                "Transferencia"
        }, new Runnable[]{
                () -> manejarOperacion("Depósito"),
                () -> manejarOperacion("Retiro"),
                () -> manejarOperacion("Transferencia")
        }));
        contenidoSidebar.revalidate();
        contenidoSidebar.repaint();
    }

    private void mostrarSidebarEstadoCuenta() {
        contenidoSidebar.removeAll();
        contenidoSidebar.add(crearCategoria("CONSULTAS", new String[]{
                "Buscar Cuenta",
                "Estado de Cuenta"
        }, new Runnable[]{
                () -> mostrarEnCentro(new BuscarCuentaPanel(c -> {
                    cuentaSeleccionada = c;
                })),
                () -> mostrarEnCentro(new JPanel())
        }));
        contenidoSidebar.revalidate();
        contenidoSidebar.repaint();
    }

    private void manejarOperacion(String tipo) {
        if (cuentaSeleccionada == null) {
            mostrarMensajeCentro("Seleccione una cuenta primero para realizar un " + tipo);
        } else {
            mostrarMensajeCentro("Operación: " + tipo + " — cuenta " + cuentaSeleccionada.getNumeroCuenta());
        }
    }

    private void mostrarEnCentro(JPanel panel) {
        panelCentral.removeAll();
        panelCentral.setLayout(new BorderLayout());
        panelCentral.add(panel, BorderLayout.CENTER);
        panelCentral.revalidate();
        panelCentral.repaint();
    }

    private void mostrarMensajeCentro(String mensaje) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(GRIS_CLARO);
        JLabel lbl = new JLabel(mensaje);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lbl.setForeground(GRIS_TEXTO);
        p.add(lbl);
        mostrarEnCentro(p);
    }


    private JPanel crearCategoria(String titulo, String[] opciones, Runnable[] acciones) {
        JPanel categoria = new JPanel();
        categoria.setLayout(new BoxLayout(categoria, BoxLayout.Y_AXIS));
        categoria.setOpaque(false);
        categoria.setAlignmentX(Component.LEFT_ALIGNMENT);
        categoria.setMaximumSize(new Dimension(Integer.MAX_VALUE, 9999));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitulo.setForeground(Color.DARK_GRAY);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        categoria.add(lblTitulo);
        categoria.add(Box.createVerticalStrut(8));

        for (int i = 0; i < opciones.length; i++) {
            final Runnable accion = acciones[i];
            JButton btn = crearCajaOpcion(opciones[i]);
            btn.addActionListener(e -> accion.run());
            categoria.add(btn);
            categoria.add(Box.createVerticalStrut(6));
        }

        return categoria;
    }


        private JButton crearCajaOpcion(String texto) {
            JButton caja = new JButton(texto);
            caja.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            caja.setForeground(Color.DARK_GRAY);
            caja.setBackground(GRIS_CAJA);
            caja.setHorizontalAlignment(SwingConstants.LEFT);
            caja.setBorder(new CompoundBorder(
                    new LineBorder(GRIS_BORDE, 1, true),
                    new EmptyBorder(10, 14, 10, 14)
            ));
            caja.setFocusPainted(false);
            caja.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            caja.setAlignmentX(Component.LEFT_ALIGNMENT);
            caja.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));


            caja.addChangeListener(e -> {
                if (caja.getModel().isRollover()) {
                    caja.setBackground(new Color(230, 230, 230));
                } else {
                    caja.setBackground(GRIS_CAJA);
                }
            });

            return caja;
        }

        private JButton crearBotonInferior(String texto) {
            JButton btn = new JButton(texto);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
            btn.setForeground(ROJO_BA);
            btn.setBackground(BLANCO);
            btn.setBorder(new LineBorder(ROJO_BA, 1, true));
            btn.setFocusPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            return btn;
        }


        private JPanel crearPanelCentral() {
            JPanel centro = new JPanel();
            centro.setLayout(new GridBagLayout());
            centro.setBackground(GRIS_CLARO);

            JPanel tarjeta = new JPanel();
            tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
            tarjeta.setBackground(BLANCO);
            tarjeta.setBorder(new CompoundBorder(
                    new LineBorder(GRIS_BORDE, 1, true),
                    new EmptyBorder(50, 70, 50, 70)
            ));

            Image img = cargarImagen("/ASSETS/LOGOBLANCO.png");
            JLabel lblLogoGrande;
            if (img != null) {
                Image escalada = img.getScaledInstance(220, 75, Image.SCALE_SMOOTH);
                lblLogoGrande = new JLabel(new ImageIcon(escalada));
            } else {
                lblLogoGrande = new JLabel("Banco Atlántida");
                lblLogoGrande.setFont(new Font("Segoe UI", Font.BOLD, 28));
                lblLogoGrande.setForeground(new Color(210, 210, 210));
            }
            lblLogoGrande.setAlignmentX(Component.CENTER_ALIGNMENT);
            tarjeta.add(lblLogoGrande);

            centro.add(tarjeta);
            return centro;
        }


        private Image cargarImagen(String ruta) {
            try {
                URL url = getClass().getResource(ruta);
                if (url != null) return new ImageIcon(url).getImage();
            } catch (Exception e) {
                System.err.println("No se pudo cargar imagen: " + ruta);
            }
            return null;
        }


    }