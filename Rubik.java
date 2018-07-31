//hmh16c
//Harrison Hill
//cop3252 game project
//7/30/2018

/*
this isn't REALLY ~700 lines of code.

a huge portion of this is... not copypasted code, but code that mirrors other code,
or repeats with slight differences.

there are probably ways to reduce the line count, but it isn't as bad as it looks.

a huge portion of it is taken up by the paintComponent() section for the cube window,
and by the declarations for 21 different JButtons

The Cube.java file isn't truly 400 lines either, it's almost all taken up by the 
process of switching sticker colors from sticker to sticker as a side moves.
I simplified everything I could by re-using the earlier definitions though.
(Moving counterclockwise is the clockwise function called 3 times in a row, etc)
*/

    //I probably imported way too much stuff, not gonna lie
import java.util.Scanner;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Container;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.Box;
import java.awt.*;

class Rubik
{
    public static void main(String[] args)
    {
        RubikFrame rf = new RubikFrame();
    }
}

class RubikFrame extends JFrame
    {
    	boolean gamewon = false;   //I think this does nothing?

    	boolean gamestarted = false;   //check if we started the game properly(so someone can't make 1 move and "win")
        Cube cube = new Cube(); //create the cube
        CubePanel cp;
        JMenu menu;             //reference variables used to add menus and menu items
        JMenuItem menuitem;

        Color ORANJI = new Color(255,128,0); //deeper orange color than the default Color.ORANGE
        Color ALPHA = new Color(0, 0, 0, 128); //used to make images "transparent"

        JDesktopPane jdp = new JDesktopPane();  //main panel
        JMenuBar jmb = new JMenuBar();          //main menubar

        RubikFrame()
        {
            setTitle("Harrison's Rubik's Cube Game (HRCG)");
            setContentPane(jdp);

            setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            setSize( 800, 800 ); // set frame size
            setVisible( true ); // display frame

            menu = new GameMenu();
            jmb.add(menu);
            setJMenuBar(jmb);

                //split the main window in two
            GridLayout layout = new GridLayout(1, 2);
            setLayout(layout);

                //the left half will show the cube
            cp = new CubePanel();
            cp.setVisible(true);
            jdp.add(cp);

                //the right half will have 3 parts stacked vertically
            JPanel dummypanel = new JPanel();
            dummypanel.setLayout(new GridLayout(3, 1));
            dummypanel.setVisible(true);
            jdp.add(dummypanel);

                //top subsection is the slice panel
            SlicePanel sp = new SlicePanel();
            sp.setVisible(true);
            sp.setLayout(new GridLayout(3, 3));
            dummypanel.add(sp);

                //then we have a label panel
            JPanel evendumberpanel = new JPanel();
            evendumberpanel.setVisible(true);
            dummypanel.add(evendumberpanel);
            evendumberpanel.setLayout(new GridLayout(2, 1));

            JLabel label = new JLabel("Slice Moves / Cube Rotations", SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.PLAIN, 24));
            evendumberpanel.add(label);

            label = new JLabel("Face Moves", SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.PLAIN, 24));
            evendumberpanel.add(label);

                //and at the bottom we have the face move panel
            FacePanel fp = new FacePanel();
            fp.setVisible(true);
            fp.setLayout(new GridLayout(4, 3));
            dummypanel.add(fp);

            jdp.validate();

            JOptionPane.showMessageDialog(jdp, "Go to \"Game Options\" and scramble the cube to start!");
        }

                //this is 110% stolen from the internet, but its just setting variables so i don't really care
        public void makeConstraints(GridBagLayout gbl, JComponent comp, int w, int h, int x, int y,
            double weightx, double weighty) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = w;
        constraints.gridheight = h;
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.weightx = weightx;
        constraints.weighty = weighty;
        gbl.setConstraints(comp, constraints);
    }

        private class GameMenu extends JMenu
        {
            GameMenu()
            {                           //standard menubar stuff
                setLabel("Game Options");
                menuitem = new JMenuItem("Reset Cube");
                menuitem.addActionListener((ActionEvent e) -> {cube.reset();cp.repaint();
                												gamewon = false; gamestarted = false;});
                add(menuitem);

                menuitem = new JMenuItem("Scramble cube / start game");
                menuitem.addActionListener((ActionEvent e) -> {cube.scramble();cp.repaint();
                												gamewon = false; gamestarted = true;
                                JOptionPane.showMessageDialog(jdp, "See if you can solve the cube!\n(There's a guide packaged with this program)");});
                add(menuitem);

                //menuitem = new JMenuItem("test win condition");
                //menuitem.addActionListener((ActionEvent e) -> {gamestarted = true; gamewon = false;});
                //add(menuitem);
            }
        }

            //hold the display of the cube

            //WARNING, THIS IS BRUTE FORCE AS ALL HELL
            //ABANDON HOPE ALL WHO ENTER HERE
        private class CubePanel extends JPanel
        {
            int cubiesize = 20;
            int topcubiex;
            int topcubiey;
            int subcubiex;
            int subcubiey;
            int offset = 20;
            int offsetx = 20;
            int offsety = 20;

            Polygon poly;

            //
//          //Three makepoly functions to create polygons for each "sticker".
            //  we have three different ones so that the stickers are made at different angles
            //  this makes the cube "look 3d"
            //
            private Polygon makepolyu(int x, int y)
            {
                int[] pointsx = {x, x + offset, x, x - offset,};
                int[] pointsy = {y, y + offset, y + offset * 2, y + offset};

                return new Polygon(pointsx, pointsy, pointsx.length);
            }

            private Polygon makepolyr(int x, int y)
            {
            	int[] pointsx = {x, x + offset, x + offset, x};
            	int[] pointsy = {y, y - offset, y, y + offset};

            	return new Polygon(pointsx, pointsy, pointsx.length);
            }

            private Polygon makepolyf(int x, int y)
            {
            	int[] pointsx = {x, x - offset, x - offset, x};
            	int[] pointsy = {y, y - offset, y, y + offset};
            	return new Polygon(pointsx, pointsy, pointsx.length);
            }

            public void paintComponent(Graphics g)
            {

                super.paintComponent(g);
                this.setBackground(Color.BLACK);
                topcubiex = (int) (getSize().getWidth() * 0.5);
                topcubiey = (int) (getSize().getHeight() * 0.25);
                
                //we record where the "top" cubie is, and then we call the other functions

                //each side gets drawn out entirely, with each sticker being the appropriate colors
                paintu(g);

                paintd(g);
                paintdalpha(g); //the alpha functions "grey out" the floating images of the sides we can't see

                paintr(g);

                paintl(g);
                paintlalpha(g);

                paintf(g);

                paintb(g);
                paintbalpha(g);             
            }


            //this is where it gets ugly.
            //there was probably a way to do this cleanly, or at least without doing it pixel by pixel.
            //but this was the easiest for me to understand

            //basically we make a bunch of stickers that are all the same shape.
            //their colors will all be what they are supposed to be.
            //it offsets the stickers by a set amount so that they form one side of the cube.
            private void paintu(Graphics g)
            {

                setcolor(g, cube.u[7]);     //set color for the sticker
                g.fillPolygon(makepolyu(topcubiex, topcubiey)); //draw poly

                setcolor(g, cube.u[8]);
                g.fillPolygon(makepolyu(topcubiex + offsetx + 1, topcubiey + offsety + 1));

                setcolor(g, cube.u[9]);
                g.fillPolygon(makepolyu(topcubiex + offsetx*2 + 2, topcubiey + offsety*2 + 2));

                setcolor(g, cube.u[4]);
                g.fillPolygon(makepolyu(topcubiex - offsetx - 1, topcubiey + offsety + 1));

                setcolor(g, cube.u[5]);
                g.fillPolygon(makepolyu(topcubiex, topcubiey + offsety*2 + 2));

                setcolor(g, cube.u[6]);
                g.fillPolygon(makepolyu(topcubiex + offsetx + 1, topcubiey + offsety*3 + 3));

                setcolor(g, cube.u[1]);
                g.fillPolygon(makepolyu(topcubiex - offsetx*2 - 2, topcubiey + offsety*2 + 2));

                setcolor(g, cube.u[2]);
                g.fillPolygon(makepolyu(topcubiex - offset - 1, topcubiey + offsety*3 + 3));

                setcolor(g, cube.u[3]);
                g.fillPolygon(makepolyu(topcubiex, topcubiey + offsety*4 + 4));
            }

            private void paintd(Graphics g)
            {
                subcubiex = topcubiex;
                subcubiey = topcubiey + 10*offsety;

                setcolor(g, cube.d[1]);     //set color for the cubie
                g.fillPolygon(makepolyu(subcubiex, subcubiey)); //draw poly

                setcolor(g, cube.d[2]);
                g.fillPolygon(makepolyu(subcubiex + offsetx + 1, subcubiey + offsety + 1));

                setcolor(g, cube.d[3]);
                g.fillPolygon(makepolyu(subcubiex + offsetx*2 + 2, subcubiey + offsety*2 + 2));

                setcolor(g, cube.d[4]);
                g.fillPolygon(makepolyu(subcubiex - offsetx - 1, subcubiey + offsety + 1));

                setcolor(g, cube.d[5]);
                g.fillPolygon(makepolyu(subcubiex, subcubiey + offsety*2 + 2));

                setcolor(g, cube.d[6]);
                g.fillPolygon(makepolyu(subcubiex + offsetx + 1, subcubiey + offsety*3 + 3));

                setcolor(g, cube.d[7]);
                g.fillPolygon(makepolyu(subcubiex - offsetx*2 - 2, subcubiey + offsety*2 + 2));

                setcolor(g, cube.d[8]);
                g.fillPolygon(makepolyu(subcubiex - offset - 1, subcubiey + offsety*3 + 3));

                setcolor(g, cube.d[9]);
                g.fillPolygon(makepolyu(subcubiex, subcubiey + offsety*4 + 4));
            }

                //the alpha functions are the same as their non alpha counterparts.
                //the only difference is that they only use one color, a transparent black.
                //basically this just colors over the side we just drew to make it look transparent.
            private void paintdalpha(Graphics g)
            {
                subcubiex = topcubiex;
                subcubiey = topcubiey + 10*offsety;

                g.setColor(ALPHA);
                
                g.fillPolygon(makepolyu(subcubiex, subcubiey)); //draw poly
                g.fillPolygon(makepolyu(subcubiex + offsetx + 1, subcubiey + offsety + 1));
                g.fillPolygon(makepolyu(subcubiex + offsetx*2 + 2, subcubiey + offsety*2 + 2));
                g.fillPolygon(makepolyu(subcubiex - offsetx - 1, subcubiey + offsety + 1));
                g.fillPolygon(makepolyu(subcubiex, subcubiey + offsety*2 + 2));
                g.fillPolygon(makepolyu(subcubiex + offsetx + 1, subcubiey + offsety*3 + 3));
                g.fillPolygon(makepolyu(subcubiex - offsetx*2 - 2, subcubiey + offsety*2 + 2));
                g.fillPolygon(makepolyu(subcubiex - offset - 1, subcubiey + offsety*3 + 3));
                g.fillPolygon(makepolyu(subcubiex, subcubiey + offsety*4 + 4));
            }

            private void paintr(Graphics g)
            {
            	int topleftx = topcubiex + 1;
            	int toplefty = topcubiey + offset*6 + 7;

            	setcolor(g, cube.r[7]);
            	g.fillPolygon(makepolyr(topleftx, toplefty));

            	setcolor(g, cube.r[8]);
            	g.fillPolygon(makepolyr(topleftx + offset + 1, toplefty - offset - 1));

            	setcolor(g, cube.r[9]);
            	g.fillPolygon(makepolyr(topleftx + offset*2 + 2, toplefty - offset*2 - 2));

            	setcolor(g, cube.r[4]);
            	g.fillPolygon(makepolyr(topleftx, toplefty + offset + 1));

            	setcolor(g, cube.r[5]);
            	g.fillPolygon(makepolyr(topleftx + offset + 1, toplefty));

            	setcolor(g, cube.r[6]);
            	g.fillPolygon(makepolyr(topleftx + offset*2 + 2, toplefty - offset - 1));

            	setcolor(g, cube.r[1]);
            	g.fillPolygon(makepolyr(topleftx, toplefty + offset*2 + 2));

            	setcolor(g, cube.r[2]);
            	g.fillPolygon(makepolyr(topleftx + offset + 1, toplefty + offset + 1));

            	setcolor(g, cube.r[3]);
            	g.fillPolygon(makepolyr(topleftx + offset*2 + 2, toplefty));
            }

            private void paintl(Graphics g)
            {
                int topleftx = topcubiex + 1;
                int toplefty = topcubiey + offset*6 + 7;

                subcubiex = topleftx - 6*offset;
                subcubiey = toplefty - 6*offset;

                setcolor(g, cube.l[9]);
                g.fillPolygon(makepolyr(subcubiex, subcubiey));

                setcolor(g, cube.l[8]);
                g.fillPolygon(makepolyr(subcubiex + offset + 1, subcubiey - offset - 1));

                setcolor(g, cube.l[7]);
                g.fillPolygon(makepolyr(subcubiex + offset*2 + 2, subcubiey - offset*2 - 2));

                setcolor(g, cube.l[6]);
                g.fillPolygon(makepolyr(subcubiex, subcubiey + offset + 1));

                setcolor(g, cube.l[5]);
                g.fillPolygon(makepolyr(subcubiex + offset + 1, subcubiey));

                setcolor(g, cube.l[4]);
                g.fillPolygon(makepolyr(subcubiex + offset*2 + 2, subcubiey - offset - 1));

                setcolor(g, cube.l[3]);
                g.fillPolygon(makepolyr(subcubiex, subcubiey + offset*2 + 2));

                setcolor(g, cube.l[2]);
                g.fillPolygon(makepolyr(subcubiex + offset + 1, subcubiey + offset + 1));

                setcolor(g, cube.l[1]);
                g.fillPolygon(makepolyr(subcubiex + offset*2 + 2, subcubiey));
            }

            private void paintlalpha(Graphics g)
            {
                int topleftx = topcubiex + 1;
                int toplefty = topcubiey + offset*6 + 7;

                subcubiex = topleftx - 6*offset;
                subcubiey = toplefty - 6*offset;

                g.setColor(ALPHA);
                
                g.fillPolygon(makepolyr(subcubiex, subcubiey));
                g.fillPolygon(makepolyr(subcubiex + offset + 1, subcubiey - offset - 1));
                g.fillPolygon(makepolyr(subcubiex + offset*2 + 2, subcubiey - offset*2 - 2));
                g.fillPolygon(makepolyr(subcubiex, subcubiey + offset + 1));
                g.fillPolygon(makepolyr(subcubiex + offset + 1, subcubiey));
                g.fillPolygon(makepolyr(subcubiex + offset*2 + 2, subcubiey - offset - 1));
                g.fillPolygon(makepolyr(subcubiex, subcubiey + offset*2 + 2));
                g.fillPolygon(makepolyr(subcubiex + offset + 1, subcubiey + offset + 1));
                g.fillPolygon(makepolyr(subcubiex + offset*2 + 2, subcubiey));
            }

            private void paintf(Graphics g)
            {
                int topleftx = topcubiex + 1;
            	int toplefty = topcubiey + offset*6 + 7;

            	setcolor(g, cube.f[9]);
            	g.fillPolygon(makepolyf(topleftx, toplefty));

            	setcolor(g, cube.f[8]);
            	g.fillPolygon(makepolyf(topleftx - offset - 1, toplefty - offset - 1));

            	setcolor(g, cube.f[7]);
            	g.fillPolygon(makepolyf(topleftx - offset*2 - 2, toplefty - offset*2 - 2));

            	setcolor(g, cube.f[6]);
            	g.fillPolygon(makepolyf(topleftx, toplefty + offset + 1));

            	setcolor(g, cube.f[5]);
            	g.fillPolygon(makepolyf(topleftx - offset - 1, toplefty));

            	setcolor(g, cube.f[4]);
            	g.fillPolygon(makepolyf(topleftx - offset*2 - 2, toplefty - offset - 1));

            	setcolor(g, cube.f[3]);
            	g.fillPolygon(makepolyf(topleftx, toplefty + offset*2 + 2));

            	setcolor(g, cube.f[2]);
            	g.fillPolygon(makepolyf(topleftx - offset - 1, toplefty + offset + 1));

            	setcolor(g, cube.f[1]);
            	g.fillPolygon(makepolyf(topleftx - offset*2 - 2, toplefty));

                
            }

            private void paintb(Graphics g)
            {
                int topleftx = topcubiex + 1;
                int toplefty = topcubiey + offset*6 + 7;

                subcubiex = topleftx + 6*offset;
                subcubiey = toplefty - 6*offset;

                setcolor(g, cube.b[7]);
                g.fillPolygon(makepolyf(subcubiex, subcubiey));

                setcolor(g, cube.b[8]);
                g.fillPolygon(makepolyf(subcubiex - offset - 1, subcubiey - offset - 1));

                setcolor(g, cube.b[9]);
                g.fillPolygon(makepolyf(subcubiex - offset*2 - 2, subcubiey - offset*2 - 2));

                setcolor(g, cube.b[4]);
                g.fillPolygon(makepolyf(subcubiex, subcubiey + offset + 1));

                setcolor(g, cube.b[5]);
                g.fillPolygon(makepolyf(subcubiex - offset - 1, subcubiey));

                setcolor(g, cube.b[6]);
                g.fillPolygon(makepolyf(subcubiex - offset*2 - 2, subcubiey - offset - 1));

                setcolor(g, cube.b[1]);
                g.fillPolygon(makepolyf(subcubiex, subcubiey + offset*2 + 2));

                setcolor(g, cube.b[2]);
                g.fillPolygon(makepolyf(subcubiex - offset - 1, subcubiey + offset + 1));

                setcolor(g, cube.b[3]);
                g.fillPolygon(makepolyf(subcubiex - offset*2 - 2, subcubiey));
            }

            private void paintbalpha(Graphics g)
            {
                int topleftx = topcubiex + 1;
                int toplefty = topcubiey + offset*6 + 7;

                subcubiex = topleftx + 6*offset;
                subcubiey = toplefty - 6*offset;

                g.setColor(ALPHA);
                g.fillPolygon(makepolyf(subcubiex, subcubiey));
                g.fillPolygon(makepolyf(subcubiex - offset - 1, subcubiey - offset - 1));
                g.fillPolygon(makepolyf(subcubiex - offset*2 - 2, subcubiey - offset*2 - 2));
                g.fillPolygon(makepolyf(subcubiex, subcubiey + offset + 1));
                g.fillPolygon(makepolyf(subcubiex - offset - 1, subcubiey));
                g.fillPolygon(makepolyf(subcubiex - offset*2 - 2, subcubiey - offset - 1));
                g.fillPolygon(makepolyf(subcubiex, subcubiey + offset*2 + 2));
                g.fillPolygon(makepolyf(subcubiex - offset - 1, subcubiey + offset + 1));
                g.fillPolygon(makepolyf(subcubiex - offset*2 - 2, subcubiey));
            }

                //sets color of g to color that 'c' represents
            private void setcolor(Graphics g, char c)
            {
                if(c == 'y') {g.setColor(Color.YELLOW); return;}
                if(c == 'w') {g.setColor(Color.WHITE); return;}
                if(c == 'r') {g.setColor(Color.RED); return;}
                if(c == 'o') {g.setColor(ORANJI); return;}
                if(c == 'g') {g.setColor(Color.GREEN); return;}
                if(c == 'b') {g.setColor(Color.BLUE); return;}
            }
        }

        ///////////////////////////////////////////////
        //FINALLY DONE WITH PAINTCOMPONENT, HOLY MOLY//
        ///////////////////////////////////////////////

            //to hold the buttons for regular face moves
        private class FacePanel extends JPanel
        {
            JButton FaceButton;
            FacePanel()
            {
                //12 buttons in here.
                //  not much else.

                FaceButton = new JButton("R");
                FaceButton.setFont(new Font("Arial", Font.PLAIN, 24));
                FaceButton.addActionListener((ActionEvent e) -> {cube.r();cp.repaint();checkwin();});
                add(FaceButton);

                FaceButton = new JButton("U");
                FaceButton.setFont(new Font("Arial", Font.PLAIN, 24));
                FaceButton.addActionListener((ActionEvent e) -> {cube.u();cp.repaint();checkwin();});
                add(FaceButton);

                FaceButton = new JButton("F");
                FaceButton.setFont(new Font("Arial", Font.PLAIN, 24));
                FaceButton.addActionListener((ActionEvent e) -> {cube.f();cp.repaint();checkwin();});
                add(FaceButton);

                FaceButton = new JButton("L");
                FaceButton.setFont(new Font("Arial", Font.PLAIN, 24));
                FaceButton.addActionListener((ActionEvent e) -> {cube.l();cp.repaint();checkwin();});
                add(FaceButton);

                FaceButton = new JButton("D");
                FaceButton.setFont(new Font("Arial", Font.PLAIN, 24));
                FaceButton.addActionListener((ActionEvent e) -> {cube.d();cp.repaint();checkwin();});
                add(FaceButton);

                FaceButton = new JButton("B");
                FaceButton.setFont(new Font("Arial", Font.PLAIN, 24));
                FaceButton.addActionListener((ActionEvent e) -> {cube.b();cp.repaint();checkwin();});
                add(FaceButton);

                FaceButton = new JButton("Ri");
                FaceButton.setFont(new Font("Arial", Font.PLAIN, 24));
                FaceButton.addActionListener((ActionEvent e) -> {cube.ri();cp.repaint();checkwin();});
                add(FaceButton);

                FaceButton = new JButton("Ui");
                FaceButton.setFont(new Font("Arial", Font.PLAIN, 24));
                FaceButton.addActionListener((ActionEvent e) -> {cube.ui();cp.repaint();checkwin();});
                add(FaceButton);

                FaceButton = new JButton("Fi");
                FaceButton.setFont(new Font("Arial", Font.PLAIN, 24));
                FaceButton.addActionListener((ActionEvent e) -> {cube.fi();cp.repaint();checkwin();});
                add(FaceButton);

                FaceButton = new JButton("Li");
                FaceButton.setFont(new Font("Arial", Font.PLAIN, 24));
                FaceButton.addActionListener((ActionEvent e) -> {cube.li();cp.repaint();checkwin();});
                add(FaceButton);

                FaceButton = new JButton("Di");
                FaceButton.setFont(new Font("Arial", Font.PLAIN, 24));
                FaceButton.addActionListener((ActionEvent e) -> {cube.di();cp.repaint();checkwin();});
                add(FaceButton);

                FaceButton = new JButton("Bi");
                FaceButton.setFont(new Font("Arial", Font.PLAIN, 24));
                FaceButton.addActionListener((ActionEvent e) -> {cube.bi();cp.repaint();checkwin();});
                add(FaceButton);
            }
        }
            //to hold the buttons for slice moves
        private class SlicePanel extends JPanel
        {
            JButton FaceButton;
            SlicePanel()
            {
                    //more buttons.
                        //9 this time

                FaceButton = new JButton("M");
                FaceButton.setFont(new Font("Arial", Font.PLAIN, 24));
                FaceButton.addActionListener((ActionEvent e) -> {cube.m();cp.repaint();checkwin();});
                add(FaceButton);

                FaceButton = new JButton("E");
                FaceButton.setFont(new Font("Arial", Font.PLAIN, 24));
                FaceButton.addActionListener((ActionEvent e) -> {cube.e();cp.repaint();checkwin();});
                add(FaceButton);

                FaceButton = new JButton("S");
                FaceButton.setFont(new Font("Arial", Font.PLAIN, 24));
                FaceButton.addActionListener((ActionEvent e) -> {cube.s();cp.repaint();checkwin();});
                add(FaceButton);

                FaceButton = new JButton("Mi");
                FaceButton.setFont(new Font("Arial", Font.PLAIN, 24));
                FaceButton.addActionListener((ActionEvent e) -> {cube.mi();cp.repaint();checkwin();});
                add(FaceButton);

                FaceButton = new JButton("Ei");
                FaceButton.setFont(new Font("Arial", Font.PLAIN, 24));
                FaceButton.addActionListener((ActionEvent e) -> {cube.ei();cp.repaint();checkwin();});
                add(FaceButton);

                FaceButton = new JButton("Si");
                FaceButton.setFont(new Font("Arial", Font.PLAIN, 24));
                FaceButton.addActionListener((ActionEvent e) -> {cube.si();cp.repaint();checkwin();});
                add(FaceButton);

                FaceButton = new JButton("X");
                FaceButton.setFont(new Font("Arial", Font.PLAIN, 24));
                FaceButton.addActionListener((ActionEvent e) -> {cube.x();cp.repaint();checkwin();});
                add(FaceButton);

                FaceButton = new JButton("Y");
                FaceButton.setFont(new Font("Arial", Font.PLAIN, 24));
                FaceButton.addActionListener((ActionEvent e) -> {cube.y();cp.repaint();checkwin();});
                add(FaceButton);

                FaceButton = new JButton("Z");
                FaceButton.setFont(new Font("Arial", Font.PLAIN, 24));
                FaceButton.addActionListener((ActionEvent e) -> {cube.z();cp.repaint();checkwin();});
                add(FaceButton);
            }
        }

            //this is how we check for the win condition.
                //just checks to see if every side is made up of only one color
            
            //in case you don't know, the centers never move relative to each other, 
                //so we can assume that the centers are always correct
        private void checkwin()
        {
            if(gamestarted == false) return;

            char c;                 //buncha ifs to see if the stickers on a side match the center sticker
            c = cube.u[5];
            for (int i = 1; i < 10; i++)
                if (c != cube.u[i]) return;

            c = cube.d[5];
            for (int i = 1; i < 10; i++)
                if (c != cube.d[i]) return;

            c = cube.r[5];
            for (int i = 1; i < 10; i++)
                if (c != cube.r[i]) return;

            c = cube.l[5];
            for (int i = 1; i < 10; i++)
                if (c != cube.l[i]) return;

            c = cube.f[5];
            for (int i = 1; i < 10; i++)
                if (c != cube.f[i]) return;

            c = cube.b[5];
            for (int i = 1; i < 10; i++)
                if (c != cube.b[i]) return;

                //sho a dialog box and set some flags if we won
            JOptionPane.showMessageDialog(jdp, "Congratulations!  You won!");
            gamestarted = false;
            gamewon = true;
        }
    }