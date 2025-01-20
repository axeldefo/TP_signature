import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.signatures.PdfPadesSigner;
import com.itextpdf.signatures.SignatureUtil;
import com.itextpdf.signatures.SignerProperties;
import com.itextpdf.signatures.PrivateKeySignature;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.forms.form.element.SignatureFieldAppearance;
import com.itextpdf.forms.fields.properties.SignedAppearanceText;
import com.itextpdf.kernel.geom.Rectangle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.Security;
import java.util.Enumeration;
import java.util.Scanner;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import com.itextpdf.signatures.CertificateUtil;

public class MySignaturePadesPdf {

    // Attributs pour stocker la clé privée, le certificat et la chaîne de certificats
    private PrivateKey privateKey;
    private X509Certificate certificate;
    private Certificate[] certificateChain;

    // Nom du fichier PDF source
    public static final String SRC_PDF = "monPdf.pdf";

    // Constructeur pour charger le Keystore
    public MySignaturePadesPdf(String pP12File, String pMotDePasse) {
        // Ajout du fournisseur BouncyCastle pour la gestion de cryptographie
        Security.addProvider(new BouncyCastleProvider());
        try {
            // Chargement du fichier PKCS12 avec BouncyCastle
            KeyStore keystore = KeyStore.getInstance("pkcs12", "BC");
            keystore.load(new FileInputStream(pP12File), pMotDePasse.toCharArray());

            // Boucle pour obtenir les alias et vérifier si c'est une clé privée
            Enumeration<String> aliases = keystore.aliases();
            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();

                // Vérification de la clé privée
                if (keystore.isKeyEntry(alias)) {
                    // Récupération de la clé privée
                    privateKey = (PrivateKey) keystore.getKey(alias, pMotDePasse.toCharArray());
                    // Récupération du certificat
                    certificate = (X509Certificate) keystore.getCertificate(alias);
                    // Récupération de la chaîne de certificats
                    certificateChain = keystore.getCertificateChain(alias);
                    break; // On arrête dès qu'on a trouvé la clé et le certificat
                }
            }

            if (certificate != null) {
                System.out.println("Certificat trouvé : " + certificate.getSubjectDN());
            } else {
                System.out.println("Aucun certificat trouvé.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Méthode pour afficher le certificat
    public X509Certificate getCertificate() {
        return certificate;
    }

    // Méthode pour générer le PDF
    public void generatePdf() {
        try {
            PdfWriter writer = new PdfWriter(new FileOutputStream(SRC_PDF));
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Je vais signer un fichier PDF"));
            document.close();

            System.out.println("Le fichier PDF a été généré avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Méthode pour signer le PDF avec le certificat et la clé privée en PAdES B-B
    public void signerPdf(String pOutFile, String pRaisonSignature, String pLieuSignature) {
        try {
            // Create PdfReader for the source PDF
            PdfReader reader = new PdfReader(SRC_PDF);

            // Create FileOutputStream for the destination file
            FileOutputStream fos = new FileOutputStream(pOutFile);

            // Create PdfPadesSigner with reader and output stream
            PdfPadesSigner signer = new PdfPadesSigner(reader, fos);

            // Create SignedAppearanceText instance (s2)
            SignedAppearanceText s2 = new SignedAppearanceText();
            s2.setSignedBy(certificate.getSubjectDN().toString());
            s2.setReasonLine(pRaisonSignature);
            s2.setLocationLine(pLieuSignature);

            // Create SignatureFieldAppearance instance (s1) and set its content
            SignatureFieldAppearance s1 = new SignatureFieldAppearance("Signature1");
            s1.setContent(s2);

            // Create SignerProperties and configure it
            SignerProperties signerProperties = new SignerProperties();
            signerProperties.setReason(pRaisonSignature);
            signerProperties.setLocation(pLieuSignature);

            // Set the signature appearance
            signerProperties.setSignatureAppearance(s1);

            // Define the signature rectangle
            Rectangle signatureRect = new Rectangle(350, 650, 200, 60);
            signerProperties.setPageRect(signatureRect);

            // Sign with PAdES Baseline-B profile using private key and certificate chain
            signer.signWithBaselineBProfile(signerProperties, certificateChain, privateKey);

            System.out.println("Le fichier PDF a été signé avec succès avec une signature visible !");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        // Demande du mot de passe à l'utilisateur
        String p12File = "C:\\Users\\axeld\\OneDrive\\Documents\\cyber\\TP_Signature\\src\\AxelDefo_cert_sign.p12";

        Scanner scanner = new java.util.Scanner(System.in);

        System.out.print("Entrez le mot de passe du fichier .p12 : ");
        String pMotDePasse = scanner.nextLine();

        // Création de l'objet et chargement du keystore
        MySignaturePadesPdf pdfGenerator = new MySignaturePadesPdf(p12File, pMotDePasse);

        // Affichage des informations du certificat
        X509Certificate cert = pdfGenerator.getCertificate();
        if (cert != null) {
            System.out.println("Certificat : " + cert.getSubjectDN());
        }

        // Appel de la méthode pour générer le PDF
        pdfGenerator.generatePdf();

        // Appel de la méthode pour signer le PDF
        String raison = "Validation du document";
        String lieu = "Le Mans, France";
        pdfGenerator.signerPdf(SRC_PDF + "-B_B.pdf", raison, lieu);
    }
}
