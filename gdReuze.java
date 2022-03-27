package imgeonderzoek;

import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

//test 
/*  versie 24 maart 2022
 * a4 210 * 297  
 */
public class gdReuze {

    public static final int A4LBreed = 297;
    public static final int A4LHoog = 210;
    public static final int A4LBreedPx = 3508;
    public static final int A4LHoogPx = 2480;
    public static int aantalKolommenA4Breed = 2;
    public static int aantalA4Hoog = 0;

    static int rest_mm, uithoogteDoel, imageHoogteInPX, hoogteNaSchalen;
    static int imageBreedteInPX;
    static File currentFile;
    static BufferedImage source;
    private static boolean eindeHorisontaalBereikt;
    private static boolean eindeVertikaalBereikt;
//    static JFrame frame;
    public static ArrayList<ArrayList> rijList;
    public static ArrayList<String> imageList;
    public static ArrayList<ArrayList<String>> rijImageList;
    public static int rowNr;
    public static String format;

    public gdReuze(File inputFile, String breedte, String hoogte) {

        try {
            // Displaing image readers
            // Buffered image vullen met inputfile; currentfile
            // berekene hoogte ; breedte inputFile
            // initialiseren verwerking variabelen

            ImageInputStream iis = ImageIO.createImageInputStream(inputFile);
            Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);
            while (imageReaders.hasNext()) {
                ImageReader reader = (ImageReader) imageReaders.next();
                System.out.printf("formatName : %s%n", reader.getFormatName());
                format = reader.getFormatName();
            }
            format = "png";  // anders doet ImageIOwrite het niet ?????  nog ophelderen!
            aantalKolommenA4Breed = Integer.parseInt(breedte);

            currentFile = inputFile;
            source = ImageIO.read(currentFile);
            imageBreedteInPX = source.getWidth();
            imageHoogteInPX = source.getHeight();
            System.out.println("Image afm h: " + imageHoogteInPX + " b: " + imageBreedteInPX);
            int oudeVerticale = 0;
            int verticale, horisontale = 0, oudeHorisontale = 0;
            verticale = A4L_HoogteInPX();
            aantalA4Hoog = heleAantalA4LandscapeInImage(imageHoogteInPX, imageBreedteInPX);

            rijImageList = new ArrayList<ArrayList<String>>();
            eindeVertikaalBereikt = false;
            rowNr = 0;
//            // looping over de rijen 
            for (int h = 1; h <= aantalA4Hoog; h++) {
//            do {
                rijImageList.add(new ArrayList<String>());
                oudeHorisontale = 0;
                hoogteNaSchalen = 0;
                horisontale = A4L_BreedteInPX();
                //verwerk alle pics voor 1 rij
                for (int b = 1; b <= aantalKolommenA4Breed; b++) {
                    System.out.println("b= " + b);
                    deelImage(oudeHorisontale, oudeVerticale, horisontale, verticale); // hier worde de pic aann de arry imageList toegevoegd
                    oudeHorisontale = horisontale;
                    horisontale = horisontale + A4L_BreedteInPX();

                }
                oudeVerticale = verticale;
                verticale = verticale + A4L_HoogteInPX();
                rowNr++;
//            } while (!eindeVertikaalBereikt);
            }
            //  verwerking resthoogte, als er is
            int resthoogte = hoogteOndersteStukjePX(imageHoogteInPX, imageBreedteInPX);
            if (resthoogte > 0) {
                rijImageList.add(new ArrayList<String>());
                oudeHorisontale = 0;
                horisontale = A4L_BreedteInPX();
                verticale = oudeVerticale + resthoogte;
                for (int b = 1; b <= aantalKolommenA4Breed; b++) {

                    deelImageOndersteRij(oudeHorisontale, oudeVerticale, horisontale, verticale); // hier worde de pic aann de arry imageList toegevoegd
                    oudeHorisontale = horisontale;
                    horisontale = horisontale + A4L_BreedteInPX();

                }
            }

            /*            // looping over de rijen 
            do {
                rijImageList.add(new ArrayList<String>());
                oudeHorisontale = 0;
                hoogteNaSchalen = 0;
                eindeHorisontaalBereikt = false;
                horisontale = A4L_BreedteInPX();

                //verwerk alle pics voor 1 rij
                do {
                    deelImage(oudeHorisontale, oudeVerticale, horisontale, verticale); // hier worde de pic aann de arry imageList toegevoegd
                    oudeHorisontale = horisontale;
                    horisontale = horisontale + A4L_BreedteInPX();

                } while (!eindeHorisontaalBereikt);
                oudeVerticale = verticale;
                verticale = verticale + A4L_HoogteInPX();
                rowNr++;

            } while (!eindeVertikaalBereikt);
             */
        } catch (IOException ex) {
            System.out.println("in gdReuze 101  IO exception ");
        }

    }

    static public void deelImage(int oudX, int oudY, int newX, int newY) {
        int maxX = imageBreedteInPX;
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
        } else {
            try {
                source = ImageIO.read(currentFile);
                Image deelSource = source.getSubimage(oudX, oudY, wijdte, hoogte);

//                Image im = schalen(deelSource, A4BreedPx, A4HoogPx);
                BufferedImage bfToConvert = convertToBufferedImage(deelSource);
//                BufferedImage bfToConvert = convertToBufferedImage(im);
                File doelFile = maakSubFile(currentFile, "");

                if (ImageIO.write(bfToConvert, format, doelFile)) {
//                System.out.println("Succesvol ImegeIO write");
                } else {
                    System.out.println("FOUT ImegeIO write");
                };

            } catch (IOException ex) {
                System.out.println("gdReuze.GdPoster.maalDeelImge()  IO exception ");
            }
        }
