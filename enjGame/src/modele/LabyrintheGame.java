package modele;

import engine.Commande;
import engine.Game;

import javax.swing.plaf.multi.MultiRootPaneUI;
import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

public class LabyrintheGame implements Game {

    private Niveau level;

    public LabyrintheGame(){
        this.level = new Niveau();
        genLabyrinth();
    }

    public void genLabyrinth(){
        this.level.chargerNiveau("Labyrinthe1");
    }

    @Override
    public String toString(){
        return this.level.toString();
    }

    @Override
    public void evolve(Commande cmd) {
        if (gestionCollision(getMur(), cmd)) {
            if (cmd == Commande.UP) this.level.deplacerHero(-1, 0);
            if (cmd == Commande.DOWN) this.level.deplacerHero(1, 0);
            if (cmd == Commande.LEFT) this.level.deplacerHero(0, -1);
            if (cmd == Commande.RIGHT) this.level.deplacerHero(0, 1);
        }
    }

    public boolean gestionCollision(ArrayList<Mur> murs, Commande cmd){
        int herox = getHeroX();
        int heroy = getHeroY();
        boolean avancer = true;
        if (cmd == Commande.UP)
            herox -=1;
        else if (cmd == Commande.DOWN)
            herox+=1;
        else if (cmd == Commande.LEFT)
            heroy-=1;
        else if (cmd == Commande.RIGHT)
            heroy+=1;
        Rectangle hero = new Rectangle(heroy, herox, 20, 20);
        for (Mur m : murs){
            if (m.getRectangle().intersects(hero))
                avancer = false;
        }
        return avancer;
    }

    public int[][] getLabyrinthe(){
        return level.getLabyrinthe();
    }

    public ArrayList<Mur> getMur(){
        return level.getMur();
    }

    public int getHeroX(){
        return level.getPlayerX();
    }

    public int getHeroY(){
        return level.getPlayerY();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
