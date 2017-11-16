/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agendasur.bean;

import client.AgendaSurService_Service;
import client.Evento;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.xml.ws.WebServiceRef;

/**
 *
 * @author Adrián
 */
@Named(value = "listadoEventos")
@RequestScoped
public class listadoEventos {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/AgendaSur-war/agendaSurService.wsdl")
    private AgendaSurService_Service service;

    private List<Evento> listaEventos;
    
    /**
     * Creates a new instance of listadoEventos
     */
    public listadoEventos() {
    }
    
    @PostConstruct
    public void init(){
        listaEventos = findAllEvento();
    }
    
    public void setListaEventos(List<Evento> listaEventos) {
        this.listaEventos = listaEventos;
    }

    public List<Evento> getListaEventos() {
        return listaEventos;
    }
    
    private java.util.List<client.Evento> findAllEvento() {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        client.AgendaSurService port = service.getAgendaSurServicePort();
        return port.findAllEvento();
    }
    
}
