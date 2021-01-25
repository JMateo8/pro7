
import java.io.Serializable;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Jorge
 */
public class Movimiento implements Serializable {

    //ATRIBUTOS
    private static int numSiguiente = 1;
    private Integer numMovimiento;
    private Calendar fecha;
    private Double importe;
    private Boolean tipoMovimiento;

    //CONSTRUCTOR
    public Movimiento(Calendar fecha, Double importe, Boolean tipoMovimiento) {
        setFecha(fecha);
        setImporte(importe);
        setTipoMovimiento(tipoMovimiento);
        setNumMovimiento();
        numSiguiente++;
        if (this.isErroneo()) {
            try {
                //Salta cuando algun atributo sea nulo
                throw new ObjetoErroneo(" Cliente ");
            } catch (ObjetoErroneo ex) {
                Logger.getLogger(Movimiento.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public Movimiento() {
        numSiguiente++;
    }

    //GETTERS Y SETTERS
    public Integer getNumMovimiento() {
        return numMovimiento;
    }

    public void setNumMovimiento() {
        this.numMovimiento = numSiguiente;
    }

    public Calendar getFecha() {
        return fecha;
    }

    public void setFecha(Calendar fecha) {
        this.fecha = fecha;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        if (importe >= 0) {
            this.importe = importe;
        }
    }

    public Boolean getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(Boolean tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }
    
    public boolean isErroneo() {
		if (importe == null || tipoMovimiento == null || fecha == null) {
			return true;
		}
		return false;
	}
    
    //toString
    @Override
    public String toString() {
        return "Movimiento{" + "Numero de movimiento: " + numMovimiento + " | Fecha: " + fecha.getTime().toLocaleString() + " | importe = " + String.format("%.2f€", importe) + " | tipoMovimiento: " + tipoMovimiento + '}';
    }
    
}
