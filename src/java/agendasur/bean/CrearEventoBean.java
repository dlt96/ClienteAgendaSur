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
 * @author Adrián
 */
@Named(value = "crearEventoBean")
@RequestScoped
public class CrearEventoBean {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/AgendaSur-war/agendaSurService.wsdl")
    private AgendaSurService_Service service;

    @Inject
    private UserBean userBean;

    private String nombre;
    private String descripcion;
    private String direccion;
    private String fechaInicio;
    private String fechaFin;
    private float latitud;
    private float longitud;
    private String selectedTags;

    private List<Tag> listaTags;

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Creates a new instance of CrearEventoBean
     */
    public CrearEventoBean() {
    }

    @PostConstruct
    public void init() {
        listaTags = this.findAllTag();
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<Tag> getListaTags() {
        return listaTags;
    }

    public void setListaTags(List<Tag> listaTags) {
        this.listaTags = listaTags;
    }

    public String getSelectedTags() {
        return selectedTags;
    }

    public void setSelectedTags(String selectedTags) {
        this.selectedTags = selectedTags;
    }

    public float getLatitud() {
        return latitud;
    }

    public void setLatitud(float latitud) {
        this.latitud = latitud;
    }

    public float getLongitud() {
        return longitud;
    }

    public void setLongitud(float longitud) {
        this.longitud = longitud;
    }

    public String doCrear() {
        Evento evento = new Evento();

        evento.setNombre(this.nombre);
        evento.setDescripcion(this.descripcion);
        evento.setDireccion(this.direccion);
        evento.setFechainicio(this.fechaInicio);
        evento.setFechafin(this.fechaFin);
        this.setValidationToEvent(evento);
        evento.setLatitud(this.latitud);
        evento.setLongitud(this.longitud);
        evento.setCreador(userBean.getUsuario());

        this.createEvento(evento);

        List<Evento> eventos = findAllEvento();

        if (selectedTags != null && !"".equals(selectedTags)) {
            String[] tags = selectedTags.split(",");
            
            this.asignarTagsAEvento(eventos.get(eventos.size() - 1), Arrays.asList(tags));

            if (userBean.getUsuario().getTipousuario() != 1) {
                this.sendMail(eventos.get(eventos.size() - 1).getId());
            }
        }

        return "listEvents";
    }

    public String volver() {
        return "listEvents";
    }

    private void setValidationToEvent(Evento evento) {
        Usuario usuario = this.userBean.getUsuario();
        evento.setValidado((usuario.getTipousuario() != 1));
    }

    /*
    private void getListEvent(String[] tags) {
        this.listaTags.clear();
        for (String tag : tags) {
            this.listaTags.add(this.findTag(tag));
        }
    }
    */
    private java.util.List<client.Tag> findAllTag() {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        client.AgendaSurService port = service.getAgendaSurServicePort();
        return port.findAllTag();
    }

    private void createEvento(client.Evento entity) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        client.AgendaSurService port = service.getAgendaSurServicePort();
        port.createEvento(entity);
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

    private void sendMail(int arg0) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        client.AgendaSurService port = service.getAgendaSurServicePort();
        port.sendMail(arg0);
    }

    private void asignarTagsAEvento(client.Evento arg0, java.util.List<java.lang.String> arg1) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        client.AgendaSurService port = service.getAgendaSurServicePort();
        port.asignarTagsAEvento(arg0, arg1);
    }

}
