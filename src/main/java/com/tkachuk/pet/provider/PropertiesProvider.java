package com.tkachuk.pet.provider;

public class PropertiesProvider {

//    private static final String CONFIG_FILE_PATH = "src/main/resources/config.properties";
//
//    private static Properties properties;
//
//    private static Properties getProperties() {
//        if (properties == null) {
//            properties = loadProperties();
//        }
//        return properties;
//    }
//
//    private static Properties loadProperties() {
//
//        try (InputStream input = new FileInputStream(PropertiesProvider.CONFIG_FILE_PATH)) {
//
//            Properties properties = new Properties();
//            properties.load(input);
//            return properties;
//
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException("Unable to find the configuration file " + PropertiesProvider.CONFIG_FILE_PATH, e);
//        } catch (IOException e) {
//            throw new RuntimeException("Unable to load configuration properties", e);
//        }
//    }
//
//    public static String getProperty(String propertyName) {
//        String property = getProperties().getProperty(propertyName);
//        if (property == null) {
//            System.err.println("Unable to find property '" + propertyName + "' in the configuration file " + CONFIG_FILE_PATH);
//        }
//        return property;
//    }
}
