public class Enemy {
    public String name;
    public int hp;
    public int maxhp;

    public Enemy(String name, int hp) {
        this.name = name;
        this.hp = hp;
        this.maxhp = hp;
    }

    public void takeDamage(int damage) {
        // Kurangi HP
        this.hp -= damage;

        // HP tidak boleh negatif
        if (this.hp < 0) {
            this.hp = 0;
        }

        // Tampilkan HP saat ini
        System.out.println(name + " took " + damage
                + " damage! HP: " + hp + "/" + maxhp);

        // Jika HP 0, Enemy kalah
        if (this.hp == 0) {
            System.out.println(name + " was defeated!");
        }
    }

    public void attack(Player player, int damage) {
        // Tampilkan serangan
        System.out.println(name
                + " unleashes bullet barrage on "
                + player.name + "!");

        // Berikan damage ke Player
        player.takeDamage(damage);
    }

    public boolean isAlive() {
        return hp > 0;
    }
}