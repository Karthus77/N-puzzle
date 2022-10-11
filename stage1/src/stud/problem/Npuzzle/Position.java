package stud.problem.Npuzzle;

import core.problem.Action;
import core.problem.State;
import core.solver.algorithm.heuristic.HeuristicType;
import core.solver.algorithm.heuristic.Predictor;
import stud.runner.PuzzleFeeder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;

/**
 * Description:
 *
 * @date:2022/10/4 10:53
 * @author:Karthus77
 */
public class Position extends State{
    private int[][] puzzle; //棋盘状态
    private int size; //棋盘大小
    private int row;
    private int col;
    private int[][] pos;//某个数的位置
    public void setSize(int size){
        this.size=size;
    }
    public int getSize(){
        return size;
    }
    public int[][] getPuzzle(){
        return puzzle;
    }
    public int[][] getPos() {
        return pos;
    }
    public void notifyChange(int x1,int y1,int x2,int y2)
    {
            puzzle[x1][y1]=puzzle[x2][y2];
            puzzle[x2][y2]=0;
            pos[0][0]=x2;
            pos[0][1]=y2;
            int pot=puzzle[x1][y1];
            pos[pot][0]=x2;
            pos[pot][1]=y2;
    }
    public void setPuzzle(int[][] copy){
        this.puzzle = new int[this.getSize()][this.getSize()];
        this.pos = new int[getSize()*getSize()][2];
        for(int i=0;i<this.getSize();i++) {
            for(int j=0;j<this.getSize();j++)
            {
                int no = copy[i][j];
                this.puzzle[i][j] = copy[i][j];
                this.pos[no][0] = i;
                this.pos[no][1] = j;
            }
        }
    }

    public static Predictor predictor(HeuristicType type){
        return predictors.get(type);
    }
    private static final EnumMap<HeuristicType, Predictor> predictors = new EnumMap<>(HeuristicType.class);
    static{
        predictors.put(HeuristicType.MISPLACED,
                (state, goal) -> ((Position)state).misplaced((Position)goal));
        predictors.put(HeuristicType.MANHATTAN,
                (state, goal) -> ((Position)state).manhattan((Position)goal));
    }
    private int misplaced(Position goal){
        int num = 0;
        int[][] st = getPuzzle();
        int[][] go = goal.getPuzzle();
        for (int i = 0; i < getSize(); i++)
            for (int j = 0; j < getSize(); j++) {
                if (st[i][j] != go[i][j] && go[i][j] != 0)
                    num++;
            }
        return num;
    }
    private int manhattan(Position goal){
        int[][] st = getPos(), go = goal.getPos();
        int num=0;
        for(int i=0;i<getSize()*getSize();i++)
        {
            int ix = st[i][0],iy = st[i][1],gx = go[i][0],gy = go[i][1];
            num += Math.abs(ix - gx) + Math.abs(iy - gy);
        }
        return num;
    }
    @Override
    public void draw() {
        for(int i=0;i<this.getSize();i++) {
            for(int j=0;j<this.getSize();j++)
            {
                System.out.print(puzzle[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println();
    }

    @Override
    public State next(Action action) {
        Position st = new Position();
        st.setSize(this.getSize());
        st.setPuzzle(this.getPuzzle());
        int[][] state = st.getPuzzle();
        int[] offsets = Direction.offset(((Move)action).getDirection());
        int col = pos[0][1] + offsets[0];
        int row = pos[0][0] + offsets[1];
        st.notifyChange(pos[0][0],pos[0][1],row,col);
        return st;
    }

    @Override
    public Iterable<? extends Action> actions() {
        Collection<stud.problem.Npuzzle.Move> moves = new ArrayList<>();
        for (stud.problem.Npuzzle.Direction d : stud.problem.Npuzzle.Direction.FOUR_DIRECTIONS)
            moves.add(new stud.problem.Npuzzle.Move(d));
        return moves;
    }
    @Override
    public int hashCode()
    {
        int num=0;
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++) {
                    num ^= PuzzleFeeder.getZobrist()[i * size + j][puzzle[i][j]];
            }
        }
        return num;
    }
    public boolean equals(Object goal) {
        if (goal instanceof stud.problem.Npuzzle.Position) {
        stud.problem.Npuzzle.Position another = (stud.problem.Npuzzle.Position) goal;
        for(int i=0;i<this.size;i++)
            for(int j=0;j<this.size;j++)
               if(this.puzzle[i][j]!=another.puzzle[i][j])
                    return false;
        }
        return true;
    }
}

