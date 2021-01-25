
import java.io.Serializable;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Jorge
 */
public class Cliente implements Comparable<Cliente>, Serializable, Separable {

    //ATRIBUTOS
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String dni;
    private String telefono;
    private String email;

    //CONSTRUCTOR
    public Cliente(String nombre, String apellido1, String apellido2, String dni, String telefono, String email) {
        setNombre(nombre);
        setApellido1(apellido1);
        setApellido2(apellido2);
        setDni(dni);
        setTelefono(telefono);
        setEmail(email);
        if (this.isErroneoP()) {
            try {
                //Salta cuando algun atributo (menos el apellido2) sea nulo
                throw new ObjetoErroneo(" Cliente ");
            } catch (ObjetoErroneo ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public Cliente(){
        
    }

    //GETTERS Y SETTERS CON RESTRICCIONES
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre.length() > 0) {
            this.nombre = nombre;
        }
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        if (apellido1.length() > 0) {
            this.apellido1 = apellido1;
        }
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getDni() {
        return dni;
    }

    public boolean setDni(String dni) {
        final String LETRAS = "TRWAGMYFPDXBNJZSQVHLCKE";// lista de las letras de verificacion
        String dni2;
        char letra;
        int numero, posicion;
        boolean correcto = false;
        if (dni != null) {
            if (dni.length() <= 9) {
                dni = ("000000000".substring(0, 9 - dni.length()) + dni).toUpperCase();// se complementa con 0 por
                // delante
                dni2 = dni;// para trabajar con el
                correcto = true;
                for (int i = 0; i < dni2.length(); i++) {
                    if (i == 0) {// Se busca que el primer caracter sea uno de estos XYZ0123456789
                        if ("XYZ0123456789".indexOf(dni2.substring(0, 1)) == -1) {// tratamiento del primer caracter
                            correcto = false;
                        } else {
                            letra = dni2.charAt(0);// Car�cter de la primera posicion
                            switch (letra) {// cambia la primera posicion por su valor numerico si es X, Y o Z solo para
                                // NIE
                                case 'X':
                                    dni2 = "0" + dni2.substring(1);// cambia el primer caracter por 0
                                    break;
                                case 'Y':
                                    dni2 = "1" + dni2.substring(1);// cambia el primer caracter por 1
                                    break;
                                case 'Z':
                                    dni2 = "2" + dni2.substring(1);// cambia el primer caracter por 2
                                    break;
                            }
                        }
                    } else if (i == 8) {// busca una letra en la ultima posicion
                        if (LETRAS.indexOf(dni2.substring(8, 9)) == -1) {// tratamiento del ultimo caracter
                            correcto = false;
                        }
                    } else {// Se busca que los siguientes caracteres del dni sea un numero
                        if ("0123456789".indexOf(dni2.substring(i, i + 1)) == -1) {
                            correcto = false;// si no es numerico ERROR
                        }
                    }
                }
                if (correcto) {// valida la letra si los caracteres son correctos
                    numero = Integer.valueOf(dni2.substring(0, 8));// La parte numerica se convierte a int para operar
                    posicion = numero % 23;// Se obtiene el resto entre 23
                    if (dni2.substring(8).equals(LETRAS.substring(posicion, posicion + 1))) {// Se compara la letra con
                        // la posicion en LETRAS dada por el resto de 23
                        this.dni = dni;// actualiza el atributo con el parametro
                    } else {
                        correcto = false;
                    }
                }
            }
        }
        return correcto;
    }

    public String getTelefono() {
        return telefono;
    }

    public boolean setTelefono(String telefono) {
        final String VALIDA = "0123456789 ";
        boolean correcto = true;
        if (telefono.length() == 9) {
            for (int i = 0; i < telefono.length(); i++) {
                if (VALIDA.indexOf(telefono.substring(i, i + 1)) == -1) {
                    correcto = false;
                }
            }
        } else {
            correcto = false;
        }

        if (correcto) {
            this.telefono = telefono;
        }
        return correcto;
    }

    public String getEmail() {
        return email;
    }

    public boolean setEmail(String email) {
        boolean correcto = false;
        // Patron para validar el email con expresiones regulares
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        // Aplica patron
        Matcher mather = pattern.matcher(email);
        // Verifica patron
        if (mather.find()) {
            this.email = email;
            correcto = true;
        }
        return correcto;
    }

    //METODO EQUALS
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Cliente other = (Cliente) obj;
        if (!Objects.equals(this.dni, other.dni)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    //toString
    @Override
    public String toString() {
        return "Cliente{" + "Nombre: " + nombre + ", 1º apellido: " + apellido1 + ", 2º apellido: " + apellido2 + " | DNI: " + dni + " | Telefono: " + telefono + " | Correo electronico: " + email + '}';
    }

    public boolean isErroneoP() { //Se ajusta para controlar la validez de los atributos del objeto
        boolean erroneo = false;
        if (nombre == null || apellido1 == null || dni == null || telefono == null || email == null) { //Solo el apellidp2 puede ser null
            erroneo = true;
        }
        return erroneo;
    }

    public int compareTo(Cliente cli2) {
        return dni.compareToIgnoreCase(cli2.dni);
    }

}
