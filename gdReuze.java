/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imgeonderzoek;

import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import static java.lang.Math.abs;
import static java.lang.System.exit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

//test npp
/*
 * a4 210 * 297  
 */
public class gdReuze {

    public static final int A4Breed = 297;
    public static final int A4Hoog = 210;
    public static final int A4BreedPx = 2480;
    public static final int A4HoogPx = 3508;

    static int breedteDoel, rest_mm, uithoogteDoel, imageHoogteInPX, hoogteNaSchalen;
    static int imageBreedteInPX;
    static File currentFile;
    static BufferedImage source;
    private static boolean eindeHorisontaalBereikt;
    private static boolean eindeVertikaalBereikt;
    static JFrame frame;
    public static ArrayList<ArrayList> rijList;
    public static ArrayList<String> imageList;
    public static ArrayList<ArrayList<String>> rijImageList;
    public static int rowNr;
    public static String format;

    public gdReuze(File inputFile, String breedte, String hoogte) {

        try {
            ImageInputStream iis = ImageIO.createImageInputStream(inputFile);

            Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);

            while (imageReaders.hasNext()) {
                ImageReader reader = (ImageReader) imageReaders.next();
                System.out.printf("formatName : %s%n", reader.getFormatName());
                format = reader.getFormatName();
            }
            format = "png";  // anders doet ImageIOwrite het niet ?????  nog ophelderen!
            currentFile = inputFile;
            eindeHorisontaalBereikt = false;
            eindeVertikaalBereikt = false;
            breedteDoel = Integer.parseInt(breedte);
            breedteDoel = breedteDoel * 10;  // ingave is in cm verwerking in mm
//            hoogteDoel = Integer.parseInt(breedte);
//            hoogteDoel = hoogteDoel * 10;
//            hoogteDoel = Integer.parseInt(breedte);
//            hoogteDoel = hoogteDoel * 10;

            source = ImageIO.read(currentFile);
            imageBreedteInPX = source.getWidth();
            imageHoogteInPX = source.getHeight();
//            System.out.println("image Breedte " + imageBreedteInPX);
//            System.out.println("      Hoogte  " + imageHoogteInPX);
//            System.out.println(heleMalenA4l());
//            System.out.println(aantalA4LinDoel());
//            System.out.println(A4L_BreedteInPX());
            int oudeVerticale = 0;
            int verticale, horisontale, oudeHorisontale;
            eindeVertikaalBereikt = false;
//            verticale = A4PHoogteInPX();
            verticale = A4L_HoogteInPX();
            // loopen over de horisontale 
            // hiermee wordt de rijList gevuld
            rijImageList = new ArrayList<ArrayList<String>>();
            rowNr = 0;
            do {
                rijImageList.add(new ArrayList<String>());
//                System.out.println(oudeVerticale + " Vertikaal   " + verticale);
                oudeHorisontale = 0;
                hoogteNaSchalen = 0;
                eindeHorisontaalBereikt = false;
                horisontale = A4L_BreedteInPX();

                //verwerk alle pics voor 1 rij
                do {
//                    System.out.println(oudeHorisontale + " horisontale  " + horisontale);
                    deelImage(oudeHorisontale, oudeVerticale, horisontale, verticale); // hier worde de pic aann de arry imageList toegevoegd
                    oudeHorisontale = horisontale;
                    horisontale = horisontale + A4L_BreedteInPX();

                } while (!eindeHorisontaalBereikt);
//                System.out.println("verticale na for loop horisontale " + verticale);
                oudeVerticale = verticale;
//                verticale = verticale + A4PHoogteInPX();
                verticale = verticale + A4L_HoogteInPX();

                rowNr++;

            } while (!eindeVertikaalBereikt);

        } catch (IOException ex) {
            System.out.println("in gdReuze 45  IO exception ");
        }

    }

    static public void deelImage(int oudX, int oudY, int newX, int newY) {
//        boolean eruit = false;
        Image sourceImage;
//        System.out.print(" oudX= " + oudX);
//        System.out.println(" newX= " + newX);
//
//        System.out.print(" oudY= " + oudY);
//        System.out.println(" newY= " + newY);
//        System.out.println("  ");
//        System.out.println("  ");
//        System.out.println("  ");

        try {
            // make  deel image
//                    File inputFile = new File(GdImageParting.starFile);
//            String erinExtension = getExtension(currentFile.getName());
            sourceImage = ImageIO.read(currentFile);

//            int maxX = source.getWidth();
            int maxX = imageBreedteInPX;
//            int maxY = source.getHeight();
            int maxY = imageHoogteInPX;
            int wijdte, hoogte;
            wijdte = newX - oudX;
            hoogte = newY - oudY;
            if (maxX < newX) {
                wijdte = maxX - oudX;
                eindeHorisontaalBereikt = true;
            }

            if (maxY < newY) {
                hoogte = maxY - oudY;
                eindeVertikaalBereikt = true;
            }
//            System.out.print("oudX= ");
//            System.out.print(oudX);
//            System.out.print(" wijdte= ");
//            System.out.println(wijdte);
//
//            System.out.print("oudY= ");
//            System.out.print(oudY);
//            System.out.print(" hoogte= ");
//            System.out.println(hoogte);
//            System.out.println("  ");
//            System.out.println("  ");

            source = ImageIO.read(currentFile);
            Image deelSource = source.getSubimage(oudX, oudY, wijdte, hoogte);

//            ImageIO.write(convertToBufferedImage(scaledImage), erinExtension, maakSubFile(currentFile,""));
//			Image im = convertToBufferedImage(deelSource);
            Image im = schalen(deelSource, A4BreedPx, A4HoogPx);
            BufferedImage bfToConvert = convertToBufferedImage(im);
            File doelFile = maakSubFile(currentFile, "");

//            if (ImageIO.write(bfToConvert, erinExtension, doelFile)) {
//            if (ImageIO.write(bfToConvert, "png", doelFile)) {
            if (ImageIO.write(bfToConvert, format, doelFile)) {
                System.out.println("Succesvol ImegeIO write");
            } else {
                System.out.println("FOUT ImegeIO write");
            };

        } catch (IOException ex) {
            System.out.println("gdposter.GdPoster.maalDeelImge()  IO exception ");
        }
//        return(eruit);
    }

    static public File maakSubFile(File bronFile, String toevoeging) {
        File eruit;
        int fileNummer = 1;
        File workDir = bronFile.getParentFile();
        int extensionIndex = bronFile.getName().lastIndexOf(".");
        String voorvoegsel = bronFile.getName().substring(0, extensionIndex);

//            String extension = bronFile.getName().substring(extensionIndex);
        do {
            eruit = new File(workDir, voorvoegsel + fileNummer + toevoeging + bronFile.getName().substring(extensionIndex));
            fileNummer++;
        } while (eruit.exists());
        rijImageList.get(rowNr).add(eruit.getName());
        try {
            System.out.println(eruit.getCanonicalPath());
        } catch (IOException ex) {
            System.out.println("maakSubFile IO exception ");
        }
        return eruit;

    }

    static Image schalen(Image imageImage, int schermwijdte, int frameHoogte) {
        double quotientSchermWH, quotientImageWH;
        int imageHoogte, imageBreedte;
//        int heightImageToBeDisplayed = imageImage.getHeight(null);
//        int widthImageToBeDisplayed = imageImage.getWidth(null);
//        quotientSchermWH = schermwijdte / (double) frameHoogte;
//        quotientImageWH = widthImageToBeDisplayed / (double) heightImageToBeDisplayed;

//        if (quotientSchermWH > quotientImageWH) {
        // schalen op hoogte
//            imageImage = imageImage.getScaledInstance(-1, frameHoogte, Image.SCALE_SMOOTH);
//            imageHoogte = imageImage.getHeight(frame);
//            imageBreedte = imageImage.getWidth(frame);
//            System.out.println("schalen op hoogte , H = " + imageHoogte + " B=" + imageBreedte + " ");
//            System.out.println("geschaald met , H = " + (frameHoogte));
//        } else {
        // schalen op breedte
        if (hoogteNaSchalen == 0) {
            imageImage = imageImage.getScaledInstance(schermwijdte, -1, Image.SCALE_SMOOTH);
            imageHoogte = imageImage.getHeight(null);
            System.out.println("Scalen op breedte");
            hoogteNaSchalen = imageHoogte;

        } else {
            imageImage = imageImage.getScaledInstance(-1, hoogteNaSchalen, Image.SCALE_SMOOTH);
            imageHoogte = imageImage.getHeight(null);
            System.out.println("Scalen op hoogte");

        }
        imageBreedte = imageImage.getWidth(null);
//        System.out.println("schalen, H= " + imageHoogte + " B= " + imageBreedte);
        System.out.println("hoogteNaSchalen= " + hoogteNaSchalen);
        System.out.println("schermwijdte= " + schermwijdte);

//        //}
        return (imageImage);

    }

