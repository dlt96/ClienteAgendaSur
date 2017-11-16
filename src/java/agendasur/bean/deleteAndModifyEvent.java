/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agendasur.bean;

import client.AgendaSurService_Service;
import client.Evento;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

import javax.xml.ws.WebServiceRef;
 import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;



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
    
    

    /**
     * Creates a new instance of deleteAndModifyEvent
     */
    public deleteAndModifyEvent() {
    }
    
    public String doEliminar(Evento evento){
        client.AgendaSurService port = service.getAgendaSurServicePort();
        port.removeEvento(evento);
        return null;
    }
    
    public String doEditar(Evento evento){
        this.nombre=evento.getNombre();
        this.descripcion=evento.getDescripcion();
        this.fechaInicio=evento.getFechainicio().toGregorianCalendar().getTime();
        this.fechaFin=evento.getFechafin().toGregorianCalendar().getTime();
        this.direccion = evento.getDireccion();
        this.longitud = evento.getLongitud();
        this.latitud = evento.getLatitud();
        return null;
    }
    
    public String doGuardar(Evento evento){
        evento.setNombre(nombre);
        evento.setDescripcion(descripcion);
        evento.setFechainicio(toXMLGregorianCalendar(this.fechaInicio));
        evento.setFechafin(toXMLGregorianCalendar(fechaFin));
        evento.setDireccion(this.direccion);
        evento.setLongitud(this.longitud);
        evento.setLatitud(this.latitud);
        client.AgendaSurService port = service.getAgendaSurServicePort();
        port.editEvento(evento);
        
        return null;
        
    }
    
    public static XMLGregorianCalendar toXMLGregorianCalendar(Date date){
        GregorianCalendar gCalendar = new GregorianCalendar();
        gCalendar.setTime(date);
        XMLGregorianCalendar xmlCalendar = null;
        try {
            xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
        } catch (DatatypeConfigurationException ex) {
          ex.printStackTrace();
        }
        return xmlCalendar;
    }


    
    
}
    
    
    

