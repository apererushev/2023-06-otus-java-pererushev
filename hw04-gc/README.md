# hw04-gc

### Определение нужного размера хипа

**Цель**: на примере простого приложения понять какое влияние оказывают сборщики мусора.

Описание/Пошаговая инструкция выполнения домашнего задания: \
Есть готовое приложение (модуль homework):
* Запустите его с размером хипа 256 Мб и посмотрите в логе время выполнения. \
Пример вывода: ```spend msec:18284, sec:18```
* Увеличьте размер хипа до 2Гб, замерьте время выполнения.
* Результаты запусков записывайте в [таблицу](results.md).
* Определите оптимальный размер хипа, т.е. размер, превышение которого, \
не приводит к сокращению времени выполнения приложения.
* Оптимизируйте работу приложения. \
Т.е. не меняя логики работы (но изменяя код), сделайте так, чтобы приложение работало быстро с минимальным хипом.\
Повторите измерения времени выполнения программы для тех же значений размера хипа.