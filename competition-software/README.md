## libxstyle and competition simulation

To run, install `Simple build tool` and execute `$ sbt run`.
This reads riders from the file `riders`.

To read from another file, use `$ sbt "run riders-female-junior"`.
You can also pass multiple files: `$ sbt "run riders-male riders-female"`

## Sample simulation output
```
----- file: riders-female-junior -----

Starting group 1 (judged by groups 2):
  641 Linda Unz
  844 Antonia Joschko
  147 Henriette Höhne
  686 Svenja Stronzik
  640 Saskia Unz
  197 Julia Pfannenstein

Starting group 2 (judged by groups 1):
  1351 Anna Plininger
  851 Emily Abendschön
  709 Emma Neumann
  908 Kim-Lilien Hoeser
  518 Aoi Uchida

Judging:
  Group 1 judging groups 2
  Group 2 judging groups 1

  Group 1 judged by groups 2
  Group 2 judged by groups 1

competitors:           11
rounds:                2
total number of runs:  17
total time needed:     2h 25min

[10min] round 1
  run length:  90 seconds
  competitors: 11
  groups:      2
  group sizes: 1x5 riders, 1x6 riders
  total time:  1h 12min
  judging:
    Group 1 judged by groups 2
    Group 2 judged by groups 1
    [15min] Starting Group 1 (6 riders)
      Judges: Groups 2
      [15min] Run 1
      [17min] Run 2
      [20min] Run 3
      [22min] Run 4
      [25min] Run 5
      [27min] Run 6
    [40min] Starting Group 2 (5 riders)
      Judges: Groups 1
      [40min] Run 1
      [42min] Run 2
      [45min] Run 3
      [47min] Run 4
      [50min] Run 5

[1h 22min] round 2 (Finals)
  run length:  2 minutes
  competitors: 6
  groups:      1
  group sizes: 1x6 riders
  total time:  53min
  judging:
    Group 1 judged by groups 1,1
    [1h 27min] Starting Group 1 (6 riders)
      Judges: Groups 1,1
      [1h 27min] Run 1
      [1h 30min] Run 2
      [1h 33min] Run 3
      [1h 36min] Run 4
      [1h 39min] Run 5
      [1h 42min] Run 6
```
