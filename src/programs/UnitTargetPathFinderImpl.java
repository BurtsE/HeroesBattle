package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.*;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {
    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;
    private static final int[][] DIRECTIONS = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {1, 1}, {-1, 1}, {1, -1}};
    /// Использован алгоритм поиска в ширину для нахождения кратчайшего пути с восстановлением пути.
    /// Сложность алгоритма - O(WIDTH*HEIGHT) + O(N), где N - длинна кратчайшего пути
    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        int startX = attackUnit.getxCoordinate();
        int startY = attackUnit.getyCoordinate();
        int targetX = targetUnit.getxCoordinate();
        int targetY = targetUnit.getyCoordinate();
        int[][][] pathMapReversed = bfs(startX, startY, targetX, targetY);
        List<Edge> pathList = new ArrayList<>();
        int x = targetX;
        int y = targetY;
        pathList.add(new Edge(x, y));

        while (x != startX || y != startY) {
            int[] next = pathMapReversed[y][x];
            if (x == next[0] && y == next[1]) {
                System.out.println("loop");
                return null;
            }
            x = next[0];
            y = next[1];
            pathList.add(new Edge(x, y));
        }

        return pathList.reversed();
    }


    private static int[][][] bfs(int x, int y, int targetX, int targetY) {
        int[][] dist = new int[HEIGHT][WIDTH];
        for (int i = 0; i < HEIGHT; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
        }
        int[][][] pr = new int[HEIGHT][WIDTH][2];

        dist[y][x] = 0; // кроме истока
        Queue<int[]> vertices_q = new LinkedList<>();
        vertices_q.add(new int[]{x, y});

        while (!vertices_q.isEmpty()) {
            int[] v = vertices_q.poll();
            for (int[] direction : DIRECTIONS) {
                int newX = v[0] + direction[0];
                int newY = v[1] + direction[1];
                if (newX < 0 || newX >= WIDTH || newY < 0 || newY >= HEIGHT) {
                    continue;
                }
                if (dist[newY][newX] == Integer.MAX_VALUE) {
                    dist[newY][newX] = dist[v[1]][v[0]] + 1;
                    pr[newY][newX][0] = v[0];
                    pr[newY][newX][1] = v[1];
                    if (newX == targetX && newY == targetY) {
                        return pr;
                    }
                    vertices_q.add(new int[]{newX, newY});
                }
            }
        }
        return pr;
    }
}
