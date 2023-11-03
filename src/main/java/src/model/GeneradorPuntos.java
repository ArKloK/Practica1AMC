/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Usuario
 */
public class GeneradorPuntos {

    private Fichero fichero;
    private boolean isPeorCaso;

    public Fichero getFichero() {
        return fichero;
    }

    public void setFichero(Fichero fichero) {
        this.fichero = fichero;
    }

    public boolean getIsPeorCaso() {
        return isPeorCaso;
    }

    public void setIsPeorCaso(boolean isPeorCaso) {
        this.isPeorCaso = isPeorCaso;
    }

    //****************************************GENERACIÓN DE PUNTOS**********************************************
    //Genera un ArrayList<Punto> colocandolos todos en la misma posicion y, simulando así el peor caso
    public ArrayList<Punto> GenerarPuntosAleatoriosPeor(int n) {
        Random rand = new Random();
        rand.setSeed(System.nanoTime());
        ArrayList<Punto> p = new ArrayList<Punto>();
        for (int i = 0; i < n; i++) {
            double aux1 = rand.nextInt(1000) + 7;
            double y = aux1 / ((double) i + 1 + i * 0.100);
            int num = rand.nextInt(3);
            y += ((i % 500) - num * (rand.nextInt(100)));
            double x = 1;
            Punto punto = new Punto(i + 1, x, y);
            p.add(punto);
        }
        return p;
    }

    //Genera un ArrayList<Punto> colocandolos en posiciones aleatorias 
    public ArrayList<Punto> GenerarPuntosAleatorios(int n) {
        Random rand = new Random();
        rand.setSeed(System.nanoTime());
        ArrayList<Punto> p = new ArrayList<Punto>();
        for (int i = 0; i < n; i++) {
            int num = rand.nextInt(4000) + 1;
            int den = rand.nextInt(11) + 7;
            double x = num / ((double) den + 0.37);
            double y = (rand.nextInt(4000) + 1) / ((double) (rand.nextInt(11) + 7) + 0.37);
            Punto punto = new Punto(i + 1, x, y);
            p.add(punto);
        }
        return p;
    }

    //Crea un fichero .tsp con la misma estructura que los dados y guardandolos en el mismo directorio
    public void crearFicheroAleatorio(int size) {
        String fileName = "dataset" + size + ".tsp";
        File file = new File(this.fichero.getRutaDelProyecto());
        for (int i = 0; i < 2; i++) {
            file = file.getParentFile();
        }
        file = new File(file.getAbsolutePath() + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "TSP" + File.separator + fileName);
        String filePath = file.toString();
        Random r = new Random();
        r.setSeed(System.nanoTime());
        DecimalFormat decimalFormat = new DecimalFormat("#.##########");
        try {
            ArrayList<Punto> puntos;
            if (isPeorCaso) {
                puntos = GenerarPuntosAleatoriosPeor(size);

            } else {
                puntos = GenerarPuntosAleatorios(size);

            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write("NAME: " + fileName);
            writer.newLine();
            writer.write("TYPE: TSP");
            writer.newLine();
            writer.write("COMMENT: " + size + " locations");
            writer.newLine();
            writer.write("DIMENSION: " + size);
            writer.newLine();
            writer.write("EDGE_WEIGHT_TYPE: EUC_2D");
            writer.newLine();
            writer.write("NODE_COORD_SECTION");
            writer.newLine();
            for (int i = 0; i < size; i++) {
                writer.write(i + 1 + " " + puntos.get(i).getX() + " " + puntos.get(i).getY());
                writer.newLine();
            }
            //Forzar escritura en el archivo
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.fichero.setFichero(file);
    }
}
