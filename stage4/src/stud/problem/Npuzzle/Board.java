package stud.problem.Npuzzle;

import core.solver.queue.Node;

import javax.swing.*;
import java.awt.*;
import java.util.Deque;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 *
 * @date:2022/10/17 16:44
 * @author:Karthus77
 */
public class Board extends JPanel {
    private final Tile[] tiles=new Tile[15];
    private final int size;
    private int[][] state;
    private int T;
    private int Tx;
    private int Ty;

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    private int row;
    private int col;
    private Move move;
    public Board(int size,Deque<Node> path)
    {
        this.size=size;
        init();
        T=0;
        this.setSize(new Dimension(size*150,size*150));
        this.setLayout(null);
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                for(Node node:path)
                {
                    if(node.equals(path.getFirst()))
                    {
                        state=((Position) (node.getState())).getPuzzle();
                        setRow(((Position) (node.getState())).getRow());
                        setCol(((Position) (node.getState())).getCol());
                        repaint();
                    }
                    else {
                        move = (Move) (node.getAction());
                        int[] offset = Direction.offset(move.getDirection());//偏移
                        state = ((Position) (node.getState())).getPuzzle();//下一个状态
                        T = state[row][col];
                        int x=row+offset[1];
                        int y=col+offset[0];
                        Tx = getX(x * size + y);
                        Ty = getY(x * size + y);
                        for (int i = 0; i < 10; i++) {
                            try {
                                Thread.sleep(10);
                                Tx -= offset[0] * 15;
                                Ty -= offset[1] * 15;
                                repaint();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            }
        });
        thread.start();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(new Color(248, 246, 246));
        g.setFont(new Font("微软雅黑",Font.BOLD,30));
        this.removeAll();
        for(int i=0;i<size;i++)
        {
            for(int j=0;j<size;j++)
            {
                int count = i * size + j;
                int number = state[i][j];
                if(number!=0&&number!=T) {
                    tiles[number-1].setLocation(getX(count), getY(count));
                    this.add(tiles[number-1]);
                }
                else if(number==T&&T!=0)
                {
                    tiles[number-1].setLocation(Tx,Ty);
                    this.add(tiles[number-1]);
                }
                else
                {
                    setRow(i);
                    setCol(j);
                }
            }
        }
    }
    private int getX(int i)
    {
        return (i%size)*150;
    }
    private int getY(int i)
    {
        return (i/size)*150;
    }
    private void init()
    {
        for(int i=0;i<size*size-1;i++)
        {
            tiles[i]=new Tile(String.valueOf(i+1));
        }
    }
}
