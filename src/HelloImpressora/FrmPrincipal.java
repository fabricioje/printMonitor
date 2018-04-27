package HelloImpressora;

import java.awt.Desktop;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author JoãoPedro
 */
public class FrmPrincipal extends javax.swing.JFrame {

    /**
     * Creates new form FrmPrincipal
     */
    static Propriedades config;
    static String[] opcoes;
    public static Image imagemTitulo,imagemTray,imagemFundo;
    Executor executor1;
    Thread network;
    
    public FrmPrincipal() {
        initComponents();
        //ICONE NA BARRA DE TITULO
        URL url = this.getClass().getResource("icones/icoRecart.png");  
        imagemTitulo = Toolkit.getDefaultToolkit().getImage(url);  
        //ARQUIVO DE CONFIGURAÇÃO PARA PUXAR DIRETORIOS
        config = new Propriedades();
        opcoes = config.carregarOpcoes();
        
        //ICONE BANDEJA DO SISTEMA (SEGUNDO PLANO)        
        URL urlTray = this.getClass().getResource("icones/iconTray.png");  
        imagemTray = Toolkit.getDefaultToolkit().getImage(urlTray);
        
        this.setIconImage(imagemTitulo);
                
        //CAMINHO DA IMAGEM PADRAO
        String destino = System.getenv("ProgramFiles")+"\\Mensagem de Impressão\\ArteMsg.png";
        InputStream is = null;
        FileOutputStream fos = null;
        int bytes = 0;               
        URL urlImg = null;
        
        try {
            urlImg = new URL(opcoes[0]);
            is = urlImg.openStream();
            fos = new FileOutputStream(destino);
            while ((bytes = is.read()) != -1) {
                fos.write(bytes);
            }
            is.close();
            fos.close();
        } catch (IOException ex) {
            try {
                urlImg = new URL("file:///"+System.getenv("ProgramFiles")+"/Mensagem de Impressão/ArteMsg.png");
            } catch (MalformedURLException ex1) {
                Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
                                                
        //SETAR IMAGEM
        ImageIcon imgIcon = new ImageIcon(urlImg);
        lblArte.setIcon(imgIcon);
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblLink = new javax.swing.JLabel();
        lblArte = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Mensagem de Impressão");
        setResizable(false);

        jPanel1.setLayout(null);

        lblLink.setToolTipText("Recart Informática");
        lblLink.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblLinkMouseClicked(evt);
            }
        });
        jPanel1.add(lblLink);
        lblLink.setBounds(324, 390, 110, 30);

        lblStatus.setFont(new java.awt.Font("Arial Narrow", 0, 16)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(102, 102, 102));
        lblStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(lblStatus);
        lblStatus.setBounds(0, 330, 450, 30);

        lblArte.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblArte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/HelloImpressora/icones/ArteMsg.png"))); // NOI18N
        jPanel1.add(lblArte);
        lblArte.setBounds(0, 0, 455, 430);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(461, 459));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void lblLinkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblLinkMouseClicked
        try {
            try {
                Desktop.getDesktop().browse(new URI("http://recartinformatica.com.br"));
            } catch (IOException ex) {
                Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }} catch (URISyntaxException ex) {
                Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);}
    }//GEN-LAST:event_lblLinkMouseClicked

  
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblArte;
    private javax.swing.JLabel lblLink;
    public static final javax.swing.JLabel lblStatus = new javax.swing.JLabel();
    // End of variables declaration//GEN-END:variables
}
