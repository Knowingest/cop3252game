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
        Cube cube = new Cube();
        CubePanel cp;
        JMenu menu;             //reference variables used to add menus and menu items
        JMenuItem menuitem;

        JDesktopPane jdp = new JDesktopPane();  //main panel
        JMenuBar jmb = new JMenuBar();          //main menubar

        RubikFrame()
        {
            setTitle("Harrison's Rubik's Cube Game (HRCG)");
            setContentPane(jdp);

            setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            setSize( 800, 600 ); // set frame size
            setVisible( true ); // display frame

            menu = new GameMenu();
            jmb.add(menu);
            setJMenuBar(jmb);

            //Container container = getContentPane();
                    //dafuq
            GridLayout layout = new GridLayout(2, 2);
            setLayout(layout);

            cp = new CubePanel();
            cp.setVisible(true);
            jdp.add(cp);

            SlicePanel sp = new SlicePanel();
            sp.setVisible(true);
            sp.setLayout(new GridLayout(2, 3));
            jdp.add(sp);

            FacePanel fp = new FacePanel();
            fp.setVisible(true);
            fp.setLayout(new GridLayout(2, 3));
            jdp.add(fp);

            PrimePanel pp = new PrimePanel();
            pp.setVisible(true);
            pp.setLayout(new GridLayout(2, 3));
            jdp.add(pp);

            jdp.validate();
        }

        private class GameMenu extends JMenu
        {
            GameMenu()
            {
                setLabel("Game Options");
                menuitem = new JMenuItem("Reset Cube");
                menuitem.addActionListener((ActionEvent e) -> {cube.reset();cp.repaint();});
                add(menuitem);

                menuitem = new JMenuItem("Scramble cube / Start timer");
                menuitem.addActionListener((ActionEvent e) -> {cube.reset();});
                add(menuitem);
            }
        }

            //to hold the display of the cube
        private class CubePanel extends JPanel
        {
            int cubiesize = 20;
            int topcubiex;
            int topcubiey;
            int offset = 20;
            int offsetx = 20;
            int offsety = 20;

            Polygon poly;

            private Polygon makepolyu(int x, int y)
            {
                int[] pointsx = {x, x + offset, x, x - offset,};
                int[] pointsy = {y, y + offset, y + offset * 2, y + offset};

                return new Polygon(pointsx, pointsy, pointsx.length);
            }

            public void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                this.setBackground(Color.BLACK);
                topcubiex = (int) (getSize().getWidth() * 0.5);
                topcubiey = (int) (getSize().getHeight() * 0.1);
                
                paintu(g);
                paintr(g);
                paintf(g);
                
            }

            private void paintu(Graphics g)
            {
                                                //template is as follows
                setcolor(g, cube.u[7]);     //set color for the cubie
                //g.fillRect(topcubiex, topcubiey,    //(x,y)
                //    cubiesize, cubiesize);          //height and width
                g.fillPolygon(makepolyu(topcubiex, topcubiey));

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


                /*setcolor(g, cube.u[8]);
                g.fillRect(topcubiex + offset, topcubiey + offset,
                    cubiesize, cubiesize);

                setcolor(g, cube.u[4]);
                g.fillRect(topcubiex - offset, topcubiey + offset,
                    cubiesize, cubiesize);

                setcolor(g, cube.u[5]);
                g.fillOval(topcubiex, topcubiey + offset * 2,
                    cubiesize, cubiesize);

                setcolor(g, cube.u[1]);
                g.fillRect(topcubiex - offset * 2, topcubiey + offset * 2,
                    cubiesize, cubiesize);

                setcolor(g, cube.u[9]);
                g.fillRect(topcubiex + offset * 2, topcubiey + offset * 2,
                    cubiesize, cubiesize);

                setcolor(g, cube.u[2]);
                g.fillRect(topcubiex - offset, topcubiey + offset * 3,
                    cubiesize, cubiesize);

                setcolor(g, cube.u[6]);
                g.fillRect(topcubiex + offset , topcubiey + offset * 3,
                    cubiesize, cubiesize);

                setcolor(g, cube.u[3]);
                g.fillRect(topcubiex, topcubiey + offset * 4,
                    cubiesize, cubiesize);*/
            }

            private void paintr(Graphics g)
            {
                int maincubiex = topcubiex + offset / 2;
                int maincubiey = topcubiey + offset * 5;

                setcolor(g, cube.r[7]);
                g.fillRect(maincubiex, maincubiey,
                    cubiesize, cubiesize);

                setcolor(g, cube.r[8]);
                g.fillRect(maincubiex + offset, maincubiey - offset,
                    cubiesize, cubiesize);

                setcolor(g, cube.r[9]);
                g.fillRect(maincubiex + offset * 2, maincubiey - offset * 2,
                    cubiesize, cubiesize);

                setcolor(g, cube.r[4]);
                g.fillRect(maincubiex, maincubiey + offset,
                    cubiesize, cubiesize);

                setcolor(g, cube.r[5]);
                g.fillOval(maincubiex + offset, maincubiey,
                    cubiesize, cubiesize);

                setcolor(g, cube.r[6]);
                g.fillRect(maincubiex + offset * 2, maincubiey - offset,
                    cubiesize, cubiesize);

                setcolor(g, cube.r[1]);
                g.fillRect(maincubiex, maincubiey + offset * 2,
                    cubiesize, cubiesize);

                setcolor(g, cube.r[2]);
                g.fillRect(maincubiex + offset, maincubiey + offset,
                    cubiesize, cubiesize);

                setcolor(g, cube.r[3]);
                g.fillRect(maincubiex + offset * 2, maincubiey,
                    cubiesize, cubiesize);

            }

            private void paintf(Graphics g)
            {
                int maincubiex = topcubiex - offset / 2;
                int maincubiey = topcubiey + offset * 5;

                setcolor(g, cube.f[9]);
                g.fillRect(maincubiex, maincubiey,
                    cubiesize, cubiesize);

                setcolor(g, cube.f[8]);
                g.fillRect(maincubiex - offset, maincubiey - offset,
                    cubiesize, cubiesize);

                setcolor(g, cube.f[7]);
                g.fillRect(maincubiex - offset * 2, maincubiey - offset * 2,
                    cubiesize, cubiesize);

                setcolor(g, cube.f[6]);
                g.fillRect(maincubiex, maincubiey + offset,
                    cubiesize, cubiesize);

                setcolor(g, cube.f[5]);
                g.fillOval(maincubiex - offset, maincubiey,
                    cubiesize, cubiesize);

                setcolor(g, cube.f[4]);
                g.fillRect(maincubiex - offset * 2, maincubiey - offset,
                    cubiesize, cubiesize);

                setcolor(g, cube.f[3]);
                g.fillRect(maincubiex, maincubiey + offset * 2,
                    cubiesize, cubiesize);

                setcolor(g, cube.f[2]);
                g.fillRect(maincubiex - offset, maincubiey + offset,
                    cubiesize, cubiesize);

                setcolor(g, cube.f[1]);
                g.fillRect(maincubiex - offset * 2, maincubiey,
                    cubiesize, cubiesize);
            }

                //sets color of g to color that 'c' represents
            private void setcolor(Graphics g, char c)
            {
                if(c == 'y') {g.setColor(Color.YELLOW); return;}
                if(c == 'w') {g.setColor(Color.WHITE); return;}
                if(c == 'r') {g.setColor(Color.RED); return;}
                if(c == 'o') {g.setColor(Color.ORANGE); return;}
                if(c == 'g') {g.setColor(Color.GREEN); return;}
                if(c == 'b') {g.setColor(Color.BLUE); return;}
            }
        }

            //to hold the buttons for regular face moves
        private class FacePanel extends JPanel
        {
            JButton FaceButton;
            FacePanel()
            {
                FaceButton = new JButton("r");
                FaceButton.addActionListener((ActionEvent e) -> {cube.r();cp.repaint();});
                add(FaceButton);

                FaceButton = new JButton("u");
                FaceButton.addActionListener((ActionEvent e) -> {cube.u();cp.repaint();});
                add(FaceButton);

                FaceButton = new JButton("f");
                FaceButton.addActionListener((ActionEvent e) -> {cube.f();cp.repaint();});
                add(FaceButton);

                FaceButton = new JButton("l");
                FaceButton.addActionListener((ActionEvent e) -> {cube.l();cp.repaint();});
                add(FaceButton);

                FaceButton = new JButton("d");
                FaceButton.addActionListener((ActionEvent e) -> {cube.d();cp.repaint();});
                add(FaceButton);

                FaceButton = new JButton("b");
                FaceButton.addActionListener((ActionEvent e) -> {cube.b();cp.repaint();});
                add(FaceButton);
            }
        }

            //to hold the buttons for prime moves
        private class PrimePanel extends JPanel
        {
            JButton FaceButton;
            PrimePanel()
            {
                FaceButton = new JButton("ri");
                FaceButton.addActionListener((ActionEvent e) -> {cube.ri();cp.repaint();});
                add(FaceButton);

                FaceButton = new JButton("ui");
                FaceButton.addActionListener((ActionEvent e) -> {cube.ui();cp.repaint();});
                add(FaceButton);

                FaceButton = new JButton("fi");
                FaceButton.addActionListener((ActionEvent e) -> {cube.fi();cp.repaint();});
                add(FaceButton);

                FaceButton = new JButton("li");
                FaceButton.addActionListener((ActionEvent e) -> {cube.li();cp.repaint();});
                add(FaceButton);

                FaceButton = new JButton("di");
                FaceButton.addActionListener((ActionEvent e) -> {cube.di();cp.repaint();});
                add(FaceButton);

                FaceButton = new JButton("bi");
                FaceButton.addActionListener((ActionEvent e) -> {cube.bi();cp.repaint();});
                add(FaceButton);
            }
        }

            //to hold the buttons for slice moves
        private class SlicePanel extends JPanel
        {
            JButton FaceButton;
            SlicePanel()
            {
                FaceButton = new JButton("m");
                FaceButton.addActionListener((ActionEvent e) -> {cube.m();cp.repaint();});
                add(FaceButton);

                FaceButton = new JButton("e");
                FaceButton.addActionListener((ActionEvent e) -> {cube.e();cp.repaint();});
                add(FaceButton);

                FaceButton = new JButton("s");
                FaceButton.addActionListener((ActionEvent e) -> {cube.s();cp.repaint();});
                add(FaceButton);

                FaceButton = new JButton("mi");
                FaceButton.addActionListener((ActionEvent e) -> {cube.mi();cp.repaint();});
                add(FaceButton);

                FaceButton = new JButton("ei");
                FaceButton.addActionListener((ActionEvent e) -> {cube.ei();cp.repaint();});
                add(FaceButton);

                FaceButton = new JButton("si");
                FaceButton.addActionListener((ActionEvent e) -> {cube.si();cp.repaint();});
                add(FaceButton);
            }
        }
    }