package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    /// Сложность алгоритма O(n*m) - n - количество рядов, m - количество юнитов
    /// Алгоритм проходится по всем рядам и каждому юниту в нем.
    /// Для каждого юнита выполняется константное число проверок,
    /// связанное со следующим юнитом в ряду.
    /// * Если юниты отсортированы по координатам
    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        List<Unit> suitableUnits = new ArrayList<>();
        int row = 0;
        // Итерация по рядам
        for (List<Unit> units : unitsByRow) {
            units.sort(Comparator.comparingInt(Unit::getyCoordinate).reversed());
            Iterator<Unit> iterator = units.iterator();
            if (!iterator.hasNext()) {
                continue;
            }
            Unit candidate = iterator.next();
            // Погибшие юниты недоступны для атаки
            if (candidate.getHealth() <= 0) {
                continue;
            }
            // Если следующий юнит в ряду стоит в соседней клетке, атаковать кандидата нельзя
            while (iterator.hasNext()) {
                Unit next = iterator.next();
                // Если следующий юнит в ряду погиб, атаковать кандидата можно
                if (next.getHealth() <= 0) {
                    suitableUnits.add(candidate);
                    candidate = next;
                    continue;
                }
                if (!(isLeftArmyTarget && isBlocked(candidate, next) || !isLeftArmyTarget && isBlocked(candidate, next))) {
                    suitableUnits.add(candidate);
                }
                candidate = next;
            }
            // Последний юнит в ряду
            if (candidate.getHealth() > 0) {
                suitableUnits.add(candidate);
            }
            row++;
        }
        if (suitableUnits.isEmpty()) {
            System.out.println("Unit can not find target for attack!");
        }
        return suitableUnits;
    }

    private boolean isBlocked(Unit candidate, Unit next) {
        return candidate.getyCoordinate() - 1 == next.getyCoordinate();
    }

}
