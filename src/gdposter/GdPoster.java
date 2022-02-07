/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gdposter;

import static java.lang.Math.abs;

/**
 *
 * @author User
 */
public class GdPoster {

    static int breedteDoel, rest_mm;
    private static int imageBreedteInPX;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        breedteDoel = 1000;
        imageBreedteInPX = 2000;
        System.out.println(heleMalenA4l());
        System.out.println(aantalA4LinDoel());
        System.out.println(restDoel());
        System.out.println(A4LBreedteInPX());
        int oudei = 0;
        int i;
        for (i = A4LBreedteInPX(); i < imageBreedteInPX; i = i + A4LBreedteInPX()) {
            System.out.println(oudei + " tot  " + i);
            oudei = i;
        }
        System.out.println(oudei + " tot  " + imageBreedteInPX);

    }

    static int restDoel() {
        return (breedteDoel - (heleMalenA4l() * 297));
    }

    static int heleMalenA4l() {
        int eruit;
        eruit = breedteDoel / 297;
        return (eruit);
    }

    static double aantalA4LinDoel() {
        double eruit;
        eruit = breedteDoel / 297.0;
        return (eruit);
    }

    static int A4LBreedteInPX() {
        int eruit;
        eruit = (int) (imageBreedteInPX / aantalA4LinDoel());
        return (eruit);
    }
}
