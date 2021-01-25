import java.util.Scanner;
import java.util.regex.PatternSyntaxException;

public class Leer {

	public static String leeCadena(String texto) {
		Scanner teclado = new Scanner(System.in);
		System.out.println(texto);
		String valor = teclado.nextLine();
		return valor;
	}

	public static String leeCadena(String regex, String texto) {
		Scanner teclado = new Scanner(System.in);
		String valor = "";
		while (true) {
			try {
				do {
					System.out.println(texto);
					valor = teclado.nextLine();
				} while (!valor.matches(regex));
				break;
			} catch (PatternSyntaxException e) {
				// TODO Auto-generated catch block
				System.out.println("No hay expresi�n regular correcta");
				valor = null;
				break;
			} 
		}
		return valor;

	}

	public static int leeEntero(String texto) {
		Scanner teclado = new Scanner(System.in);

		int valor = 0;

		while (true) {
			System.out.println(texto);
			try {
				String leido = teclado.nextLine();
				valor = Integer.parseInt(leido);
				break;
			} catch (NumberFormatException e) {
				System.out.println("Error de datos al teclear");
			}
		}
		// int valor = teclado.nextInt();
		return valor;
	}

	public static Integer leeEntero(Integer val1, String texto, Integer val2) {

		int valor = 0;
		if (val1 == null && val2 == null) {
			valor = leeEntero(texto);
		} else if (val1 == null && val2 != null) {
			valor = val2 + 1;
			while (valor > val2) {
				valor = leeEntero(texto);
			}
		} else if (val1 != null && val2 == null) {
			valor = val1 - 1;
			while (valor < val1) {
				valor = leeEntero(texto);
			}
		} else {
			if (val2 > val1) {
				valor = val1 - 1;
				while (valor < val1 || valor > val2) {
					valor = leeEntero(texto);
				}
			} else {
				return null;
			}
		}
		return valor;
	}

	public static int leeEntero(String[] texto) {
		Scanner teclado = new Scanner(System.in);

		int valor = -1;

		while (valor < 0 || valor >= texto.length) {
			for (int i = 0; i < texto.length; i++) {
				System.out.println(i + " " + texto[i]);
			}
			try {
				String leido = teclado.nextLine();
				valor = Integer.parseInt(leido);

			} catch (NumberFormatException e) {
				System.out.println("Error de datos al teclear");
			}
		}
		// int valor = teclado.nextInt();
		return valor;
	}

	public static float leeFloat(String texto) {
		Scanner teclado = new Scanner(System.in);
		float valor;
		while (true) {
			System.out.println(texto);

			try {
				String leido = teclado.nextLine();
				valor = Float.parseFloat(leido);
				break;
			} catch (NumberFormatException e) {
				System.out.println("Error de datos al teclear");
			}
		}
		return valor;
	}

	public static float leeReal(String texto) {
		Scanner teclado = new Scanner(System.in);
		float valor;
		while (true) {
			System.out.println(texto);

			try {
				String leido = teclado.nextLine();
				valor = Float.parseFloat(leido);
				break;
			} catch (NumberFormatException e) {
				System.out.println("Error de datos al teclear");
			}
		}
		return valor;
	}

	public static double leeDouble(String texto) {
		Scanner teclado = new Scanner(System.in);
		double valor;
		while (true) {
			System.out.println(texto);

			try {
				String leido = teclado.nextLine();
				valor = Double.parseDouble(leido);
				break;
			} catch (NumberFormatException e) {
				System.out.println("Error de datos al teclear");
			}

		}
		return valor;
	}
}