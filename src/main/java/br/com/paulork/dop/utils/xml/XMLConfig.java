package br.com.paulork.dop.utils.xml;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.w3c.dom.Document;

/**
 * @author Paulo R. Kraemer <paulork10@gmail.com>
 */
public class XMLConfig {

    private static XMLUtils xml = null;
    private static Document doc;
    private static File arq = null;
    private static boolean loaded = false;

    private XMLConfig() {
    }

    public static void loadXML() throws Exception {
        arq = new File(XMLConfig.class.getResource("/commons").getPath(), "config.xml");
        if (arq.exists()) {
            xml = new XMLUtils(arq);
            xml.setCharset(XMLUtils.UTF_8);
            doc = xml.getDocument();
            loaded = true;
            configXML();
        } else {
            loaded = false;
            throw new Exception("Não foi possivel determinar a localização do arquivo de configuração do sistema!!");
        }
    }
    
    private static void configXML() throws Exception {
        String host = xml.getValue("host");
        String porta = xml.getValue("porta");
        String db = xml.getValue("db");
        String owner = xml.getValue("owner");
        String senha = xml.getValue("senha");
        String schema = xml.getValue("schema");

        String hibernate_url = xml.getValue("hibernate.connection.url")
                .replace("<host>", host)
                .replace("<porta>", porta)
                .replace("<db>", db);
        xml.setValue("hibernate.connection.url", hibernate_url);
        xml.setValue("hibernate.connection.username", owner);
        xml.setValue("hibernate.connection.password", senha);
        xml.setValue("hibernate.default_schema", schema);
    }

    public static String getValorOriginal(String tag) {
        return doc.getElementsByTagName(tag).item(0).getTextContent();
    }

    public static String getValor(String tag) {
        return xml.getValue(tag);
    }

    public static List<String> getValores(String tag) throws Exception {
        return Arrays.asList(xml.getValues(tag));
    }

    public static boolean existeTag(String tag) throws Exception {
        return xml.getNumOccur(tag) > 0;
    }

    public static HashMap<String, String> getConfigHibernate() throws Exception {
        return getConfig("hibernate");
    }

    public static HashMap<String, String> getConfigEmail() throws Exception {
        return getConfig("email");
    }

    public static HashMap<String, String> getConfigBanco() throws Exception {
        return getConfig("banco");
    }

    public static HashMap<String, String> getConfig(String tag) throws Exception {
        if (!loaded) {
            loadXML();
        }
        return xml.getMapChilds(tag);
    }

    public static boolean isLoaded() {
        return loaded;
    }

}
