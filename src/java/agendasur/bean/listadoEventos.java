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
import javax.inject.Inject;
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
    @Inject
    private UserBean userBean;

    private List<Evento> listaEventos;
    private List<Tag> listaTags;
    private String tagSelected;
    
    private double latitude;
    private double longitude;
    
    class Point {
        double latitude;
        double longitude;
    }
    
    /**
     * Creates a new instance of listadoEventos
     */
    public listadoEventos() {
    }
    
    @PostConstruct
    public void init(){
        listaEventos = this.findEventosNoCaducadosYValidados();
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
        if(!tagSelected.equals("--")){
            client.AgendaSurService port = service.getAgendaSurServicePort();
            Tag t = port.findTag(tagSelected);
            List<Evento> listaEventosFiltrada = this.findEventsByTag(t);
            if (listaEventosFiltrada.isEmpty()){
                this.listaEventos.clear();
            } else {
                this.listaEventos = listaEventosFiltrada;
            }
        }else{
            init();
        }
    }
    
    public String showTags(){
        return null;
    }
    
    public void noValidados(){
        client.AgendaSurService port = service.getAgendaSurServicePort();
        this.listaEventos = port.findEventosNoValidados();
    }
    
    public java.util.List<client.Evento> findEventsByTag(Tag tag){
        client.AgendaSurService port = service.getAgendaSurServicePort();
        return port.findEventosByTag(tag);
    }
    
    public String doBorrar(Evento evento){
            removeEvento(evento);
            init();
        return null;
    }
    
    public String orderByLocation(){
        this.listaEventos = this.findEventosOrdenadosPorDistancia(userBean.getLongitude(), userBean.getLatitude());
        return null;
    }
    
    private java.util.List<client.Tag> findAllTags() {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        client.AgendaSurService port = service.getAgendaSurServicePort();
        return port.findAllTag();
    }

    private void removeEvento(client.Evento entity) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        client.AgendaSurService port = service.getAgendaSurServicePort();
        port.removeEvento(entity);
    }

    private java.util.List<client.Evento> findEventosNoCaducadosYValidados() {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        client.AgendaSurService port = service.getAgendaSurServicePort();
        return port.findEventosNoCaducadosYValidados();
    }

    private java.util.List<client.Evento> findEventosNoValidados() {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        client.AgendaSurService port = service.getAgendaSurServicePort();
        return port.findEventosNoValidados();
    }

    private java.util.List<client.Evento> findEventosOrdenadosPorDistancia(double arg0, double arg1) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        client.AgendaSurService port = service.getAgendaSurServicePort();
        return port.findEventosOrdenadosPorDistancia(arg0, arg1);
    }
    
}
