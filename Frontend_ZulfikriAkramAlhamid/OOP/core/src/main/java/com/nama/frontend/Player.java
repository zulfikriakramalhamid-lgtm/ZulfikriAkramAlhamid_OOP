public class Player {
    public String name;
    public int hp;
    public int power;
    public int spellCard;

public Player(String name, int hp, int power, int spellCard){

    this.name = name;
    this.hp = hp;
    this.power = power;
    this.spellCard = spellCard;

}

public void takeDamage(int damage) {
        this.hp -= damage;
        
        if(hp < 0){
            hp = 0;
            System.out.println(name + " has been defeated!");
        }
        
        else if(hp > 0){
            System.out.println(name + " took " + damage + " damage! Remaining HP: " + hp);
        }

        else if(hp == 0){
            System.out.println(name + " has been defeated!");
        }
    }
    public void shoot(Enemy target) {

        int damage = power + 10;
        // 2. Tampilkan informasi bahwa Player menembak Enemy dalam format: [name] shoots [TargetName] dealing [damage] DMG!
        System.out.println(name + "Shoots" + target + "Dealing" + damage + "DMG!");
        // 3. Panggil method takeDamage() milik object Enemy.
        target.takeDamage(damage);

    }

    public boolean isAlive() {
        // 1. Kembalikan true jika hp > 0 dan false jika sebaliknya
        if(hp > 0){
            = true
        }
    }


}




