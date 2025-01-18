package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.*;

public class GeneratePresetImpl implements GeneratePreset {
    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;
    /// Метод shuffle - O(n*m) +
    /// sort - O(n *log(n)) +
    /// цикл заполнения списка - O(11 * m).
    /// Сложность алгоритма берем по вырыжению с наибольшим порядком. Итоговая алгоритмическая сложность: O(n*m)
    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        List<Unit> armyList = new ArrayList<>();
        List<int[]> positions = new LinkedList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                positions.add(new int[]{i, j});
            }
        }
        Collections.shuffle(positions);
        unitList.sort(Comparator.comparingDouble(
                unit -> -((double) unit.getBaseAttack() * (double) unit.getHealth() / unit.getCost()))
        );
        Unit first = unitList.getFirst();
        if (first == null) {
            return null;
        }
        int counter;
        int unitNumber;
        int totalPoints = 0;
        for (Unit unit : unitList) {
            counter = 0;
            unitNumber = 1;
            while (counter < 10 && totalPoints + unit.getCost() <= maxPoints) {
                totalPoints += unit.getCost();
                int[] position = positions.removeLast();
                armyList.add(new Unit(
                        unit.getName() + " " + unitNumber,
                        unit.getUnitType(),
                        unit.getHealth(),
                        unit.getBaseAttack(),
                        unit.getCost(),
                        unit.getAttackType(),
                        unit.getAttackBonuses(),
                        unit.getDefenceBonuses(),
                        position[0],
                        position[1]
                ));
                unitNumber++;
                counter++;
            }
        }
        Army army = new Army(armyList);
        army.setPoints(totalPoints);
        return army;
    }
}