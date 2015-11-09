/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cr.ac.una.prograIII.appMVC.Controlador;

import cr.ac.una.prograIII.appMVC.Domain.jBlocked;
import cr.ac.una.prograIII.appMVC.Vista.Pantallabloqueada;
import cr.ac.una.prograIII.appMVC.Vista.VistaClienteServer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Gustavo
 */
public class ClienteServerControlador implements ActionListener, DocumentListener {
    VistaClienteServer vistaCliente;
    Pantallabloqueada pantallabloqueadaView;
    private String NombUsuario, Direccion = "localhost";
    private ArrayList<String> Usuarios = new ArrayList();
    private int puerto = 2222;
    private Boolean EnLinea = false;

    private Socket sock;
    private BufferedReader reader;
    private PrintWriter writer;

    public Pantallabloqueada getPantallabloqueadaView() {
        return pantallabloqueadaView;
    }

    public void setPantallabloqueadaView(Pantallabloqueada pantallabloqueadaView) {
        this.pantallabloqueadaView = pantallabloqueadaView;
    }
    

    public ClienteServerControlador(VistaClienteServer vistaCliente, Pantallabloqueada pantallabloqueadaview) {
        this.vistaCliente = vistaCliente;
        this.pantallabloqueadaView=pantallabloqueadaview;
        this.vistaCliente.btConectar.addActionListener(this);
        
        this.vistaCliente.btEnviar.addActionListener(this);
        this.vistaCliente.txtDireccionIP.addActionListener(this);
        this.vistaCliente.txtMensajeEnviar.addActionListener(this);
        this.vistaCliente.txtNombreUsuario.addActionListener(this);
        this.vistaCliente.txtPuerto.addActionListener(this);
        
        this.vistaCliente.txtDireccionIP.setEnabled(false);
        this.vistaCliente.txtPuerto.setEnabled(false);
        this.vistaCliente.txtDireccionIP.setText("localhost");
        this.vistaCliente.txtPuerto.setText("2222");
    }

    public VistaClienteServer getVistaCliente() {
        return vistaCliente;
    }

    public void setVistaCliente(VistaClienteServer vistaCliente) {
        this.vistaCliente = vistaCliente;
    }

    public String getNombUsuario() {
        return NombUsuario;
    }

    public void setNombUsuario(String NombUsuario) {
        this.NombUsuario = NombUsuario;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String Direccion) {
        this.Direccion = Direccion;
    }

    public ArrayList<String> getUsuarios() {
        return Usuarios;
    }

    public void setUsuarios(ArrayList<String> Usuarios) {
        this.Usuarios = Usuarios;
    }

    public int getPuerto() {
        return puerto;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }

    public Boolean getEnLinea() {
        return EnLinea;
    }

    public void setEnLinea(Boolean EnLinea) {
        this.EnLinea = EnLinea;
    }

    public Socket getSock() {
        return sock;
    }

    public void setSock(Socket sock) {
        this.sock = sock;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public void setWriter(PrintWriter writer) {
        this.writer = writer;
    }

    

    @Override
    public void actionPerformed(ActionEvent e) {
       if (e.getSource() == this.vistaCliente.btConectar) {
           this.vistaCliente.btConectar.setEnabled(false);
           
         if (EnLinea == false) {
            NombUsuario = vistaCliente.txtNombreUsuario.getText();
            vistaCliente.txtNombreUsuario.setEditable(false);

            try {
                //Se crea el sokect
                sock = new Socket(Direccion, puerto);
                InputStreamReader streamreader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(streamreader);
                
                //se envia un mensaje al servidor codificando que se 
                //conecto un nuevo cliente indicando al inicio del mensaje
                //"N:" lo que indica Nuevo usuario, despues va el nombre del usuario
                writer = new PrintWriter(sock.getOutputStream());
                writer.println("N:"+NombUsuario + ":te has conectado.:Conectado");
                writer.flush();
                
                
                EnLinea = true;
            } catch (Exception ex) {
                vistaCliente.Chat_Cliente.append("no se a podido conectar pruebe nuevamente \n");
                vistaCliente.txtNombreUsuario.setEditable(true);
            }

            ListenThread();

        } else if (EnLinea == true) {
            vistaCliente.Chat_Cliente.append("Ya esta conectado. \n");
        }

       } if (e.getSource() == this.vistaCliente.btEnviar) {
           String nothing = "";
        if ((vistaCliente.txtMensajeEnviar.getText()).equals(nothing)) {
            vistaCliente.txtMensajeEnviar.setText("");
            vistaCliente.txtMensajeEnviar.requestFocus();
        } else {
            try {
                writer.println(NombUsuario + ":" + vistaCliente.txtMensajeEnviar.getText() + ":" + "Chat");
                writer.flush(); // flushes the buffer
            } catch (Exception ex) {
                vistaCliente.Chat_Cliente.append("No se a enviado el mensaje\n");
            }
            vistaCliente.txtMensajeEnviar.setText("");
            vistaCliente.txtMensajeEnviar.requestFocus();
        }

        vistaCliente.txtMensajeEnviar.setText("");
        vistaCliente.txtMensajeEnviar.requestFocus();

       }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
     public void ListenThread() {
        Thread IncomingReader = new Thread(new IncomingReader());
        IncomingReader.start();
    }

    public void userAdd(String data) {
        Usuarios.add(data);
    }

    public void userRemove(String data) {
        vistaCliente.Chat_Cliente.append(data + " is now offline.\n");
    }

    public void writeUsers() {
        String[] tempList = new String[(Usuarios.size())];
        Usuarios.toArray(tempList);
        for (String token : tempList) {
            //users.append(token + "\n");
        }
    }

    public void sendDisconnect() {
        String bye = (NombUsuario + ": :Desconectado");
        try {
            writer.println(bye);
            writer.flush();
        } catch (Exception e) {
            vistaCliente.Chat_Cliente.append("no se ha podido enviar desconectar.\n");
        }
    }
    
    public void Disconnect() {
        try {
            vistaCliente.Chat_Cliente.append("");
            sock.close();
            reader.close();
            writer.close();
        } catch (Exception ex) {
            vistaCliente.Chat_Cliente.append("fallo al Desconectar. \n");
        }
        EnLinea = false;
        vistaCliente.txtNombreUsuario.setEditable(true);

    }

    public class IncomingReader implements Runnable {

        @Override
        public void run() {
            String[] data;
            String stream, done = "Done", conectado = "Conectado", desconectado = "Desconectado", chat = "Chat";
            

            try {
                while ((stream = reader.readLine()) != null) {
                    //********************************************
                    //Aqui se reciben los mensajes del servidor
                    //aca se debe de codificar lo que se desea hacer
                    //********************************************
                    if(stream.equals("Bloqueado")){
                        sendDisconnect();
                        Disconnect();
//                        pantallabloqueadaView.setVisible(true);
//                        new jBlocked( pantallabloqueadaView ).block();
                    }if(stream.equals("Desbloqueado")){
                        pantallabloqueadaView.setVisible(false);
                    }
                    vistaCliente.Chat_Cliente.append(stream);
                    vistaCliente.btConectar.setEnabled(true);
                }
            } catch (Exception ex) {
            }
        }
    }
    

    
}
