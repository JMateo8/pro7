import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
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
public class GestionCuentas implements Separable {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //Creamos las listas
        Set<Cliente> clientes = new TreeSet<>();
        List<CuentaCorriente> cuentas = new ArrayList<>();

        //Cargamos el fichero LEER
        lecturaFichero(cuentas, clientes);
        Collections.sort(cuentas);

        //Para controlar el numero de movimiento (static) y que no empiece por 1 cada vez que se inicia el programa.
        for (CuentaCorriente cuenta : cuentas) {
            for (int j = 0; j < cuenta.getMovimientos().size(); j++) {
                System.out.println(cuenta.getMovimientos().get(j));
                Movimiento mov = new Movimiento();
            }
        }
        //MENU como constante
        //Añadir nuevas opciones
        final String[] MENU = {".-Salir",
            ".-Crear un cliente",
            ".-Modificar un cliente",
            ".-Crear nueva cuenta",
            ".-Asignar cliente a cuenta",
            ".-Borrar cuenta",
            ".-Ingresar en cuenta",
            ".-Retirar de cuenta",
            ".-Visualizar cuenta",
            ".-Visualizar movimientos de una cuenta",
            ".-Visualizar todas las cuentas",
            ".-Visualizar las cuentas de un cliente",
            ".-Visualizar todos los movimientos de un cliente"};

