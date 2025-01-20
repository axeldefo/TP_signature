import util.ByteToHex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class MyPassword {
	
	private String password;
	
	public MyPassword(String password) {
		this.password = ByteToHex.convert(hacheSha256(password));
	}

	public  String getPassword() {
		return password;
	}

	public String toString() {
		return "Mot de passe stocké : "+ this.password;
	}

	public static byte[] hacheSha256(String pMessage){
		try {
			// Obtient une instance de SHA-256
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			// Convertit le message en tableau d'octets (UTF-8)
			byte[] encodedHash = digest.digest(pMessage.getBytes(StandardCharsets.UTF_8));
			return encodedHash;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Algorithme de hachage non disponible", e);
		}
	}

	public boolean equals(Object pPass) {
		if (pPass instanceof MyPassword) {
			return this.password.equals(((MyPassword) pPass).getPassword());
		}
		return false;
	}

	// Méthode de contrôle d'accès
	public boolean controleAcces(String pPass) {
		// Crée un nouvel objet MyPassword pour le mot de passe saisi
		MyPassword myPasswordControleAcces = new MyPassword(pPass);
		// Compare les hachés des deux mots de passe
		return this.equals(myPasswordControleAcces);
	}

	public static void main(String[] args) {
		System.out.println("Veuillez définir un mot de passe : ");
		Scanner scanner = new Scanner(System.in);
		String passString = scanner.nextLine();

		// Création de l'objet MyPassword
		MyPassword myPassword = new MyPassword(passString);
		System.out.println(myPassword);

		// Demander la confirmation pour le contrôle d'accès
		System.out.println("Veuillez saisir votre mot de passe pour vous connecter : ");
		String passConfirmation = scanner.nextLine();

		// Tester le contrôle d'accès
		if (myPassword.controleAcces(passConfirmation)) {
			System.out.println("Succès : Mot de passe correct !");
		} else {
			System.out.println("Échec : Mot de passe incorrect.");
		}

		// Fermer le scanner
		scanner.close();
	}

}
