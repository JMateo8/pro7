
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Jorge
 */
public class CuentaCorriente implements Serializable, Comparable<CuentaCorriente> {

    //ATRIBUTOS
    private static int numCuenta = 100;
    private String numeroCuenta;
    private Double saldo;
    private Set<Cliente> clientes = new TreeSet<>();
    private List<Movimiento> movimientos = new ArrayList<>();

    //CONSTRUCTOR
    public CuentaCorriente(Cliente cliente) {
        this.saldo = 0.0;
        this.clientes.add(cliente);
        setNumeroCuenta();
        sumaNumero();
    }

    public CuentaCorriente() {
        this.saldo = 0.0;
    }

    //GETTERS Y SETTERS
    public int getNumCuenta() {
        return numCuenta;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta() {
        Formatter fmt = new Formatter();
        fmt.format("%08d", numCuenta);
        this.numeroCuenta = "c/c " + fmt;
        fmt.close();
    }

    public void setNumeroCuentaB(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
    	if (saldo>0.0) {
    		this.saldo = saldo;
		}	
    }

    public Set<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(Cliente cli) {
        this.clientes.add(cli);
    }

    public List<Movimiento> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(Movimiento mov) {
        this.movimientos.add(mov);
    }

    //OTROS METODOS
    public void ingresar(Double importe, Calendar fecha) {
        if (importe >= 0) {
            saldo += importe;
        }
    }

    public boolean isIngresar(Double importe, Calendar fecha) {
        boolean correcto = true;
        if (importe >= 0) {
            //saldo += importe;
            correcto = true;
        } else {
            System.out.println("Importe negativo");
            correcto = false;
        }
        return correcto;
    }

    public void sacar(Double importe, Calendar fecha) {
        if (importe >= 0 && saldo >= importe) {
            saldo = saldo - importe;
        } else {
            System.out.println("ERROR");
        }
    }

    public boolean isSacar(Double importe, Calendar fecha) {
        boolean correcto = true;
        if (importe >= 0 && saldo >= importe) {
            correcto = true;
        } else if (importe < 0 && saldo >= importe) {
            System.out.println("Importe negativo");
            correcto = false;
        } else if (importe >= 0 && saldo < importe) {
            System.out.println("No se pueden retirar " + importe + "€");
            correcto = false;
        } else {
            System.out.println("ERROR");
            correcto = false;
        }
        return correcto;
    }

    public void sumaNumero() {
        numCuenta++;
    }

//toString
    @Override
    public String toString() {
        return "CuentaCorriente{" + "Numero de cuenta: " + numeroCuenta + " | Saldo = " + String.format("%.2f€", saldo) + " | clientes=" + clientes + " | movimientos=" + movimientos + '}';
    }

    @Override
    public int compareTo(CuentaCorriente o) {
        return this.numeroCuenta.compareTo(o.getNumeroCuenta());
    }

}
