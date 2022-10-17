package stud.solver;

import core.problem.Problem;
import core.problem.State;
import core.solver.algorithm.Searcher;
import core.solver.algorithm.heuristic.EvaluationType;
import core.solver.algorithm.heuristic.Predictor;
import core.solver.queue.Frontier;
import core.solver.queue.Node;
import stud.queue.ListFrontier;

import java.util.*;

/**
 * 迭代加深的A*算法，需要同学们自己编写完成
 */
public class IDAStar implements Searcher {
    Predictor predictor;
    private Set<State> explored = new HashSet<>();
    private Set<State> expanded = new HashSet<>();
    // 返回的路径
    Deque<Node> result = new ArrayDeque<>();
    Problem local_problem;

    public IDAStar(Predictor predictor) {
        this.predictor = predictor;
    }
    /**
     *
     * @param node  当前状态
     * @param parentNode 上一状态
     * @param depth  最大探索深度
     * @return
     */
    boolean depthFirstSearch(Node node, Node parentNode, int depth){
        //剪枝
        if (node.getPathCost() >= depth)
            return false;
        if (local_problem.goal(node.getState())) {
            //如果抵达目标状态，回溯得到路径
            result = generatePath(node);
            return true;
        }
        //添加扩展结点
        expanded.add(node.getState());
        //生成子结点
        List<Node> children = local_problem. childNodes(node, predictor);
        //对每个子结点进行迭代
        for (var child : children){
            //添加生成结点
            explored.add(child.getState());
            //确保不会回到上一步
            if (parentNode != null)
                if(child.equals(parentNode))
                    continue;
            //小于最大深度则继续迭代
            if (child.evaluation() < depth && depthFirstSearch(child, node, depth)) {
                return true;
            }
        }
        return false;
    }
    @Override
    public Deque<Node> search(Problem problem) {
        local_problem = problem;
        //是否可解
        if (!local_problem.solvable()) {
            System.out.println("No Solution!");
            return null;
        }
        // 起始节点root
        Node root = local_problem.root(predictor);
        //最大探索深度
        int depth = root.getHeuristic();
        while (!depthFirstSearch(root, null, depth)) {
            depth++;
        }
        return result;
    }
    @Override
    public int nodesExpanded() {
        return expanded.size();
    }
    @Override
    public int nodesGenerated() {
        return explored.size();
    }
}

