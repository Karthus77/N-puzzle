package stud.problem.Npuzzle;

import java.util.EnumMap;
import java.util.List;

/**
 * Description:
 *
 * @date:2022/10/3 19:12
 * @author:Karthus77
 */
public enum Direction {
    U('↑'),  //上
    R('→'),  //右
    D('↓'),  //下
    L('←');  //左
    /**
     * 构造函数
     * @param symbol 枚举项的代表符号--箭头
     */
    Direction(char symbol){
        this.symbol = symbol;
    }
    private final char symbol;
    public char symbol(){
        return symbol;
    }
    public static final int SCALE = 1;       //单元格的长度
    public static final List<stud.problem.Npuzzle.Direction> FOUR_DIRECTIONS = List.of(stud.problem.Npuzzle.Direction.values());
    public static int cost(stud.problem.pathfinding.Direction dir){
        return SCALE;
    }
    //各个方向移动的坐标位移量
    private static final EnumMap<stud.problem.Npuzzle.Direction, int[]> DIRECTION_OFFSET = new EnumMap<>(stud.problem.Npuzzle.Direction.class);
    static{
        //列号（或横坐标）增加量；行号（或纵坐标）增加量
        DIRECTION_OFFSET.put(U, new int[]{0, -1});
        DIRECTION_OFFSET.put(R, new int[]{1, 0});
        DIRECTION_OFFSET.put(D, new int[]{0, 1});
        DIRECTION_OFFSET.put(L, new int[]{-1, 0});
    }
    public static int[] offset(stud.problem.Npuzzle.Direction dir){
        return DIRECTION_OFFSET.get(dir);
    }
}