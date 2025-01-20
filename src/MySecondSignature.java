import java.security.Signature;

public class MySecondSignature extends  MyFirstSignature {
    public MySecondSignature( String myMessage) {
        super( myMessage);
    }

    // Méthode pour signer le message en utilisant JCA Signature
    public byte[] signWithJCA() throws Exception {
        // Création de l'objet Signature pour l'algorithme RSA
        Signature signature = Signature.getInstance("SHA256withRSA");

        // Initialisation avec la clé privée
        signature.initSign(this.privateKey);

        // Hachage du message
        byte[] messageBytes = this.myMessage.getBytes();
        signature.update(messageBytes);

        // Signature et retour du résultat
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

    }
}