//        return(eruit);
    }
    static public void deelImageOndersteRij(int oudX, int oudY, int newX, int newY) {
        int maxX = imageBreedteInPX;
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
        } else {
            try {
                source = ImageIO.read(currentFile);
                Image deelSource = source.getSubimage(oudX, oudY, wijdte, hoogte);

//                Image im = schalen(deelSource, A4BreedPx, A4HoogPx);
//                BufferedImage bfToConvert = convertToBufferedImage(deelSource);
                BufferedImage bfToConvert = vergrootHetImage(convertToBufferedImage(deelSource), A4L_BreedteInPX(), A4L_HoogteInPX() );
                File doelFile = maakSubFile(currentFile, "");

                if (ImageIO.write(bfToConvert, format, doelFile)) {
//                System.out.println("Succesvol ImegeIO write");
                } else {
                    System.out.println("FOUT ImegeIO write");
                };

            } catch (IOException ex) {
                System.out.println("gdReuze.GdPoster.maalDeelImge()  IO exception ");
            }
        }
//        return(eruit);
    }
    static public BufferedImage vergrootHetImage(BufferedImage bronFile, int gewensteBreedte, int gewensteHoogte) throws IOException {
        BufferedImage bi;
//        BufferedImage originalImage = bronFile;
//            File doelFile = maakSubFile(bronFile, "");
        bi = new BufferedImage(
                gewensteBreedte,
                gewensteHoogte,
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics2D = bi.createGraphics();
        graphics2D.drawImage(bronFile, 0, 0, null);
        graphics2D.dispose();

        return (bi);
    }
    
    static Image schalen(Image imageImage, int schermwijdte, int frameHoogte) {
        double quotientSchermWH, quotientImageWH;
        int imageHoogte, imageBreedte;
        int heightImageToBeDisplayed = imageImage.getHeight(null);
        int widthImageToBeDisplayed = imageImage.getWidth(null);
        quotientSchermWH = schermwijdte / (double) frameHoogte;
        quotientImageWH = widthImageToBeDisplayed / (double) heightImageToBeDisplayed;

        if (quotientSchermWH > quotientImageWH) {
//         schalen op hoogte
            imageImage = imageImage.getScaledInstance(-1, frameHoogte, Image.SCALE_SMOOTH);
//            imageHoogte = imageImage.getHeight(frame);
//            imageBreedte = imageImage.getWidth(frame);
            System.out.println("schalen op hoogte , H = " + frameHoogte + " B=" + schermwijdte + " ");
//            System.out.println("geschaald met , H = " + (frameHoogte));
        } else {
            // schalen op breedte
            imageImage = imageImage.getScaledInstance(schermwijdte, -1, Image.SCALE_SMOOTH);
            System.out.println("schalen op breedte , H = " + frameHoogte + " B=" + schermwijdte + " ");

        }
        return (imageImage);
    }

    /*
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
        System.out.println("schalen, H= " + imageHoogte + " B= " + imageBreedte);
        System.out.print("hoogteNaSchalen= " + hoogteNaSchalen);
        System.out.println("    schermwijdte= " + schermwijdte);

        //}
     */
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

    static public File maakSubFile(File bronFile, String toevoeging) {
        File eruit;
        int fileNummer = 1;
        File workDir = bronFile.getParentFile();
        int extensionIndex = bronFile.getName().lastIndexOf(".");
        String voorvoegsel = bronFile.getName().substring(0, extensionIndex);
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

//    static int restDoelBreedte() {
//        return (breedteDoel - (heleMalenA4l() * A4Hoog));
//    }
    static double aantalA4LandscapeInImage(int imagePxHoog, int imagePxBreed) {
        double double1 = imagePxHoog * A4LBreed * aantalKolommenA4Breed;
        double double2 = A4LHoog * imagePxBreed;
        double eruit = double1 / double2;
        return (eruit);
    }

    static int heleAantalA4LandscapeInImage(int imagePxHoog, int imagePxBreed) {
        int eruit = 0;
        double double1 = aantalA4LandscapeInImage(imagePxHoog, imagePxBreed);
        eruit = (int) double1;
        System.out.print("aantalA4LandscapeInImage = ");
        System.out.println(eruit);
        return eruit;
    }

//    static double aantalA4LinDoel() {
//        double eruit;
//        eruit = breedteDoel * 1.0 / A4LHoog;
//        return (eruit);
//    }
    static int A4L_HoogteInPX() {
//  dit is de hoogte van een landscape a4 tje vertaald naat aantal px
        int eruit = 0;
        double doubl1;
        doubl1 = A4L_BreedteInPX() / Math.sqrt(2.0);
        eruit = (int) doubl1;
//        System.out.print("A4L_HoogteInPX = ");
//        System.out.println(eruit);
        return (eruit);
    }

    static int A4L_BreedteInPX() {
        // dit is de breedte van een landscape A4 tje vertaald naar aantal picels in het plaatje
        int eruit = 0;
        eruit = imageBreedteInPX / aantalKolommenA4Breed;
        System.out.println("A4LBreedteInPX = " + eruit);
        return (eruit);
    }

    static int hoogteOndersteStukjePX(int imagePxHoog, int imagePxBreed) {
        int eruit = 0;
        int x = heleAantalA4LandscapeInImage(imagePxHoog, imagePxBreed);
        double y = aantalA4LandscapeInImage(imagePxHoog, imagePxBreed);
        double  verschil =  y - x;
        double uitkomst = A4L_HoogteInPX() * verschil ;
        eruit = (int)uitkomst;
        return (eruit);
    }

    //    public static String getExtension(String fileName) {
//        int index = fileName.lastIndexOf('.');
//        if (index == -1) {
//            return "";
//        } else {
//            index++;
//            return fileName.substring(index);
//        }
//    }
}
