package stud.problem.Npuzzle;

import javax.swing.*;
import java.awt.*;

/**
 * Description:
 *
 * @date:2022/10/17 16:12
 * @author:Karthus77
 */
public class Tile extends JPanel {
    private JLabel number;
    private final int scale=150;
    Tile(String num)
    {
        number =new JLabel();
        number.setHorizontalAlignment(JLabel.CENTER);
        Font font =new Font("微软雅黑",Font.BOLD,30);
        number.setFont(font);
        number.setText(num);
        this.setLayout(new GridLayout(1,1));
        this.add(number);
        this.setSize(new Dimension(scale,scale));
    }
}
