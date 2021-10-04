// Parsing DOM y visualización de documento DOM generado
package dom_parser;

import java.io.File;
import java.io.PrintStream;
import java.io.FileNotFoundException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;

public class DOM_Parser {

    private static final String INDENT_NIVEL = "  ";  // Para indentaci�n

    public static void muestraNodo(Node nodo, int nivel, PrintStream ps) {
        if (nodo.getNodeType() == Node.TEXT_NODE) { // Ignora textos vac�os
            String text = nodo.getNodeValue();
            if (text.trim().length() == 0) {
                return;
            }
        }
        for (int i = 0; i < nivel; i++) {    // Indentaci�n
            ps.print(INDENT_NIVEL);
        }
        switch (nodo.getNodeType()) {  // Escribe información de nodo seg�nn tipo
            case Node.DOCUMENT_NODE:  // Documento
                Document doc = (Document) nodo;
                ps.println("Documento DOM, versi�n: " + doc.getXmlVersion()
                        + ", codificaci�n: " + doc.getXmlEncoding());
                break;
            case Node.ELEMENT_NODE:    // Elemento
                ps.print("<" + nodo.getNodeName());
                NamedNodeMap listaAtr = nodo.getAttributes();  // Lista atributos
                for (int i = 0; i < listaAtr.getLength(); i++) {
                    Node atr = listaAtr.item(i);
                    ps.print(" @" + atr.getNodeName() + "[" + atr.getNodeValue() + "]");
                }
                ps.println(">");
                break;
            case Node.TEXT_NODE:    // Texto
                ps.println(nodo.getNodeName() + "[" + nodo.getNodeValue() + "]");
                break;
            default:    // Otro tipo de nodo
                ps.println("(nodo de tipo: " + nodo.getNodeType() + ")");
        }
        NodeList nodosHijos = nodo.getChildNodes();    // Muestra nodos hijos
        for (int i = 0; i < nodosHijos.getLength(); i++) {
            muestraNodo(nodosHijos.item(i), nivel + 1, ps);
        }
    }

    public static void main(String[] args) {
        String nomFich = "clientes.xml";

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setIgnoringComments(true);
        dbf.setIgnoringElementContentWhitespace(true);

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document domDoc = db.parse(new File(nomFich));
            muestraNodo(domDoc, 0, System.out);
        } catch (FileNotFoundException | ParserConfigurationException
                | SAXException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}