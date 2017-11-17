/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agendasur.bean;

import client.AgendaSurService_Service;
import client.Comentario;
import client.Evento;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.xml.ws.WebServiceRef;

/**
 *
 * @author Adrian
 */
@Named(value = "detailEventBean")
@RequestScoped
public class detailEventBean {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/AgendaSur-war/agendaSurService.wsdl")
    private AgendaSurService_Service service;

    /**
     * Creates a new instance of detailEventBean
     */
    public detailEventBean() {
    }
    
    private Evento event;

    public Evento getEvent() {
        return event;
    }

    public void setEvent(Evento event) {
        this.event = event;
    }
    
    public String cargarEvento(Evento event){
        setEvent(event);
        return "Event";
    }
    
    public List<Comentario> getComentarios(){
        return findCometariosEvento(event);
    }

    private java.util.List<client.Comentario> findCometariosEvento(client.Evento arg0) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        client.AgendaSurService port = service.getAgendaSurServicePort();
        return port.findCometariosEvento(arg0);
    }
    
}
