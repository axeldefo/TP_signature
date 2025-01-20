import java.util.Scanner;

public class MyPassword {
	
	private String password;
	
	public MyPassword(String password) {
		this.password = password;
	}

	public  String getPassword() {
		return password;
	}

	public String toString() {
		return "Mot de passe stocké : "+ this.password;
	}
	

	public static void main(String[] args) {
		System.out.println("Veuillez définir un mot de passe : ");
		Scanner scanner = new Scanner( System.in );
		String passString = scanner.nextLine();
		MyPassword myPassword = new MyPassword(passString);
		System.out.println(myPassword);
	}

}
