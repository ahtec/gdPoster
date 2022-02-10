/*
 * a4 210 * 297  
 */
package gdposter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static java.lang.Math.abs;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class GdPoster {

    public static final int A4Hoog = 297;
    public static final int A4Breed = 210;

    static int breedteDoel, rest_mm, hoogteDoel, imageHoogteInPX;
    static int imageBreedteInPX;
    static File currentFile;
    static BufferedImage source;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        breedteDoel = Integer.parseInt(args[1]);
        hoogteDoel = Integer.parseInt(args[2]);
        currentFile = new File(args[0]);
//        currentFile = new File("C:\\Users\\User\\Pictures\\142_1440.jpg");
        BufferedImage source = ImageIO.read(currentFile);
        imageBreedteInPX = source.getWidth();
        imageHoogteInPX = source.getHeight();
//        System.out.println("image Breedte" + imageBreedteInPX);
//        System.out.println(heleMalenA4l());
//        System.out.println(aantalA4LinDoel());
//        System.out.println(restDoel());
//        System.out.println(A4LBreedteInPX());
        int oudeVerticale = 0;
        int verticale = 0;
        for (verticale = A4PHoogteInPX();
                verticale < imageHoogteInPX;
                verticale = verticale + A4PHoogteInPX()) {
//            System.out.println(oudeVerticale + " Vertikaal   " + verticale);
            int oudeHorisontale = 0;
            int horisontale = 0;
            for (horisontale = A4LBreedteInPX();
                    horisontale < imageBreedteInPX;
                    horisontale = horisontale + A4LBreedteInPX()) {
//                System.out.println(oudeHorisontale + " horisontale  " + horisontale);
                deelImage(oudeHorisontale, oudeVerticale,
                        horisontale, verticale);
                oudeHorisontale = horisontale;
            }
//            System.out.println("verticale na for loop horisontale " + verticale);
            oudeVerticale = verticale;
        }
    }

    static int restDoelBreedte() {
        return (breedteDoel - (heleMalenA4l() * A4Hoog));
    }

    static int restDoelHoogte() {
        return (hoogteDoel - (heleMalenA4l() * A4Hoog));
    }

    static int heleMalenA4l() {
        int eruit;
        eruit = breedteDoel / A4Hoog;
        return (eruit);
    }

    static int heleMalenA4P() {
        int eruit;
        eruit = hoogteDoel / A4Breed;
        return (eruit);
    }

    static double aantalA4LinDoel() {
        double eruit;
        eruit = breedteDoel * 1.0 / A4Hoog;
        return (eruit);
    }

    static double aantalA4PinDoel() {
        double eruit;
        eruit = hoogteDoel * 1.0 / A4Breed;
        return (eruit);
    }

    static int A4LBreedteInPX() {
        int eruit;
        eruit = (int) (imageBreedteInPX / aantalA4LinDoel());
        return (eruit);
    }

    static int A4PHoogteInPX() {
        int eruit;
        eruit = (int) (imageHoogteInPX / aantalA4PinDoel());
//        System.out.println("eruit = " + eruit);
        return (eruit);
    }

    static public File maakSubFile(File bronFile) {
        File eruit;
        int fileNummer = 1;
        File workDir = bronFile.getParentFile();
        int extensionIndex = bronFile.getName().lastIndexOf(".");
        String voorvoegsel = bronFile.getName().substring(0, extensionIndex);

//            String extension = bronFile.getName().substring(extensionIndex);
        do {
            eruit = new File(workDir, voorvoegsel + fileNummer + bronFile.getName().substring(extensionIndex));
            fileNummer++;
        } while (eruit.exists());

        return eruit;

    }

    static public void deelImage(int oudX, int oudY, int newX, int newY) {
        try {
            // make  deel image
//                    File inputFile = new File(GdImageParting.starFile);
            String erinExtension = getExtension(currentFile.getName());

//            int maxX = source.getWidth();
            int maxX = imageBreedteInPX;
//            int maxY = source.getHeight();
            int maxY = imageHoogteInPX;
            int wijdte, hoogte;
            wijdte = newX - oudX;
            hoogte = newY - oudY;
            if (maxX < newX) {
                wijdte = maxX - oudX;
            }

            if (maxY < newY) {
                hoogte = maxY - oudY;
            }
//            System.out.println(oudX);
//            System.out.println(newX);
//            System.out.println(oudY);
//            System.out.println(newY);
            BufferedImage source = ImageIO.read(currentFile);
            ImageIO.write(source.getSubimage(oudX, oudY, wijdte, hoogte), erinExtension, maakSubFile(currentFile));
//            } else {
//                System.out.println(" Geen image gemaakt omdat coor 0 zijn");
//            }
//                    ImageIO.write(source.getSubimage(linksX, bovenY, widthX, widthY), erinExtension, new File(inputFile.getCanonicalPath() + idx++ + "." + erinExtension));
        } catch (IOException ex) {
            System.out.println("gdposter.GdPoster.maalDeelImge()  IO exception ");
        }
    }

    public static String getExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        if (index == -1) {
            return "";
        } else {
            index++;
            return fileName.substring(index);
        }
    }
}