//    static double aantal_mm_per_px(int erin) {
//        return (1 / aantal_px_per_mm(erin));
//    }
//    static double aantal_px_per_mm(int erin) {
//        double eruit = 0;
//        eruit = imageBreedteInPX / breedteDoel;
//        return (eruit);
//    }
    static int restDoelBreedte() {
        return (breedteDoel - (heleMalenA4l() * A4Hoog));
    }

//    static int restDoelHoogte() {
//        return (hoogteDoel - (heleMalenA4l() * A4Hoog));
//    }
    static int heleMalenA4l() {
        int eruit;
        eruit = breedteDoel / A4Hoog;
        return (eruit);
    }

//    static int heleMalenA4P() {
//        int eruit;
//        eruit = hoogteDoel / A4Breed;
//        return (eruit);
//    }
    static double aantalA4LinDoel() {
        double eruit;
        eruit = breedteDoel * 1.0 / A4Hoog;
        return (eruit);
    }

//    static double aantalA4PinDoel() {
//        double eruit;
//        eruit = hoogteDoel * 1.0 / A4Breed;
//        return (eruit);
//    }
    static int A4L_BreedteInPX() {
        int eruit = A4Breed * imageBreedteInPX / breedteDoel;
//        System.out.println("A4LBreedteInPX = " + eruit);
        return (eruit);
    }

    static int A4L_HoogteInPX() {
//  diyt is de hoogte van een landscape a4 tje vertaald naat aantal px
// verhouding is breedte doel   en  aantal pic horizontall van plaatje 
        int eruit = A4Hoog * imageBreedteInPX / breedteDoel;
//        System.out.println("A4L BreedteInPX = " + eruit);
        return (eruit);
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

    // convert Image to BufferedImage
    public static BufferedImage convertToBufferedImage(Image img) {

        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bi = new BufferedImage(
                img.getWidth(null), img.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics2D = bi.createGraphics();
        graphics2D.drawImage(img, 0, 0, null);
        graphics2D.dispose();

        return bi;
    }

}
