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
    private static Map<String,Integer> map1=new HashMap<>();
    private static Map<String,Integer> map2=new HashMap<>();
    private static Map<String,Integer> map3=new HashMap<>();
    private final int[] pattern1={2,3,4};
    private final int[] pattern2={1,5,6,9,10,13};
    private final int[] pattern3={7,8,11,12,14,15};
    public void setHash(int hash) {
        this.hash = hash;
    }
    public int getManhattan() {
        return manhattan;
    }

    public void setManhattan(int manhattan) {
        this.manhattan = manhattan;
    }
    private static final String file1="C:\\Users\\CrazyBin\\Desktop\\course\\人工智能\\N-pullze\\SearchFramework2.1-toStudents\\resources\\puzzle3.txt";
    private static final String file2="C:\\Users\\CrazyBin\\Desktop\\course\\人工智能\\N-pullze\\SearchFramework2.1-toStudents\\resources\\puzzle6-1.txt";
    private static final String file3="C:\\Users\\CrazyBin\\Desktop\\course\\人工智能\\N-pullze\\SearchFramework2.1-toStudents\\resources\\puzzle6-2.txt";
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
//    static {
//        File firstFile=new File(file1);
//        File secondFile=new File(file2);
//        File thirdFile=new File(file3);
//        try {
//            BufferedReader br1=new BufferedReader(new FileReader(firstFile));
//            BufferedReader br2=new BufferedReader(new FileReader(secondFile));
//            BufferedReader br3=new BufferedReader(new FileReader(thirdFile));
//            String line=null;
//            while((line=br1.readLine())!=null)
//            {
//                String[] now=line.split(" ");
//                map1.put(now[0],Integer.parseInt(now[1]));
//            }
//            while((line=br2.readLine())!=null)
//            {
//                String[] now=line.split(" ");
//                map2.put(now[0],Integer.parseInt(now[1]));
//            }
//            while((line=br3.readLine())!=null)
//            {
//                String[] now=line.split(" ");
//                map3.put(now[0],Integer.parseInt(now[1]));
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    public Position(Position state) {
        this.size = state.getSize();
        this.puzzle = new int[size][];
        for (int i = 0; i < state.getSize(); i++) {
            this.puzzle[i] = new int[size];
            for (int j = 0; j < state.getSize(); j++) {
                this.puzzle[i][j] = state.getPuzzle()[i][j];
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
//    public void notifyChange(int x1,int y1,int x2,int y2)
//    {
//            puzzle[x1][y1]=puzzle[x2][y2];
//            puzzle[x2][y2]=0;
//            pos[0][0]=x2;
//            pos[0][1]=y2;
//            int pot=puzzle[x1][y1];
//            pos[pot][0]=x2;
//            pos[pot][1]=y2;
//    }
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
//    public int search(String st,Map<String,Integer> map)
//    {
//        return map.get(st);
//    }
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
    public String getState(int[] group)
    {
        StringBuilder stringBuilder=new StringBuilder();
        for(int i=0;i<group.length;i++)
        {
            stringBuilder.append(pos[group[i]][0]);
            stringBuilder.append(pos[group[i]][1]);
        }
        return stringBuilder.toString();
    }
    private int disjoint_pattern(Position goal)  {
        int num=0;
//        num=search(getState(pattern1),map1)+search(getState(pattern2),map2)+search(getState(pattern3),map3);
        return num;
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

