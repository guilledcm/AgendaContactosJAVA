package Modelo;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TratarArchivo {
    static ArrayList<Contacto> contactos;
    public TratarArchivo(){
        identifyArchivo();
        contactos=new ArrayList<>();
    }
    public void identifyArchivo(){
        ConfigArchivo configuracion = new ConfigArchivo("config.properties");
        String tipo = configuracion.obtenerTipoFichero();
        String ruta = configuracion.obtenerRuta();
        String parser = configuracion.obtenerParser();
        
        if(tipo.equalsIgnoreCase("txt")){
            leerTxt(ruta);
        } else if (tipo.equalsIgnoreCase("dat")) {
            leerBinario(ruta);
        } else if (tipo.equalsIgnoreCase("xml")) {
            if(parser.equalsIgnoreCase("DOM")){
                leerDOM(ruta);
            } else if (parser.equalsIgnoreCase("SAX")) {
                leerSAX(ruta);
            } else if (parser.equalsIgnoreCase("JAXB")) {
                leerJAXB(ruta);
            }
        }
    }
        public List<Contacto> leerTxt(String ruta) {
            List<Contacto> contactos = new ArrayList<>();
            try {
                BufferedReader br = new BufferedReader(new FileReader(ruta));
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] partes = linea.split(",");
                    if (partes.length == 2) {
                        String nombre = partes[0];
                        String telefono = partes[1];
                        contactos.add(new Contacto(nombre, telefono));
                    }
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return contactos;
        }
    public void escribirTxt(List<Contacto> contactos, String ruta) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(ruta));
            for (Contacto contacto : contactos) {
                bw.write(contacto.getNombre() + "," + contacto.getTelefono());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Contacto leerBinario(String ruta) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ruta))) {
            Contacto contacto = (Contacto) ois.readObject();
            System.out.println("Contacto cargado desde " + ruta);
            return contacto;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void guardarBinario(Contacto contacto, String ruta) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ruta))) {
            oos.writeObject(contacto);
            System.out.println("Contacto guardado en " + ruta);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void leerDOM(String ruta){
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(ruta));
            NodeList contactoNodes = document.getElementsByTagName("Contacto");
            List<Contacto> contactos = new ArrayList<>();
            for (int i = 0; i < contactoNodes.getLength(); i++) {
                Element contactoElement = (Element) contactoNodes.item(i);
                String nombre = contactoElement.getElementsByTagName("nombre").item(0).getTextContent();
                String telefono = contactoElement.getElementsByTagName("telefono").item(0).getTextContent();
                Contacto contacto = new Contacto(nombre, telefono);
                contactos.add(contacto);
            }
            for (Contacto contacto : contactos) {
                System.out.println("Nombre: " + contacto.getNombre());
                System.out.println("Teléfono: " + contacto.getTelefono());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void escribirDOM(String ruta){
        try {
            DocumentBuilderFactory factory  = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Contacto contacto1 = new Contacto("Guille", "608259265");
            Contacto contacto2 = new Contacto("Jose", "666666666");

            Element contactosElement = document.createElement("Contactos");
            document.appendChild(contactosElement);

            Element contactoElement1 = document.createElement("Contacto");
            contactosElement.appendChild(contactoElement1);

            Element nombreElement1 = document.createElement("nombre");
            nombreElement1.appendChild(document.createTextNode(contacto1.getNombre()));
            contactoElement1.appendChild(nombreElement1);

            Element telefonoElement1 = document.createElement("telefono");
            telefonoElement1.appendChild(document.createTextNode(contacto1.getTelefono()));
            contactoElement1.appendChild(telefonoElement1);

            Element contactoElement2 = document.createElement("Contacto");
            contactosElement.appendChild(contactoElement2);

            Element nombreElement2 = document.createElement("nombre");
            nombreElement2.appendChild(document.createTextNode(contacto2.getNombre()));
            contactoElement2.appendChild(nombreElement2);

            Element telefonoElement2 = document.createElement("telefono");
            telefonoElement2.appendChild(document.createTextNode(contacto2.getTelefono()));
            contactoElement2.appendChild(telefonoElement2);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(ruta));
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void leerSAX(String ruta) {
        contactos = new ArrayList<>();
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(ruta));

            Contacto Contacto = null;
            String Element = null;

            while (reader.hasNext()) {
                int event = reader.next();

                if (event == XMLStreamConstants.START_ELEMENT) {
                    Element = reader.getLocalName();
                    if ("contacto".equals(Element)) {
                        Contacto = new Contacto(null, null);
                    }
                } else if (event == XMLStreamConstants.CHARACTERS && Contacto != null) {
                    String value = reader.getText();
                    if ("nombre".equals(Element)) {
                        Contacto.setNombre(value);
                    } else if ("telefono".equals(Element)) {
                        Contacto.setTelefono(value);
                    }
                } else if (event == XMLStreamConstants.END_ELEMENT) {
                    if ("contacto".equals(Element)) {
                        contactos.add(Contacto);
                        Contacto = null;
                    }
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Contacto c:contactos) {
            System.out.println(c.toString());
        }
    }
    public static void leerJAXB(String ruta){
        try {
            File file = new File(ruta);
            JAXBContext jaxbContext = JAXBContext.newInstance(Contacto.class);
            Unmarshaller um = jaxbContext.createUnmarshaller();
            Contacto contacto = (Contacto) um.unmarshal(file);
        } catch (JAXBException e) {
            e.printStackTrace();

        }
    }

    public List<Contacto> getContactos() {
        return contactos;
    }
}


