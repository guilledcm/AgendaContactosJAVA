package Modelo;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
public class ConfigArchivo {
    private Properties propiedades;
    public ConfigArchivo(String rutaArchivo) {
        propiedades = new Properties();
        try (FileInputStream fis = new FileInputStream(rutaArchivo)) {
            propiedades.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String obtenerTipoFichero() {
        return propiedades.getProperty("fichero.tipo");
    }
    public String obtenerRuta() {
        return propiedades.getProperty("fichero.ruta");
    }
    public String obtenerParser() {
        return propiedades.getProperty("fichero.parser");
    }
}
