/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agendasur.bean;

import client.AgendaSurService_Service;
import client.Comentario;
import client.ComentarioPK;
import client.Evento;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
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
    @Inject
    private UserBean userBean;
    private Boolean validar;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public detailEventBean() {
    }

    @PostConstruct
    public void init() {
        validar = (userBean.getUsuario().getTipousuario()) == 3 && (userBean.getEvent().isValidado());
    }

    public Boolean getValidar() {
        return validar;
    }

    public void setValidar(Boolean validar) {
        this.validar = validar;
    }

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String cargarEvento(Evento event) {
        userBean.setEvent(event);
        return "Event";
    }

    public List<Comentario> getComentarios() {
        List<Comentario> list = findComentariosEvento(userBean.getEvent().getId());
        return list;
    }

    private java.util.List<client.Comentario> findComentariosEvento(int arg0) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        client.AgendaSurService port = service.getAgendaSurServicePort();
        return port.findComentariosEvento(arg0);
    }

    public String enviarComentario() {
        Comentario c = new Comentario();
        c.setEvento(userBean.getEvent());
        c.setComentario(text);
        Date d = new Date();
        c.setFecha(formatter.format(d));
        c.setUsuario(userBean.getUsuario());
        ComentarioPK cPK = new ComentarioPK();
        cPK.setEventoId(userBean.getEvent().getId());
        cPK.setUsuarioEmail(userBean.getEmail());
        c.setComentarioPK(cPK);
        this.createComentario(c);
        return null;
    }

    public String darMeGusta() {
        darMeGusta(userBean.getEvent(), userBean.getUsuario());
        return null;
    }

    public String validarEvento() {
        userBean.getEvent().setValidado(true);
        editEvento(userBean.getEvent());
        this.sendMail("Tu evento "+this.userBean.getEvent().getNombre()+" ha sido publicado.", this.userBean.getEvent().getCreador().getEmail());
        return null;
    }
    
    public String rechazarEvento(){
        removeEvento(userBean.getEvent());
        this.sendMail("Tu evento "+this.userBean.getEvent().getNombre()+" ha sido rechazado.", this.userBean.getEvent().getCreador().getEmail());
        userBean.setEvent(null);
        return "listEvents";
    }

    private void createComentario(client.Comentario entity) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        client.AgendaSurService port = service.getAgendaSurServicePort();
        port.createComentario(entity);
    }

    public String volver() {
        return "listEvents";
    }

    private void editEvento(client.Evento entity) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        client.AgendaSurService port = service.getAgendaSurServicePort();
        port.editEvento(entity);
    }

    private void removeEvento(client.Evento entity) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        client.AgendaSurService port = service.getAgendaSurServicePort();
        port.removeEvento(entity);
    }

    private void darMeGusta(client.Evento arg0, client.Usuario arg1) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        client.AgendaSurService port = service.getAgendaSurServicePort();
        port.darMeGusta(arg0, arg1);
    }

    private void sendMail(java.lang.String msj, java.lang.String email) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        client.AgendaSurService port = service.getAgendaSurServicePort();
        port.sendMail(msj, email);
    }

}
