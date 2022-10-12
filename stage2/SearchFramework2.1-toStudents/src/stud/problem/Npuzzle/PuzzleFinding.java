package stud.problem.Npuzzle;

import core.problem.Action;
import core.problem.Problem;
import core.problem.State;
import core.solver.queue.Node;
import stud.problem.pathfinding.GridType;

import java.util.Deque;

/**
 * Description:
 *
 * @date:2022/10/3 19:18
 * @author:Karthus77
 */
public class PuzzleFinding extends Problem {


    public PuzzleFinding(State initialState, State goal) {  //构造函数
        super(initialState, goal, ((Position)(initialState)).getSize());
    }
    public void setSize(int size) {    //问题规模
        this.size = size;
    }

    private int getInversions(int[][] state) {
        int inversion = 0;
        int temp = 0;
        for(int i=0;i<state.length;i++) {
            for(int j=0;j<state[i].length;j++) {
                int index = i* state.length + j + 1;
                while(index < (state.length * state.length)) {
                    if(state[index/state.length][index%state.length] != 0
                            && state[index/state.length]
                            [index%state.length] < state[i][j]) {
                        temp ++;
                    }
                    index ++;
                }
                inversion = temp + inversion;
                temp = 0;
            }
        }
        return inversion;
    }

    @Override
    public boolean solvable() {
        int[][] state = ((Position)getInitialState()).getPuzzle();
        int[][] pos = ((Position)getInitialState()).getPos();
        if(state.length % 2 == 1) { //问题宽度为奇数
            return (getInversions(state) % 2 == 0);
        } else { //问题宽度为偶数
            if((state.length - ((Position)getInitialState()).getRow()) % 2 == 1) { //从底往上数,空格位于奇数行
                return (getInversions(state) % 2 == 0);
            } else { //从底往上数,空位位于偶数行
                return (getInversions(state) % 2 == 1);
            }
        }
    }
    public State getInitialState() {   //获得状态对象
        return initialState;
    }
    @Override
    public int stepCost(State state, Action action) {
        return 1;//单步为1
    }

    @Override
    public boolean applicable(State state, Action action) {
        int size = ((Position)state).getSize();
        int[] offsets = Direction.offset(((stud.problem.Npuzzle.Move)action).getDirection());
        int row = ((Position)state).getRow() + offsets[0];
        int col = ((Position)state).getCol() + offsets[1];
        return row >= 0 && row < size &&
                col >= 0 && col < size;
    }

    @Override
    public void showSolution(Deque<Node> path) {
        if (path == null){
            System.out.println("No Solution.");
            return;
        }
//        for(Node node : path)
//        {
//            if(node.getAction() != null)
//                node.getAction().draw();
//            node.getState().draw();
//        }
    }
}
