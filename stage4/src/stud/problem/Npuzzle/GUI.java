package stud.problem.Npuzzle;

import core.solver.queue.Node;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Deque;

/**
 * Description:
 *
 * @date:2022/10/17 16:12
 * @author:Karthus77
 */
public class GUI extends JFrame {
    private int size;//棋盘大小
    private Board board;
    private final int scale=150;
    public GUI(Deque<Node> path, int s) {
        this.size = s;
        board=new Board(size,path);
        this.setTitle("G03N-puzzle问题");
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        int edge=size*scale;//边长
        this.setBounds((width - edge) / 2, (height - edge) / 2, edge+20, edge+20);//显示在屏幕正中间
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.add(board);
        this.setVisible(true);
    }
}
