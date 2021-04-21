package test;

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

public class HeroTest {

	Hero hero;
	Enemy enemy;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("Avant le démarrage");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("Après tous les tests");
	}

	@Before
	public void setUp() throws Exception {
		hero = new Hero("Jaina Portvaillant");
		enemy = new Enemy("Lord Voldemort", 1);
		System.out.println("Avant un test");
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("Après un test");
	}

	@Test
	public void testHeroLevelUp() throws Exception {
		assertThat(hero, hasProperty("level"));
		hero.levelUp();
		assertThat(hero, hasProperty("level", is(2)));
	}

	@Test
	public void testHeroTakeDamage() throws Exception {
		assertThat(hero, hasProperty("hp"));
		hero.takeDamage(5);
		assertThat(hero, hasProperty("hp", is(15)));
	}

	@Test
	public void testHeroAttack() throws Exception {
		assertThat(hero, hasProperty("atk"));
		assertThat(enemy, hasProperty("hp"));
		hero.attack(enemy);

		assertThat(enemy, hasProperty("hp", lessThanOrEqualTo(13)));
		assertThat(enemy, hasProperty("hp", greaterThanOrEqualTo(11)));

		assertThat(hero, hasProperty("atk", lessThanOrEqualTo(this.hero.getAtk() +2)));
		assertThat(hero, hasProperty("atk", greaterThanOrEqualTo(this.hero.getAtk())));
	}

	@Test
	public void testHeroProperties() throws Exception {
		assertThat(hero, hasProperty("name"));
        assertThat(hero, hasProperty("name", is("Jaina Portvaillant")));

		assertThat(hero, hasProperty("hp"));
		assertEquals(hero.getHp(), 20);

        assertThat(hero, hasProperty("level"));
        assertEquals(hero.getLevel(), 1);

		assertThat(hero, hasProperty("atk"));
		assertSame(2, hero.getAtk());
	}
}
