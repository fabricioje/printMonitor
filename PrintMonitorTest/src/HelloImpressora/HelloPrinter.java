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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.Date;
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



public class HelloPrinter {

	/**
	 * @param args
	 */
    
    private static String impressora;
    static FrmPrincipal principal;
    private static int status = 1;    
    
    
    
    @SuppressWarnings("empty-statement")
	public static void main(String[] args) {
            
            Process p;            
            try {
                UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());   
            }catch(Exception ex) {
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
                /*
                PopupMenu popup2 = new PopupMenu("SubMenu de Opções");
                MenuItem mostramsg2 = new MenuItem("Item1");
                MenuItem mostramsg3 = new MenuItem("Item2");
                MenuItem mostramsg4 = new MenuItem("Item3");
                
                popup2.add(mostramsg2);
                popup2.add(mostramsg3);
                popup2.add(mostramsg4);
                popup.add(popup2);
                */
                //popup.add(defaultItem);
                
                //Icone Tray                                
                final TrayIcon trayIcon = new TrayIcon(imagemTray,"Mensagem de Impressão",popup);                
                MouseListener mlOpcoes = new MouseListener(){  
                    @Override
                    public void mouseClicked(MouseEvent e) {     
                    }  
                    @Override
                    public void mousePressed(MouseEvent e) {}  
                    @Override
                    public void mouseReleased(MouseEvent e) {}   
                    @Override
                    public void mouseEntered(MouseEvent e) {}   
                    @Override
                    public void mouseExited(MouseEvent e) {}
                };
                                
                trayIcon.setImageAutoSize(true);
                trayIcon.addMouseListener(mlOpcoes);                
                try {
                    // Adiciona o Ícone no SystemTray
                    tray.add(trayIcon);                    
                    
                } catch (AWTException e) {}   
                
                /*********************************************************************************************************************/
                
		// TODO Auto-generated method stub
		//PrintRequestAttributeSet requestAttributeSet = new HashPrintRequestAttributeSet();		
        	//requestAttributeSet.add(new Copies(1));
		/*PrintService[] services = PrintServiceLookup.lookupPrintServices(DocFlavor.SERVICE_FORMATTED.PAGEABLE, null);
		for (PrintService service1 : services) {
			System.out.println(service1);
		}
                
		PrintService service = services[2];
                */
                
                //CLASSE USADA PARA OBTER PARAMETROS DA FILA DE IMPRESSAO
                PrintService service = PrintServiceLookup.lookupDefaultPrintService();						                                                         
		service.addPrintServiceAttributeListener(new PrintServiceAttributeListener(){
			
			@Override
			public void attributeUpdate(PrintServiceAttributeEvent event) {
				PrintServiceAttributeSet serviceAttributeSet = event.getAttributes();                                                                
                                                                
				StringBuilder s = new StringBuilder();
				s.append("PrintServiceAttributeEvent\n");
				for (Attribute attribute : serviceAttributeSet.toArray()) {                                                                            
					PrintServiceAttribute printServiceAttribute = (PrintServiceAttribute)attribute;					                                                                                
					s.append(printServiceAttribute.getCategory().getName() + "/" + 
							printServiceAttribute.getName() + " = " + printServiceAttribute.toString() + "\n");					                                                                                                                
				}          
                                
			}
							                                         	
		});                                                                                   
                
                //LOOP COM THREAD PARA FICA MONITORANDO ALTERAÇOES NA FILA
		while (true) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}                                                
                        //SAIDAS DE CONTROLE APENAS
			System.out.println("I'm alive and it's " + new Date());
			System.out.println("Job attributes");
			/*for (Attribute attribute : job.getAttributes().toArray()) {
				System.out.println((attribute.getCategory().getName() + "/" + 
						attribute.getName() + " = " + attribute.toString() + "\n"));
			}*/
			System.out.println("Service attributes");                        
			for (Attribute attribute : service.getAttributes().toArray()) {
                            opcoes = FrmPrincipal.config.carregarOpcoes();
                            if(attribute.getName().equals("printer-name")){
                                setImpressora(attribute.toString());
                            }
                            if(attribute.getName().equals("queued-job-count")){
                                if(Integer.parseInt(attribute.toString()) > 0){
                                    /*if(isMaquinaEquals(getImpressora())){*///METODO QUE FOI USADO PARA TENTAR DEFINIR MELHOR QUE IMPRIME MAIS SEM SUCESSO
                                        if(!principal.isVisible()){               
                                            setTextStatus(lblStatus, FrmPrincipal.opcoes[1]);
                                            principal.setVisible(true);
                                            principal.setExtendedState(Frame.NORMAL);
                                            //principal = new FrmPrincipal();
                                            //Abrir na frente
                                            principal.setAlwaysOnTop(true);
                                            //Voltar ao normal
                                            principal.setAlwaysOnTop(false);
                                        }
                                   /* } */                                   
                                }else{                                    
                                        if(principal.isVisible()){                                        
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
				//System.out.println((attribute.getCategory().getName() + "/" +	attribute.getName() + " = " + attribute.toString() + "\n"));                                
			}
		}
	}                                                
        
    public static void setTextStatus(final JLabel label, final String text)
{
    EventQueue.invokeLater(new Runnable()
    {
        @Override
        public void run()
        {
            label.setText(text);
        }
    });
}
        public static int getPosicao(String linha, String ocorrencia) {
		return linha.indexOf(ocorrencia)+1;
	}
        
        //METODO USANDO ARQUIVO Prnjobs.vbs PARA TENTAR OBTER NOME DA MAQUINA OU USUARIO
        public static boolean isMaquinaEquals(String print){
        
        boolean result = false;
        
        ProcessBuilder processBuilder;        
        Process process = null;
        
        if(System.getProperty("os.name").contains("XP")){
            String[] cmd = {"cmd","/c", "Cscript %WINDIR%\\System32\\Prnjobs.vbs -l"};
            processBuilder = new ProcessBuilder(cmd);             
        }else{
            String[] cmd = {"cmd","/c", "Cscript %WINDIR%\\System32\\Printing_Admin_Scripts\\pt-BR\\Prnjobs.vbs -l"};  
            processBuilder = new ProcessBuilder(cmd); 
        }                                   
        //String print = "Cscript.exe %WINDIR%\\System32\\Printing_Admin_Scripts\\pt-BR\\Prnjobs.vbs -l -s SERVIDOR -p RECEPÇÃO";
                
        try {
            process = processBuilder.start();
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String s1 = null;
            while (br.readLine() != null){                                       
                s1 = "|"+br.readLine()+"#";
                //System.out.println(s1);
                //System.out.println("getInputStream:");                                                                	
                //System.out.println(s1);
                String maquina = s1.substring(s1.indexOf("|")+1, s1.indexOf("#"));   
                /*if(maquina.contains("Propriet")){                                                     
                    System.out.println(System.getProperty("os.name"));//SO
                    String PC = null;
                    if(maquina.contains("\\\\")){
                        PC = maquina.substring(maquina.lastIndexOf(" ")+3);
                    }else{
                        PC = maquina.substring(maquina.lastIndexOf(" ")+1);
                    }
                    System.out.println(PC);                    
                    String nome = System.getProperty("user.name");
                                        
                    System.out.println(nome);   
                    if(PC.equalsIgnoreCase(nome)){
                        result = true;
                        System.out.println("YES");
                    }else{
                        result = false;
                        System.out.println("NO");
                    }
                } */
                if(maquina.contains("Nome da m")){                                                     
                    System.out.println(System.getProperty("os.name"));//SO
                    String PC = null;
                    if(maquina.contains("\\\\")){
                        PC = maquina.substring(maquina.lastIndexOf(" ")+3);
                    }else{
                        PC = maquina.substring(maquina.lastIndexOf(" ")+1);
                    }
                    System.out.println(PC);                    
                    String nome = InetAddress.getLocalHost().getHostName();
                                        
                    System.out.println(nome);   
                    if(PC.equalsIgnoreCase(nome)){
                        result = true;
                        System.out.println("YES");
                    }else{
                        result = false;
                        System.out.println("NO");
                    }
                }                                                               
            }
        } catch (IOException ex) {
            Logger.getLogger(HelloPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
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
