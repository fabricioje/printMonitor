/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HelloImpressora;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author JoãoPedro
 */
public class Propriedades {
       
            
    public File file = new File(System.getenv("ProgramFiles")+"\\Mensagem de Impressão\\properties.properties");
    //File file = new File("properties.properties");
    
    //Grava as opções de configuração
    public void preencherOpcoes(String[] opcoes){    
        Properties props = new Properties();       
            try{
                props.setProperty("caminho_fundo", opcoes[0]);
                props.setProperty("msg_inicial", opcoes[1]);
                props.setProperty("msg_impressao", opcoes[2]);
            }catch(Exception er){
                er.printStackTrace();
            }                 
        try{
            FileOutputStream fos = new FileOutputStream(file);
            props.store(fos, null);
        
            fos.flush();
            fos.close();
        }catch(IOException er){
            
        }
    }

    //Retorna as opções de configuração
    public String[] carregarOpcoes(){
        String[] opcoes = new String[3];        
        Properties props = new Properties();        
        try{
            FileInputStream selecionado = new FileInputStream(file);                
            props.load(selecionado);
        }catch(IOException er){            
        }
            try{
                opcoes[0] = props.getProperty("caminho_fundo");
                opcoes[1] = props.getProperty("msg_inicial");
                opcoes[2] = props.getProperty("msg_impressao");
            }catch(Exception er){
                er.printStackTrace();
            }
        return opcoes;
    }

    public String opcoesReq(String opcoes){
        String retorno = "";        
        Properties props = new Properties();        
        try{
            FileInputStream selecionado = new FileInputStream(file);                
            props.load(selecionado);
        }catch(IOException er){            
        }
            try{
                retorno = props.getProperty(opcoes);                
            }catch(Exception er){
                er.printStackTrace();
            }
        return retorno;
        
    }
}
