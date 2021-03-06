package modele;

import exception.ExceptionTailleLaby;
import modele.elements.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Un Niveau du jeu
 */
public class Niveau {

    /**
     * Generateur de labyrinthe
     */
    private LabyGenerator lg;

    /**
     * Le labyrinthe
     */
    private Labyrinthe labyrinthe;

    /**
     * Le heros
     */
    private Hero hero;

    /**
     * Liste des monstres
     */
    private final ArrayList<Monstre> monstres;

    /**
     * Difficulte des monstres
     */
    private int difficulte;

    /**
     * Constructeur de Niveau
     */
    public Niveau(){
        this.labyrinthe = new Labyrinthe();
        this.hero = new Hero();
        this.monstres = new ArrayList<>();
        this.lg = new LabyGenerator(15,15);
        this.difficulte = 1;
    }

    /**
     * Kill monstres.
     *
     * @param toKill the to kill
     */
    public void killMonstres(ArrayList<Monstre> toKill){
        this.monstres.removeAll(toKill);
    }

    /**
     * Hero sprint boolean.
     *
     * @return the boolean
     */
    public boolean heroSprint(){
        return hero.canSprint();
    }

    /**
     * Sprint handler.
     *
     * @param isSprinting the is sprinting
     */
    public void sprintHandler(boolean isSprinting){
        hero.handleStamina(isSprinting);
    }

    /**
     * Get stamina int.
     *
     * @return the int
     */
    public int getStamina(){
        return this.hero.getStamina();
    }

    /**
     * Get labyrinthe int [ ] [ ].
     *
     * @return the int [ ] [ ]
     */
    public int[][] getLabyrinthe(){
        return labyrinthe.getLabyrinthe();
    }

    /**
     * Set la difficulte
     * @param diff nouvel difficulte
     */
    public void setDifficulte(int diff){
        this.difficulte = diff;
    }

    /**
     * Generer niveau.
     */
    public void genererNiveau() {
        this.setDifficulte(2);
        this.labyrinthe.setLabyrinthe(this.lg.generate());
        setPlayerX(labyrinthe.getHeroposX());
        setPlayerY(labyrinthe.getHeroposY());
    }

    /**
     * Retourne la coordonnée X du hero
     *
     * @return coordonnée x
     */
    public int getPlayerX(){return this.hero.getX();}

    /**
     * Retourne la coordonnée Y du hero
     *
     * @return coordonnée y
     */
    public int getPlayerY(){return this.hero.getY();}

    /**
     * Set player x.
     *
     * @param x the x
     */
    public void setPlayerX(int x){
        hero.setX(x);
    }

    /**
     * Set player y.
     *
     * @param y the y
     */
    public void setPlayerY(int y){
        hero.setY(y);
    }

