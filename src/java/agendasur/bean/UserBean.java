/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agendasur.bean;

import client.AgendaSurService_Service;
import client.Evento;
import client.Usuario;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.xml.ws.WebServiceRef;

/**
 *
 * @author Adrian
 */
@Named(value = "UserBean")
@SessionScoped
public class UserBean implements Serializable {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/AgendaSur-war/agendaSurService.wsdl")
    private AgendaSurService_Service service;

    private String email;
    private String password;
    private Usuario usuario;
    private Evento event;
    private Evento eventoAeditar;
    
    public UserBean() {
    }

    public Evento getEventoAeditar() {
        return eventoAeditar;
    }

    public void setEventoAeditar(Evento eventoAeditar) {
        this.eventoAeditar = eventoAeditar;
    }

    public String getPassword() {
        return password;
    }
    public Evento getEvent() {
        return event;
    }

    public void setEvent(Evento event) {
        this.event = event;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String doLogin() {
        if (email != null) {
            usuario = findUsuario(email);
        }

        if (usuario != null && usuario.getPassword().equals(password)) {
            return "listEvents";
        } else {
            return null;
        }
    }
    
    public String doCrear(){
        return "crearEvento";
    }

    private java.util.List<client.Usuario> findAllUsuario() {
        client.AgendaSurService port = service.getAgendaSurServicePort();
        return port.findAllUsuario();
    }

    private Usuario findUsuario(java.lang.Object id) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        client.AgendaSurService port = service.getAgendaSurServicePort();
        return port.findUsuario(id);
    }
    
    
}
