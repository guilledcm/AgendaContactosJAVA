package Vista;

import Modelo.Contacto;
import Modelo.TratarArchivo;

import java.util.Scanner;

public class Menu {
    Scanner scanner = new Scanner(System.in);
    TratarArchivo tratarArchivo = new TratarArchivo();
    boolean salir = false;
    public Menu(){
        mostrarMenu();

    }
    public void mostrarMenu(){
        while (!salir) {
            System.out.println("==========Menú==========");
            System.out.println("1. Leer contactos desde un archivo de texto");
            System.out.println("2. Guardar contactos en un archivo de texto");
            System.out.println("3. Leer contacto desde archivo binario");
            System.out.println("4. Guardar contacto en archivo binario");
            System.out.println("5. Leer contactos desde XML (DOM)");
            System.out.println("6. Escribir contactos en XML (DOM)");
            System.out.println("7. Leer contactos desde XML (SAX)");
            System.out.println("8. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese la ruta del archivo de texto: ");
                    String rutaTxt = scanner.nextLine();
                    tratarArchivo.leerTxt(rutaTxt);
                    break;

                case 2:
                    System.out.print("Ingrese la ruta del archivo de texto para guardar los contactos: ");
                    String rutaGuardarTxt = scanner.nextLine();
                    tratarArchivo.escribirTxt(tratarArchivo.getContactos(), rutaGuardarTxt);
                    break;

                case 3:
                    System.out.print("Ingrese la ruta del archivo binario: ");
                    String rutaBinario = scanner.nextLine();
                    Contacto contactoBinario = tratarArchivo.leerBinario(rutaBinario);
                    if (contactoBinario != null) {
                        System.out.println(contactoBinario);
                    }
                    break;

                case 4:
                    System.out.print("Ingrese la ruta del archivo binario para guardar el contacto: ");
                    String rutaGuardarBinario = scanner.nextLine();
                    Contacto c = new Contacto("Guille","608259265");
                    tratarArchivo.guardarBinario(c,rutaGuardarBinario);
                    break;

                case 5:
                    System.out.print("Ingrese la ruta del archivo XML (DOM): ");
                    String rutaXmlDom = scanner.nextLine();
                    tratarArchivo.leerDOM(rutaXmlDom);
                    break;

                case 6:
                    System.out.print("Ingrese la ruta para guardar el archivo XML (DOM): ");
                    String rutaGuardarXmlDom = scanner.nextLine();
                    tratarArchivo.escribirDOM(rutaGuardarXmlDom);
                    break;

                case 7:
                    System.out.print("Ingrese la ruta del archivo XML (SAX): ");
                    String rutaXmlSax = scanner.nextLine();
                    tratarArchivo.leerSAX(rutaXmlSax);
                    break;

                case 8:
                    salir = true;
                    break;

                default:
                    System.out.println("Opción no válida. Inténtelo de nuevo.");
            }
        }
    }
}

