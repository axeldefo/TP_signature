import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.FileOutputStream;

public class MySignaturePadesPdf {

    // Nom du fichier PDF source
    public static final String SRC_PDF = "monPdf.pdf";

    // Méthode pour générer le PDF
    public void generatePdf() {
        try {
            // Création d'un PdfWriter
            PdfWriter writer = new PdfWriter(new FileOutputStream(SRC_PDF));

            // Création d'un PdfDocument à partir du PdfWriter
            PdfDocument pdf = new PdfDocument(writer);

            // Création d'un document à partir du PdfDocument
            Document document = new Document(pdf);

            // Ajout d'un paragraphe au document
            document.add(new Paragraph("Je vais signer un fichier PDF"));

            // Fermeture du document
            document.close();

            System.out.println("Le fichier PDF a été généré avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MySignaturePadesPdf pdfGenerator = new MySignaturePadesPdf();
        pdfGenerator.generatePdf();
        System.out.println("Fin de la génération du PDF");
    }
}
