public class Enemy {
    public String Ename;
    public int Ehp;
    public int maxhp;

public Enemy(String Ename, int Ehp){
    
    this.Ename = Ename;
    this.Ehp = Ehp;
    this.maxhp = Ehp;

}

public void takeDamage(int damage) {

        this.Ehp -= damage;
        
        if (Ehp < 0) {
            Ehp = 0;
        }
        System.out.println(Ename + " took " + damage + " damage! HP: " + Ehp + "/" + maxhp);

        else if (Ehp == 0) {
            System.out.println(Ename + " was defeated!");
        }
    }

    public void attack(Player player, int damage) {
        // 1. Tampilkan informasi bahwa Enemy menyerang Player dalam format: [EnemyName] unleashes bullet barrage on [PlayerName]!
        System.out.println(Ename + "Unleashes bullet barrage on" + name + "!");
        // 2. Panggil takeDamage() milik Player menggunakan damage yang diberikan.
        target.takeDamage(damage);
    }



} 