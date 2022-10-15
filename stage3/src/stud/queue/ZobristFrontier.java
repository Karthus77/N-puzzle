package stud.queue;

import core.problem.State;
import core.solver.queue.Frontier;
import core.solver.queue.Node;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Description:
 *
 * @date:2022/10/12 9:41
 * @author:Karthus77
 */
public class ZobristFrontier extends PriorityQueue<Node> implements Frontier {
    //1. 比较器
    private final Comparator<Node> evaluator;
    private final HashMap<Integer, Node> hashMap = new HashMap<>();

    public ZobristFrontier(Comparator<Node> evaluator) {
        super(evaluator);
        this.evaluator = evaluator;
    }

    @Override
    public Node poll(){
        Node node = super.poll();
        hashMap.remove((node.getState()).hashCode());
        return node;
    }

    private Node getNode(State state) {return hashMap.get(state.hashCode());}

    @Override
    public boolean contains(Node node) {
        return getNode(node.getState()) != null;
    }

    @Override
    public boolean offer(Node node) {
        Node oldNode = getNode(node.getState());
        if (oldNode == null) {
            super.offer(node);
            hashMap.put((node.getState()).hashCode(), node);
            return true;
        } else { //child已经在Frontier中
            return discardOrReplace(oldNode, node);
        }
    }

    //4. 如果已经在Frontier中，则判断新生成的节点是否应该替换
    private void replace(Node oldNode, Node newNode) {
        hashMap.put((oldNode.getState()).hashCode(),newNode);
        super.offer(newNode);
    }

    private boolean discardOrReplace(Node oldNode, Node node) {
        // 如果旧结点的估值比新的大，即新生成的结点更好
        if (evaluator.compare(oldNode, node) > 0) {
            // 用新节点替换旧节点
            replace(oldNode, node);
            return true;
        }
        return false;   //discard，扔掉新结点
    }

}
