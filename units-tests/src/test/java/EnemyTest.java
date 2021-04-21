import org.junit.Test;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import codingfactory.rpgconsole.hero.Hero;
import codingfactory.rpgconsole.enemy.Enemy;

public class EnemyTest {

    Enemy enemy;
    Hero hero;

    @BeforeClass
    public static void setUpBeforeClass() {
        System.out.println("Avant le démarrage");
    }

    @AfterClass
    public static void tearDownAfterClass() {
        System.out.println("Après tous les tests");
    }

    @Before
    public void setUp() {
        enemy = new Enemy("Lord Voldemort", 1);
        hero = new Hero("Harry Potter");
        System.out.println("Avant un test");
    }

    @After
    public void tearDown() {
        System.out.println("Après un test");
    }

    @Test
    public void testEnemyTakeDamage() {
        assertThat(enemy, hasProperty("hp"));
        enemy.takeDamage(5);
        assertThat(enemy.getHp(), is(10));
    }

    @Test
    public void testEnemyAttack() {
        assertThat(enemy, hasProperty("atk"));
        assertThat(hero, hasProperty("hp"));
        enemy.attack(hero);

        assertThat(hero, hasProperty("hp", lessThanOrEqualTo(19)));
        assertThat(hero, hasProperty("hp", greaterThanOrEqualTo(17)));

        assertThat(enemy, hasProperty("atk", lessThanOrEqualTo(this.enemy.getAtk() +1)));
        assertThat(enemy, hasProperty("atk", greaterThanOrEqualTo(this.enemy.getAtk())));
    }

    @Test
    public void testHeroProperties() {
        assertThat(enemy, hasProperty("name"));
        assertEquals(enemy.getName(), "Lord Voldemort");

        assertThat(enemy, hasProperty("hp"));
        assertThat(enemy.getHp(), is(15));

        assertThat(enemy, hasProperty("atk"));
        assertThat(enemy.getAtk(), is(1));
    }
}