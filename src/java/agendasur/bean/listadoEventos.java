/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agendasur.bean;

import client.AgendaSurService_Service;
import client.Evento;
import client.Tag;
import java.util.ArrayList;
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
        for(Evento ev : findAllEvento()){
            Point p = new Point();
            p.latitude = (double) ev.getLatitud();
            System.err.println(p.latitude);
            p.longitude = (double) ev.getLongitud();
            System.err.println(p.longitude);
            double theta = p.longitude - userBean.getLongitude();
            double dist = Math.sin(deg2rad(p.latitude)) * Math.sin(deg2rad(userBean.getLatitude()))
                    + Math.cos(deg2rad(p.latitude)) * Math.cos(deg2rad(userBean.getLongitude()))
                    * Math.cos(deg2rad(theta));
            dist = Math.acos(dist);
            dist = rad2deg(dist);
            System.out.println(ev.getNombre() + " " + dist);
        }
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
    
    private Double distanceFromMe(Point p) {
        double theta = p.longitude - userBean.getLongitude();
        double dist = Math.sin(deg2rad(p.latitude)) * Math.sin(deg2rad(userBean.getLatitude()))
                + Math.cos(deg2rad(p.latitude)) * Math.cos(deg2rad(userBean.getLongitude()))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        return dist;
    }

    private double deg2rad(double deg) { return (deg * Math.PI / 180.0); }
    private double rad2deg(double rad) { return (rad * 180.0 / Math.PI); }
    
}
