/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cr.ac.una.prograIII.appMVC.Main;

import cr.ac.una.prograIII.appMVC.Controlador.ClienteServerControlador;
import cr.ac.una.prograIII.appMVC.Vista.Pantallabloqueada;
import cr.ac.una.prograIII.appMVC.Vista.VistaClienteServer;

/**
 *
 * @author Gustavo
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       VistaClienteServer vistaCliente =new VistaClienteServer();
        Pantallabloqueada pantallaBloqueadaView =new Pantallabloqueada();
       ClienteServerControlador CSControlador =new ClienteServerControlador(vistaCliente,pantallaBloqueadaView);
       CSControlador.getVistaCliente().setVisible(true);
    }
    
}
