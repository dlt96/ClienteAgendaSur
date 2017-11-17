/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agendasur.bean;

import client.Evento;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author Adrian
 */
@Named(value = "detailEventBean")
@RequestScoped
public class detailEventBean {

    /**
     * Creates a new instance of detailEventBean
     */
    public detailEventBean() {
    }
    
    private Evento event;

    public Evento getEvent() {
        return event;
    }

    public void setEvent(Evento event) {
        this.event = event;
    }
    
    public String cargarEvento(Evento event){
        setEvent(event);
        return "Event";
    }
    
}
