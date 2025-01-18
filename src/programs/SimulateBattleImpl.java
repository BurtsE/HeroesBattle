package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.io.PrintStream;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class SimulateBattleImpl implements SimulateBattle {
    private PrintBattleLog printBattleLog; // Позволяет логировать. Использовать после каждой атаки юнита
    private final PrintStream printStream = System.out;

    /// Сортировка юнитов - O(N * log(N))
    /// Каждый раунд -  N ходов
    /// Атака юнита  - O(1)
    /// Число раундов M зависит от типов юнитов
    /// Итоговая сложность - O(M * N * log(N))
    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        int round = 1;
        List<Unit> playerUnits = playerArmy.getUnits();
        List<Unit> computerUnits = computerArmy.getUnits();
        if (!playerUnits.isEmpty() && !computerUnits.isEmpty()) {
            playerUnits.sort(Comparator.comparingInt(Unit::getBaseAttack).reversed());
            computerUnits.sort(Comparator.comparingInt(Unit::getBaseAttack).reversed());
        }
        while (!playerUnits.isEmpty() && !computerUnits.isEmpty()) {
            Round(playerUnits, computerUnits);
            System.out.println();
            System.out.println("Round " + round + " is over!");
            System.out.println("Player army has " + playerUnits.size() + " units");
            System.out.println("Computer army has " + computerUnits.size() + " units");
            System.out.println();
            round++;
        }
        System.out.println("Battle is over!");
        // Ваше решение
    }

    private void Round(List<Unit> playerUnits, List<Unit> computerUnits) throws InterruptedException {
        Iterator<Unit> player = playerUnits.iterator();
        Iterator<Unit> computer = computerUnits.iterator();
        while (player.hasNext() && computer.hasNext()) {
            Unit playerUnit = player.next();
            if (playerUnit.isAlive()) {
                Unit target = playerUnit.getProgram().attack();
                if (target != null) {
                    printBattleLog.printBattleLog(playerUnit, target);
                }
            } else {
                player.remove();
            }
            Unit computerUnit = computer.next();
            if (computerUnit.isAlive()) {
                Unit target = computerUnit.getProgram().attack();
                if (target != null) {
                    printBattleLog.printBattleLog(computerUnit, target);
                }
            } else {
                computer.remove();
            }
        }
    }
}
