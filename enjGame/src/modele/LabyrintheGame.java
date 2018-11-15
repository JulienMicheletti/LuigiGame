package modele;

import engine.Commande;
import engine.Game;
import modele.elements.Arrive;
import modele.elements.Case;
import modele.elements.Mur;
import modele.elements.Sol;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class LabyrintheGame implements Game {

    private Niveau level;
    private boolean gameWin;
    private int floor;
    private final static int HAUT = 0;
    private final static int BAS = 1;
    private final static int GAUCHE = 2;
    private final static int DROITE = 3;

    public LabyrintheGame(){
        this.gameWin = false;
        this.level = new Niveau();
        genLabyrinth(true);
    }

    public void genLabyrinth(boolean useGenerator){
        TextureFactory.genererCombinaison();
       if (useGenerator){
            this.level.genererNiveau();
        }else{
            this.level.chargerNiveau("Labyrinthe1");
        }
    }

    @Override
    public String toString(){
        return this.level.toString();
    }

    @Override
    public void evolve(Commande cmd) {
        if (gameWon()){
            this.gameWin = true;
            this.incrementFloor();
            new Timer(5000, e -> resetGame()).start();
            this.genLabyrinth(true);
        }
        if (gestionCollision(getMur(), cmd)) {
            if (cmd == Commande.UP) this.level.deplacerHero(-1, 0);
            if (cmd == Commande.DOWN) this.level.deplacerHero(1, 0);
            if (cmd == Commande.LEFT) this.level.deplacerHero(0, -1);
            if (cmd == Commande.RIGHT) this.level.deplacerHero(0, 1);
        }
    }

    private boolean gameWon() {
        int herox = getHeroX();
        int heroy = getHeroY();
        Arrive arrive = this.getArrive();
        Rectangle hero = new Rectangle(heroy, herox, 20, 20);
        return arrive.getRectangle().intersects(hero);
    }

    public boolean gestionCollision(Mur mur, Commande cmd){
        int herox = getHeroX();
        int heroy = getHeroY();
        boolean avancer = true;
        if (cmd == Commande.UP) {
            herox -= 1;
            changerDirection(HAUT);
        } else if (cmd == Commande.DOWN) {
            herox += 1;
            changerDirection(BAS);
        }else if (cmd == Commande.LEFT) {
            heroy -= 1;
            changerDirection(GAUCHE);
        }else if (cmd == Commande.RIGHT) {
            heroy += 1;
            changerDirection(DROITE);
        }
        Rectangle hero = new Rectangle(heroy, herox, 20, 20);
        for (Case c : mur){
            if (c.getRectangle().intersects(hero))
                avancer = false;
        }
        return avancer;
    }

    public int[][] getLabyrinthe(){
        return level.getLabyrinthe();
    }

    public Hero getHero(){ return level.getHero();}
    public Mur getMur(){
        return level.getMur();
    }

    public void changerDirection(int dir){
        level.changerDirection(dir);
    }

    public ArrayList<Sol> getChemin(){return level.getChemin();}

    public Arrive getArrive(){
        return this.level.getArrive();
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

    public boolean getGameWin() {
        return this.gameWin;
    }

    public void resetGame(){
        this.gameWin = false;
    }

    public String getFloor() {
        return this.floor+"";
    }

    public void incrementFloor(){
        this.floor++;
    }
}
