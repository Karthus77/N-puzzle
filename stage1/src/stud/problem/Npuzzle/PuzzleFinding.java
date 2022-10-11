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

    public PuzzleFinding(State initialState, State goal) {
        super(initialState, goal);
    }

    public PuzzleFinding(State initialState, State goal, int size) {
        super(initialState, goal, size);
    }

    @Override
    public boolean solvable() {
        Position position =(Position)initialState;
        int size=position.getSize();
        int [][]st=position.getPuzzle();
        int cnt=0;
        int[] copy=new int[size*size];
        for(int i=0;i<size;i++)
            for(int j=0;j<size;j++)
                copy[cnt++]=st[i][j];
        cnt=0;
        for(int i=0;i<size*size;i++)
        {
            for(int j=i;j<size*size;j++)
                if(copy[j]<copy[i]&&copy[j]!=0)
                    cnt++;
        }
        int[][] row=position.getPos();
        int r=row[0][0];//0的行
        if(size%2==1)
        {
            if(cnt%2==0)
                return true;
            else
                return false;
        }
        else//奇数
        {
            if(cnt%2==0&&r%2==1)
                return true;
            if(cnt%2==1&&r%2==0)
                return true;
            else
                return false;
        }
    }

    @Override
    public int stepCost(State state, Action action) {
        return 1;//单步为1
    }

    @Override
    public boolean applicable(State state, Action action) {
        int size = ((Position)state).getSize();
        int[] offsets = Direction.offset(((stud.problem.Npuzzle.Move)action).getDirection());
        int row = ((Position)state).getPos()[0][0]+offsets[1];
        int col = ((Position)state).getPos()[0][1]+offsets[0];
        return row >= 0 && row < size &&
                col >= 0 && col < size;
    }

    @Override
    public void showSolution(Deque<Node> path) {
        if (path == null){
            System.out.println("No Solution.");
            return;
        }
        for(Node node : path)
        {
            if(node.getAction() != null)
                node.getAction().draw();
            node.getState().draw();
        }
    }
}
