/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HelloImpressora;

import static HelloImpressora.FrmPrincipal.imagemTray;
import static HelloImpressora.FrmPrincipal.lblStatus;
import static HelloImpressora.FrmPrincipal.opcoes;
import java.awt.AWTException;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.Attribute;
import javax.print.attribute.PrintServiceAttribute;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.event.PrintServiceAttributeEvent;
import javax.print.event.PrintServiceAttributeListener;
import javax.swing.JLabel;
import javax.swing.UIManager;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapBpfProgram;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.protocol.network.Ip4;

public class HelloPrinter {

    /**
     * @param args
     */
    private static String impressora;
    static FrmPrincipal principal;
    private static int status = 1;

    @SuppressWarnings("empty-statement")
    public static void main(String[] args) {

        //Variável original
        //Process p
        Process pcs;
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }

        //FORM de Mensagem
        principal = new FrmPrincipal();

        //Objeto instancia em segundo plano
        SystemTray tray = SystemTray.getSystemTray();
        // adiciona o mouseListener no TrayIcon                                                                            
        // Criamos um ActionListener para a ação de encerramento do programa.
        ActionListener exitListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Imprime uma mensagem de despedida na tela            
                System.exit(0);
            }

        };
        // Criamos um ActionListener para a exibir uma mensagem na tela ao clicarmos
        //em um item do menu.
        ActionListener mostramsglistener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FrmSobre().setVisible(true);
            }
        };

        //Criando um objeto PopupMenu
        PopupMenu popup = new PopupMenu("Mensagem de Impressão");

        //criando itens do menu
        MenuItem mostramsg = new MenuItem("Sobre");
        //MenuItem defaultItem = new MenuItem("Sair");
        //na linha a seguir associamos os objetos aos eventos
        mostramsg.addActionListener(mostramsglistener);
        //defaultItem.addActionListener(exitListener);
        //Adicionando itens ao PopupMenu
        popup.add(mostramsg);
        //adiconando um separador
        //popup.addSeparator();
        //Criando objetos do tipo Checkbox                
        //Criando um submenu

        //Icone Tray                                
        final TrayIcon trayIcon = new TrayIcon(imagemTray, "Mensagem de Impressão", popup);
        MouseListener mlOpcoes = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        };

        trayIcon.setImageAutoSize(true);
        trayIcon.addMouseListener(mlOpcoes);
        try {
            // Adiciona o Ícone no SystemTray
            tray.add(trayIcon);

        } catch (AWTException e) {
        }

        //LOOP COM THREAD PARA FICA MONITORANDO ALTERAÇOES NA FILA
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }

        List<PcapIf> alldevs = new ArrayList<PcapIf>(); // Will be filled with NICs
        StringBuilder errbuf = new StringBuilder(); // For any error msgs
        int r = Pcap.findAllDevs(alldevs, errbuf);

        if (r != Pcap.OK || alldevs.isEmpty()) {
            System.err.printf("Can't read list of devices, error is %s",
                    errbuf.toString());
            return;
        }

        System.out.println("Network devices found:");
        int i = 0;

        for (PcapIf device : alldevs) {
            String description = (device.getDescription() != null) ? device
                    .getDescription() : "No description available";
            System.out.printf("#%d: %s [%s]\n", i++, device.getName(),
                    description);
        }

        PcapIf device = alldevs.get(0); // Get first device in list
        System.out.printf("\nChoosing '%s' on your behalf:\n",
                (device.getDescription() != null) ? device.getDescription()
                : device.getName());

        int snaplen = 64 * 1024; // Capture all packets, no trucation
        int flags = Pcap.MODE_PROMISCUOUS; // capture all packets
        int timeout = 10 * 1000; // 10 seconds in millis
        Pcap pcap = Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);
        if (pcap == null) {
            System.err.printf("Error while opening device for capture: "
                    + errbuf.toString());
            return;
        }

        PcapPacketHandler<String> jpacketHandler = new PcapPacketHandler<String>() {
            public void nextPacket(PcapPacket packet, String user) {
                byte[] data = packet.getByteArray(0, packet.size()); // the package data
                byte[] sIP = new byte[4];
                byte[] dIP = new byte[4];
                Ip4 ip = new Ip4();
                if (packet.hasHeader(ip) == false) {
                    return; // Not IP packet
                }
                sIP = ip.source();
                dIP = ip.destination();

                /* Use jNetPcap format utilities */
                String sourceIP = org.jnetpcap.packet.format.FormatUtils.ip(sIP);
                String destinationIP = org.jnetpcap.packet.format.FormatUtils.ip(dIP);

                showTela(destinationIP);

                /*
                if (!destinationIP.isEmpty()) {
                    System.out.println("PEGOU IP");

                    int cont = 0;
                    boolean aberto = false;

                    if (!principal.isVisible()) {

                        setTextStatus(lblStatus, FrmPrincipal.opcoes[1]);
                        principal.setVisible(true);
                        principal.setExtendedState(Frame.NORMAL);
                        //principal = new FrmPrincipal();
                        //Abrir na frente
                        principal.setAlwaysOnTop(true);
                        //Voltar ao normal
                        principal.setAlwaysOnTop(false);
                    }

                    if (principal.isVisible()) {

                        setTextStatus(lblStatus, FrmPrincipal.opcoes[2]);

                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(HelloPrinter.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        principal.dispose();

                    }

                } else {
                    System.out.println("Não");
                }
                 */
                System.out.println("srcIP=" + sourceIP
                        + " dstIP=" + destinationIP
                        + " caplen=" + packet.getCaptureHeader().caplen());
            }
        };
        
        PcapBpfProgram filter = new PcapBpfProgram();
        String expression = "tcp port 9100 and host 192.168.1.173";
        int optimize = 0; // 0 = false
        int netmask = 0xFFFFFF00; // 255.255.255.0
        if (pcap.compile(filter, expression, optimize, netmask) != Pcap.OK) {
            System.err.println(pcap.getErr());
            return;
        }
        if (pcap.setFilter(filter) != Pcap.OK) {
            System.err.println(pcap.getErr());
            return;
        }
        

        // capture first 10 packages
        //pcap.loop(Pcap.LOOP_INFINITE, jpacketHandler, "jNetPcap");
        pcap.loop(10, jpacketHandler, "jNetPcap");
        pcap.close();

    }

    public static void showTela(String destinationIP) {
        new Thread() {

            @Override
            public void run() {

                if (!destinationIP.isEmpty()) {
                    System.out.println("PEGOU IP");

                    if (!principal.isVisible()) {

                        setTextStatus(lblStatus, FrmPrincipal.opcoes[1]);
                        principal.setVisible(true);
                        principal.setExtendedState(Frame.NORMAL);
                        //principal = new FrmPrincipal();
                        //Abrir na frente
                        principal.setAlwaysOnTop(true);
                        //Voltar ao normal
                        principal.setAlwaysOnTop(false);
                    }

                    if (principal.isVisible()) {

                        setTextStatus(lblStatus, FrmPrincipal.opcoes[2]);

                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(HelloPrinter.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        principal.dispose();
                    }
                }
            }
        }.start();

    }

    public static void setTextStatus(final JLabel label, final String text) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                label.setText(text);
            }
        });
    }

    public static int getPosicao(String linha, String ocorrencia) {
        return linha.indexOf(ocorrencia) + 1;
    }

    /**
     * @return the impressora
     */
    public static String getImpressora() {
        return impressora;
    }

    /**
     * @param aImpressora the impressora to set
     */
    public static void setImpressora(String aImpressora) {
        impressora = aImpressora;
    }

    /**
     * @return the status
     */
    public static int getStatus() {
        return status;
    }

    /**
     * @param aStatus the status to set
     */
    public static void setStatus(int aStatus) {
        status = aStatus;
    }

}
