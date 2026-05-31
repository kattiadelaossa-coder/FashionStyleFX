/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fashionstylefx;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author pc
 */
public class DataStorage {

    private static final String ARCHIVO_COMPRAS = "src/fashionstylefx/compras.json";
    private static final String ARCHIVO_DESEOS = "src/fashionstylefx/deseos.json";

    // Guardar compras por usuario
    public static void guardarCompras(String email, List<Compra> compras) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Map<String, List<Compra>> datos = new HashMap<>();

            // Cargar datos existentes
            FileReader reader = new FileReader(ARCHIVO_COMPRAS);
            Type tipo = new TypeToken<Map<String, List<Compra>>>() {
            }.getType();
            datos = gson.fromJson(reader, tipo);
            reader.close();

            if (datos == null) {
                datos = new HashMap<>();
            }

            // Actualizar compras del usuario
            datos.put(email, compras);

            // Guardar
            FileWriter writer = new FileWriter(ARCHIVO_COMPRAS);
            gson.toJson(datos, writer);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Compra> cargarCompras(String email) {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(ARCHIVO_COMPRAS);
            Type tipo = new TypeToken<Map<String, List<Compra>>>() {
            }.getType();
            Map<String, List<Compra>> datos = gson.fromJson(reader, tipo);
            reader.close();

            System.out.println("Datos cargados del archivo: " + datos);

            if (datos != null && datos.containsKey(email)) {
                System.out.println("Compras encontradas para " + email + ": " + datos.get(email).size());
                return datos.get(email);
            } else {
                System.out.println("No se encontraron compras para " + email);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    // Guardar deseos por usuario
    public static void guardarDeseos(String email, List<Producto> deseos) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Map<String, List<Producto>> datos = new HashMap<>();

            FileReader reader = new FileReader(ARCHIVO_DESEOS);
            Type tipo = new TypeToken<Map<String, List<Producto>>>() {
            }.getType();
            datos = gson.fromJson(reader, tipo);
            reader.close();

            if (datos == null) {
                datos = new HashMap<>();
            }

            datos.put(email, deseos);

            FileWriter writer = new FileWriter(ARCHIVO_DESEOS);
            gson.toJson(datos, writer);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Cargar deseos por usuario
    public static List<Producto> cargarDeseos(String email) {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(ARCHIVO_DESEOS);
            Type tipo = new TypeToken<Map<String, List<Producto>>>() {
            }.getType();
            Map<String, List<Producto>> datos = gson.fromJson(reader, tipo);
            reader.close();

            if (datos != null && datos.containsKey(email)) {
                return datos.get(email);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new java.util.ArrayList<>();
    }
}
