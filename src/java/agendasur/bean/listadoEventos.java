/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agendasur.bean;

import client.AgendaSurService_Service;
import client.Evento;
import client.Tag;
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

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/AgendaSur-war/AgendaSurService.wsdl")
    private AgendaSurService_Service service;

    private List<Evento> listaEventos;
    private List<Tag> listaTags;
    private String tagSelected;
    /**
     * Creates a new instance of listadoEventos
     */
    public listadoEventos() {
    }
    
    @PostConstruct
    public void init(){
        listaEventos = findAllEvento();
        listaTags = findAllTags();
    }
    
    public void setListaEventos(List<Evento> listaEventos) {
        this.listaEventos = listaEventos;
    }

    public List<Evento> getListaEventos() {
        return listaEventos;
    }
   
    public List<Tag> getListaTags() {
        return listaTags;
    }

    public void setListaTags(List<Tag> listaTags) {
        this.listaTags = listaTags;
    }
   
    public String getTagSelected() {
        return tagSelected;
    }

    public void setTagSelected(String tagSelected) {
        this.tagSelected = tagSelected;
        System.out.println(tagSelected);
        if(!tagSelected.equals("--")){
            client.AgendaSurService port = service.getAgendaSurServicePort();
            Tag t = port.findTag(tagSelected);
            this.listaEventos =  findEventsByTag(t);
            System.out.println(this.listaEventos);
        }else{
            init();
        }
    }
    
    public java.util.List<client.Evento> findEventsByTag(Tag tag){
        client.AgendaSurService port = service.getAgendaSurServicePort();
        return port.findTagsEvento(tag);
    }
    
    public String doBorrar(Evento evento){
            removeEvento(evento);
            init();
        return null;
    }
    
    private java.util.List<client.Tag> findAllTags() {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        client.AgendaSurService port = service.getAgendaSurServicePort();
        return port.findAllTag();
    }

    private java.util.List<client.Evento> findAllEvento() {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        client.AgendaSurService port = service.getAgendaSurServicePort();
        return port.findAllEvento();
    }

    private void removeEvento(client.Evento entity) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        client.AgendaSurService port = service.getAgendaSurServicePort();
        port.removeEvento(entity);
    }
    
}