        int opcion = -1;
        while (opcion != 0) {
            switch (opcion) {
                case 1:
                    crearCliente(clientes);
                    break;
                case 2:
                    modificaCliente(clientes);
                    break;
                case 3:
                    crearCuenta(clientes, cuentas);
                    break;
                case 4:
                    asignaCliente(clientes, cuentas);
                    break;
                case 5:
                    borrarCuenta(cuentas);
                    //borrar2(cuentas);
                    break;
                case 6:
                    ingresar(cuentas);
                    break;
                case 7:
                    sacar(cuentas);
                    break;
                case 8:
                    visualizarCuenta(cuentas);
                    break;
                case 9: //Visualizar los movimientos de una cuenta (FORMATO)
                    verMovimientos(cuentas);
                    break;
                case 10:
                    visualizarTodasCuentas(cuentas);
                    break;
                case 11:
                    cuentasCliente(clientes, cuentas);
                    break;
                case 12:
                    movimientosCliente(clientes, cuentas);
                    break;
            }
            System.out.println("========BANCO ONLINE========");
            System.out.println("Fecha y hora: " + GregorianCalendar.getInstance().getTime().toLocaleString());
            opcion = Leer.leeEntero(MENU);
        }
        //Guardar cambios escribiendo en el fichero los objetos nuevos (ESCRIBIR) con los nuevos agregados
        escribirTodo(cuentas, clientes);
        System.out.println("-------------------------------------");
        System.out.println("Muchas gracias por su visita. Hasta la proxima");
    }

    private static void lecturaFichero(List<CuentaCorriente> cc, Set<Cliente> c) {
        String lectura;
        FileReader f = null;
        FileInputStream f2 = null;
        ObjectInputStream flujo = null;
        try {
            f2 = new FileInputStream("cuentas.dat");
            flujo = new ObjectInputStream(f2);
            Cliente cli;
            CuentaCorriente cue;
            boolean haydatos = true;
            while (haydatos) {
                try {
                    Object objeto = flujo.readObject(); //Leemos el objeto
                    if (objeto instanceof Cliente) { //Si es de la clase Cliente
                        cli = (Cliente) objeto; //Casting de Object a Cliente
                        c.add(cli); //Lo agregamos al TreeSet de clientes
                    } else if (objeto instanceof CuentaCorriente) { //Si es de la clase CuentaCorriente
                        cue = (CuentaCorriente) objeto; //Casting de Object a CuentaCorriente
                        cc.add(cue); //Lo agregamos al ArrayList de cuentas
                    }
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    System.out.println("No se cargan datos");
                } catch (EOFException e) {
                    haydatos = false;
                }
            }
            flujo.close();
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            try {
                f = new FileReader("cuentas.txt");
                BufferedReader buffer = new BufferedReader(f);
                Cliente cli = new Cliente();
                CuentaCorriente cue = new CuentaCorriente();
                while ((lectura = buffer.readLine()) != null) {
                    String[] datos = lectura.split(cli.getSeparador());
                    cli = new Cliente(datos[1], datos[2], datos[3], datos[4], datos[5], datos[6]);
                    if (!cli.isErroneoP()) {
                        c.add(cli);
                    }
                    cue = new CuentaCorriente(cli);
                    cue.setNumeroCuentaB(datos[0]);
                    cc.add(cue);
                }
                buffer.close();
                escribirTodo(cc, c);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block

            } catch (IOException e) {
                // TODO Auto-generated catch block
                System.out.println("Problemas con la lectura");
            } finally {
                if (f != null) {
                    try {
                        f.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Problemas con la lectura del fichero de objetos");
        } finally {
            if (f2 != null) {
                try {
                    f2.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    public static void escribirTodo(List<CuentaCorriente> cc, Set<Cliente> c) {
        //List<Cliente> clientes = new ArrayList<>(c);
        ObjectOutputStream fich = null;
        try {
            fich = new ObjectOutputStream(new FileOutputStream("cuentas.dat"));
            System.out.println("Guardamos la informacion de estas cuentas corrientes y los clientes en el fichero cuentas.dat");
            for (Cliente cliente : c) {
                fich.writeObject(cliente);
                //System.out.println("Agregado cliente con DNI: " + cliente.getDni());
            }
            for (CuentaCorriente c1 : cc) {
                fich.writeObject(c1);
                //System.out.println("Agregada cuenta con numero de cuenta: " + c1.getNumeroCuenta());
            }
            fich.close();
        } catch (IOException ex) {
            System.out.println("Error E/S: " + ex.getMessage());
        } finally {
            try {
                if (fich != null) {
                    fich.close();
                }
            } catch (IOException ex) {
                System.out.println("Error al cerrar fichero: " + ex.getMessage());
            }
        }
    }

    public static void listarClientes(Set<Cliente> c) {
        System.out.println("-----------LISTADO CLIENTES------------");
        List<Cliente> clientes = new ArrayList<>(c);
        if (!c.isEmpty()) {
            for (int i = 0; i < clientes.size(); i++) {
                System.out.println(i + " | " + clientes.get(i));
            }
        } else {
            System.out.println("No hay clientes");
        }
    }

    public static int escogeClientes(Set<Cliente> c) {
        int x = -1;
        if (!c.isEmpty()) {
            while (x < 0 || x >= c.size()) {
                listarClientes(c);
                x = Leer.leeEntero("Escoge un cliente");
            }
        } else {
            System.out.println("No hay clientes");
        }
        return x;
    }

    public static void listarCuentas(List<CuentaCorriente> cc) {
        if (!cc.isEmpty()) {
            System.out.println("-----------LISTADO CUENTAS-------------");
            for (int i = 0; i < cc.size(); i++) {
                System.out.println(i + " | " + cc.get(i).toString());
            }
        } else {
            System.out.println("No hay cuentas corrientes");
        }

    }

    public static int escogeCuenta(List<CuentaCorriente> cc) {
        int x = -1;
        if (!cc.isEmpty()) {
            while (x < 0 || x >= cc.size()) {
                listarCuentas(cc);
                x = Leer.leeEntero("Escoge una cuenta");
            }
        } else {
            System.out.println("No hay cuentas corrientes");
        }
        return x;
    }

    public static void verMovimientos(List<CuentaCorriente> cc) {
        //Seleccionamos la cuenta
        int x = escogeCuenta(cc);
        CuentaCorriente cuenta = cc.get(x);
        //Creamos la cabecera
        informeMovimientos(cuenta);
    }

    public static void informeMovimientos(CuentaCorriente cuenta) {
        System.out.println("numeroCuenta = " + cuenta.getNumeroCuenta()); //Mostramos el numero de la cuenta
        System.out.println("Fecha               Ingreso   Reintrego   Saldo");
        System.out.println("-----               -------   ---------   -----");
        double total = 0.0; //Inicializamos la variable del saldo total
        for (int i = 0; i < cuenta.getMovimientos().size(); i++) { //Recorremos todos los movimientos de la cuenta
            String fecha = cuenta.getMovimientos().get(i).getFecha().getTime().toLocaleString(); //Fecha del movimiento
            double importe = cuenta.getMovimientos().get(i).getImporte(); //Cantidad del importe
            if (cuenta.getMovimientos().get(i).getTipoMovimiento()) {//Cuando se trate de un ingreso
                total = total + importe; //Sumamos el importe al total
                String fila = String.format("%-10s %8.2f  %8s  %8.2f\n", fecha, importe, "-------", total);//Mostramos el importe en la 2ª columna
                System.out.println(fila);
            } else { //Cuando se trate de una retirada
                total = total - importe; //Restamos el importe al total
                String fila = String.format("%-10s %8s  %8.2f  %8.2f\n", fecha, "-------", importe, total); //Mostramos el importe en la 3ª columna
                System.out.println(fila);
            }
        }
        System.out.println("Saldo                                   " + String.format("%8.2f�", total)); //Mostramos el saldo final

    }

    public static String devuelveMovimientos(CuentaCorriente cuenta) {
        String output = "";
        output = output + "numeroCuenta = " + cuenta.getNumeroCuenta() + "\n"; //Mostramos el numero de la cuenta
        output = output + "\n===============Movimientos===============\n";
        output = output + "Fecha               Ingreso   Reintrego   Saldo\n";
        output = output + "-----               -------   ---------   -----\n";
        double total = 0.0; //Inicializamos la variable del saldo total
        for (int i = 0; i < cuenta.getMovimientos().size(); i++) { //Recorremos todos los movimientos de la cuenta
            String fecha = cuenta.getMovimientos().get(i).getFecha().getTime().toLocaleString(); //Fecha del movimiento
            double importe = cuenta.getMovimientos().get(i).getImporte(); //Cantidad del importe
            if (cuenta.getMovimientos().get(i).getTipoMovimiento()) {//Cuando se trate de un ingreso
                total = total + importe; //Sumamos el importe al total
                output = output + String.format("%-10s %8.2f  %8s  %8.2f\n", fecha, importe, "-------", total) + "\n";//Mostramos el importe en la 2ª columna
            } else { //Cuando se trate de una retirada
                total = total - importe; //Restamos el importe al total
                output = output + String.format("%-10s %8s  %8.2f  %8.2f\n", fecha, "-------", importe, total) + "\n"; //Mostramos el importe en la 3ª columna
            }
        }
        output = output + "Saldo-----------------------------------" + String.format("%8.2f�", total) + "\n"; //Mostramos el saldo final
        output = output + "\n===============Clientes===============\n";
        for (Cliente cl : cuenta.getClientes()) {
            output = output + "----->" + cl.toString() + "\n";
        }
        return output;
    }

    public static void asignaCliente(Set<Cliente> c, List<CuentaCorriente> cc) {
        List<Cliente> clientes = new ArrayList<>(c);
        int cli, cu;
        if (!cc.isEmpty()) {
            //Escogemos un cliente y una cuenta
            cli = escogeClientes(c);
            cu = escogeCuenta(cc);
            CuentaCorriente cuenta = cc.get(cu);
            Cliente cliente = clientes.get(cli);
            if (cuenta.getClientes().contains(cliente)) { //Si el cliente ya forma parte de la cuenta lanzamos el mensaje y no lo agregamos
                System.out.println("El cliente ya pertence a la cuenta");
            } else { //En caso contrario, lo agregamos a la lista de clientes de la cuenta
                cuenta.setClientes(cliente);
                System.out.println("Cliente agregado");
                System.out.println("Clientes de la cuenta: " + cuenta.getClientes());
            }
        } else {
            System.out.println("No hay ninguna cuenta");
        }
    }

    public static void crearCliente(Set<Cliente> c) {
        List<Cliente> clientes = new ArrayList<>(c); //Para comprobar el DNI
        String nombre, apellido1, apellido2, dni, telefono, email;
        //Pedimos los atributos del cliente por pantalla
        System.out.println("===CREAR CLIENTE===");
        System.out.println("Rellene la informacion | (*) = campo obligatorio");
        nombre = Leer.leeCadena("Nombre: (*)");
        apellido1 = Leer.leeCadena("Primer apellido: (*)");
        apellido2 = Leer.leeCadena("Segundo apellido (opcional):");
        dni = Leer.leeCadena("DNI: (*)");
        boolean correcto = compruebaDNI(clientes, dni);
        if (correcto) {
            telefono = Leer.leeCadena("Telefono: (*)");
            email = Leer.leeCadena("Correo electronico: (*)");
            //Creamos el cliente con esos atributos
            Cliente cli = new Cliente(nombre, apellido1, apellido2, dni, telefono, email);
            if (!cli.isErroneoP()) {
                //Agregamos al cliente al TreeSet
                c.add(cli);
                System.out.println("Cliente creado");
            } else {
                System.out.println("Datos erroneos");
            }
        } else {
            System.out.println("DNI repetido. Cliente no agregado.");
        }
        //Si el DNI se repite, el programa no agegaria el cliente al TreeSet automaticamente
        //listarClientes(c);
    }

    public static void crearCuenta(Set<Cliente> c, List<CuentaCorriente> cc) {
        List<Cliente> clientes = new ArrayList<>(c);
        Set<String> numeros = new TreeSet<>();
        for (int i = 0; i < cc.size(); i++) {
            numeros.add(cc.get(i).getNumeroCuenta());
        }
        //System.out.println(numeros);
        //Escogemos un cliente para crear la cuenta
        int x = escogeClientes(c);
        Cliente cli = clientes.get(x);
        //Creamos la cuenta
        CuentaCorriente cuenta = new CuentaCorriente();

        cuenta.setNumeroCuentaB("c/c " + String.format("%08d", cuenta.getNumCuenta()));
        //System.out.println(cuenta.getNumeroCuenta());
        while (numeros.contains(cuenta.getNumeroCuenta())) {
            //System.out.println("Numero de cuenta repetido");
            //System.out.println(cuenta.getNumeroCuenta());
            cuenta.sumaNumero();
            cuenta.setNumeroCuentaB("c/c " + String.format("%08d", cuenta.getNumCuenta()));
        }
        cuenta.setClientes(cli);
        //Agregamos la cuenta a la lista
        cc.add(cuenta);

        System.out.println("Cuenta creada correctamente");
        System.out.println(cuenta.toString());
        //listarCuentas(cc);
    }

    public static boolean compruebaDNI(List<Cliente> clientes, String dni) {
        boolean correcto = true;
        for (Cliente cliente : clientes) {
            if (cliente.getDni().equalsIgnoreCase(dni)) {
                //Comparamos el dni introducido con el del resto de clientes
                correcto = false;
                System.out.println("Ya hay un cliente con ese DNI");
                break;
            }
        }
        return correcto;
    }

    public static void modificaCliente(Set<Cliente> c) {
        List<Cliente> clientes = new ArrayList<>(c);
        //Escogemos el cliente a modificar
        int x = escogeClientes(c);
        Cliente cli = clientes.get(x);
        int opc = -1;
        final String[] OPCIONES = {".-Salir", ".-Modificar nombre", ".-Modificar primer apellido",
            ".-Modificar segundo apellido", ".-Modificar DNI", ".-Modificar telefono", ".-Modificar correo electronico"};
        //Modificamos los datos usando los setters
        while (opc != 0) {
            switch (opc) {
                case 1:
                    String nombre = Leer.leeCadena("Nombre:");
                    cli.setNombre(nombre);
                    break;
                case 2:
                    String apellido1 = Leer.leeCadena("Primer apellido:");
                    cli.setApellido1(apellido1);
                    break;
                case 3:
                    String apellido2 = Leer.leeCadena("Segundo apellido:");
                    cli.setApellido2(apellido2);
                    break;
                case 4:
                    String dni = Leer.leeCadena("DNI:");
                    boolean correcto = compruebaDNI(clientes, dni); //Comprobamos que el DNI no se repita
                    if (correcto) {
                        cli.setDni(dni);
                    } else {
                        System.out.println("No se ha podido modificar");
                    }
                    break;
                case 5:
                    String telefono = Leer.leeCadena("Telefono:");
                    cli.setTelefono(telefono);
                    break;
                case 6:
                    String email = Leer.leeCadena("Correo electronico:");
                    cli.setEmail(email);
                    break;
            }
            opc = Leer.leeEntero(OPCIONES);
        }
        System.out.println("--------FIN--------");
    }

    public static void borrarCuenta(List<CuentaCorriente> cc) {
        if (!cc.isEmpty()) {
            int x = escogeCuenta(cc);
            CuentaCorriente cuenta = cc.get(x);
            if (cuenta.getSaldo() == 0) {
                cc.remove(x); //cc.remove(cuenta)
                //escribirObjetoBorrar(cuenta);
                escribirDatosBorrar(cuenta);
            } else {
                System.out.println("Saldo distinto de 0");
                final String OPCIONES = "0.- Mantener la cuenta\n1.-Retirar saldo y borrar";
                int opc = Leer.leeEntero(0, OPCIONES, 1);
                if (opc == 1) {
                    //Ponemos el saldo a 0 como un movimiento de tipo flase
                    Calendar fecha = GregorianCalendar.getInstance();
                    cuenta.setMovimientos(new Movimiento(fecha, cuenta.getSaldo(), false));
                    //cuenta.setSaldo(0.0);
                    cc.remove(x); //cc.remove(cuenta)
                    //escribirObjetoBorrar(cuenta);
                    escribirDatosBorrar(cuenta);
                } else {
                    System.out.println("No se ha borrado la cuenta");
                }
            }
        } else {
            System.out.println("No hay cuentas corrientes");
        }
    }

    public static void escribirObjetoBorrar(CuentaCorriente cuenta) { //Genera el fichero con la informacion de la cuenta
        ObjectOutputStream fich = null;
        System.out.println("Cuenta borrada");
        String n = cuenta.getNumeroCuenta();
        String cadena = n + ".txt"; //El nombre del archivo sera el numero de cuenta
        //System.out.println(cadena);
        try {
            fich = new ObjectOutputStream(new FileOutputStream(cadena));
            System.out.println("Guardamos la cuenta corriente en el fichero " + cadena);
            fich.writeObject(cuenta);
            fich.close();
        } catch (IOException ex) {
            System.out.println("Error E/S: " + ex.getMessage());
        } finally {
            try {
                if (fich != null) {
                    fich.close();
                }
            } catch (IOException ex) {
                System.out.println("Error al cerrar fichero: " + ex.getMessage());
            }
        }
    }

    public static void escribirDatosBorrar(CuentaCorriente cuenta) { //Genera el fichero con la informacion de la cuenta
        System.out.println("Cuenta borrada");
        String n = cuenta.getNumeroCuenta();
        int pos = (n.length()-8);
        int num = Integer.parseInt(n.substring(pos));
        System.out.println(num);
        String cadena = num + ".txt"; //El nombre del archivo sera el num de cuenta
        FileWriter fichEsc = null;
        try {
            fichEsc = new FileWriter(cadena);
            String cadAEscribir = devuelveMovimientos(cuenta);
            fichEsc.write(cadAEscribir);
            // fichEsc.close();
        } catch (IOException ex) {
            System.out.println("Error de escritura: " + ex.getMessage());
        } finally {
            try {
                if (fichEsc != null) {
                    fichEsc.close();
                }
            } catch (IOException ex) {
                System.out.println("Error al cerrar el fichero: " + ex.getMessage());
            }
        }
    }

    public static void borrar2(List<CuentaCorriente> cc) { //Metodo para que solo permita borrar cuentas con saldo 0
        List<CuentaCorriente> nueva = new ArrayList<>();
        for (int i = 0; i < cc.size(); i++) {
            if (cc.get(i).getSaldo() == 0.0) {
                nueva.add(cc.get(i)); //Agregamos las cuentas con saldo 0 a la nueva lista
            }
        }
        //Escogemos una de las cuentas con saldo 0 para borrarla
        int x = escogeCuenta(nueva);
        nueva.remove(x);
        CuentaCorriente cuenta = nueva.get(x);
        escribirObjetoBorrar(cuenta);
    }

    public static void cuentasCliente(Set<Cliente> c, List<CuentaCorriente> cc) {
        List<Cliente> clientes = new ArrayList<>(c);
        //Escogemos un cliente
        int x = escogeClientes(c);
        Cliente cli = clientes.get(x);
        for (int i = 0; i < cc.size(); i++) { //Recorremos todas las cuentas
            if (cc.get(i).getClientes().contains(cli)) { //Si el cliente es uno de los propietarios de la cuenta
                System.out.println(i + " | " + cc.get(i).toString()); //Mostramos la cuenta por pantalla
            }
        }
    }

    public static void movimientosCliente(Set<Cliente> c, List<CuentaCorriente> cc) {
        List<Cliente> clientes = new ArrayList<>(c);
        //Escogemos un cliente
        int x = escogeClientes(c);
        Cliente cli = clientes.get(x);
        for (int i = 0; i < cc.size(); i++) { //Recorremos todas las cuentas
            if (cc.get(i).getClientes().contains(cli)) { //Para las cuentas en las que el cliente sea propietario
                int size = cc.get(i).getMovimientos().size();
                for (int j = 0; j < size; j++) { //Visualizamos los movimentos de cada cuenta
                    System.out.println("---> " + cc.get(i).getMovimientos().get(j).toString());
                }
            }
        }
    }

    public static void visualizarCuenta(List<CuentaCorriente> cc) {
        if (!cc.isEmpty()) {
            //Escogemos una cuenta
            int x = escogeCuenta(cc);
            CuentaCorriente cuenta = cc.get(x);
            verCuenta(cuenta);
        } else {
            System.out.println("No hay ninguna cuenta");
        }
        System.out.println("============== FIN ==============");
    }

    public static void visualizarTodasCuentas(List<CuentaCorriente> cc) {
        if (!cc.isEmpty()) { //cc.size()!=0
            for (int i = 0; i < cc.size(); i++) { //Recorremos todas las cuentas
                verCuenta(cc.get(i));
            }
        } else {
            System.out.println("No hay ninguna cuenta");
        }
        System.out.println("============== FIN ==============");
    }

    public static void verCuenta(CuentaCorriente cuenta) {
        //Mostramos su infromacion
        System.out.println("=========== " + cuenta.getNumeroCuenta() + " ===========");
        System.out.println("Saldo: " + String.format("%.2f€", cuenta.getSaldo())); //2 decimales
        System.out.println("========== Movimientos ==========");
        if (!cuenta.getMovimientos().isEmpty()) {
            for (int i = 0; i < cuenta.getMovimientos().size(); i++) {
                System.out.println("---> Movimiento " + (i + 1) + ": " + cuenta.getMovimientos().get(i));
            }
        } else {
            System.out.println("Todavia no se ha realizado ningun movimiento");
        }

        System.out.println("=========== Clientes ============");
        for (Cliente c : cuenta.getClientes()) { //Bucle for each para recorrer el Set de clientes
            System.out.println("---> " + c.toString());
        }
        System.out.println("=================================");
    }

    public static void ingresar(List<CuentaCorriente> cc) {
        //Escogemos una cuenta
        int x = escogeCuenta(cc);
        CuentaCorriente cuenta = cc.get(x);
        int longitud = cuenta.getMovimientos().size();
        int posicion = longitud - 1;
        Calendar hoy = GregorianCalendar.getInstance();
        if (longitud > 0) {
            Calendar ultima = cuenta.getMovimientos().get(posicion).getFecha();
            //Creamos el movimiento
            double importe = Leer.leeDouble("Cantidad a ingresar");
            //Pedimos la fecha por teclado como String y la transformamos a Calendar
            String xx = Leer.leeCadena("Fecha (DD/MM/YYYY)");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); //Formato
            Calendar fecha = Calendar.getInstance(); //La inicializamos
            try {
                Date date = sdf.parse(xx);
                fecha.setTime(date);
            } catch (ParseException ex) {
                Logger.getLogger(GestionCuentas.class.getName()).log(Level.SEVERE, null, ex);
            }
            //Otra opcion seria pedir dia, mes y anio separados y construir la fecha
            //Calendar fecha = new GregorianCalendar(anio, mes, dia); 
            //Si queremos que la fecha de ingreso sea la actual con .getInstance()
            //Calendar fecha = Calendar.getInstance();
            //--------------------------------------------------------------------------------
            if (ultima.before(fecha) && hoy.after(fecha)) { //Si es un movimiento posterior al ultimo y anterior a hoy
                Movimiento mov = new Movimiento(fecha, importe, true); //Como es ingresar, tipoMovimiento = TRUE 
                if (cuenta.isIngresar(importe, fecha)) { //Comprobamos que se pueda realizar (importe>=0)
                    cuenta.ingresar(importe, fecha); //Realizamos el movimiento
                    cuenta.setMovimientos(mov); //Agregamos el movimiento a la lista de movimientos de la cuenta
                    System.out.println("Ingreso completado");

                    System.out.println("Saldo actual: " + String.format("%.2f�", cuenta.getSaldo()));
                }
            } else if (ultima.after(fecha)) {
                System.out.println("Error.\nFecha del movimiento anterior a la ultima realizada.");
            } else if (hoy.before(fecha)) {
                System.out.println("Error.\nFecha del movimiento posterior a HOY");
            } else {
                System.out.println("ERROR. Fecha incorrecta");
            }
        } else {
            //Creamos el movimiento
            double importe = Leer.leeDouble("Cantidad a ingresar");
            //Pedimos la fecha por teclado como String y la transformamos a Calendar
            String xx = Leer.leeCadena("Fecha (DD/MM/YYYY)");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); //Formato
            Calendar fecha = Calendar.getInstance(); //La inicializamos
            try {
                Date date = sdf.parse(xx);
                fecha.setTime(date);
            } catch (ParseException ex) {
                Logger.getLogger(GestionCuentas.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (hoy.after(fecha)) {
                Movimiento mov = new Movimiento(fecha, importe, true); //Como es ingresar, tipoMovimiento = TRUE 
                if (cuenta.isIngresar(importe, fecha)) { //Comprobamos que se pueda realizar (importe>=0)
                    cuenta.ingresar(importe, fecha); //Realizamos el movimiento
                    cuenta.setMovimientos(mov); //Agregamos el movimiento a la lista de movimientos de la cuenta
                    System.out.println("Ingreso completado");
                    System.out.println("Saldo actual: " + String.format("%.2f�", cuenta.getSaldo()));
                }
            } else {
                System.out.println("Error.\nFecha del movimiento posterior a HOY");
            }
        }
    }

    public static void sacar(List<CuentaCorriente> cc) {
        //Escogemos una cuenta
        int x = escogeCuenta(cc);
        CuentaCorriente cuenta = cc.get(x);
        if (cuenta.getSaldo() == 0) {
            System.out.println("Cuenta con saldo 0. No se puede retirar dinero.");
        } else {
            int longitud = cuenta.getMovimientos().size();
            int posicion = longitud - 1;
            Calendar hoy = GregorianCalendar.getInstance();
            if (longitud > 0) {
                Calendar ultima = cuenta.getMovimientos().get(posicion).getFecha();
                //Creamos el movimiento
                double importe = Leer.leeDouble("Cantidad a retirar");
                //Pedimos la fecha por teclado como String y la transformamos a Calendar
                String xx = Leer.leeCadena("Fecha (DD/MM/YYYY)");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); //Formato
                Calendar fecha = Calendar.getInstance(); //La inicializamos
                try {
                    Date date = sdf.parse(xx);
                    fecha.setTime(date);
                } catch (ParseException ex) {
                    Logger.getLogger(GestionCuentas.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (ultima.before(fecha) && hoy.after(fecha)) { //Si es un movimiento posterior al ultimo
                    Movimiento mov = new Movimiento(fecha, importe, false); //Como es sacar, tipoMovimiento = FALSE
                    if (cuenta.isSacar(importe, fecha)) { //Comprobamos que se pueda realizar (importe>=0 y no se queda el saldo negativo)
                        cuenta.sacar(importe, fecha); //Realizamos el movimiento
                        cuenta.setMovimientos(mov); //Agregamos el movimiento a la lista de movimientos de la cuenta
                        System.out.println("Retirada completada");
                        System.out.println("Saldo actual: " + String.format("%.2f�", cuenta.getSaldo()));
                    }
                } else if (ultima.after(fecha)) {
                    System.out.println("Error.\nFecha del movimiento anterior a la ultima realizada.");
                } else if (hoy.before(fecha)) {
                    System.out.println("Error.\nFecha del movimiento posterior a HOY");
                } else {
                    System.out.println("ERROR. Fecha incorrecta");
                }
            } else {
                //Creamos el movimiento
                double importe = Leer.leeDouble("Cantidad a ingresar");
                //Pedimos la fecha por teclado como String y la transformamos a Calendar
                String xx = Leer.leeCadena("Fecha (DD/MM/YYYY)");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); //Formato
                Calendar fecha = Calendar.getInstance(); //La inicializamos
                try {
                    Date date = sdf.parse(xx);
                    fecha.setTime(date);
                } catch (ParseException ex) {
                    Logger.getLogger(GestionCuentas.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (hoy.after(fecha)) {
                    Movimiento mov = new Movimiento(fecha, importe, false); //Como es sacar, tipoMovimiento = FALSE
                    if (cuenta.isSacar(importe, fecha)) { //Comprobamos que se pueda realizar (importe>=0 y no se queda el saldo negativo)
                        cuenta.sacar(importe, fecha); //Realizamos el movimiento
                        cuenta.setMovimientos(mov); //Agregamos el movimiento a la lista de movimientos de la cuenta
                        System.out.println("Retirada completada");
                        System.out.println("Saldo actual: " + String.format("%.2f�", cuenta.getSaldo()));
                    }
                } else {
                    System.out.println("Error.\nFecha del movimiento anterior a la ultima realizada.");
                }
            }
        }
    }
}
