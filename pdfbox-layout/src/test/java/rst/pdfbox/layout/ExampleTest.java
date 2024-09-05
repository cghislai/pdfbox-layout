package rst.pdfbox.layout;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rst.pdfbox.layout.compatibility.CompatibilityHelper;
import rst.pdfbox.layout.util.WordBreakerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class ExampleTest {

    private static Path tempDirectory;
    private File newPdf;

    @BeforeAll
    public static void setUpOutputPath() throws Exception {
        tempDirectory = Paths.get("./test-outputs/examples/");
        deleteRecursive(tempDirectory);
        Files.createDirectories(tempDirectory);

        // reset test situation
        System.clearProperty(WordBreakerFactory.WORD_BREAKER_CLASS_PROPERTY);
    }

    @BeforeEach
    public void setUp() throws Exception {
        // reset test situation
        System.clearProperty(WordBreakerFactory.WORD_BREAKER_CLASS_PROPERTY);
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (newPdf != null && newPdf.exists()) {
//      newPdf.deleteOnExit();
        }
    }

    @Test
    public void testAligned() throws Exception {
        checkExample("Aligned");
    }

    @Test
    public void testColumns() throws Exception {
        checkExample("Columns");
    }

    @Test
    public void testCustomAnnotation() throws Exception {
        checkExample("CustomAnnotation");
    }

    @Test
    public void testFrames() throws Exception {
        checkExample("Frames");
    }

    @Test
    public void testHelloDoc() throws Exception {
        checkExample("HelloDoc");
    }

    @Test
    public void testIndentation() throws Exception {
        checkExample("Indentation");
    }

    @Test
    public void testLandscape() throws Exception {
        checkExample("Landscape");
    }

    @Test
    public void testLetter() throws Exception {
        checkExample("Letter");
    }

    @Test
    public void testLineSpacing() throws Exception {
        checkExample("LineSpacing");
    }

    @Test
    public void testLinks() throws Exception {
        checkExample("Links");
    }

    @Test
    public void testListener() throws Exception {
        checkExample("Listener");
    }

    @Test
    public void testLowLevelText() throws Exception {
        checkExample("LowLevelText");
    }

    @Test
    public void testMargin() throws Exception {
        checkExample("Margin");
    }

    @Test
    public void testMarkup() throws Exception {
        checkExample("Markup");
    }

    @Test
    public void testMultiplePages() throws Exception {
        checkExample("MultiplePages");
    }

    @Test
    public void testRotation() throws Exception {
        checkExample("Rotation");
    }

    public void checkExample(final String example) throws Exception {
        System.out.println("example: " + example);

        Class<?> exampleClass = Class.forName("examples." + example);
        Method mainMethod = exampleClass.getDeclaredMethod("main",
                String[].class);
        mainMethod.invoke(null, new Object[]{new String[]{tempDirectory.toString() + "/"}});

        String pdfName = example.toLowerCase() + ".pdf";
        newPdf = new File(tempDirectory + "/" + pdfName);

        InputStream oldPdf = this.getClass().getResourceAsStream(
                "/examples/pdf/" + pdfName);
        assertNotNull(oldPdf);

        comparePdfs(tempDirectory, newPdf, oldPdf);

    }

    public static BufferedImage toImage(final PDDocument document,
                                        final int pageIndex) throws IOException {
        return CompatibilityHelper
                .createImageFromPage(document, pageIndex, 210);
    }

    protected static void comparePdfs(Path tempDirectory, final File newPdf, InputStream toCompareTo)
            throws IOException, AssertionError {

        try (PDDocument currentDoc = Loader.loadPDF(new RandomAccessReadBufferedFile(newPdf));
             PDDocument oldDoc = Loader.loadPDF(new RandomAccessReadBuffer(toCompareTo))) {

            if (currentDoc.getNumberOfPages() != oldDoc.getNumberOfPages()) {
                throw new AssertionError(String.format(
                        "expected %d pages, but is %d",
                        oldDoc.getNumberOfPages(),
                        currentDoc.getNumberOfPages()));
            }

            for (int i = 0; i < oldDoc.getNumberOfPages(); i++) {
                BufferedImage currentPageImg = toImage(currentDoc, i);
                BufferedImage oldPageImg = toImage(oldDoc, i);
                BufferedImage diff = compareImage(currentPageImg, oldPageImg);
                if (diff != null) {
                    File diffFile = new File(newPdf.getAbsoluteFile() + ".diff.png");
                    ImageIO.write(diff, "png", new File(newPdf.getAbsoluteFile() + ".diff.png"));
                    ImageIO.write(currentPageImg, "png", new File(newPdf.getAbsoluteFile() + ".cur.png"));
                    ImageIO.write(oldPageImg, "png", new File(newPdf.getAbsoluteFile() + ".old.png"));
                    throw new AssertionError(String.format(
                            "page %d different, wrote diff image %s", i + 1,
                            diffFile));
                }

            }
        }
    }

    public static BufferedImage compareImage(final BufferedImage img1,
                                             final BufferedImage img2) throws IOException {

        final double colorDistanceTolerance = 0.08;
        final int w = img1.getWidth();
        final int h = img1.getHeight();
        final int[] p1 = img1.getRGB(0, 0, w, h, null, 0, w);
        final int[] p2 = img2.getRGB(0, 0, w, h, null, 0, w);
        final BufferedImage out = new BufferedImage(w, h,
                BufferedImage.TYPE_INT_ARGB);
        long diffCount = 0;
        long minDiffTOFail = 16;

        if (!(java.util.Arrays.equals(p1, p2))) {
            for (int i = 0; i < p1.length; i++) {
                if (normalizedRgbDistance(p1[i], p2[i]) > colorDistanceTolerance) {
                    diffCount++;

                    p1[i] = Color.red.getRGB();
                }
            }
            out.setRGB(0, 0, w, h, p1, 0, w);
        }


        if (diffCount > 0) {
            System.err.println(diffCount + "  diff found in images");
        }
        if (diffCount > minDiffTOFail) {
            return out;
        }

        return null;
    }


    private static double normalizedRgbDistance(int one, int other) {
        return normalizedDistance(new Color(one), new Color(other));
    }

    private static double normalizedDistance(Color one, Color other) {
        int distanceR = one.getRed() - other.getRed();
        int distanceG = one.getGreen() - other.getGreen();
        int distanceB = one.getBlue() - other.getBlue();

        double distance = Math.sqrt((double) (distanceR * distanceR +
                distanceG * distanceG +
                distanceB * distanceB));

        return distance / MAX_VECTOR_LENGTH;
    }


    private static final double MAX_VECTOR_LENGTH = Math.sqrt(3.0 * 255.0 * 255.0);

    private static void deleteRecursive(Path path) throws IOException {
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}

