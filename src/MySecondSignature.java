import java.security.Signature;

public class MySecondSignature extends  MyFirstSignature {
    public MySecondSignature( String myMessage) {
        super( myMessage);
    }

    // Surcharge de la méthode sign() pour effectuer la signature
    @Override
    public byte[] sign() throws Exception {
        // 1. Initialisation de l'objet Signature avec l'algorithme "SHA256withRSA"
        Signature signature = Signature.getInstance("SHA256withRSA");

        // 2. Initialisation avec la clé privée pour signer
        signature.initSign(this.privateKey);

        // 3. Mise à jour de l'objet Signature avec le message (en UTF-8)
        byte[] messageBytes = this.myMessage.getBytes("UTF-8");
        signature.update(messageBytes);

        // 4. Engagement de la signature et retour du condensat signé
        return signature.sign();
    }

    // Méthode pour vérifier la signature en utilisant JCA Signature
    public boolean verifySignatureWithJCA(byte[] signedData) throws Exception {
        // Création de l'objet Signature pour l'algorithme RSA
        Signature signature = Signature.getInstance("SHA256withRSA");

        // Initialisation avec la clé publique
        signature.initVerify(this.publicKey);

        // Hachage du message
        byte[] messageBytes = this.myMessage.getBytes();
        signature.update(messageBytes);

        // Vérification de la signature
        return signature.verify(signedData);
    }

    public static void main(String[] args) {
        try {
            // Création d'un objet MySecondSignature avec le message
            MySecondSignature mySecondSignature = new MySecondSignature("Je signe un message électroniquement");

            // Générer la signature
            byte[] signedMessage = mySecondSignature.sign();

            // Affichage de la signature en hexadécimal
            System.out.print("La signature du message: [Je signe un message électroniquement] est : ");
            for (byte b : signedMessage) {
                System.out.printf("%02x ", b);
            }
            System.out.println(); // Retour à la ligne après l'affichage
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
