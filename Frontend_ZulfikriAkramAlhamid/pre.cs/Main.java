import java.util.Scanner;

class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Your Name : ");
        String name = scanner.nextLine();

        System.out.println("Enter Your HP : ");
        int hp = scanner.nextInt();

        System.out.println("Enter Your Power : ");
        int power = scanner.nextInt();

        System.out.println("Enter Your Spell Card : ");
        int spellCard = scanner.nextInt();

        Player player = new Player(name, hp, power, spellCard);

        scanner.nextLine();

        System.out.println("Enter Enemy Name : ");
        String Ename = scanner.nextLine();

        System.out.println("Enter Enemy Hp : ");
        int Ehp = scanner.nextInt();

        Enemy enemy = new Enemy(Ename, Ehp);

        // OUTPUT
        System.out.println("Player Name : " + player.name);
        System.out.println("Player HP : " + player.hp);
        System.out.println("Player Power : " + player.power);
        System.out.println("Player Spell Card : " + player.spellCard);

        System.out.println("=====================================================");

        System.out.println("Enemy Name : " + enemy.Ename);
        System.out.println("Enemy HP : " + enemy.Ehp);
        System.out.println("Enemy Max Hp : " + enemy.maxhp);
    }
}