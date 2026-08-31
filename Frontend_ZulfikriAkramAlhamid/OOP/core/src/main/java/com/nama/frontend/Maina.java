import java.util.Scanner;

class Maina {
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


        

        System.out.println("Player Name : " + player.name);
        System.out.println("Player HP : " + player.hp);
        System.out.println("Player Power : " + player.power);
        System.out.println("Player Spell Card : " + player.spellCard);

        System.out.println("=====================================================");

        System.out.println("Enemy Name : " + enemy.Ename);
        System.out.println("Enemy HP : " + enemy.Ehp);
        System.out.println("Enemy Max Hp : " + enemy.maxhp);

        
        scanner.nextLine();
        
        System.out.println("do u want to attack the enemy? (yes/no)");
        String answer = scanner.next();

        if (answer.equalsIgnoreCase("yes")){
        System.out.println("Enter the damage you want to give to the enemy : ");
        int damage = scanner.nextInt();
        player.attack(enemy, damage);
        }
        else if (answer.equalsIgnoreCase("no")){
            System.out.println("You choose not to attack the enemy");
        }
        else {
            System.out.println("Invalid input. Please enter 'yes' or 'no'.");
        }




        System.out.println("=====================================================");

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