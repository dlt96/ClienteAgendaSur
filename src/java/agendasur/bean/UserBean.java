/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agendasur.bean;

import client.AgendaSurService_Service;
import client.Evento;
import client.Tag;
import client.Usuario;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
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
    private double latitude;
    private double longitude;

    private List<Evento> listaEventos;
    private List<Tag> listaTags;
    private String listaUserTagsSelected;

    public UserBean() {
    }

    public String getListaUserTagsSelected() {
        return listaUserTagsSelected;
    }

    public void setListaUserTagsSelected(String listaUserTagsSelected) {
        this.listaUserTagsSelected = listaUserTagsSelected;
    }

    public void cargarEventosYTags() {
        listaEventos = this.findEventosNoCaducadosYValidados();
    }

    public void cargarTags() {
        listaTags = this.findAllTag();
    }

    public void cargarTagsUsuario() {
        List<Tag> list = findTagsUsuario(this.usuario.getEmail());
        if(list.size()>0){
            listaUserTagsSelected = "";
            listaUserTagsSelected = list.get(0).getNombre();
            for(int i = 1; i<list.size();i++){
                listaUserTagsSelected =listaUserTagsSelected + "," + list.get(i).getNombre();
            }
        }
    }

    public List<Tag> getListaTags() {
        return listaTags;
    }

    public void setListaTags(List<Tag> listaTags) {
        this.listaTags = listaTags;
    }

    public List<Evento> getListaEventos() {
        return listaEventos;
    }

    public void setListaEventos(List<Evento> listaEventos) {
        this.listaEventos = listaEventos;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        System.out.println(latitude);
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        System.out.println(longitude);
        this.longitude = longitude;
    }

    public String doLogin() {
        if (email != null) {
            usuario = findUsuario(email);
        }

        if (usuario != null && usuario.getPassword().equals(password)) {
            this.cargarEventosYTags();
            this.cargarTags();
            this.cargarTagsUsuario();
            return "listEvents";
        } else {
            return null;
        }
    }

    public String doLogOut() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "LoginJSF";
    }

    public String doCrear() {
        return "crearEvento";
    }

    public String cargarEvento(Evento evento) {
        this.event = evento;
        return "Event";
    }

    public boolean isJournalist() {
        return this.usuario.getTipousuario() == 3;
    }

    public boolean isValidar() {
        return isJournalist() && !event.isValidado();
    }

    public boolean isMeGusta() {
        return !existeMeGusta(event, usuario);
    }

    private Usuario findUsuario(java.lang.Object id) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        client.AgendaSurService port = service.getAgendaSurServicePort();
        return port.findUsuario(id);
    }

    private boolean existeMeGusta(client.Evento arg0, client.Usuario arg1) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        client.AgendaSurService port = service.getAgendaSurServicePort();
        return port.existeMeGusta(arg0, arg1);
    }

    private java.util.List<client.Evento> findEventosNoCaducadosYValidados() {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        client.AgendaSurService port = service.getAgendaSurServicePort();
        return port.findEventosNoCaducadosYValidados();
    }

    private java.util.List<client.Tag> findAllTag() {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        client.AgendaSurService port = service.getAgendaSurServicePort();
        return port.findAllTag();
    }

    private java.util.List<client.Tag> findTagsUsuario(java.lang.String arg0) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        client.AgendaSurService port = service.getAgendaSurServicePort();
        return port.findTagsUsuario(arg0);
    }

}
