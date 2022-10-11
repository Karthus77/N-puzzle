package stud.problem.Npuzzle;

import core.problem.Action;

/**
 * Description:
 *
 * @date:2022/10/3 19:09
 * @author:Karthus77
 */
public class Move extends Action {
    private final stud.problem.Npuzzle.Direction direction;

    public Move(stud.problem.Npuzzle.Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }


    private String dir;
    @Override
    public void draw() {
        System.out.println("Move:"+getDirection());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;

        if (obj instanceof stud.problem.pathfinding.Move) {
            stud.problem.Npuzzle.Move another = (stud.problem.Npuzzle.Move) obj;
            //两个Node对象的状态相同，则认为是相同的
            return this.direction.equals(another.direction);
        }
        return false;
    }
    @Override
    public int stepCost() {
        return 1;
    }
}
