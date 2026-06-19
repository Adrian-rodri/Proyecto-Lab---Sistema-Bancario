package GUI;

import java.awt.*;
import javax.swing.*;

public class MenuScreen extends JPanel {


    public MenuScreen(){
        this.setLayout(new BorderLayout());
        this.setBackground(Color.red);
        this.add(buildUI(), BorderLayout.CENTER);
    }
    public JPanel buildUI(){
        JPanel panelPrincipal= new JPanel();
        panelPrincipal.setLayout(new GridBagLayout());
        GridBagConstraints gbc= new GridBagConstraints();
        
        JButton btnCuenta= new JButton("Cuenta");
        btnCuenta.setBackground(Color.green);
        gbc.gridx=0;
        gbc.gridy=0;
        panelPrincipal.add(btnCuenta,gbc);
        return panelPrincipal;
        
    }
}

