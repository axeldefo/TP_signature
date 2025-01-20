import java.nio.charset.StandardCharsets;
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

    // Surcharge de la méthode verifySignature
    @Override
    public boolean verifySignature(byte[] pCondensat) {
        try {
            // Initialisation de l'objet Signature avec l'algorithme SHA256withRSA
            Signature signature = Signature.getInstance("SHA256WithRSA");

            // Initialisation avec la clé publique du signataire pour la vérification
            signature.initVerify(publicKey);

            // Injection des octets du message à vérifier
            String message = "Je signe un message électroniquement";
            signature.update(message.getBytes("UTF-8"));

            // Vérification du condensat signé
            return signature.verify(pCondensat);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static void main(String[] args) {
        try {

            // Message à signer
            String message = "Je signe un message électroniquement";

            // Instanciation de l'objet MySecondSignature
            MySecondSignature mySecondSignature = new MySecondSignature(message);

            // Appel de la méthode sign() pour obtenir la signature du message
            byte[] signedMessage = mySecondSignature.sign();

            // Appel de la méthode verifySignature() pour vérifier la signature
            boolean isValid = mySecondSignature.verifySignature(signedMessage);

            // Affichage du résultat de la vérification
            System.out.println("La signature est " + (isValid ? "Valide" : "Invalide"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
