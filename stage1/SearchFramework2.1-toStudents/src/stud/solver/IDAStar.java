package stud.solver;

import core.problem.Problem;
import core.solver.algorithm.Searcher;
import core.solver.algorithm.heuristic.Predictor;
import core.solver.queue.Node;

import java.util.Deque;

/**
 * 迭代加深的A*算法，需要同学们自己编写完成
 */
public class IDAStar implements Searcher {
    Predictor predictor;
    public IDAStar(Predictor predictor) {
        //FixMe
        this.predictor = predictor;
    }

    @Override
    public Deque<Node> search(Problem problem) {
        //FixMe
        return null;
    }

    @Override
    public int nodesExpanded() {
        //FixMe
        return 0;
    }

    @Override
    public int nodesGenerated() {
        //FixMe
        return 0;
    }
}
