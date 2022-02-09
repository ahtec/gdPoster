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
    private static int imageBreedteInPX;
    static File currentFile ;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        breedteDoel = 1000;
        hoogteDoel = 1000;
        imageBreedteInPX = 2000;
        imageHoogteInPX = 3000;
         currentFile = new File("C:\\Users\\User\\Pictures\\142_1440.jpg");
//        System.out.println(heleMalenA4l());
        System.out.println(aantalA4LinDoel());
//        System.out.println(restDoel());
//        System.out.println(A4LBreedteInPX());
        int oudej = 0;
        int j;
        for (j = A4PHoogteInPX(); j < imageHoogteInPX; j = j + A4PHoogteInPX()) {
            System.out.println(oudej + " Vertikaal tot  " + j);
            oudej = j;

            int oudei = 0;
            int i;
            for (i = A4LBreedteInPX(); i < imageBreedteInPX; i = i + A4LBreedteInPX()) {
                System.out.println(oudei + " tot  " + i);
                deelImage(0,20,40,100);
                oudei = i;
            }
            System.out.println(oudei + " tot  " + imageBreedteInPX);
        }
        System.out.println(oudej + " Vertikaal tot  " + imageHoogteInPX);

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

    static public void deelImage(int drukX, int drukY, int losX, int losY) {
        try {
            // make  deel image
//                    File inputFile = new File(GdImageParting.starFile);
            String erinExtension = getExtension(currentFile.getName());
            final BufferedImage source = ImageIO.read(currentFile);
//            System.out.println("SelectRectangle.MListener.mouseReleased()" + drukX + " " + drukY + " " + losX + " " + losY);
            int maxX = source.getWidth();
            int maxY = source.getHeight();

            int linksX = Math.min(losX, drukX);
            int bovenY = Math.min(losY, drukY);

            int rechtsX = Math.max(losX, drukX);
            rechtsX = Math.min(rechtsX, maxX);
            int onderY = Math.max(losY, drukY);
            onderY = Math.min(onderY, maxY);

            int widthX = rechtsX - linksX;
            int widthY = onderY - bovenY;
            if (widthX > 0 & widthY > 0) {
                ImageIO.write(source.getSubimage(linksX, bovenY, widthX, widthY), erinExtension, maakSubFile(currentFile));
            } else {
                System.out.println(" Geen image gemaakt omdat coor 0 zijn");
            }
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
