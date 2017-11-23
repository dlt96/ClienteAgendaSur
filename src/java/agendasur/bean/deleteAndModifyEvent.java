/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agendasur.bean;

import client.AgendaSurService_Service;
import client.Evento;
import client.Tag;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.xml.ws.WebServiceRef;

/**
 *
 * @author dlope
 */
@Named(value = "deleteAndModifyEvent")
@RequestScoped
public class deleteAndModifyEvent {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/AgendaSur-war/agendaSurService.wsdl")
    private AgendaSurService_Service service;
    private String nombre;
    private String descripcion;
    private String fechaInicio;
    private String fechaFin;
    private String direccion;
    private float longitud;
    private float latitud;
    private String selectedTags;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Inject
    private listadoEventos listado;

    @Inject
    private UserBean usuarioSesion;

    /**
     * Creates a new instance of deleteAndModifyEvent
     */
    public deleteAndModifyEvent() {
    }

    public String getSelectedTags() {
        return selectedTags;
    }

    public void setSelectedTags(String selectedTags) {
        this.selectedTags = selectedTags;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaInicio() throws ParseException {
        return (fechaInicio != null) ? formatter.parse(fechaInicio) : new Date();
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = formatter.format(fechaInicio);
    }

    public Date getFechaFin() throws ParseException {
        return (fechaFin != null) ? formatter.parse(fechaFin) : new Date();
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = formatter.format(fechaFin);
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public float getLongitud() {
        return longitud;
    }

    public void setLongitud(float longitud) {
        this.longitud = longitud;
    }

    public float getLatitud() {
        return latitud;
    }

    public void setLatitud(float latitud) {
        this.latitud = latitud;
    }

    public listadoEventos getListado() {
        return listado;
    }

    public void setListado(listadoEventos listado) {
        this.listado = listado;
    }

    public String updateUserTags() {
        String[] tags = usuarioSesion.getListaUserTagsSelected().split(",");
        this.asignarTagsAUsuario(usuarioSesion.getUsuario().getEmail(), Arrays.asList(tags));
        return null;
    }

    public String doEliminar(Evento evento) {
        client.AgendaSurService port = service.getAgendaSurServicePort();
        port.removeEvento(evento);
        usuarioSesion.cargarEventosYTags();
        return null;
    }

    public String doEditar(Evento evento) throws ParseException {
        List<Tag> taglist = findTagsEvento(evento.getId());

        if (taglist.size() > 0) {
            selectedTags = "";
            selectedTags = taglist.get(0).getNombre();
            for (int i = 1; i < taglist.size(); i++) {
                selectedTags = selectedTags + "," + taglist.get(i).getNombre();
            }
        }
        this.usuarioSesion.setEventoAeditar(evento);
        this.nombre = evento.getNombre();
        this.descripcion = evento.getDescripcion();
        this.fechaInicio = (evento.getFechainicio());
        this.fechaFin = (evento.getFechafin());
        this.direccion = evento.getDireccion();
        this.longitud = evento.getLongitud();
        this.latitud = evento.getLatitud();
        return "editarEvento";
    }

    public String doGuardar() {
        String[] tags = selectedTags.split(",");
        //this.getListEvent(tags);
        this.usuarioSesion.getEventoAeditar().setNombre(this.nombre);
        this.usuarioSesion.getEventoAeditar().setDescripcion(this.descripcion);
        this.usuarioSesion.getEventoAeditar().setFechainicio(this.fechaInicio);
        this.usuarioSesion.getEventoAeditar().setFechafin(this.fechaFin);
        this.usuarioSesion.getEventoAeditar().setDireccion(this.direccion);
        this.usuarioSesion.getEventoAeditar().setLongitud(this.longitud);
        this.usuarioSesion.getEventoAeditar().setLatitud(this.latitud);
        client.AgendaSurService port = service.getAgendaSurServicePort();
        port.editEvento(this.usuarioSesion.getEventoAeditar());
        this.asignarTagsAEvento(this.usuarioSesion.getEventoAeditar(), Arrays.asList(tags));
        this.usuarioSesion.cargarEventosYTags();
        return "listEvents";

    }

    /*
    private void getListEvent(String[] tags){
        this.listaTags.clear();
        for (String tag : tags){
            this.listaTags.add(this.findTag(tag));
        }
    }
     */
    public String doValidar() {
        this.usuarioSesion.getEvent().setValidado(true);

        client.AgendaSurService port = service.getAgendaSurServicePort();
        port.editEvento(this.usuarioSesion.getEvent());
        return "listEvents";
    }

    private Tag findTag(java.lang.Object id) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        client.AgendaSurService port = service.getAgendaSurServicePort();
        return port.findTag(id);
    }

    private java.util.List<client.Evento> findAllEvento() {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        client.AgendaSurService port = service.getAgendaSurServicePort();
        return port.findAllEvento();
    }

    public String volver() {
        return "listEvents";
    }

    private void asignarTagsAEvento(client.Evento arg0, java.util.List<java.lang.String> arg1) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        client.AgendaSurService port = service.getAgendaSurServicePort();
        port.asignarTagsAEvento(arg0, arg1);
    }

    private void asignarTagsAUsuario(java.lang.String arg0, java.util.List<java.lang.String> arg1) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        client.AgendaSurService port = service.getAgendaSurServicePort();
        port.asignarTagsAUsuario(arg0, arg1);
    }

    private java.util.List<client.Tag> findTagsEvento(int arg0) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        client.AgendaSurService port = service.getAgendaSurServicePort();
        return port.findTagsEvento(arg0);
    }

}
