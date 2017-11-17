/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agendasur.bean;

import client.AgendaSurService_Service;
import client.Evento;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private Date fechaInicio;
    private Date fechaFin;
    private String direccion;
    private float longitud;
    private float latitud;
    SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd hh:mm:ss");
    
    @Inject
    private listadoEventos listado;
    
    

    /**
     * Creates a new instance of deleteAndModifyEvent
     */
    public deleteAndModifyEvent() {
    }
    
    public String doEliminar(Evento evento){
        client.AgendaSurService port = service.getAgendaSurServicePort();
        port.removeEvento(evento);
        listado.init();
        return null;
    }
    
    public String doEditar(Evento evento) throws ParseException{
        this.nombre=evento.getNombre();
        this.descripcion=evento.getDescripcion();
        this.fechaInicio= format.parse(evento.getFechainicio());
        this.fechaFin=format.parse(evento.getFechafin());
        this.direccion = evento.getDireccion();
        this.longitud = evento.getLongitud();
        this.latitud = evento.getLatitud();
        return null;
    }
    
    public String doGuardar(Evento evento){
        evento.setNombre(nombre);
        evento.setDescripcion(descripcion);
        evento.setFechainicio(fechaInicio.toString());
        evento.setFechafin(fechaFin.toString());
        evento.setDireccion(this.direccion);
        evento.setLongitud(this.longitud);
        evento.setLatitud(this.latitud);
        client.AgendaSurService port = service.getAgendaSurServicePort();
        port.editEvento(evento);
        
        return null;
        
    }
    
    


}
