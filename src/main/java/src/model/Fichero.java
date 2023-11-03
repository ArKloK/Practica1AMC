/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 *
 * @author Usuario
 */
public class Fichero {

    private String rutaDelProyecto;
    private File fichero;

    public String getRutaDelProyecto() {
        return rutaDelProyecto;
    }

    public void setRutaDelProyecto(String rutaDelProyecto) {
        this.rutaDelProyecto = rutaDelProyecto;
    }

    public File getFichero() {
        return fichero;
    }

    public void setFichero(File fichero) {
        this.fichero = fichero;
    }

    //****************************************MANEJO DE FICHERO**********************************************
    
    //Dado un archivo .tsp recorre el archivo y crea un ArrayList<Punto> para guardar los puntos del archivo
    public ArrayList<Punto> leerPuntos(File archivo) {
        ArrayList<Punto> puntos = new ArrayList<>();
        if (archivo.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(archivo));

                String line;
                boolean isParsingCoordinates = false;

                while ((line = reader.readLine()) != null) {
                    if (isParsingCoordinates) {
                        if (line.equals("EOF")) {
                            break; // Termina cuando se encuentra "EOF"
                        }
                        String[] parts = line.split(" ");
                        if (parts.length == 3) {
                            int id = Integer.parseInt(parts[0]);
                            double x = Double.parseDouble(parts[1]);
                            double y = Double.parseDouble(parts[2]);
                            puntos.add(new Punto(id, x, y));
                        }
                    } else if (line.equals("NODE_COORD_SECTION")) {
                        isParsingCoordinates = true; // Comienza a analizar las coordenadas
                    }
                }
            } catch (Exception ex) {
                ex.getMessage();
            }
        } else {
            System.out.println("El archivo no existe.");
        }
        return puntos;
    }

    //Dado un nombre de fichero lo busca en la ruta relativa del proyecto
    public File buscarRuta(String nombreFichero) {
        this.setFichero(new File(this.getRutaDelProyecto()));

        for (int i = 0; i < 2; i++) {
            setFichero(getFichero().getParentFile());
        }

        setFichero(new File(getFichero().getAbsolutePath() + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "TSP" + File.separator + nombreFichero));
        return getFichero();
    }
}
