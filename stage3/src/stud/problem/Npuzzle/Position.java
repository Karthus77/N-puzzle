package stud.problem.Npuzzle;

import algs4.util.StopwatchCPU;
import core.problem.Action;
import core.problem.State;
import core.solver.algorithm.heuristic.HeuristicType;
import core.solver.algorithm.heuristic.Predictor;
import stud.queue.Zobrist;
import stud.runner.PuzzleFeeder;

import java.io.*;
import java.util.*;

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
    private int manhattan = 0;
    private int hash = 0;
    static final int[] tilePositions = {-1, 0, 0, 1, 2, 1, 2, 0, 1, 3, 4, 2, 3, 5, 4, 5};
    static final int[] tileSubsets = {-1, 1, 0, 0, 0, 1, 1, 2, 2, 1, 1, 2, 2, 1, 2, 2};
    public void setHash(int hash) {
        this.hash = hash;
    }
    public int getManhattan() {
        return manhattan;
    }
    public static final int[] costTable_15_puzzle_0 = new int[4096],
            costTable_15_puzzle_1 = new int[16777216],
            costTable_15_puzzle_2 = new int[16777216];
    public void setManhattan(int manhattan) {
        this.manhattan = manhattan;
    }
    private static final String file1="C:\\Users\\CrazyBin\\Desktop\\course\\人工智能\\N-pullze\\SearchFramework2.1-toStudents\\resources\\663_0.txt";
    private static final String file2="C:\\Users\\CrazyBin\\Desktop\\course\\人工智能\\N-pullze\\SearchFramework2.1-toStudents\\resources\\663_1.txt";
    private static final String file3="C:\\Users\\CrazyBin\\Desktop\\course\\人工智能\\N-pullze\\SearchFramework2.1-toStudents\\resources\\663_2.txt";
    public void setSize(int size){
        this.size=size;
    }
    public int getSize(){
        return size;
    }
    public int[][] getPuzzle(){
        return puzzle;
    }
    public int getCol() {
        return col;
    }
    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
    private int[][] zobrist;
    static {
        File firstFile=new File(file1);
        File secondFile=new File(file2);
        File thirdFile=new File(file3);
        try {
            BufferedReader br1=new BufferedReader(new FileReader(firstFile));
            BufferedReader br2=new BufferedReader(new FileReader(secondFile));
            BufferedReader br3=new BufferedReader(new FileReader(thirdFile));
            String line=null;
            int tmp;
            int count=0;
            while((line=br1.readLine())!=null)
            {
                tmp=Integer.parseInt(line);
                costTable_15_puzzle_0[count++]=tmp;
            }
            count=0;
            while((line=br2.readLine())!=null)
            {
                tmp=Integer.parseInt(line);
                costTable_15_puzzle_1[count++]=tmp;
            }
            count=0;
            while((line=br3.readLine())!=null)
            {
                tmp=Integer.parseInt(line);
                costTable_15_puzzle_2[count++]=tmp;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Position(Position state) {
        this.size = state.getSize();
        this.puzzle = new int[size][];
        for (int i = 0; i < state.getSize(); i++) {
            this.puzzle[i] = new int[size];
            for (int j = 0; j < state.getSize(); j++) {
                this.puzzle[i][j] = state.getPuzzle()[i][j];
                if(this.puzzle[i][j]==0)
                {
                    row=i;
                    col=j;
                }
            }
        }
        this.zobrist = state.zobrist;
    }
    public Position(int size, int[] board, boolean isRoot) {
        //initiate size
        this.size = size;
        //initiate states
        this.puzzle = new int[size][];
        for (int i = 0; i < size; i++) {
            this.puzzle[i] = new int[size];
            for (int j = 0; j < size; j++) {
                this.puzzle[i][j] = board[i * size + j];
                if (this.puzzle[i][j] == 0) {
                    row = i;
                    col = j;
                }
            }
        }
        if (isRoot) {
            Zobrist z = new Zobrist(size);
            this.zobrist = z.getZobrist();
        }
        else{
            this.zobrist = Zobrist.getZobrist();
        }
        //initiate zobrist hash code
        //注意目标节点也要进行hash初始化
        for (int i = 0; i < size * size; i++) {
            if (puzzle[i / size][i % size] != 0) {
                hash ^= zobrist[i][puzzle[i / size][i % size]];
            }
        }
    }
    public int[] convert(Position position)
    {
        int size=position.getSize();
        int[] tmp=new int[size*size];
        int[][] state=position.getPuzzle();
        for(int i=0;i<size*size;i++)
        {
            tmp[i]=state[i/size][i%size];
        }
        return tmp;
    }
    public void setPuzzle(int[][] copy){
        this.puzzle = new int[this.getSize()][this.getSize()];
        for(int i=0;i<this.getSize();i++) {
            for(int j=0;j<this.getSize();j++)
            {
                int no = copy[i][j];
                this.puzzle[i][j] = copy[i][j];
                if(this.puzzle[i][j]==0)
                {
                    row=i;
                    col=j;
                }
            }
        }
    }
    public static Predictor predictor(HeuristicType type){
        return predictors.get(type);
    }
    private static final EnumMap<HeuristicType, Predictor> predictors = new EnumMap<>(HeuristicType.class);
    static{
        predictors.put(HeuristicType.DISJOINT_PATTERN,
                (state, goal) -> ((Position)state).disjoint_pattern((Position)goal));
        predictors.put(HeuristicType.MISPLACED,
                (state, goal) -> ((Position)state).misplaced((Position)goal));
        predictors.put(HeuristicType.MANHATTAN,
                (state, goal) -> ((Position)state).manhattan((Position)goal));
    }
    private int disjoint_pattern(Position goal)  {
        int num=0;
        num=getH(convert(this));
        return num;
    }
    public static int getH(int [] tmp) {

        int index0 = 0, index1 = 0, index2 = 0;
        for (int pos = 15; pos >= 0; --pos) {
            final int tile = tmp[pos];
            if (tile != 0) {
                final int subsetNumber = tileSubsets[tile];
                switch (subsetNumber) {
                    case 2:
                        index2 |= pos << (tilePositions[tile] << 2);
                        break;
                    case 1:
                        index1 |= pos << (tilePositions[tile] << 2);
                        break;
                    default:
                        index0 |= pos << (tilePositions[tile] << 2);
                        break;
                }
            }
        }
        return costTable_15_puzzle_0[index0] +
                costTable_15_puzzle_1[index1] +
                costTable_15_puzzle_2[index2];
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
        int ans=0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (puzzle[i][j] == 0) continue;
                ans += Math.abs((puzzle[i][j] - 1) / size - i) + Math.abs((puzzle[i][j] - 1) % size - j);
            }
        }
        return ans;
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
        Direction dir = ((Move) action).getDirection();
        int[] offsets = Direction.offset(dir);
        int newRow = row + offsets[0];
        int newCol = col + offsets[1];
        Position newState = new Position(this);
        int temp = puzzle[newRow][newCol];
        newState.puzzle[row][col] = temp;//更新
        newState.puzzle[newRow][newCol] = 0;
        //提取出新的states
        int[][] newStates = newState.getPuzzle();
        newState.setCol(newCol); newState.setRow(newRow);
        int old = zobrist[newRow * size + newCol][temp];//修改hash值
        int fresh = zobrist[row * size + col][temp];
        newState.setHash(hash ^ old ^ fresh);
        return newState;
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
        return hash;
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