    /**
     * Permet de charger le niveau souhaité
     *
     * @param file informations nécessaire au niveau
     */
    public void chargerNiveau(String file){
        int hauteur = 0;
        String line;
        String strArray[];
        try {
            BufferedReader br = new BufferedReader (new InputStreamReader(getClass().getResourceAsStream("/res/" + file)));
            String dimensions[] = br.readLine().split(",");
            int labyrinthe[][] = new int[Integer.parseInt(dimensions[0])][Integer.parseInt(dimensions[1])];
            Scanner scanner = new Scanner(new InputStreamReader(getClass().getResourceAsStream("/res/" + file)));
            while (hauteur < labyrinthe.length+1) {
                line = scanner.nextLine();
                strArray = line.split(",");
                if (hauteur > 0) {
                    if (strArray.length != labyrinthe[0].length){
                        throw new ExceptionTailleLaby();
                    }
                    for (int i = 0; i < strArray.length; i++) {
                        labyrinthe[hauteur - 1][i] = Integer.parseInt(strArray[i]);
                    }
                }
                hauteur++;
            }
            this.labyrinthe.setLabyrinthe(labyrinthe);
            setPlayerX(this.labyrinthe.getHeroposX());
            setPlayerY(this.labyrinthe.getHeroposY());
        }
        catch (NullPointerException ex){
            System.out.println("Le fichier n'existe pas");
            System.exit(0);
        }
        catch (FileNotFoundException exception){
            System.out.println("Le fichier n'existe pas");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (ExceptionTailleLaby exceptionTailleLaby) {
            System.out.println(exceptionTailleLaby.getMessage());
            System.exit(0);
        } catch (NumberFormatException numberException){
            System.out.println("Fichier corrompu");
            System.exit(0);
        }
    }

    /**
     * Deplace le héro dans le labyrinthe
     *
     * @param x deplacement en X du héro
     * @param y deplacement en Y du héro
     */
    public void deplacerHero(int x, int y){
        if (!hero.isDead()){
        this.hero.setX(this.getPlayerX() + x);
        this.hero.setY(this.getPlayerY() + y);
        }
    }

    /**
     * Get hero hero.
     *
     * @return the hero
     */
    public Hero getHero(){ return hero; }

    /**
     * Gets monstres.
     *
     * @return the monstres
     */
    public ArrayList<Monstre> getMonstres() {
        return monstres;
    }

    /**
     * Ajouter monstre.
     *
     * @param x      the x
     * @param y      the y
     * @param nocoll the nocoll
     */
    public void ajouterMonstre(int x, int y,boolean nocoll){
        this.monstres.add(new Monstre(this,x,y, nocoll));
    }

    /**
     * Poser monstres.
     */
    public void poserMonstres(){
        this.monstres.clear();
        int[][] lab = this.getLabyrinthe();
        int nbmonstre = 0;
        boolean needboo = true;
        int nbboo = 0;
        Random rng = new Random();
        while(nbmonstre < 2*difficulte){
            int x = rng.nextInt(lab[1].length);
            int y = rng.nextInt(lab.length);
            if (heroNear(x,y,lab)) {
                if (lab[x][y] == 0) {
                    int type = rng.nextInt(1);
                    if (type == 0) {
                        this.ajouterMonstre(x * 32, y * 32, needboo);
                        if (needboo)
                            if (nbboo == difficulte-1)
                                needboo = false;
                            else
                                nbboo++;
                        nbmonstre++;
                    }
                }
            }
        }
    }

    /**
     * Hero near boolean.
     *
     * @param x    the x
     * @param y    the y
     * @param grid the grid
     * @return the boolean
     */
    public boolean heroNear(int x, int y, int[][] grid){
        int cpt = 0;
        if (x != 0 && grid[x-1][y] == 2) cpt++;
        if (x != grid.length-1 && grid[x+1][y] == 2) cpt++;
        if (y != 0 && grid[x][y-1] == 2) cpt++;
        if (y != grid.length-1 && grid[x][y+1] == 2) cpt++;
        return cpt != 3;
    }

    /**
     * Dammage hero.
     */
    public void dammageHero(){
        this.hero.enleverPv();
    }

    /**
     * Unset invincible hero.
     */
    public void unsetInvincibleHero(){
        this.hero.noInvincible();
    }

    /**
     * Changer direction.
     *
     * @param dir the dir
     */
    public void changerDirection(int dir){ hero.changerDirection(dir);}

    /**
     * Get mur mur.
     *
     * @return the mur
     */
    public Mur getMur(){
        return labyrinthe.getMurs();
    }

    /**
     * Get arrive arrive.
     *
     * @return the arrive
     */
    public Arrive getArrive(){return labyrinthe.getArrive();}

    /**
     * Hero attaque.
     *
     * @param anim the anim
     */
    public void heroAttaque(int anim) { this.hero.attaqueAnimation(anim);}

    /**
     * Get cases speciales array list.
     *
     * @return the array list
     */
    public ArrayList<Case> getCasesSpeciales(){
        return this.labyrinthe.getCasesSpeciales();
    }

    /**
     * Get chemin array list.
     *
     * @return the array list
     */
    public ArrayList<Sol> getChemin(){return labyrinthe.getChemin();}

    /**
     * Print le labyrinthe, les joueurs en string
     */
    @Override
    public String toString(){
        int[][] labyrinthe = this.labyrinthe.getLabyrinthe();
        int playerX = this.getPlayerX();
        int playerY = this.getPlayerY();
        System.out.println("Hero("+playerX+","+playerY+")");
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        for (int i = 0; i<labyrinthe.length;i++){
            sb.append("[");
            for (int j = 0; j<labyrinthe[0].length; j++){
                if (playerX == i && playerY == j){
                    sb.append("H,");
                }else{
                    sb.append(labyrinthe[i][j]).append(",");
                }
            }
            sb.setLength(sb.length() - 1);
            sb.append("]\n");
        }
        return sb.toString();
    }
}
